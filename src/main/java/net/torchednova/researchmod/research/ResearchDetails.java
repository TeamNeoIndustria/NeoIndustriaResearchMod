package net.torchednova.researchmod.research;

public class ResearchDetails {
	public final int stageID;
	public final String stageIDName;
	public final ResearchState state;
	public final String displayName;
	public final String displayItemID;
	public final String displayLore;
	public final String typeADisplayName;
	public final ResearchItem itemA1;
	public final ResearchItem itemA2;
	public final ResearchItem itemA3;
	public final String typeBDisplayName;
	public final ResearchItem itemB1;
	public final ResearchItem itemB2;
	public final ResearchItem itemB3;
	public final String typeCDisplayName;
	public final ResearchItem itemC1;
	public final ResearchItem itemC2;
	public final ResearchItem itemC3;

	public ResearchDetails(int stageID, String stageIDName, ResearchState state, String displayName, String displayItemID, String displayLore, String typeADisplayName, ResearchItem itemA1, ResearchItem itemA2, ResearchItem itemA3, String typeBDisplayName, ResearchItem itemB1, ResearchItem itemB2, ResearchItem itemB3, String typeCDisplayName, ResearchItem itemC1, ResearchItem itemC2, ResearchItem itemC3) {
		this.stageID = stageID;
		this.stageIDName = stageIDName;
		this.state = state;
		this.displayName = displayName;
		this.displayItemID = displayItemID;
		this.displayLore = displayLore;
		this.typeADisplayName = typeADisplayName;
		this.itemA1 = itemA1;
		this.itemA2 = itemA2;
		this.itemA3 = itemA3;
		this.typeBDisplayName = typeBDisplayName;
		this.itemB1 = itemB1;
		this.itemB2 = itemB2;
		this.itemB3 = itemB3;
		this.typeCDisplayName = typeCDisplayName;
		this.itemC1 = itemC1;
		this.itemC2 = itemC2;
		this.itemC3 = itemC3;
	}
}
