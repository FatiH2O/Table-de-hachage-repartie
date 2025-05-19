package Connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.endpoints.ContentNodeCompositeEndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.LoadPolicyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ParallelMapReduceCI;
import fr.sorbonne_u.cps.mapreduce.utils.SerializablePair;

public class DHTmanagment_Connectors extends AbstractConnector implements DHTManagementCI{

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
		((DHTManagementCI)this.offering).computeChords(computationURI, numberOfChords);		
	}

	@Override
	public SerializablePair<ContentNodeCompositeEndPointI<ContentAccessCI, ParallelMapReduceCI, DHTManagementCI>, Integer> getChordInfo(
			int offset) throws Exception {
		return ((DHTManagementCI)this.offering).getChordInfo(offset);
	}

}
