package net.torchednova.researchmod.research;

public class ResearchItem {
	public final String itemID;
	public final String displayName;
	public final int count;

	public ResearchItem(String itemID, String displayName, int count) {
		this.itemID = itemID;
		this.displayName = displayName;
		this.count = count;
	}
}
