package net.torchednova.researchmod.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.torchednova.researchmod.Config;
import net.torchednova.researchmod.ResearchMod;
import net.torchednova.researchmod.research.*;
import xyz.neonetwork.neolib.api.APIRequest;
import xyz.neonetwork.neolib.api.APIResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResearchAPI {
	public static final String neoNetworkResearchEndpoint = Config.ResearchWebEndpoint.get();
	public static final String apiKey = Config.ResarchWebApiKey.get();

	public static List<Research> getAvailableResearches()
	{
		try {
			APIResponse response = APIRequest.apiRequest(neoNetworkResearchEndpoint + "getresearchlist", new HashMap<>() {{
				put("apikey", apiKey);
			}});
			if (!response.getSuccess()) {
				ResearchMod.LOGGER.warn("ResearchAPI#getAvailableResearches failed. Code: {}, Message: {}",
					response.getStatusCode(), response.getStatusMessage());
				return null;
			}
			List<Research> researchEntries = new ArrayList<>();
			for (JsonElement entry : response.getDataNode().getAsJsonArray()) {
				JsonObject jsonNode = entry.getAsJsonObject();
				Research researchEntry = new Research(
					jsonNode.get("stageID").getAsInt(),
					jsonNode.get("stageIDName").getAsString(),
					jsonNode.get("displayItemID").getAsString(),
					jsonNode.get("displayName").getAsString(),
					jsonNode.get("displayLore").getAsString()
				);
				researchEntry.state = ResearchState.AVAILABLE;
				researchEntries.add(researchEntry);
			}
			return researchEntries;
		} catch (Exception e) {
			ResearchMod.LOGGER.warn("ResearchAPI#getAvailableResearches failed to parse response");
			return null;
		}
	}

	public static List<Research> getResearchTree()
	{
		try {
			APIResponse response = APIRequest.apiRequest(neoNetworkResearchEndpoint + "getresearchtree", new HashMap<>() {{
				put("apikey", apiKey);
			}});
			if (!response.getSuccess()) {
				ResearchMod.LOGGER.warn("ResearchAPI#getResearchTree failed. Code: {}, Message: {}",
					response.getStatusCode(), response.getStatusMessage());
				return null;
			}
			List<Research> researchEntries = new ArrayList<>();
			for (JsonElement entry : response.getDataNode().getAsJsonArray()) {
				JsonObject jsonNode = entry.getAsJsonObject();
				ResearchMod.LOGGER.info(jsonNode.toString() + " | " + entry.toString());
				Research researchEntry = new Research(
					jsonNode.get("stageID").getAsInt(),
					jsonNode.get("stageIDName").getAsString(),
					jsonNode.get("displayItemID").getAsString(),
					jsonNode.get("displayName").getAsString(),
					jsonNode.get("displayLore").getAsString()
				);
				researchEntry.state = ResearchState.fromStateID(jsonNode.get("complete").getAsInt());
				for (JsonElement dependency : jsonNode.get("dependencies").getAsJsonArray().asList()) {
					researchEntry.dependencies.add(dependency.getAsInt());
				}
				researchEntries.add(researchEntry);
			}
			return researchEntries;
		} catch (Exception e) {
			ResearchMod.LOGGER.warn("ResearchAPI#getResearchTree failed to parse response: " + e.getMessage());
			return null;
		}
	}

	public static List<Research> getCompleteResearches()
	{
		List<Research> allResearches = getResearchTree();
		List<Research> completeResearches = new ArrayList<>();
		if (allResearches == null) return null;
		for (Research research : allResearches) {
			if (research.state != ResearchState.COMPLETE) continue;
			completeResearches.add(research);
		}
		return completeResearches;
	}

	public static ResearchProgress getResearchProgress()
	{
		try {
			APIResponse response = APIRequest.apiRequest(neoNetworkResearchEndpoint + "getresearchcounts", new HashMap<>() {{
				put("apikey", apiKey);
			}});
			if (!response.getSuccess()) {
				ResearchMod.LOGGER.warn("ResearchAPI#getResearchProgress failed. Code: {}, Message: {}",
					response.getStatusCode(), response.getStatusMessage());
				return null;
			}

			JsonObject data = response.getDataNode().getAsJsonObject();
			return new ResearchProgress(
				data.get("stageID").getAsInt(),
				ResearchState.fromStateID(data.get("complete").getAsInt()),
				data.get("A1Count").getAsInt(),
				data.get("A2Count").getAsInt(),
				data.get("A3Count").getAsInt(),
				data.get("B1Count").getAsInt(),
				data.get("B2Count").getAsInt(),
				data.get("B3Count").getAsInt(),
				data.get("C1Count").getAsInt(),
				data.get("C2Count").getAsInt(),
				data.get("C3Count").getAsInt()
			);
		} catch (Exception e) {
			ResearchMod.LOGGER.warn("ResearchAPI#getResearchProgress failed to parse response");
			return null;
		}
	}

	public static ResearchDetails getCurrentResearch()
	{
		try {
			APIResponse response = APIRequest.apiRequest(neoNetworkResearchEndpoint + "getresearch", new HashMap<>() {{
				put("apikey", apiKey);
			}});
			if (!response.getSuccess()) {
				ResearchMod.LOGGER.warn("ResearchAPI#getCurrentResearch failed. Code: {}, Message: {}",
					response.getStatusCode(), response.getStatusMessage());
				return null;
			}

			JsonObject data = response.getDataNode().getAsJsonObject();
			return new ResearchDetails(
				data.get("stageID").getAsInt(),
				data.get("stageIDName").getAsString(),
				ResearchState.fromStateID(data.get("complete").getAsInt()),
				data.get("displayName").getAsString(),
				data.get("displayItemID").getAsString(),
				data.get("displayLore").getAsString(),
				data.get("ItemTypeADisplayName").getAsString(),
				new ResearchItem(
					data.get("ItemA1ID").getAsString(),
					data.get("ItemA1DisplayName").getAsString(),
					data.get("ItemA1Count").getAsInt()
				),
				new ResearchItem(
					data.get("ItemA2ID").getAsString(),
					data.get("ItemA2DisplayName").getAsString(),
					data.get("ItemA2Count").getAsInt()
				),
				new ResearchItem(
					data.get("ItemA3ID").getAsString(),
					data.get("ItemA3DisplayName").getAsString(),
					data.get("ItemA3Count").getAsInt()
				),
				data.get("ItemTypeBDisplayName").getAsString(),
				new ResearchItem(
					data.get("ItemB1ID").getAsString(),
					data.get("ItemB1DisplayName").getAsString(),
					data.get("ItemB1Count").getAsInt()
				),
				new ResearchItem(
					data.get("ItemB2ID").getAsString(),
					data.get("ItemB2DisplayName").getAsString(),
					data.get("ItemB2Count").getAsInt()
				),
				new ResearchItem(
					data.get("ItemB3ID").getAsString(),
					data.get("ItemB3DisplayName").getAsString(),
					data.get("ItemB3Count").getAsInt()
				),
				data.get("ItemTypeCDisplayName").getAsString(),
				new ResearchItem(
					data.get("ItemC1ID").getAsString(),
					data.get("ItemC1DisplayName").getAsString(),
					data.get("ItemC1Count").getAsInt()
				),
				new ResearchItem(
					data.get("ItemC2ID").getAsString(),
					data.get("ItemC2DisplayName").getAsString(),
					data.get("ItemC2Count").getAsInt()
				),
				new ResearchItem(
					data.get("ItemC3ID").getAsString(),
					data.get("ItemC3DisplayName").getAsString(),
					data.get("ItemC3Count").getAsInt()
				)

			);
		} catch (Exception e) {
			ResearchMod.LOGGER.warn("ResearchAPI#getCurrentResearch failed to parse response");
			return null;
		}
	}

	public static boolean setResearch(int stageID) {
		return setResearch(stageID, false) > 0;
	}

	public static boolean setResearchForce(int stageID) {
		return setResearch(stageID, true) > 0;
	}

	public static boolean clearResearch() {
		return setResearch(-1, true) == 0;
	}

	private static int setResearch(int stageID, boolean force)
	{
		try {
			APIResponse response = APIRequest.apiRequest(neoNetworkResearchEndpoint + "getresearchcounts", new HashMap<>() {{
				put("apikey", apiKey);
			}});
			if (!response.getSuccess()) {
				ResearchMod.LOGGER.warn("ResearchAPI#setResearch failed. Code: {}, Message: {}",
					response.getStatusCode(), response.getStatusMessage());
				return -1;
			}

			return response.getDataNode().getAsJsonObject().get("stageID").getAsInt();
		} catch (Exception e) {
			ResearchMod.LOGGER.warn("ResearchAPI#setResearch failed to parse response");
			return -1;
		}
	}

	public static void updateResearchProgress()
	{

	}







}
