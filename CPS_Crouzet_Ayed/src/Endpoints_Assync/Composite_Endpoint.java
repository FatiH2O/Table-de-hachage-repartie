package Endpoints_Assync;

import fr.sorbonne_u.components.endpoints.BCMCompositeEndPoint;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.endpoints.ContentNodeCompositeEndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ParallelMapReduceCI;

public class Composite_Endpoint extends BCMCompositeEndPoint implements ContentNodeCompositeEndPointI<ContentAccessCI, ParallelMapReduceCI, DHTManagementCI> {

	private static final long serialVersionUID = 1L;
	protected static final int NUMBER_OF_ENDPOINTS = 3;

	public Composite_Endpoint() {
		super(NUMBER_OF_ENDPOINTS);

		AccessCI_edp Access_edp = new AccessCI_edp();
		this.addEndPoint(Access_edp);

		ParallelMapReduceCI_edp PMap_edp = new ParallelMapReduceCI_edp();
		this.addEndPoint(PMap_edp);
		
		DHTmanagment_edp DHTmanagment_edp= new DHTmanagment_edp();
		this.addEndPoint(DHTmanagment_edp);

		

	}

	
	@Override
	public EndPointI<ContentAccessCI> getContentAccessEndpoint() {
	
		return this.getEndPoint(ContentAccessCI.class);
	}

	@Override
	public EndPointI<ParallelMapReduceCI> getMapReduceEndpoint() {
		
		return this.getEndPoint(ParallelMapReduceCI.class);
	}

	@Override
	public EndPointI<DHTManagementCI> getDHTManagementEndpoint() {
		
		return this.getEndPoint(DHTManagementCI.class);	

	}
}
