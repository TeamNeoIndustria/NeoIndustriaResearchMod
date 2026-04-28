package net.torchednova.researchmod.neoscreens;

import com.mojang.brigadier.ParseResults;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.torchednova.researchmod.ResearchMod;
import net.torchednova.researchmod.api.IRSAPI;
import net.torchednova.researchmod.api.ResearchAPI;
import net.torchednova.researchmod.research.PlayerResearchDetails;
import net.torchednova.researchmod.research.Research;
import net.torchednova.researchmod.utils.Utils;
import xyz.neonetwork.neobanking.api.IRS;
import xyz.neonetwork.neobanking.api.IRSPaymentState;
import xyz.neonetwork.neobanking.paymentprocessor.CurrencyHandler;
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
		//ResearchMod.LOGGER.info("main functino firing");
		MainScreen(sp, 0);
	}
	public static void MainScreen(ServerPlayer sp, int page)
	{

		//ResearchMod.LOGGER.info("button function firing");

		NeoServerScreenGrid nssg = new NeoServerScreenGrid(20, 20, 2, 8,8);
		nssg.addButtonWidget(2, 7, 4, 1, "close", Component.literal("Close"), null, false, (finalScreen, finalGrid) -> {
			finalScreen.close(); });


		List<Research> avResearch = ResearchAPI.getCompleteResearches();


		if (avResearch == null)
		{
			//ResearchMod.LOGGER.info("No research found");
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
					//ResearchMod.LOGGER.info(String.valueOf(i));
					i--;
				}
			}
		}
		if (avResearch == null || avResearch.size() < 1)
		{
			//ResearchMod.LOGGER.info("After removing unavaible stuff none left");
			nssg.addStringWidget(0, 1, 8, 6, "none", Component.literal("You do not have any available researches to purchase"), NeoStringAlign.Horizontal.CENTER, NeoStringAlign.Vertical.MIDDLE);
		}
		for (int i = pageSize * page; i < pageSize * (page + 1); i++)
		{
			if (i >= avResearch.size()) break;

			//LOGGER.info(avResearch.get(i).splitItemID()[0] + " " + avResearch.get(i).splitItemID()[1]);

			//nssg.addItemWidget(0, 0, 5, 5, "CakeItem", BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "cake")));

			PlayerResearchDetails prd = ResearchAPI.getPlayerResearch(avResearch.get(i).id);
			String unlockCost = "Cost: "+ prd.moneyCost + " Items: " + prd.itemA1.count + " " + prd.itemA1.displayName;
			if (prd.itemB1.isValid()) unlockCost += ", " + prd.itemB1.count + " " + prd.itemB1.displayName;
			if (prd.itemC1.isValid()) unlockCost += ", " + prd.itemC1.count + " " + prd.itemC1.displayName;

			if (i % 2 == 0)
			{
				nssg.addItemWidget(0, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "dirt")));
				//nssg.addItemWidget(0, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(avResearch.get(i).splitItemID()[0], avResearch.get(i).splitItemID()[1])));
				int finalI = i;
				nssg.addButtonWidget(1, 1 + (((i - (pageSize * page)) / 2) * 2), 3, 1, avResearch.get(i).displayname, Component.literal(avResearch.get(i).displayname), Component.literal(avResearch.get(i).lore + "\n" + unlockCost), false, (finalScreen, finalGrid) -> { unlockStage(sp, avResearch.get(finalI)); MainScreen(sp, page);});
			}
			else
			{
				nssg.addItemWidget(4, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "dirt")));
				//nssg.addItemWidget(4, 1 + (((i - (pageSize * page)) / 2) * 2), 1, 1, "image" + i, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(avResearch.get(i).splitItemID()[0], avResearch.get(i).splitItemID()[1])));
				int finalI = i;
				nssg.addButtonWidget(5, 1 + (((i - (pageSize * page)) / 2) * 2), 3, 1, avResearch.get(i).displayname, Component.literal(avResearch.get(i).displayname), Component.literal(avResearch.get(i).lore + "\n" + unlockCost), false, (finalScreen, finalGrid) -> { unlockStage(sp, avResearch.get(finalI)); MainScreen(sp, page);});
			}

		}

		//nssg.addItemWidget(0, 0, 1, 1, "image", BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(avResearch.get(0).splitItemID()[0], avResearch.get(0).splitItemID()[1])));

		nssg.addButtonWidget(7, 7, 1, 1, "next", Component.literal(">"), null, !(avResearch.size() > (pageSize * (page + 1))), (finalScreen, finalGrid) -> {MainScreen(sp, page+1);});
		nssg.addButtonWidget(0, 7, 1, 1, "back", Component.literal("<"), null, !(page > 0), (finalScreen, finalGrid) -> {MainScreen(sp, page-1);});




		NeoServerScreen nss = new NeoServerScreen(sp, Component.literal("Researches"), NeoTexture.RESEARCH,
			nssg, (finalPlayer, finalGrid) -> {/*close event*/});
		nss.show(true);

	}

	private static void unlockStage(ServerPlayer sp, Research res)
	{
		if (sp == null || sp.getServer() == null) return;

		CommandSourceStack css = sp.getServer().createCommandSourceStack();

		boolean doUnlock = false;
		boolean cashcredit = false;

		boolean hasa = false;
		boolean hasb = false;
		boolean hasc = false;

		PlayerResearchDetails prd = ResearchAPI.getPlayerResearch(res.id);
		if (prd == null) return;
		if (CurrencyHandler.calculateSimpleInventoryValue(sp) >= prd.moneyCost)
		{
			//LOGGER.info("inv money found");
			//CurrencyHandler.removeValueFromInventory(sp, prd.moneyCost)
			doUnlock = true;
			cashcredit = false;
		}
		else if (prd.moneyCost <= IRS.getUserBalance(sp.getStringUUID()))
		{
			//LOGGER.info("account money found");
			//IRS.serverReceiveMoney(sp.getStringUUID(), prd.moneyCost, res.displayname + " unlock");
			doUnlock = true;
			cashcredit = true;
		}
		else {
			//LOGGER.info("no money found");
			return;
		}

		if (prd.itemA1.isValid())
		{
			if (Utils.playerHasItems(sp, new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(prd.itemA1.itemID.split(":")[0], prd.itemA1.itemID.split(":")[1]))), prd.itemA1.count))
			{
				//LOGGER.info("found item a found");
				doUnlock = true;
				hasa = true;
			}
			else
			{
				doUnlock = false;
			}
		}
		if (prd.itemB1.isValid())
		{
			if (Utils.playerHasItems(sp, new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(prd.itemB1.itemID.split(":")[0], prd.itemB1.itemID.split(":")[1]))), prd.itemB1.count))
			{
				doUnlock = true;
				hasb = true;
			}
			else
			{
				//LOGGER.info("failing item b found");
				doUnlock = false;
			}
		}
		if (prd.itemC1.isValid())
		{
			if (Utils.playerHasItems(sp, new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(prd.itemC1.itemID.split(":")[0], prd.itemC1.itemID.split(":")[1]))), prd.itemC1.count))
			{
				doUnlock = true;
				hasc = true;
			}
			else
			{
				//LOGGER.info("failing item c found");
				doUnlock = false;
			}
		}

		if (doUnlock == false) return;

		if (cashcredit == true)
		{
			if (!(IRS.serverReceiveMoney(sp.getStringUUID(), prd.moneyCost, res.displayname + " unlock").getState() == IRSPaymentState.ACCEPTED))
			{
				return;
			}
		}
		else
		{
			if (!CurrencyHandler.removeValueFromInventory(sp, prd.moneyCost))
			{
				return;
			}
		}

		if (hasa == true)
		{
			if (!Utils.playerTakeItems(sp, new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(prd.itemA1.itemID.split(":")[0], prd.itemA1.itemID.split(":")[1]))), prd.itemA1.count))
			{
				return;
			}
		}
		if (hasb == true)
		{
			if (!Utils.playerTakeItems(sp, new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(prd.itemB1.itemID.split(":")[0], prd.itemB1.itemID.split(":")[1]))), prd.itemB1.count))
			{
				return;
			}
		}
		if (hasc == true)
		{
			if (!Utils.playerTakeItems(sp, new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(prd.itemC1.itemID.split(":")[0], prd.itemC1.itemID.split(":")[1]))), prd.itemC1.count))
			{
				return;
			}
		}




		var disp = sp.getServer().getCommands().getDispatcher();
		ParseResults<CommandSourceStack> parse = disp.parse("astages add " + sp.getDisplayName().getString() + " " + res.name + " true true", css);



		sp.getServer().getCommands().performCommand(parse, "");
		NeoNotify.sendTitle(sp, Component.literal("Unlocked " + res.displayname).withStyle(ChatFormatting.GREEN), null);
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



