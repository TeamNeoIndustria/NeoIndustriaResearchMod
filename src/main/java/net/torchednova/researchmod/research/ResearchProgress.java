package net.torchednova.researchmod.research;

public class ResearchProgress {
	public final int stageID;
	public final ResearchState state;
	public final int a1Count;
	public final int a2Count;
	public final int a3Count;
	public final int b1Count;
	public final int b2Count;
	public final int b3Count;
	public final int c1Count;
	public final int c2Count;
	public final int c3Count;

	public ResearchProgress(int stageID, ResearchState state, int a1Count, int a2Count, int a3Count, int b1Count, int b2Count, int b3Count, int c1Count, int c2Count, int c3Count) {
		this.stageID = stageID;
		this.state = state;
		this.a1Count = a1Count;
		this.a2Count = a2Count;
		this.a3Count = a3Count;
		this.b1Count = b1Count;
		this.b2Count = b2Count;
		this.b3Count = b3Count;
		this.c1Count = c1Count;
		this.c2Count = c2Count;
		this.c3Count = c3Count;
	}
}
