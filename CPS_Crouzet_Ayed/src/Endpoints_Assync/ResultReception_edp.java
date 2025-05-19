package Endpoints_Assync;

import Connectors.ResultReceptionCI_Connector;
import Ports.ResultReceptionCI_InboundPort;
import Ports.ResultReceptionCI_OutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.endpoints.BCMEndPoint;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ResultReceptionCI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;

public class ResultReception_edp extends BCMEndPoint<ResultReceptionCI>{

	private static final long serialVersionUID = 1L;

	public ResultReception_edp() {
        super(ResultReceptionCI.class, ResultReceptionCI.class, URIGenerator.generateURI());
    }

	@Override
	protected AbstractInboundPort makeInboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c!=null;
		
		ResultReceptionCI_InboundPort port = new ResultReceptionCI_InboundPort(inboundPortURI, c);
		
        port.publishPort();
        
        assert port.isPublished();
        
        return port;
	}

	@Override
	protected ResultReceptionCI makeOutboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		assert c!=null;
		
		ResultReceptionCI_OutboundPort port= new ResultReceptionCI_OutboundPort(URIGenerator.generateURI(),c);
		port.publishPort();
		
		assert port.isPublished();
	    
		c.doPortConnection(port.getPortURI(), inboundPortURI, ResultReceptionCI_Connector.class.getCanonicalName());
		
		assert port.connected();
		
		return port;
	}
}
