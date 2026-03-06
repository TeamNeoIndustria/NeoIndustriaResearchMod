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
import java.util.UUID;

public class TargetDataStorage {
    private static final Type LIST_TYPE = new TypeToken<List<Research>>() {}.getType();
    private static final Type RESEARCH_TYPE = new TypeToken<Research>() {}.getType();
    private static final Type String_TYPE = new TypeToken<String>() {}.getType();
    private static final Type INT_TYPE = new TypeToken<Integer>() {}.getType();
    private static final Type UUID_LIST_TYPE = new TypeToken<List<UUID>>() {}.getType();
    private static final Type INT_LIST_TYPE = new TypeToken<List<Integer>>() {}.getType();


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
            if (json.length() < 4) return new ArrayList<Research>();

            ArrayList<Research> data = ModJson.GSON.fromJson(json, LIST_TYPE);

            return data != null ? data : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveOptions(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getOptionsDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson(ResearchController.Options);

            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Research> loadOptions(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getOptionsDataFile(server);

            if (Files.exists(file) == false)
            {
                return new ArrayList<Research>();
            }

            String json = Files.readString(file);
            if (json.length() < 4) return new ArrayList<Research>();

            ArrayList<Research> data = ModJson.GSON.fromJson(json, LIST_TYPE);

            return data != null ? data : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveAPI(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getAPIKeyDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson("API KEY HERE");
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String loadAPI(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getAPIKeyDataFile(server);

            if (Files.exists(file) == false)
            {
                saveAPI(server);
            }

            String json = Files.readString(file);

            String data = ModJson.GSON.fromJson(json, String_TYPE);

            return data != null ? data : "";

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void saveDispAPI(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getDispAPIKeyDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson("API KEY HERE");
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String loadDispAPI(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getDispAPIKeyDataFile(server);

            if (Files.exists(file) == false)
            {
                saveDispAPI(server);
            }

            String json = Files.readString(file);

            String data = ModJson.GSON.fromJson(json, String_TYPE);

            return data != null ? data : "";

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void saveCurVotes(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getcurvotesDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson(ResearchController.currentVotes);
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static int loadCurVotes(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getcurvotesDataFile(server);

            if (Files.exists(file) == false)
            {
                saveCurVotes(server);
            }

            String json = Files.readString(file);

            int data = ModJson.GSON.fromJson(json, INT_TYPE);

            return data;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveCurrent(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getCurrentDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson(ResearchController.current);
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
            Path file = ModDataPath.getCurrentDataFile(server);

            if (Files.exists(file) == false)
            {
                saveCurrent(server);
                return new Research(0, null, null, null, null);
            }

            String json = Files.readString(file);
            if (json.length() < 4) return new Research(0, null, null, null, null);

            Research data = ModJson.GSON.fromJson(json, RESEARCH_TYPE);

            return data != null ? data : new Research(0, null, null, null, null);

        } catch (IOException e) {
            e.printStackTrace();
            return new Research(0, null, null, null, null);
        }
    }

    public static void savePlayerVoted(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getVotedDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson(ResearchController.playersVotes);
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<UUID> loadPlayerVoted(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getVotedDataFile(server);

            if (Files.exists(file) == false)
            {
                savePlayerVoted(server);
                return new ArrayList<UUID>();
            }

            String json = Files.readString(file);
            if (json.length() < 4) return new ArrayList<UUID>();

            ArrayList<UUID> data = ModJson.GSON.fromJson(json, UUID_LIST_TYPE);

            return data != null ? data : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<UUID>();
        }
    }

    public static void saveTickTimings(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getTickTimingsDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            ArrayList<Integer> tempList = new ArrayList<>();
            tempList.add(ResearchController.voteTicks);
            tempList.add(ResearchController.tickTimeForVote);

            String json = ModJson.GSON.toJson(tempList);
            tempList = null;
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> loadTickTimings(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getTickTimingsDataFile(server);

            if (Files.exists(file) == false)
            {
                saveTickTimings(server);
                return new ArrayList<Integer>();
            }

            String json = Files.readString(file);

            ArrayList<Integer> data = ModJson.GSON.fromJson(json, INT_LIST_TYPE);

            return data != null ? data : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Integer>();
        }
    }
}
