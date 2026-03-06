package net.torchednova.researchmod.savedata;

import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public class ModDataPath {
    public static Path getDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("research.json");
    }

    public static Path getCurrentDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("current.json");
    }

    public static Path getOptionsDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("options.json");
    }

    public static Path getVotedDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("voted.json");
    }

    public static Path getcurvotesDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("curvotes.json");
    }

    public static Path getAPIKeyDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("apikey.json");
    }

    public static Path getDispAPIKeyDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("dispapikey.json");
    }

    public static Path getTickTimingsDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("neoResearch").resolve("ticktimings.json");
    }
}
