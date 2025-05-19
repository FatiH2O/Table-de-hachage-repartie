package Ports;

import java.io.Serializable;

import Components.Node;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.CombinatorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceSyncCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ProcessorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ReductorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.SelectorI;

public class MapCI_InboundPort extends AbstractInboundPort implements MapReduceCI {

	private static final long serialVersionUID = 1L;

	public MapCI_InboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, MapReduceCI.class, owner);

		assert owner instanceof Node;

	}

	
	/*****************************************************/
	/**         MapReduceCI                         **/
	/****************************************************/
	
	
	@Override
	public <R extends Serializable> void mapSync(String computationURI, SelectorI selector, ProcessorI<R> processor)
			throws Exception {
		this.getOwner().handleRequest(owner -> {
			((Node) owner).mapSync(computationURI, selector, processor);
			return null;
		});

	}

	@Override
	public <A extends Serializable, R> A reduceSync(String computationURI, ReductorI<A, R> reductor,
			CombinatorI<A> combinator, A currentAcc) throws Exception {

		return this.getOwner()
				.handleRequest(owner -> ((Node) owner).reduceSync(computationURI, reductor, combinator, currentAcc));

	}

	@Override
	public void clearMapReduceComputation(String computationURI) throws Exception {
		this.getOwner().handleRequest(owner -> {
			((Node) owner).clearMapReduceComputation(computationURI);
			return null;
		});
	}

	
	/*****************************************************/
	/**        assync MapReduceCI                             **/
	/****************************************************/
	
	
	
	


	@Override
	public <R extends Serializable, I extends MapReduceResultReceptionCI> void map(String computationURI,
			SelectorI selector, ProcessorI<R> processor) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Node) owner).map(computationURI, selector, processor);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		});
		
	}
	
	@Override
	public <A extends Serializable, R, I extends MapReduceResultReceptionCI>
									void	reduce(
		String computationURI,
		ReductorI<A, R> reductor,
		CombinatorI<A> combinator,
		A identityAcc,
		A currentAcc,
		EndPointI<I> caller
		) throws Exception {

		this.getOwner().runTask(owner -> {
			try {
				((Node) owner).reduce(computationURI, reductor, combinator, identityAcc, currentAcc, caller);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable e) {
								e.printStackTrace();
			}
		});

	}

}
