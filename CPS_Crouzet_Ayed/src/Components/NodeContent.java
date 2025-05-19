package Components;

import java.util.concurrent.ConcurrentHashMap;

import Endpoints_Assync.Composite_Endpoint;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.DHTManagementI.NodeContentI;
import fr.sorbonne_u.cps.mapreduce.utils.IntInterval;

public class NodeContent implements NodeContentI {

	private static final long serialVersionUID = 1L;

	IntInterval NewIntervalle;
	int seuil= 10;
	IntInterval BigInterval;
	Composite_Endpoint Client_side;
	Composite_Endpoint Server_side;
	ConcurrentHashMap<ContentKeyI, ContentDataI> tab;

	public NodeContent(IntInterval BigInterval, IntInterval monInterval, Composite_Endpoint Client_side,
			Composite_Endpoint Server_side,ConcurrentHashMap<ContentKeyI, ContentDataI> tab ) {
		
		this.NewIntervalle= new IntInterval(monInterval.first()+seuil,monInterval.last());
		this.tab= tab;
		this.BigInterval=BigInterval;
		this.Client_side=Client_side;
		this.Server_side=Server_side;
		
		
	}
	
	
	
	
	
}
