package net.torchednova.researchmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.jdi.connect.Connector;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.torchednova.researchmod.ChestUI.ChestUI;
import net.torchednova.researchmod.research.ResearchController;

public class ResearchVote {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("research").requires(source -> source.hasPermission(2))
            .then(Commands.argument("player", EntityArgument.player())
                .executes(ResearchVote::OpenVoteMenu)

            )
            .then(Commands.literal("endvote").requires(source -> source.hasPermission(2))
                .executes(ResearchVote::endvote)
            )
            .then(Commands.literal("info").requires(source -> source.hasPermission(2))
                .executes(ResearchVote::info)
            )
            .then(Commands.literal("load").requires(source -> source.hasPermission(2))
                .executes(ResearchVote::load)
            )
            .then(Commands.literal("settick").requires(source -> source.hasPermission(2))
                .executes(ResearchVote::set)
            )
        );
    }

    public static int OpenVoteMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = EntityArgument.getPlayer(context, "player");

        ChestUI.openVoteMenu(player, 54, context.getSource().getLevel());
        return 1;
    }

    public static int endvote(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        context.getSource().sendSuccess(() ->
                Component.literal("Attemping to end voting period"),
            false);
        ResearchController.SelectResearch(context.getSource().getServer());
        return 1;
    }

    public static int info(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        context.getSource().sendSuccess(() ->
            Component.literal("Ticks done " + ResearchController.voteTicks + " | Ticks Needed " + ResearchController.tickTimeForVote),
            false);
        context.getSource().sendSuccess(() ->
                Component.literal("Total Votes " + ResearchController.currentVotes),
            false);
        return 1;
    }

    public static int load(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        ResearchController.load(context.getSource().getServer());
        info(context);
        return 1;
    }

    public static int set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        ResearchController.voteTicks = 100;
        context.getSource().sendSuccess(() ->
                Component.literal("Set Ticked time to 100"),
            false);
        return 1;
    }

}
