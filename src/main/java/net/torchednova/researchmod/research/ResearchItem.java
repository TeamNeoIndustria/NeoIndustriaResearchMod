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

	public boolean isValid()
	{
		if (count == 0 || this.itemID.isEmpty() || this.displayName.isEmpty())
		{
			return false;
		}

		return true;
	}

}
