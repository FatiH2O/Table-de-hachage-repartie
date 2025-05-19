package Ports;

import Components.Node;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.endpoints.ContentNodeCompositeEndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.LoadPolicyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ParallelMapReduceCI;
import fr.sorbonne_u.cps.mapreduce.utils.SerializablePair;

public class DHTmanagment_InboundPort extends AbstractInboundPort implements DHTManagementCI  {

	private static final long serialVersionUID = 1L;


	public DHTmanagment_InboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, DHTManagementCI.class, owner);
			}


	@Override
	public void initialiseContent(NodeContentI content) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public NodeStateI getCurrentState() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public NodeContentI suppressNode() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <CI extends ResultReceptionCI> void split(String computationURI, LoadPolicyI loadPolicy,
			EndPointI<CI> caller) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public <CI extends ResultReceptionCI> void merge(String computationURI, LoadPolicyI loadPolicy,
			EndPointI<CI> caller) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void computeChords(String computationURI, int numberOfChords) throws Exception {
				
		this.getOwner().handleRequest( owner -> {
		    ((Node) owner).computeChords(computationURI, numberOfChords);
		    return null;
		});
	}
	
	
	@Override
	public SerializablePair<
	    ContentNodeCompositeEndPointI<ContentAccessCI, 
	    ParallelMapReduceCI, DHTManagementCI>, 
	    Integer>
	getChordInfo(int offset) throws Exception {
		
	    return this.getOwner().handleRequest(owner ->((Node) owner).getChordInfo(offset));
	}



	
	
	

}
