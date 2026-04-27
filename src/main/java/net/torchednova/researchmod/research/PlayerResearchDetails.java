package net.torchednova.researchmod.research;

public class PlayerResearchDetails {
	public final int stageID;
	public final int moneyCost;
	public final String typeADisplayName;
	public final ResearchItem itemA1;
	public final String typeBDisplayName;
	public final ResearchItem itemB1;
	public final String typeCDisplayName;
	public final ResearchItem itemC1;

	public PlayerResearchDetails(int stageID, int moneyCost, String typeADisplayName, ResearchItem itemA1, String typeBDisplayName, ResearchItem itemB1, String typeCDisplayName, ResearchItem itemC1) {
		this.stageID = stageID;
		this.moneyCost = moneyCost;
		this.typeADisplayName = typeADisplayName;
		this.itemA1 = itemA1;
		this.typeBDisplayName = typeBDisplayName;
		this.itemB1 = itemB1;
		this.typeCDisplayName = typeCDisplayName;
		this.itemC1 = itemC1;
	}
}
