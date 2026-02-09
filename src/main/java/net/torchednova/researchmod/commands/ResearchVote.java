package net.torchednova.researchmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.torchednova.researchmod.ChestUI.ChestUI;

public class ResearchVote {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("ViewShops")
            .then(Commands.argument("player", EntityArgument.player())
                .executes(ResearchVote::OpenVoteMenu)

        ));
    }

    public static int OpenVoteMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = EntityArgument.getPlayer(context, "player");

        ChestUI.openVoteMenu(player, 54, context.getSource().getLevel());
        return 1;
    }

}
