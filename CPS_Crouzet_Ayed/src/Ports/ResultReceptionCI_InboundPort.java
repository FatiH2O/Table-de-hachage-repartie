package Ports;

import java.io.Serializable;

import Components.Front;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;

public class ResultReceptionCI_InboundPort extends AbstractInboundPort implements ResultReceptionCI {

	private static final long serialVersionUID = 1L;
	private Front facade;

	public ResultReceptionCI_InboundPort(String inboundPortURI, ComponentI owner) throws Exception {

		super(inboundPortURI, ResultReceptionCI.class, owner);

		assert owner != null : "L'objet owner est null dans ResultReceptionCI_InboundPort.";
		assert inboundPortURI != null : "L'URI est null dans ResultReceptionCI_InboundPort.";
		assert owner.isOfferedInterface(ResultReceptionCI.class)
				: "L'interface ResultReceptionCI n'est pas offerte par le composant.";

		this.facade = (Front) owner;

	}

	@Override
	public void acceptResult(String computationURI, Serializable result) throws Exception {
		//System.out.println("[RR_IB - Accept] acceptResult() appelé avec computationURI : " + computationURI);

		if (this.facade != null) {
			//System.out.println("[RR_IB - Accept] Envoi du résultat au Front via handleIncomingResult.");
			this.facade.handleIncomingResult(computationURI, result);
		}
	}

}
