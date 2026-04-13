package net.torchednova.researchmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.torchednova.researchmod.ChestUI.ChestUI;
import net.torchednova.researchmod.neoscreens.ResearchBuyUI;

public class ResearchBuy {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("researchbuy")
			.then(Commands.argument("player", EntityArgument.player())
				.executes(ResearchBuy::OpenVoteMenu)
			));
	}

	public static int OpenVoteMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		if (context.getSource().getLevel().isClientSide()) return 1;
		Player player = EntityArgument.getPlayer(context, "player");

		ResearchBuyUI.MainScreen((ServerPlayer) player);
		return 1;
	}
}
