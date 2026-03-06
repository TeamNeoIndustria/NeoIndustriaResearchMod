package net.torchednova.researchmod.utils;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

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


}
