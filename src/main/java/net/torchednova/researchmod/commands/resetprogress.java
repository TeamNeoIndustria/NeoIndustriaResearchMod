package net.torchednova.researchmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.torchednova.researchmod.ChestUI.ChestUI;
import net.torchednova.researchmod.research.ResearchController;

import java.util.ArrayList;

public class resetprogress {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("resetresearch").requires(source -> source.hasPermission(2))
            .executes(resetprogress::openresetcommand)
        );
    }

    public static int openresetcommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        new Thread(() -> {
            ResearchController.setCurrent();
            ResearchController.setFinished();
            ResearchController.setOptions();

            ResearchController.voteTicks = -1;
            ResearchController.currentVotes = 0;
            ResearchController.playersVotes = new ArrayList<>();

            ResearchController.closing(context.getSource().getServer());
        }).start();


        return 1;
    }
}
