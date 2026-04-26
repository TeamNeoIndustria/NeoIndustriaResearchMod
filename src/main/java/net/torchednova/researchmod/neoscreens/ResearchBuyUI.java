package net.torchednova.researchmod.neoscreens;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.torchednova.researchmod.api.ResearchAPI;
import net.torchednova.researchmod.research.Research;
import net.torchednova.researchmod.utils.Utils;
import xyz.neonetwork.neolib.gui.NeoStringAlign;
import xyz.neonetwork.neolib.servergui.NeoServerScreen;
import xyz.neonetwork.neolib.servergui.NeoServerScreenGrid;
import xyz.neonetwork.neolib.textures.NeoTexture;
import xyz.neonetwork.neolib.utilities.NeoNotify;

import java.util.List;

import static net.torchednova.researchmod.ResearchMod.LOGGER;


public class ResearchBuyUI {
	private static final int pageSize = 6;


	public static void MainScreen(ServerPlayer sp)
	{
		MainScreen(sp, 0);
	}
	public static void MainScreen(ServerPlayer sp, int page)
	{


		NeoServerScreenGrid nssg = new NeoServerScreenGrid(20, 20, 2, 8,8);
		nssg.addButtonWidget(2, 7, 4, 1, "close", Component.literal("Close"), null, false, (finalScreen, finalGrid) -> {
			finalScreen.close(); });


		List<Research> avResearch = ResearchAPI.getCompleteResearches();


		if (avResearch == null)
		{
			nssg.addStringWidget(0, 1, 8, 6, "none", Component.literal("You do not have any available researches to purchase"), NeoStringAlign.Horizontal.CENTER, NeoStringAlign.Vertical.MIDDLE);
		}
		else
		{
			//LOGGER.info(String.valueOf(avResearch.size()));
			for (int i = 0; i < avResearch.size(); i++ )
			{
				if (!Utils.playerCanUnlockStage(sp, avResearch.get(i), avResearch))
				{
					avResearch.remove(i);
					i--;
				}
			}
		}
		if (avResearch.size() < 1)
		{
			nssg.addStringWidget(0, 1, 8, 6, "none", Component.literal("You do not have any available researches to purchase"), NeoStringAlign.Horizontal.CENTER, NeoStringAlign.Vertical.MIDDLE);
		}
		for (int i = pageSize * page; i < pageSize * (page + 1); i++)
		{
			if (i >= avResearch.size()) break;

			LOGGER.info(avResearch.get(i).splitItemID()[0] + " " + avResearch.get(i).splitItemID()[1]);

			//nssg.addItemWidget(0, 0, 5, 5, "CakeItem", BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "cake")));

			if (i % 2 == 0)
			{
				nssg.addItemWidget(0, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "dirt")));
				//nssg.addItemWidget(0, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(avResearch.get(i).splitItemID()[0], avResearch.get(i).splitItemID()[1])));
				int finalI = i;
				nssg.addButtonWidget(1, 1 + (((i - (pageSize * page)) / 2) * 2), 3, 1, avResearch.get(i).displayname, Component.literal(avResearch.get(i).displayname), Component.literal(avResearch.get(i).lore), false, (finalScreen, finalGrid) -> { unlockStage(sp, avResearch.get(finalI)); MainScreen(sp, page);});
			}
			else
			{
				nssg.addItemWidget(4, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "dirt")));
				//nssg.addItemWidget(4, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(avResearch.get(i).splitItemID()[0], avResearch.get(i).splitItemID()[1])));
				int finalI = i;
				nssg.addButtonWidget(5, 1 + (((i - (pageSize * page)) / 2) * 2), 3, 1, avResearch.get(i).displayname, Component.literal(avResearch.get(i).displayname), null, false, (finalScreen, finalGrid) -> { unlockStage(sp, avResearch.get(finalI)); MainScreen(sp, page);});
			}

		}

		//nssg.addItemWidget(0, 0, 1, 1, "image", BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(avResearch.get(0).splitItemID()[0], avResearch.get(0).splitItemID()[1])));

		nssg.addButtonWidget(7, 7, 1, 1, "next", Component.literal(">"), null, !(avResearch.size() > (pageSize * (page + 1))), (finalScreen, finalGrid) -> { MainScreen(sp, page+1);});
		nssg.addButtonWidget(0, 7, 1, 1, "back", Component.literal("<"), null, !(page > 0), (finalScreen, finalGrid) -> {MainScreen(sp, page-1);});




		NeoServerScreen nss = new NeoServerScreen(sp, Component.literal("Researches"), NeoTexture.RESEARCH,
			nssg, (finalPlayer, finalGrid) -> {/*close event*/});
		nss.show(true);

	}

	private static void unlockStage(ServerPlayer sp, Research res)
	{
		if (sp == null || sp.getServer() == null) return;

		CommandSourceStack css = sp.getServer().createCommandSourceStack();
		var disp = sp.getServer().getCommands().getDispatcher();
		ParseResults<CommandSourceStack> parse = disp.parse("astages add " + sp.getDisplayName().getString() + " " + res.name + " true true", css);
		//astages add NovaAssassin mek_basic true true

		sp.getServer().getCommands().performCommand(parse, "");
		NeoNotify.sendTitle(sp, Component.literal("Unlocked " + res.displayname), null);
		//NeoNotify.playSound(sp, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.MASTER, 10.0f, 1.f);
		sp.serverLevel().playSound(
			null,
			sp.getX(),
			sp.getY(),
			sp.getZ(),
			SoundEvents.EXPERIENCE_ORB_PICKUP,
			SoundSource.MASTER,
			10.0F,
			1.0F
		);
	}


}



