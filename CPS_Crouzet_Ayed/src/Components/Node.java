package Components;

import java.io.Serializable;

import fr.sorbonne_u.exceptions.PostconditionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import Endpoints_Assync.Composite_Endpoint;
import Endpoints_Assync.MapReduceResultReceptionCI_edp;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.ConnectionException;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.endpoints.ContentNodeCompositeEndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementI.NodeContentI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.LoadPolicyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.CombinatorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ProcessorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ReductorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.SelectorI;
import fr.sorbonne_u.cps.mapreduce.utils.IntInterval;
import fr.sorbonne_u.cps.mapreduce.utils.SerializablePair;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ParallelMapReduceCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ParallelMapReduceI.ParallelismPolicyI;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@OfferedInterfaces(offered = { MapReduceCI.class, ContentAccessCI.class,MapReduceResultReceptionCI.class,DHTManagementCI.class })
@RequiredInterfaces(required = { MapReduceCI.class, ContentAccessCI.class, ResultReceptionCI.class,
		MapReduceResultReceptionCI.class ,DHTManagementCI.class})

public class Node extends AbstractComponent {

	
	/**
	 * @param Front_to_Node_edp Composite edp qui relie front au Noeud, apelé par le
	 *                          front
	 * 
	 * @param Client_side       Composite edp pour qui relie le noeud courant au
	 *                          noeud client avant moi
	 * 
	 * @param Server_side       Composite edp qui relie le noeud courant au prochain
	 *                          noeud server
	 * 
	 * @param BigInterval       Interval qu'aucune hashtable ne doit dépasser
	 * 
	 * @param monInterval       Interval assigné au noeud et qui appartient à
	 *                          [0,BigInterval]
	 * 
	 * @param tab               hashtable du noeud
	 * 
	 * 
	 * 
	 */

	public Composite_Endpoint Front_to_Node_edp;
	public Composite_Endpoint Client_side;
	public Composite_Endpoint Server_side;

	private int BigInterval;
	private IntInterval monInterval;
	private ConcurrentHashMap<ContentKeyI, ContentDataI> tab;
	private Set<String> Visited; // permettra de vérifier si nous somme deja passer par ce neouD ou
													// pas (critère d'arret)
	private Set<String> Visited_Reduce; // pareille
	private ConcurrentHashMap<String, CompletableFuture<? extends Serializable>> futureRestemp; 
	
	//ensemble des cordes
	Set<SerializablePair<ContentNodeCompositeEndPointI<ContentAccessCI, 
	ParallelMapReduceCI, DHTManagementCI>,
    Integer>> PaireSet= new HashSet<>();
	//private ExecutorService executor;


	private final Lock edp_Lock = new ReentrantLock();
	

	/**********************************************************************************************************************************************************************************************/
	/**																		 Debut Cycle de vie du composant 																					**/
	/*********************************************************************************************************************************************************************************************/
	protected Node(int BigInterval, IntInterval monInterval, Composite_Endpoint Front_to_Node_edp,
			Composite_Endpoint Client_side, Composite_Endpoint Server_side) throws ConnectionException {
		super(10, 0);
		this.tab = new ConcurrentHashMap<ContentKeyI, ContentDataI>();

		this.BigInterval = BigInterval;
		this.monInterval = monInterval;

		this.Visited = new HashSet<String>();
		this.Visited_Reduce = new HashSet<String>();
		this.futureRestemp = new ConcurrentHashMap<String, CompletableFuture<? extends Serializable>>();
		//this.executor = Executors.newFixedThreadPool(1);


		this.Front_to_Node_edp = Front_to_Node_edp;
		assert Front_to_Node_edp != null : new PostconditionException("Front_to_Client_edp is null");

		this.Server_side = Server_side;
		assert Server_side != null : new PostconditionException("Server_side is null");

		this.Client_side = Client_side;
		assert Client_side != null : new PostconditionException("Client_side is null");

		this.Front_to_Node_edp.initialiseServerSide(this);
		this.Client_side.initialiseServerSide(this);

//		this.toggleLogging();
//		this.toggleTracing();

	}

	protected Node(int BigInterval, IntInterval monInterval, Composite_Endpoint Client_side,
			Composite_Endpoint Server_side) throws ConnectionException {
		super(10, 0);
		this.tab = new ConcurrentHashMap<ContentKeyI, ContentDataI>();
		this.BigInterval = BigInterval;
		this.monInterval = monInterval;

		this.Visited = new ConcurrentSkipListSet<String>(); // permettra de vérifier si nous somme deja passer par ce neouD
														// ou pas (critère d'arret)
		this.Visited_Reduce = new ConcurrentSkipListSet<String>();
		this.futureRestemp = new ConcurrentHashMap<String, CompletableFuture<? extends Serializable>>();
		//this.executor = Executors.newFixedThreadPool(1);


		this.Server_side = Server_side;
		assert Server_side != null : new PostconditionException("Server_side is null");

		this.Client_side = Client_side;
		assert Client_side != null : new PostconditionException("Client_side is null");

		this.Client_side.initialiseServerSide(this);
//
//		this.toggleLogging();
//		this.toggleTracing();
//		

	}

	@Override
	public void start() throws ComponentStartException {
		this.logMessage("starting Node component.");
		super.start();
		try {
			this.Server_side.initialiseClientSide(this);
			System.out.println("[DEBUG] ResultReception_edp initialisé comme ClienSide.");
		} catch (ConnectionException e) {

			throw new ComponentStartException(e);
		}
		;
	}

	/*****************************************************************************************************************************************************************************************************************/
	/**																							 ASSYNCHRONE 																											**/
	/****************************************************************************************************************************************************************************************************************/

	public <I extends ResultReceptionCI> void get(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {

		int h = key.hashCode() % BigInterval;


			if (monInterval.in(h)) {

				Visited.add(computationURI);

					caller.initialiseClientSide(this);
					caller.getClientSideReference().acceptResult(computationURI, tab.get(key));
					caller.cleanUpClientSide();
				

			} else {

				if (!Visited.contains(computationURI)) {
					
					Visited.add(computationURI);
					edp_Lock.lock();
					try {

						this.traceMessage("[NODE - Get] Transmission de la requête au prochain Node...");
						this.Server_side.getContentAccessEndpoint().getClientSideReference().get(computationURI, key,
								caller.copyWithSharable());
					}

					finally {
						edp_Lock.unlock();
					}
				}

			}

		
	}
	
	
	

	
	public <I extends ResultReceptionCI> void put(String computationURI, ContentKeyI key, ContentDataI value,
			EndPointI<I> caller) throws Exception {

		int h = key.hashCode() % BigInterval;

	

		
			if (monInterval.in(h)) {
				Visited.add(computationURI);
				if (tab.put(key, value) == null) {//car pour la concurenthashmap, si la valeur renvoyé est null alors l'insertion a bien été faite

					caller.initialiseClientSide(this);
					caller.getClientSideReference().acceptResult(computationURI, tab.put(key, value));
					caller.cleanUpClientSide();


				}

				else {

					this.traceMessage("Deja dans le noeud, On ne peut pas remplacer les clé pour l'instant");
					caller.initialiseClientSide(this);
					caller.getClientSideReference().acceptResult(computationURI, null);
					caller.cleanUpClientSide();
				}

			} else {

				if (!Visited.contains(computationURI)) {
					
					edp_Lock.lock();

					try {
						Visited.add(computationURI);

						this.traceMessage("[NODE - Put] Requête PUT transmise au Node suivant.");
						this.Server_side.getContentAccessEndpoint().getClientSideReference().put(computationURI, key, value,
								caller.copyWithSharable());
					}

					finally {
						edp_Lock.unlock();
					}
				}
			}
		

	}
	

	public <I extends ResultReceptionCI> void remove(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {

		int h = key.hashCode() % BigInterval;

			if (monInterval.in(h)) {

				Visited.add(computationURI);
		
					caller.initialiseClientSide(this);
					caller.getClientSideReference().acceptResult(computationURI, tab.remove(key));
					caller.cleanUpClientSide();


			} else {

				if (!Visited.contains(computationURI)) {
					Visited.add(computationURI);

						
					edp_Lock.lock();

					try {
						
					  this.Server_side.getContentAccessEndpoint().getClientSideReference().remove(computationURI, key,
								caller.copyWithSharable());
					}

				finally {
					edp_Lock.unlock();
				}
			}
		}
		
	}

	

	/*
	 * Method castFuture: methode qui gère le problème de cast car il n'était pas
	 * possible de caster CompletableFuture<Stream<R>> vers
	 * CompletableFuture<Stream<? extends Serializable dans putifAbsent
	 */

	@SuppressWarnings("unchecked")
	private <R extends Serializable> CompletableFuture<Stream<? extends Serializable>> castFuture(
			CompletableFuture<Stream<R>> future) {
		return (CompletableFuture<Stream<? extends Serializable>>) (CompletableFuture<?>) future;
	}

	/*
	 * Cette Map permet de synchroniser map et reduce car on ne sait pas qui
	 * arrivera en premier sur le noeud, reduce attendra map sur le futur
	 */

	private ConcurrentHashMap<String, CompletableFuture<Stream<? extends Serializable>>> FuturSet = new ConcurrentHashMap<String, CompletableFuture<Stream<? extends Serializable>>>();

	@SuppressWarnings("unchecked")
	public <R extends Serializable, I extends MapReduceResultReceptionCI> void map(String computationURI,
			SelectorI selector, ProcessorI<R> processor) throws Exception {

		
		boolean shouldGo = false;
	
		synchronized (Visited) {
			
		    if (!Visited.contains(computationURI)) {
		        Visited.add(computationURI);
		        shouldGo = true;
		    }
		}
			
			if(shouldGo) {//Le thread passera uniquement s'il a ajouter computationURI dans visited

				CompletableFuture<Stream<R>> TempData = CompletableFuture
						.supplyAsync(() -> tab.values().stream().filter(selector).map(processor));

				FuturSet.putIfAbsent(computationURI, castFuture(TempData));

				edp_Lock.lock();

				try {
					    	((MapReduceCI) Server_side.getMapReduceEndpoint().getClientSideReference())
				            .map(computationURI, selector, processor);
				} 
				
				finally {
					edp_Lock.unlock();
					}
		}
		
		else {//sinon on se trouve sur le 1er node deja visité

			System.out.println("last nodeMAP");
		}

}

	
	@SuppressWarnings("unchecked")
	public <A extends Serializable> void handleReduceRes(String computationURI, Serializable result) {



			@SuppressWarnings("unchecked")
			CompletableFuture<A> future = (CompletableFuture<A>) futureRestemp.remove(computationURI);
			if (future != null) {
				future.complete((A) result);
			}
		}
	
	
	
	
	public <A extends Serializable, R, I extends MapReduceResultReceptionCI> void reduce(
	        String computationURI,
	        ReductorI<A, R> reductor,
	        CombinatorI<A> combinator,
	        A identityAcc,
	        A currentAcc,
	        EndPointI<I> callerNode
	) throws Throwable, ExecutionException {

		boolean shouldGo = false;

		synchronized (Visited_Reduce) {
			
		    if (!Visited_Reduce.contains(computationURI)) {
		    	Visited_Reduce.add(computationURI);
		        shouldGo = true;
		    }
		   }

			
		if(shouldGo) {
		    //edp qui récupèere le resultat de la combinaison du noeud d'apres
			MapReduceResultReceptionCI_edp resultReceptionEndpoint=new MapReduceResultReceptionCI_edp();
			resultReceptionEndpoint.initialiseServerSide(this);
			
			//creer un completable futur pour attendre le resultat de la reduction
			//du nextNode
			CompletableFuture<A> RestempRequest = new CompletableFuture<>(); 
			futureRestemp.put(computationURI,RestempRequest);
			
			
			edp_Lock.lock();

			try {
			//lancer la reduction dans le noeud suivant
			((MapReduceCI) Server_side.getMapReduceEndpoint().getClientSideReference())
		                .reduce(computationURI, reductor, combinator, identityAcc, currentAcc,
		              		resultReceptionEndpoint.copyWithSharable());  
			} 
			
			finally {
				edp_Lock.unlock();
				}
			
			//Effectuer la reduction dans notre noeud 
			CompletableFuture<Stream<? extends Serializable>> futureStream = FuturSet.get(computationURI);
		
			Stream<? extends Serializable> stream = futureStream.get();
		
			
			@SuppressWarnings("unchecked")
			//le reductor prend des resultat de type R et les reduit en resultat de type A,
			// ici il ne detecte pas que les elements du stream sont de type R d'ou la conversion avec map
			A resTempReduce =  stream.map(v -> (R) v).reduce(currentAcc, reductor, combinator);

			
			
			// Ensuite, on attend le résultat du noeud suivant et on combine
			A ResReduceNextNode= RestempRequest.get();
			System.out.println( ResReduceNextNode);

			
			A combitationResult=combinator.apply(resTempReduce,(A) ResReduceNextNode);
			
		         	//renvoyer le resultat au noeud d'avant
			 		callerNode.initialiseClientSide(this);
			 		callerNode.getClientSideReference().acceptResult(computationURI,computationURI, 
			 				combitationResult);
			 		callerNode.cleanUpClientSide();	
	}
	else {
		System.out.println( "C'est le premier noeud : the END");
			callerNode.initialiseClientSide(this);
	 		callerNode.getClientSideReference().acceptResult(computationURI,computationURI, 0);
	 		callerNode.cleanUpClientSide();

		}
		
	}
	
	
	//permet d'avoir la plus petite valeur de hachage detenu par le noeud
	public int MinHach(ConcurrentHashMap<ContentKeyI, ContentDataI> tab) {
		
		int minHash = Integer.MAX_VALUE;
		
		for (ContentKeyI key : tab.keySet()) {
	    	int hash = key.hashCode();  
	    	if (hash < minHash) {
	       	 minHash = hash;
	   	    }
	     }
		return minHash;
	}
	
	
	//s'occuper de la concurence

	@SuppressWarnings("unchecked")
	public SerializablePair<
    ContentNodeCompositeEndPointI<ContentAccessCI, ParallelMapReduceCI, DHTManagementCI>, 
    Integer>   getChordInfo(int offset) throws Exception {
		
		
		    if (offset == 0) { //on est arrivé au bon noued, recuperer les informations 
		    	return new SerializablePair<
			    	    ContentNodeCompositeEndPointI<
			    	        ContentAccessCI,
			    	        ParallelMapReduceCI,
			    	        DHTManagementCI>,
			    	    Integer
			    	>(
			    	    (ContentNodeCompositeEndPointI<ContentAccessCI, ParallelMapReduceCI, DHTManagementCI>)
			    	    (Server_side.copyWithSharable()),MinHach(tab));
		    }
		    else{ //on est pas encore arrivé appeler avec offfset-1
		    	return (Server_side.getDHTManagementEndpoint().getClientSideReference().getChordInfo(offset-1));
		    }
				
	}
	
	//s'occuper de la concurence
	public void computeChords(String computationURI, int numberOfChords) throws Exception {
		
    	if(numberOfChords!=0) {
	    	
	    	for(int i = 0; i <= numberOfChords; i++) {
	    		
	    		int e = 2^i;
	    		
	    		SerializablePair< ContentNodeCompositeEndPointI<ContentAccessCI, 
	    		ParallelMapReduceCI, DHTManagementCI>,
	            Integer> Paire = getChordInfo(e);
	    		
	    		if(Paire!=null) {
	    			Paire.first().initialiseClientSide(this);
		    		PaireSet.add(Paire);
	    		}
	    	}
	    	
	    		Server_side.getDHTManagementEndpoint().getClientSideReference()
	                .computeChords(computationURI, numberOfChords-1);
	        } 
	} 
	
	public void initialiseContent(NodeContentI content) {
				
		String newNode= AbstractComponent.createComponent(Node.class.getCanonicalName(),
				new Object[] { content.BigInter,
				content.NewIntervalle, Node1_to_Node2_edp.copyWithSharable(), Node2toNode3_edp.copyWithSharable() });
		
	}

	public void split(String computationURI, LoadPolicyI loadPolicy, EndPointI<ResultReceptionCI> caller) {
				
			if(loadPolicy.shouldSplitInTwoAdjacentNodes(tab.size())) {
				
				ConcurrentHashMap<ContentKeyI, ContentDataI> tableau= new ConcurrentHashMap<ContentKeyI, ContentDataI>();
				
				NodeContentI content= new NodeContent(monInterval,  Client_side,
						 Server_side,tableau);
				
				//mettre les clé dans le nouvel intervalle 
				for (ContentKeyI key: tab.keySet()) {
					
				    int hash= key.hashCode() % BigInterval;
				    
				    if(content.N.in(hash)) {
				    	
				    	tableau.put(key,tab.get(key));
				    	this.tab.remove(key);
				    	}
				    
				}
				
				
				
				
				
				
			}
	}

	

//	public void merge(String computationURI, LoadPolicyI loadPolicy, EndPointI<CI> caller) {
//		// TODO Auto-generated method stub
//		
//	}

	
	
	public <R extends Serializable> void parallelMap(String computationURI, SelectorI selector, ProcessorI<R> processor,
			ParallelismPolicyI parallelismPolicy) throws Exception {
	
	}
	
	
	public <A extends Serializable, R, I extends MapReduceResultReceptionCI> void parallelReduce(String computationURI,
			ReductorI<A, R> reductor, CombinatorI<A> combinator, A identityAcc, A currentAcc,
			ParallelismPolicyI parallelismPolicy, EndPointI<I> caller) throws Exception {
		
	}
	

	

	
	
	/**********************************************************************************************************************************************************************************************/
	/**                                                                             SYNCHRONE                                                                                                  * */
	/*********************************************************************************************************************************************************************************************/

	public ContentDataI getSync(String computationURI, ContentKeyI key) throws Exception {
		int h = key.hashCode() % BigInterval;
		if (monInterval.in(h)) {
			if (tab.get(key) != null) {
				System.out.println("Key " + key.toString() + " with hash " + h + " well get from node with interval ["
						+ monInterval.first() + ", " + monInterval.last() + "]" + " value is: "
						+ tab.get(key).toString());
			} else {
				System.out.println("Warning : key " + key.toString() + " not in table !");
			}
			return tab.get(key);
		} else {

			return Server_side.getContentAccessEndpoint().getClientSideReference().getSync(computationURI, key);

		}
	}

	/** mettre une donnée dans la hashtable **/
	public ContentDataI putSync(String computationURI, ContentKeyI key, ContentDataI value) throws Exception {
		int h = key.hashCode() % BigInterval;
		if (monInterval.in(h)) {
			System.out.println("Key " + key.toString() + " with hash " + h + " well put in node with interval ["
					+ monInterval.first() + ", " + monInterval.last() + "]");
			return tab.put(key, value);
		} else {

			return Server_side.getContentAccessEndpoint().getClientSideReference().putSync(computationURI, key, value);
		}
	}

	/** enlever une donnée de a hashtable **/
	public ContentDataI removeSync(String computationURI, ContentKeyI key) throws Exception {
		int h = key.hashCode() % BigInterval;
		if (monInterval.in(h)) {
			if (tab.get(key) != null) {
				System.out.println("Key " + key.toString() + " with hash " + h + " removed from node with interval ["
						+ monInterval.first() + ", " + monInterval.last() + "]");
			} else {
				System.out.println("Warning : key " + key.toString() + " cannot be removed from table !");
			}
			return tab.remove(key);
		} else {
			return this.Server_side.getContentAccessEndpoint().getClientSideReference().removeSync(computationURI, key);

		}
	}

	/** pas utiliser pour le moment **/
	public void clearComputation(String computationURI) throws Exception {
		this.Server_side.getContentAccessEndpoint().getClientSideReference().clearComputation(computationURI);
	}

	/**
	 * Attribut qui nous permet de stocker les resultats intermédiaires renvoyés par
	 * mapSync
	 **/
	private HashMap<String, Stream<Serializable>> resTempMap = new HashMap<String, Stream<Serializable>>();

	@SuppressWarnings("unchecked")
	public <R extends Serializable> void mapSync(String computationURI, SelectorI selector, ProcessorI<R> processor)
			throws Exception {

		Stream<R> TempData = (Stream<R>) tab.values().stream().filter(selector).map(processor);

		resTempMap.put(computationURI, (Stream<Serializable>) TempData);

		// afficher le contenu de TempData pour debuger
		// resTempMap.get(computationURI).forEach(n ->System.out.println( n));

		if (this.monInterval.last() < BigInterval) {
			// this.logMessage("au prochain noeud" ); // pour debugger
			Server_side.getMapReduceEndpoint().getClientSideReference().mapSync(computationURI, selector, processor);
		}
	}

	public <A extends Serializable, R> A reduceSync(String computationURI, ReductorI<A, R> reductor,
			CombinatorI<A> combinator, A currentAcc) throws Exception {

		/** appliquer le reduce sur les resultats temporaires **/
		@SuppressWarnings("unchecked")
		A resTempReduce = resTempMap.get(computationURI).map(v -> (R) v).reduce((A) currentAcc, reductor, combinator);

		System.out.println("resultat du mapreduce Avant le combinator : " + resTempReduce);

		/**
		 * Si ce n'est pas le dernier noeud, appliquer combiner avec le prochain noeud
		 * 
		 **/
		if (this.monInterval.last() < BigInterval)
			resTempReduce = combinator.apply(resTempReduce, Server_side.getMapReduceEndpoint().getClientSideReference()
					.reduceSync(computationURI, reductor, combinator, currentAcc));

		System.out.println("resultat du mapreduce Apres le combinator : " + resTempReduce);
		return resTempReduce;

	}

	public void clearMapReduceComputation(String computationURI) throws Exception {
		Server_side.getMapReduceEndpoint().getClientSideReference().clearMapReduceComputation(computationURI);
	}

	/**********************************************************************************************************************************************************************************************************/
	/**                                                                      Fin Cycle de vie du composant                                                                                                    **/
	/*********************************************************************************************************************************************************************************************************/
	@Override
	public synchronized void finalise() throws Exception {
		this.Server_side.cleanUpClientSide();
		super.finalise();

	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {

			if (this.Front_to_Node_edp != null)
				this.Front_to_Node_edp.cleanUpServerSide();

			this.Client_side.cleanUpServerSide();

		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}

	
	
	

	

}
