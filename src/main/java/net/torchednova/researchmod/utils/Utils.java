package net.torchednova.researchmod.utils;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.torchednova.researchmod.research.Research;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static Component Chat(String message, Object... args) {
        return Component.literal(String.format(message.replace("&", "§"), args));
    }

    public static void tellAll(String msg, MinecraftServer server)
    {
        if (server.getPlayerList().getPlayerCount() == 0) return;

        CommandSourceStack css;
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (int i = 0; i < players.size(); i++)
        {
            if (players.get(i).hasDisconnected()) continue;
            css = players.get(i).createCommandSourceStack();
            css.sendSuccess(
                    () -> Component.literal(msg),
                    false
            );
        }
    }

    public static boolean playerHasDependencies(ServerPlayer sp, Research r, List<Research> res)
    {
        AStagesUtil.hasStage(sp, r.name);
        for (int i = 0; i < r.dependencies.size(); i++)
        {
            for (int ii = 0; ii < res.size(); ii++) {
                if (res.get(ii).id != r.dependencies.get(i)) continue;

                if (!AStagesUtil.hasStage(sp, res.get(ii).name)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean playerCanUnlockStage(ServerPlayer sp, Research r, List<Research> res)
    {
        if (AStagesUtil.hasStage(sp, r.name) == true)
        {
            return false;
        }

        return playerHasDependencies(sp, r, res);
    }



}
