package Endpoints_Assync;


import Connectors.AccessSyncCI_Connector;
import Ports.ContentAccessCI_InboundPort;
import Ports.ContentAccessCI_OutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.endpoints.BCMEndPoint;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessCI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;



public class AccessCI_edp extends BCMEndPoint<ContentAccessCI> {
	
	public AccessCI_edp() {
		super(ContentAccessCI.class, ContentAccessCI.class, URIGenerator.generateURI());
		
	}

	private static final long serialVersionUID = 1L;
	

	@Override
	protected AbstractInboundPort makeInboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c!=null;
		ContentAccessCI_InboundPort port= new ContentAccessCI_InboundPort(inboundPortURI,c);
		port.publishPort();
		
		assert port.isPublished();
		
		return port;
	}

	

	@Override
	protected ContentAccessCI makeOutboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c!=null;
		
		ContentAccessCI_OutboundPort port= new  ContentAccessCI_OutboundPort(c,URIGenerator.generateURI());
		port.publishPort();
		
		assert port.isPublished();
		
		c.doPortConnection(port.getPortURI(), inboundPortURI, AccessSyncCI_Connector.class.getCanonicalName());
		
		assert port.connected();
		
		return port;
	}


}
