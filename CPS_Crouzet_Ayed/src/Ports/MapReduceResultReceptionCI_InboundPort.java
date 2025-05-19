package Ports;

import java.io.Serializable;

import Components.Front;
import Components.Node;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.MapReduceResultReceptionCI;

public class MapReduceResultReceptionCI_InboundPort extends AbstractInboundPort implements MapReduceResultReceptionCI {
	public MapReduceResultReceptionCI_InboundPort(String uri, ComponentI owner) throws Exception {

		super(uri, MapReduceResultReceptionCI.class, owner);

		assert owner != null : "L'objet owner est null dans MapReduceResultReceptionCI_InboundPort.";
		assert uri != null : "L'URI est null dans MapReduceResultReceptionCI_InboundPort.";
		assert owner.isOfferedInterface(MapReduceResultReceptionCI.class)
				: "L'interface MapReduceResultReceptionCI n'est pas offerte par le composant.";
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void acceptResult(String computationURI, String emitterId, Serializable acc) throws Exception {

		if( owner instanceof Front) {
			 ((Front) owner).handleIncomingMap(computationURI,acc);
			}else {
				 ((Node) owner).handleReduceRes(computationURI,acc);
			
		}
	}

}
