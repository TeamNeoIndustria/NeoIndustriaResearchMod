package net.torchednova.researchmod.neoscreens;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.torchednova.researchmod.api.ResearchAPI;
import net.torchednova.researchmod.research.Research;
import xyz.neonetwork.neolib.gui.NeoStringAlign;
import xyz.neonetwork.neolib.servergui.NeoServerScreen;
import xyz.neonetwork.neolib.servergui.NeoServerScreenGrid;
import xyz.neonetwork.neolib.textures.NeoTexture;

import java.util.List;


public class ResearchBuyUI {



	public static void MainScreen(ServerPlayer sp)
	{

		NeoServerScreenGrid nssg = new NeoServerScreenGrid(20, 20, 2, 8,8);
		nssg.addButtonWidget(0, 7, 8, 1, "close", Component.literal("Close"), null, false, (finalScreen, finalGrid) -> {
			System.out.print("------------------------here____-------------------------------------------------------");
			finalScreen.close(); });


		List<Research> avResearch;// = ResearchAPI.getCompleteResearches();
		avResearch = null;
		if (avResearch == null)
		{
			nssg.addStringWidget(0, 1, 8, 6, "none", Component.literal("You do not have any available researches to purchase"), NeoStringAlign.Horizontal.CENTER, NeoStringAlign.Vertical.MIDDLE);
		}
		else
		{
			for (int i = 0; i < avResearch.size(); i++ )
			{

			}
		}




		NeoServerScreen nss = new NeoServerScreen(sp, Component.literal("Researches"), NeoTexture.RESEARCH,
			nssg, (finalPlayer, finalGrid) -> {/*close event*/});
		nss.show(true);

	}
}



