package Ports;


import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;

public class dhtAsyncContentAccess_Outboundprot extends DHTServicesCI_OutboundPort implements ContentAccessCI {
    private static final long serialVersionUID = 1L;

    public dhtAsyncContentAccess_Outboundprot(String uri, ComponentI owner) throws Exception {
        super(uri, owner);
    }

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