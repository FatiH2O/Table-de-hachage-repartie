package Endpoints_Assync;

import Connectors.MapReduceResultReceptionCI_Connector;
import Connectors.Map_Connector;
import Ports.MapReduceResultReceptionCI_InboundPort;
import Ports.MapReduceResultReceptionCI_OutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.endpoints.BCMEndPoint;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;

public class MapReduceResultReceptionCI_edp extends BCMEndPoint<MapReduceResultReceptionCI>{

	private static final long serialVersionUID = 1L;

	public MapReduceResultReceptionCI_edp() {
		super(MapReduceResultReceptionCI.class, MapReduceResultReceptionCI.class, URIGenerator.generateURI());

	}

	@Override
	protected AbstractInboundPort makeInboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c != null;
		MapReduceResultReceptionCI_InboundPort MapReduceResultReceptionCI_inboundPort = new MapReduceResultReceptionCI_InboundPort(inboundPortURI, c);
		MapReduceResultReceptionCI_inboundPort.publishPort();

		assert MapReduceResultReceptionCI_inboundPort.isPublished();

		return MapReduceResultReceptionCI_inboundPort;
	}

	@Override
	protected MapReduceResultReceptionCI makeOutboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c != null;

		MapReduceResultReceptionCI_OutboundPort MapReduceResultReceptionCI_outboundPort = new MapReduceResultReceptionCI_OutboundPort(URIGenerator.generateURI(), c);
		MapReduceResultReceptionCI_outboundPort.publishPort();

		assert MapReduceResultReceptionCI_outboundPort.isPublished();

		c.doPortConnection(MapReduceResultReceptionCI_outboundPort.getPortURI(), inboundPortURI,
				MapReduceResultReceptionCI_Connector.class.getCanonicalName());
		assert MapReduceResultReceptionCI_outboundPort.connected();
		return MapReduceResultReceptionCI_outboundPort;
	}
}
