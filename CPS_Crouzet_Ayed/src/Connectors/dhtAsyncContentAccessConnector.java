package Connectors;


import fr.sorbonne_u.components.endpoints.EndPointI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;

public class dhtAsyncContentAccessConnector extends DHTServicesConnector_Connector implements ContentAccessCI {

    @Override
    public <I extends ResultReceptionCI> void get(String computationURI, ContentKeyI key, EndPointI<I> caller)
            throws Exception {
        ((ContentAccessCI) this.offering).get(computationURI, key, caller);
    }

    @Override
    public <I extends ResultReceptionCI> void put(String computationURI, ContentKeyI key, ContentDataI value,
                                                  EndPointI<I> caller) throws Exception {
        ((ContentAccessCI) this.offering).put(computationURI, key, value, caller);
    }

    @Override
    public <I extends ResultReceptionCI> void remove(String computationURI, ContentKeyI key, EndPointI<I> caller)
            throws Exception {
        ((ContentAccessCI) this.offering).remove(computationURI, key, caller);
    }

    
    //Pas besoin
	@Override
	public ContentDataI getSync(String computationURI, ContentKeyI key) throws Exception {
		
		return null;
	}

	@Override
	public ContentDataI putSync(String computationURI, ContentKeyI key, ContentDataI value) throws Exception {
		
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