package net.torchednova.researchmod.savedata;

import com.google.common.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.torchednova.researchmod.research.Research;
import net.torchednova.researchmod.research.ResearchController;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TargetDataStorage {
    private static final Type LIST_TYPE = new TypeToken<List<Research>>() {}.getType();
    private static final Type RESEARCH_TYPE = new TypeToken<Research>() {}.getType();

    public static void save(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson(ResearchController.Finshed);
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Research> load(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getDataFile(server);

            if (Files.exists(file) == false)
            {
                return new ArrayList<Research>();
            }

            String json = Files.readString(file);

            ArrayList<Research> data = ModJson.GSON.fromJson(json, LIST_TYPE);

            return data != null ? data : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveCurrent(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson(ResearchController.Finshed);
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Research loadCurrent(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getDataFile(server);

            if (Files.exists(file) == false)
            {
                return new Research(0, null, null, null);
            }

            String json = Files.readString(file);

            Research data = ModJson.GSON.fromJson(json, RESEARCH_TYPE);

            return data != null ? data : new Research(0, null, null, null);

        } catch (IOException e) {
            e.printStackTrace();
            return new Research(0, null, null, null);
        }
    }
}
