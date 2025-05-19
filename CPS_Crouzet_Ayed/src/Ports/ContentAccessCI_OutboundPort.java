package Ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessSyncCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;

public class ContentAccessCI_OutboundPort extends AbstractOutboundPort implements ContentAccessCI {

	private static final long serialVersionUID = 1L;

	public ContentAccessCI_OutboundPort(ComponentI owner, String uri) throws Exception {
		super(uri, ContentAccessSyncCI.class, owner);

		assert owner != null && uri != null;

	}
	

	/*****************************************************/
	/**                SYNCHRONE                        **/
	/****************************************************/
	
	
	@Override
	public ContentDataI getSync(String computationURI, ContentKeyI key) throws Exception {

		return ((ContentAccessSyncCI) this.getConnector()).getSync(computationURI, key);
	}

	@Override
	public ContentDataI putSync(String computationURI, ContentKeyI key, ContentDataI value) throws Exception {

		return ((ContentAccessSyncCI) this.getConnector()).putSync(computationURI, key, value);
	}

	@Override
	public ContentDataI removeSync(String computationURI, ContentKeyI key) throws Exception {

		return ((ContentAccessSyncCI) this.getConnector()).removeSync(computationURI, key);
	}

	@Override
	public void clearComputation(String computationURI) throws Exception {
		((ContentAccessSyncCI) this.getConnector()).clearComputation(computationURI);

	}
	
	
	/*****************************************************/
	/**                ASSYNCHRONE                     **/
	/****************************************************/
	

	@Override
	public <I extends ResultReceptionCI> void get(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {
		
		((ContentAccessCI) this.getConnector()).get(computationURI, key, caller);
	}

	@Override
	public <I extends ResultReceptionCI> void put(String computationURI, ContentKeyI key, ContentDataI value,
			EndPointI<I> caller) throws Exception {
	
		((ContentAccessCI) this.getConnector()).put(computationURI, key, value, caller);
	}

	@Override
	public <I extends ResultReceptionCI> void remove(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {
		
		((ContentAccessCI) this.getConnector()).remove(computationURI, key, caller);
	}

}
