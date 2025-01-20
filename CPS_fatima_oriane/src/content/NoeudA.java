package content;

import java.util.Hashtable;

import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentAccessSyncI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentDataI;
import fr.sorbonne_u.cps.dht_mapreduce.interfaces.content.ContentKeyI;
import fr.sorbonne_u.cps.mapreduce.utils.IntInterval;

public class NoeudA implements ContentAccessSyncI {

	private Hashtable<ContentKeyI, ContentDataI> tab;
	private IntInterval interval;
	
	public NoeudA () {
		tab = new Hashtable<ContentKeyI, ContentDataI>();
		interval = new IntInterval(0, 100);
	}
	
	@Override
	public ContentDataI getSync(String computationURI, ContentKeyI key) throws Exception {
		int h = key.hashCode();
		if(interval.in(h)) {
			return tab.get(key);
		}
		else {
			// renvoyer au prochain
			System.out.println("Passé au noeud suivant");
			return null;
		}
	}

	@Override
	public ContentDataI putSync(String computationURI, ContentKeyI key, ContentDataI value) throws Exception {
		int h = key.hashCode();
		if(interval.in(h)) {
			return tab.put(key, value);
		}
		else {
			// renvoyer au prochain
			System.out.println("Passé au noeud suivant");
			return null;
		}
	}

	@Override
	public ContentDataI removeSync(String computationURI, ContentKeyI key) throws Exception {
		int h = key.hashCode();
		if(interval.in(h)) {
			return tab.remove(key);
		}
		else {
			// renvoyer au prochain
			System.out.println("Passé au noeud suivant");
			return null;
		}
	}

	@Override
	public void clearComputation(String computationURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
