package Endpoints_Sync;

import Connectors.DHTServicesConnector_Connector;
import Ports.DHTServicesCI_InboundPort;
import Ports.DHTServicesCI_OutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.endpoints.BCMEndPoint;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.frontend.DHTServicesCI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;

public class DHTServices_Simple_endpoint extends BCMEndPoint<DHTServicesCI> {

	public DHTServices_Simple_endpoint() {
		super(DHTServicesCI.class, DHTServicesCI.class, URIGenerator.generateURI());

	}

	private static final long serialVersionUID = 1L;

	@Override
	protected AbstractInboundPort makeInboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c != null;

		DHTServicesCI_InboundPort p = new DHTServicesCI_InboundPort(c, inboundPortURI);
		p.publishPort();

		assert p.isPublished();
		return p;
	}

	@Override
	protected DHTServicesCI makeOutboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c != null;

		DHTServicesCI_OutboundPort p;
		p = new DHTServicesCI_OutboundPort(URIGenerator.generateURI(), c);
		p.publishPort();

		assert p.isPublished();
		c.doPortConnection(p.getPortURI(), inboundPortURI,
				DHTServicesConnector_Connector.class.getCanonicalName());

		assert p.connected();
		return p;
	}

}
