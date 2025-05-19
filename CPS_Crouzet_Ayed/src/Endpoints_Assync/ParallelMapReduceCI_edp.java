package Endpoints_Assync;


import Connectors.ParallelMapReduceCI_Connector;
import Ports.ParallelMapReduceCI_InboundPort;
import Ports.ParallelMapReduceCI_OutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.endpoints.BCMEndPoint;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.mapreduce.ParallelMapReduceCI;
import fr.sorbonne_u.cps.mapreduce.utils.URIGenerator;

public class ParallelMapReduceCI_edp extends BCMEndPoint<ParallelMapReduceCI>{

	private static final long serialVersionUID = 1L;

	public ParallelMapReduceCI_edp() {
		super(ParallelMapReduceCI.class, ParallelMapReduceCI.class);
			}

	@Override
	protected AbstractInboundPort makeInboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		
		assert c != null;
		ParallelMapReduceCI_InboundPort ParallelMapReduceCI_InboundPort = new ParallelMapReduceCI_InboundPort(inboundPortURI, c);
		ParallelMapReduceCI_InboundPort.publishPort();

		assert ParallelMapReduceCI_InboundPort.isPublished();

		return ParallelMapReduceCI_InboundPort;
	}
	

	@Override
	protected ParallelMapReduceCI makeOutboundPort(AbstractComponent c, String inboundPortURI) throws Exception {
		
		assert c!=null;
		ParallelMapReduceCI_OutboundPort OutboundPort= new  ParallelMapReduceCI_OutboundPort(URIGenerator.generateURI(),c);
		OutboundPort.publishPort();
		
		assert OutboundPort.isPublished();
		
		c.doPortConnection(OutboundPort.getPortURI(), inboundPortURI, ParallelMapReduceCI_Connector.class.getCanonicalName());
		
		assert OutboundPort.connected();
		
		return OutboundPort;	
		}


	

}
