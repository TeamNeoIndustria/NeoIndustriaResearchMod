package net.torchednova.researchmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.torchednova.researchmod.Config;
import net.torchednova.researchmod.research.ResearchController;

import java.util.ArrayList;

public class SetCountCount {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("neoresearchshottime").requires(source -> source.hasPermission(2))
			.then(Commands.argument("count", IntegerArgumentType.integer())
				.executes(SetCountCount::setshorttimecount)
			)
		);
	}

	public static int setshorttimecount(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		int count = IntegerArgumentType.getInteger(context, "count");

		Config.VotesToLowerTime.set(count);
		Config.VotesToLowerTime.save();

		return 1;
	}
}
