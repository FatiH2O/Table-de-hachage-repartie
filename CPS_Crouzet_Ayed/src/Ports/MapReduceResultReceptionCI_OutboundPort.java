package Ports;

import java.io.Serializable;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;

public class MapReduceResultReceptionCI_OutboundPort extends AbstractOutboundPort implements MapReduceResultReceptionCI {

	private static final long serialVersionUID = 1L;

	public MapReduceResultReceptionCI_OutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, MapReduceResultReceptionCI.class, owner);
		
		assert owner != null && uri != null;
	}

	@Override
	public void acceptResult(String computationURI, String emitterId, Serializable acc) throws Exception {
		MapReduceResultReceptionCI connector = (MapReduceResultReceptionCI) this.getConnector();
		connector.acceptResult(computationURI, emitterId, acc);
	}

}
