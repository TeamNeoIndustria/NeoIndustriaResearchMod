package net.torchednova.researchmod.research;

public enum ResearchState {
	LOCKED(0, "Locked"),
	COMPLETE(1, "Complete"),
	AVAILABLE(2, "Available");

	private final int stateID;
	private final String description;

	ResearchState(int stateID, String description) {
		this.stateID = stateID;
		this.description = description;
	}

	public int getStateID() {
		return this.stateID;
	}

	public String getDescription() {
		return this.description;
	}

	private static final ResearchState[] VALUES = values();
	public static ResearchState fromStateID(int stateID) {
		for (ResearchState state : VALUES) {
			if (state.getStateID() == stateID) return state;
		}
		throw new IllegalArgumentException("Unknown state ID: " + stateID);
	}
}
