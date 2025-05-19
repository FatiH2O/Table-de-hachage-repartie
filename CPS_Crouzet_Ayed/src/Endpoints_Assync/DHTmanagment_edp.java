package Endpoints_Assync;

import Connectors.DHTmanagment_Connectors;
import Ports.DHTmanagment_InboundPort;
import Ports.DHTmanagment_OutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.endpoints.BCMEndPoint;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementCI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;

public class DHTmanagment_edp extends BCMEndPoint<DHTManagementCI>{

	private static final long serialVersionUID = 1L;

	public DHTmanagment_edp() {
		super(DHTManagementCI.class, DHTManagementCI.class);
			}

	@Override
	protected AbstractInboundPort makeInboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c!=null;
		DHTmanagment_InboundPort port= new DHTmanagment_InboundPort(inboundPortURI,c);
		port.publishPort();
		
		assert port.isPublished();
		
		return port;	
	}

	@Override
	protected DHTManagementCI makeOutboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c!=null;
		
		DHTmanagment_OutboundPort port= new  DHTmanagment_OutboundPort(URIGenerator.generateURI(),c);
		port.publishPort();
		
		assert port.isPublished();
		
		c.doPortConnection(port.getPortURI(), inboundPortURI, DHTmanagment_Connectors.class.getCanonicalName());
		
		assert port.connected();
		
		return port;
	}

}
