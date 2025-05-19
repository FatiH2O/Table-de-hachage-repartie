package Ports;

import Components.Node;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessSyncCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;

public class ContentAccessCI_InboundPort extends AbstractInboundPort implements ContentAccessCI{

	public ContentAccessCI_InboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ContentAccessSyncCI.class, owner);

		assert owner instanceof Node;
	}

	private static final long serialVersionUID = 1L;	
	
	/*****************************************************/
	/**                SYNCHRONE                        **/
	/****************************************************/

	@Override
	public ContentDataI getSync(String computationURI, ContentKeyI key) throws Exception {

		return this.getOwner().handleRequest(owner -> ((Node) owner).getSync(computationURI, key));

	}

	@Override
	public ContentDataI putSync(String computationURI, ContentKeyI key, ContentDataI value) throws Exception {

		return this.getOwner().handleRequest(owner -> ((Node) owner).putSync(computationURI, key, value));
	}

	@Override
	public ContentDataI removeSync(String computationURI, ContentKeyI key) throws Exception {

		return this.getOwner().handleRequest(owner -> ((Node) owner).removeSync(computationURI, key));
	}

	@Override
	public void clearComputation(String computationURI) throws Exception {
		this.getOwner().handleRequest(owner -> {
			((Node) owner).clearComputation(computationURI);
			return null;
		});

	}
	
	/*****************************************************/
	/**                ASSYNCHRONE                     **/
	/****************************************************/

	@Override
	public <I extends ResultReceptionCI> void get(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Node) owner).get(computationURI, key, caller);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	@Override
	public <I extends ResultReceptionCI> void put(String computationURI, ContentKeyI key, ContentDataI value,
			EndPointI<I> caller) throws Exception {

		this.getOwner().runTask(owner -> {
			try {
				((Node) owner).put(computationURI, key, value, caller);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	@Override
	public <I extends ResultReceptionCI> void remove(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Node) owner).remove(computationURI, key, caller);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
	

}
