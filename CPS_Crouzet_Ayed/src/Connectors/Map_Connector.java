package Connectors;

import java.io.Serializable;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.CombinatorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceSyncCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ProcessorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ReductorI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.SelectorI;

public  class Map_Connector extends AbstractConnector implements MapReduceCI {
	
	/*****************************************************/
	/**         MapReduceSYNCCI                         **/
	/****************************************************/
	
	@Override
	public <R extends Serializable> void mapSync(String computationURI, SelectorI selector, ProcessorI<R> processor)
			throws Exception {
		((MapReduceCI)this.offering).mapSync(computationURI, selector, processor);
		
	}
	
	
	@Override
	public <A extends Serializable, R> A reduceSync(String computationURI, ReductorI<A, R> reductor,
			CombinatorI<A> combinator, A currentAcc) throws Exception {
	
		return ((MapReduceSyncCI)this.offering).reduceSync(computationURI, reductor, combinator, currentAcc);
	}

	@Override
	public void clearMapReduceComputation(String computationURI) throws Exception {
		((MapReduceSyncCI)this.offering).clearMapReduceComputation(computationURI);
		
	}
	
	
	
	/*****************************************************/
	/**            MapReduceCI                         **/
	/****************************************************/
	
	

	@Override
	public <R extends Serializable, I extends MapReduceResultReceptionCI> void map(String computationURI,
			SelectorI selector, ProcessorI<R> processor) throws Exception {
		
		((MapReduceCI)this.offering).map(computationURI, selector, processor);
		
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
		
		((MapReduceCI)this.offering).reduce(computationURI, reductor, combinator, identityAcc,currentAcc,caller);

	}




	
}
