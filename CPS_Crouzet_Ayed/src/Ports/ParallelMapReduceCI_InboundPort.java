package Ports;

import java.io.Serializable;

import Components.Node;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.CombinatorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ParallelMapReduceCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ProcessorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ReductorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.SelectorI;

public class ParallelMapReduceCI_InboundPort extends MapCI_InboundPort implements ParallelMapReduceCI {

	private static final long serialVersionUID = 1L;

	public ParallelMapReduceCI_InboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, owner);
			}

	@Override
	public <R extends Serializable> void parallelMap(String computationURI, SelectorI selector, ProcessorI<R> processor,
			ParallelismPolicyI parallelismPolicy) throws Exception {
		
		this.getOwner()
		.handleRequest(owner -> {((Node) owner).parallelMap(computationURI, selector, processor, parallelismPolicy);
		return null;});

	}

	@Override
	public <A extends Serializable, R, I extends MapReduceResultReceptionCI> void parallelReduce(String computationURI,
			ReductorI<A, R> reductor, CombinatorI<A> combinator, A identityAcc, A currentAcc,
			ParallelismPolicyI parallelismPolicy, EndPointI<I> caller) throws Exception {
		
		this.getOwner()
		.handleRequest(owner -> {((Node) owner).parallelReduce(computationURI, reductor, combinator,identityAcc
				,currentAcc, parallelismPolicy,caller);
		return null;});		
	}



}
