package Ports;

import java.io.Serializable;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;

public class ResultReceptionCI_OutboundPort extends AbstractOutboundPort implements ResultReceptionCI {

	private static final long serialVersionUID = 1L;

	public ResultReceptionCI_OutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ResultReceptionCI.class, owner);

		assert owner != null && uri != null;
	}

	@Override
	public void acceptResult(String computationURI, Serializable result) throws Exception {
		ResultReceptionCI connector = (ResultReceptionCI) this.getConnector();
		connector.acceptResult(computationURI, result);
	}

}
