package Ports;
import Components.Node;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;

public class dhtAsyncContentAccessInboundport extends DHTServicesCI_InboundPort implements ContentAccessCI {
    private static final long serialVersionUID = 1L;

    public dhtAsyncContentAccessInboundport(String uri, ComponentI owner) throws Exception {
        super(owner, uri);
    }

	@Override
	public <I extends ResultReceptionCI> void get(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {
		this.getOwner().runTask( o -> {
		    try {
		        ((Node) o).get(computationURI, key, caller);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		});
	}

	@Override
	public <I extends ResultReceptionCI> void put(String computationURI, ContentKeyI key, ContentDataI value,
			EndPointI<I> caller) throws Exception {
		this.getOwner().runTask( o -> {
	        try {
	            ((Node) o).put(computationURI, key, value, caller);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		});
		
	}

	@Override
	public <I extends ResultReceptionCI> void remove(String computationURI, ContentKeyI key, EndPointI<I> caller)
			throws Exception {
		this.getOwner().runTask( o -> {
	        try {
	            ((Node) o).remove(computationURI, key, caller);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }	
		});
	}

	//pas besoin
	@Override
	public ContentDataI getSync(String computationURI, ContentKeyI key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentDataI putSync(String computationURI, ContentKeyI key, ContentDataI value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentDataI removeSync(String computationURI, ContentKeyI key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearComputation(String computationURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

}