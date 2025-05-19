package Endpoints_Assync;

import Connectors.Map_Connector;
import Ports.MapCI_InboundPort;
import Ports.MapCI_OutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.endpoints.BCMEndPoint;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceCI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;

public class MapCI_edp extends BCMEndPoint<MapReduceCI> {

	private static final long serialVersionUID = 1L;

	public MapCI_edp() {
		super(MapReduceCI.class, MapReduceCI.class, URIGenerator.generateURI());

	}

	@Override
	protected AbstractInboundPort makeInboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c != null;
		MapCI_InboundPort MapReduceCI_inboundport = new MapCI_InboundPort(inboundPortURI, c);
		MapReduceCI_inboundport.publishPort();

		assert MapReduceCI_inboundport.isPublished();

		return MapReduceCI_inboundport;
	}

	@Override
	protected MapReduceCI makeOutboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c != null;

		MapCI_OutboundPort MapReuceCI_outboundPort = new MapCI_OutboundPort(URIGenerator.generateURI(), c);
		MapReuceCI_outboundPort.publishPort();

		assert MapReuceCI_outboundPort.isPublished();

		c.doPortConnection(MapReuceCI_outboundPort.getPortURI(), inboundPortURI,
				Map_Connector.class.getCanonicalName());
		assert MapReuceCI_outboundPort.connected();
		return MapReuceCI_outboundPort;
	}

}
