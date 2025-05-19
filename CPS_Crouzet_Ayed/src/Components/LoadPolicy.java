package Components;

import fr.sorbonne_u.cps.dht_mapreduce.interfaces.management.LoadPolicyI;

public class LoadPolicy implements LoadPolicyI {

	private static final long serialVersionUID = 1L;

	private int seuil= 10;
	@Override
	public boolean shouldSplitInTwoAdjacentNodes(int currentSize) {
		
		return currentSize>seuil;
	}

	@Override
	public boolean shouldMergeWithNextNode(int thisNodeCurrentSize, int nextNodeCurrentSize) {
		
		return false;
	}

}
