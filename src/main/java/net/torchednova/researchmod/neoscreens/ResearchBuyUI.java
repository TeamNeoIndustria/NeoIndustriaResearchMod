package net.torchednova.researchmod.neoscreens;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neolib.servergui.NeoServerScreen;
import xyz.neonetwork.neolib.servergui.NeoServerScreenGrid;
import xyz.neonetwork.neolib.textures.NeoTexture;


public class ResearchBuyUI {



	public static void MainScreen(ServerPlayer sp)
	{



		NeoServerScreenGrid nssg = new NeoServerScreenGrid(20, 20, 2, 8,8);
		NeoServerScreen nss = new NeoServerScreen(sp, Component.literal("Researches"), NeoTexture.RESEARCH,
			nssg, (finalPlayer, finalGrid) -> {/*close event*/});
	}
}



