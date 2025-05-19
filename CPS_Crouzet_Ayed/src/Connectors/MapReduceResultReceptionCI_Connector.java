package Connectors;

import java.io.Serializable;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;

public class MapReduceResultReceptionCI_Connector extends AbstractConnector implements MapReduceResultReceptionCI {

	@Override
	public void acceptResult(String computationURI, String emitterId, Serializable acc) throws Exception {
		((MapReduceResultReceptionCI) this.offering).acceptResult(computationURI, emitterId, acc);
		
	}
}
