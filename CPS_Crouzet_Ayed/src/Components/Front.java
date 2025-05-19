package Components;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import Endpoints_Assync.Composite_Endpoint;
import Endpoints_Assync.MapReduceResultReceptionCI_edp;
import Endpoints_Assync.ResultReception_edp;
import Endpoints_Sync.DHTServices_Simple_endpoint;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.frontend.DHTServicesCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.CombinatorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ProcessorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ReductorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.SelectorI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;

@OfferedInterfaces(offered = { DHTServicesCI.class, ResultReceptionCI.class, 
		MapReduceResultReceptionCI.class,DHTManagementCI.class })
@RequiredInterfaces(required = { MapReduceCI.class, ContentAccessCI.class, ResultReceptionCI.class,
		MapReduceResultReceptionCI.class,DHTManagementCI.class })

public class Front extends AbstractComponent {

	/**
	 * @param Client_Front_edp  Endpoint qui relie le Client au Front
	 * 
	 * @param Front_to_Node_edp Composite edp qui relie front au Noeud
	 */

	private DHTServices_Simple_endpoint Client_Front_edp;
	private Composite_Endpoint Front_to_Node_edp;
	private final ResultReception_edp resultReceptionEndpoint;
	//private final MapReduceResultReceptionCI_edp mapResEndpoint;

	private final Map<String, CompletableFuture<ContentDataI>> pendingRequests;
	private final Map<String, CompletableFuture<? extends Serializable>> mapreduceRequests;
	
	protected Front(DHTServices_Simple_endpoint Client_Front_edp, Composite_Endpoint Front_to_Node_edp)
			throws Exception {
		super(10, 0);
		this.Client_Front_edp = Client_Front_edp;
		this.Front_to_Node_edp = Front_to_Node_edp;
		this.Client_Front_edp.initialiseServerSide(this);
		this.pendingRequests = new ConcurrentHashMap<>();
		this.mapreduceRequests = new ConcurrentHashMap<>();
		this.resultReceptionEndpoint = new ResultReception_edp();
		this.resultReceptionEndpoint.initialiseServerSide(this);
//		this.mapResEndpoint = new MapReduceResultReceptionCI_edp();
//		this.mapResEndpoint.initialiseServerSide(this);
	}

	/****************************************************************************************************************************************************************************************************/
	/** 			                                                                          Début cycle de vie du front                                                                       		**/
	/***************************************************************************************************************************************************************************************************/

	@Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			this.Front_to_Node_edp.initialiseClientSide(this);
			this.resultReceptionEndpoint.initialiseClientSide(this);
			assert this.resultReceptionEndpoint.getClientSideReference() != null : "Endpoint client non initialisé";

		} catch (Exception e) {
			throw new ComponentStartException("Échec initialisation endpoints", e);
		}
	}

	

	/****************************************************************************************************************************************************************************************************/
	/** 			                                                            	 ASYNCHRONE			                                                                                     	**/
	/***************************************************************************************************************************************************************************************************/

	public ContentDataI get(ContentKeyI key) throws Exception {
		String computationURI = URIGenerator.generateURI();

		CompletableFuture<ContentDataI> future = new CompletableFuture<>();
		pendingRequests.put(computationURI, future);

		this.traceMessage("_______________________________________________________________");
		
		this.Front_to_Node_edp.getContentAccessEndpoint().getClientSideReference().get(computationURI, key,
				resultReceptionEndpoint.copyWithSharable());

		ContentDataI result = future.get();
		
		System.out.println( "get:" + result);


		this.traceMessage(
				"[FRONT - Get] Résultat reçu après get() pour computationURI : " + computationURI + " : " + result);

		return result;
	}
	
	

	public ContentDataI put(ContentKeyI key, ContentDataI value) throws Exception {
		String computationURI = URIGenerator.generateURI();
		CompletableFuture<ContentDataI> future = new CompletableFuture<>();
		pendingRequests.put(computationURI, future);

		this.traceMessage("_______________________________________________________________");

		this.Front_to_Node_edp.getContentAccessEndpoint().getClientSideReference().put(computationURI, key, value,
				resultReceptionEndpoint.copyWithSharable());

		ContentDataI result = future.get();
		System.out.println( "PUT:" + result);

		this.traceMessage(
				"[FRONT - Put] Résultat reçu après put() pour computationURI : " + computationURI + " : " + result);

		return result;
	}

	
	
	public ContentDataI remove(ContentKeyI key) throws Exception {
		String computationURI = URIGenerator.generateURI();

		CompletableFuture<ContentDataI> future = new CompletableFuture<>();
		pendingRequests.put(computationURI, future);

		this.traceMessage("_______________________________________________________________");
		this.Front_to_Node_edp.getContentAccessEndpoint().getClientSideReference().remove(computationURI, key,
				resultReceptionEndpoint.copyWithSharable());

		ContentDataI result = future.get();
		System.out.println( "remove:" + result);


		this.traceMessage("[FRONT - Remove] Résultat reçu après remove() pour computationURI : " + computationURI
				+ " : " + result);

		return result;
	}
	
	
	/**
	 * Méthode utilisée dans la méthode "acceptResult" de la classe
	 * "ResultReceptionCI_InboundPort"
	 * 
	 * @param computationURI : l'URI de la requête
	 * @param result         : le résultat entrant
	 */
	
	public void handleIncomingResult(String computationURI, Serializable result) {
		this.traceMessage("[FRONT - HandleContent] handleIncomingResult() appelée avec computationURI : " + computationURI);

		CompletableFuture<ContentDataI> future = pendingRequests.remove(computationURI);
		if (future != null) {
			future.complete((ContentDataI) result);
			this.traceMessage("[FRONT - HandleContent] Résultat complété pour computationURI : " + computationURI);
		} else {
			this.traceMessage("[FRONT - HandleContent] Aucune requête en attente pour computationURI : " + computationURI);
		}
	}
	
	
	
	 public void computeChords(String computationURI, int numberOfChords) throws Exception {
	    	String computationURI_chord = URIGenerator.generateURI();
	    	Front_to_Node_edp.getDHTManagementEndpoint().getClientSideReference().computeChords(computationURI_chord, numberOfChords);
		}
	
	/****************************************************************************************************************************************************************************************************/
	/** 			                                                                  	MapReduce ASYNCHRONE	                                                                                		**/
	/****************************************************************************************************************************************************************************************************/

	public <A extends Serializable, R extends Serializable, I extends MapReduceResultReceptionCI> A mapReduceAsync(
	        SelectorI selector, ProcessorI<R> processor, ReductorI<A, R> reductor, CombinatorI<A> combinator, A identityAcc) 
	        throws Exception {

	    String computationURI = URIGenerator.generateURI();
	    A currentAcc = identityAcc;
	    
	    CompletableFuture<A> future = new CompletableFuture<>();
	    mapreduceRequests.put(computationURI, future);
	    
	    this.traceMessage("_______________________________________________________________");
	    this.traceMessage("[FRONT - Map-Reduce] Envoi de la requête MAP-REDUCE avec computationURI : " + computationURI);

        this.Front_to_Node_edp.getMapReduceEndpoint().getClientSideReference()
            .map(computationURI, selector, processor);
        //on creer le edp ici pour ne pas avoir à mettre un lock dans node car chaque thread aura sa version(pas de data race)
        MapReduceResultReceptionCI_edp mapResEndpoint= new MapReduceResultReceptionCI_edp();
		mapResEndpoint.initialiseServerSide(this);

        
        this.Front_to_Node_edp.getMapReduceEndpoint().getClientSideReference()
            .reduce(computationURI, reductor, combinator, identityAcc, currentAcc, 
                   mapResEndpoint.copyWithSharable());

	    A result = future.get();
	    this.traceMessage("[FRONT - Map-Reduce] Résultat reçu après mapReduce() pour computationURI : " 
	    + computationURI + " : " + result);
		mapResEndpoint.cleanUpServerSide();
		
		System.out.println("Reduce : "+ result);

		return result;
	}
    
    /**
	 * Méthode utilisée dans la méthode "acceptResult" de la classe
	 * "MapReduceResultReceptionCI_InboundPort"
	 * 
	 * @param computationURI : l'URI de la requête
	 * @param result         : le résultat du map/reduce entrant
	 */
	@SuppressWarnings("unchecked")
	public <A extends Serializable> void handleIncomingMap(String computationURI, Serializable result) {
		this.traceMessage("[FRONT - HandleMap] handleIncomingMap() appelée avec computationURI : " + computationURI);

		CompletableFuture<A> future = (CompletableFuture<A>) mapreduceRequests.remove(computationURI);
		if (future != null) {
			future.complete((A) result);
			this.traceMessage("[FRONT - HandleMap] Résultat complété pour computationURI : " + computationURI);
		} else {
			this.traceMessage("[FRONT - HandleMap] Aucune requête en attente pour computationURI : " + computationURI);
		}
	}
	
	
	
	
	/****************************************************************************************************************************************************************************************************/
	/** 			                                                                  	SYNCHRONE		                                                                                		**/
	/***************************************************************************************************************************************************************************************************/

	public ContentDataI getSync(ContentKeyI key) throws Exception {

		return this.Front_to_Node_edp.getContentAccessEndpoint().getClientSideReference().getSync(URIGenerator.generateURI(), key);
	}

	public ContentDataI putSync(ContentKeyI key, ContentDataI value) throws Exception {

		return this.Front_to_Node_edp.getContentAccessEndpoint().getClientSideReference().putSync(URIGenerator.generateURI(), key,
				value);
	}

	public ContentDataI removeSync(ContentKeyI key) throws Exception {

		return this.Front_to_Node_edp.getContentAccessEndpoint().getClientSideReference().removeSync(URIGenerator.generateURI(),
				key);
	}
	
	/****************************************************************************************************************************************************************************************************/
	/** 			                                                                 	MapReduce SYNCHRONE		         	                                                                           	**/
	/***************************************************************************************************************************************************************************************************/

	public <R extends Serializable, A extends Serializable> A mapReduce(SelectorI selector, ProcessorI<R> processor,
			ReductorI<A, R> reductor, CombinatorI<A> combinator, A initialAcc) throws Exception {

		String Uri = URIGenerator.generateURI();

		this.Front_to_Node_edp.getMapReduceEndpoint().getClientSideReference().mapSync(Uri, selector, processor);

		return this.Front_to_Node_edp.getMapReduceEndpoint().getClientSideReference().reduceSync(Uri, reductor, combinator,
				initialAcc);
	}

	/***************************************************************************************************************************************************************************************/
	/**				                                                                Fin cycle de vie du front 	                                                                    		**/
	/**************************************************************************************************************************************************************************************/

	@Override
	public synchronized void finalise() throws Exception {

		this.resultReceptionEndpoint.cleanUpClientSide();
		this.Front_to_Node_edp.cleanUpClientSide();

		super.finalise();

	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
//			this.mapResEndpoint.cleanUpServerSide();
			this.resultReceptionEndpoint.cleanUpServerSide();
			this.Client_Front_edp.cleanUpServerSide();
			this.pendingRequests.clear();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		} finally {
			super.shutdown();
		}
	}

}
