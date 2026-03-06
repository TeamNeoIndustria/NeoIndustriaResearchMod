package net.torchednova.researchmod.research;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.torchednova.researchmod.Config;
import net.torchednova.researchmod.api.IRSAPI;
import net.torchednova.researchmod.savedata.TargetDataStorage;
import net.torchednova.researchmod.utils.Utils;

import javax.swing.text.html.Option;

import static net.torchednova.researchmod.ResearchMod.LOGGER;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class ResearchController {
    public static ArrayList<Research> Finshed;
    public static ArrayList<Research> Options;
    public static Research current;

    public static int currentVotes = 0;

    public static ArrayList<UUID> playersVotes;

    public static int tickTimeForVote = 0;
    public static int voteTicks = -1;

    public static int timenotchecked = 0;
    public static int tickstillcheck = 0;



    public static void init(MinecraftServer server)
    {
        Finshed = TargetDataStorage.load(server);
        Options = TargetDataStorage.loadOptions(server);
        current = TargetDataStorage.loadCurrent(server);
        currentVotes = TargetDataStorage.loadCurVotes(server);
        playersVotes = TargetDataStorage.loadPlayerVoted(server);

        setCurrent();

        tickstillcheck = Config.timetoCheckDone.getAsInt() * 20;

        ArrayList<Integer> ticktimings = TargetDataStorage.loadTickTimings(server);
        if (!ticktimings.isEmpty() && ticktimings.size() >= 2)
        {
            voteTicks = ticktimings.get(0);
            tickTimeForVote = ticktimings.get(1);
        }

        if (Options.isEmpty())
        {
            setOptions();
        }
        if (current == null || current.name == null)
        {
            voteTicks = -1;
        }

        closing(server);
    }

    public static void closing(MinecraftServer server)
    {
        TargetDataStorage.save(server);
        TargetDataStorage.saveOptions(server);
        TargetDataStorage.saveCurrent(server);
        TargetDataStorage.saveCurVotes(server);
        TargetDataStorage.savePlayerVoted(server);
        TargetDataStorage.saveTickTimings(server);
    }

    public static Research getResearchOption(int id)
    {
        for (int i = 0; i < Options.size(); i++)
        {
            if (Options.get(i).id == id)
            {
                return Options.get(i);
            }
        }

        return null;
    }


    public static void checkIfDone(MinecraftServer server)
    {
        if (current == null) return;
        if (Finshed.contains(current)) return;

        //LOGGER.info(current.name);

        String SJSON = IRSAPI.checkCurrentResearch();
        JsonObject json = JsonParser.parseString(SJSON).getAsJsonObject();

        //System.out.print(json);



        if (Objects.equals(json.get("success").getAsString(), "false"))
        {
            return;
        }
        else if (json.get("data").getAsJsonObject().get("complete").getAsInt() == 1)
        {
            finishedCurrentResearch();
            Utils.tellAll("Research has completed please vote for the next", server);
        }
        closing(server);
    }

    public static void finishedCurrentResearch()
    {
        Finshed.add(current);
        current = null;
        setOptions();
    }

    public static void playerVote(Player p, int id)
    {

        if (ResearchController.playersVotes.contains(p.getUUID()))
        {
            return;
        }
        playersVotes.add(p.getUUID());
        currentVotes++;

        for (int i = 0; i < Options.size(); i++)
        {
            if (Options.get(i).id == id)
            {
                Options.get(i).votes++;
                break;
            }
        }

        if (ResearchController.currentVotes >= Config.VotesToLowerTime.getAsInt())
        {
            ResearchController.tickTimeForVote = (Config.LowerTimeForVote.getAsInt() * 60) * 20;
        }

        closing(p.getServer());
    }


    public static void SelectResearch(MinecraftServer server)
    {
        voteTicks = -1;
        Research selected = null;
        int maxCount = 0;
        for (int i = 0; i < Options.size(); i++)
        {
            maxCount += Options.get(i).votes;
        }

        if (maxCount == 0)
        {
            setOptions();
            return;
        }

        Random rng = new Random();
        maxCount = rng.nextInt(0, maxCount);

        for (int i = 0; i < Options.size(); i++)
        {
            if (maxCount < Options.get(i).votes)
            {
                selected = Options.get(i);
                break;
            }
        }

        //LOGGER.info(selected.name);

        if (selected != null)
        {
            current = selected;
            currentVotes = 0;
            Options.clear();
            playersVotes.clear();


            Utils.tellAll("Voting has finish and " + current.displayname + " has started researching", server);
            //make call to API to set
            IRSAPI.setResearch(current.id);
            closing(server);
        }


    }

    public static void setFinished()
    {
        //get new options from API
        String SJSON = IRSAPI.getAllResearch();
        JsonObject json = JsonParser.parseString(SJSON).getAsJsonObject();

        //System.out.print(json);

        if (Objects.equals(json.get("success").getAsString(), "false"))
        {
            LOGGER.error("Failed to talk to API - " + json.get("codeMessage").getAsString());
            setCurrent();
            return;
        }

        //LOGGER.info(json.getAsString());

        Options = new ArrayList<>();
        JsonObject curobj;


        //LOGGER.info(String.valueOf(json.get("data").getAsJsonArray().size()));
        //LOGGER.info(json.get("data").getAsJsonArray().get(0).getAsJsonObject().get("stageIDName").getAsString());

        for (int i = 0; i < json.get("data").getAsJsonArray().size(); i++)
        {
            curobj = json.get("data").getAsJsonArray().get(i).getAsJsonObject();
            if (curobj.get("complete").getAsInt() == 1) {
                Finshed.add(new Research(curobj.get("stageID").getAsInt(), curobj.get("stageIDName").getAsString(), curobj.get("displayItemID").getAsString(), curobj.get("displayName").getAsString(), curobj.get("displayLore").getAsString()));
                //LOGGER.info(curobj.get("stageIDName").getAsString());
            }
        }
    }

    public static void setCurrent()
    {
        String json = IRSAPI.checkCurrentResearch();
        JsonObject j = JsonParser.parseString(json).getAsJsonObject();
        if (j.get("success").getAsBoolean() == false) {
            current = null;
            return;
        }
        JsonObject jObj = j.get("data").getAsJsonObject();

        //System.out.print(jObj);

        current = new Research(jObj.get("stageID").getAsInt(), jObj.get("stageIDName").getAsString(), jObj.get("displayItemID").getAsString(), jObj.get("displayName").getAsString(), jObj.get("displayLore").getAsString());
    }

    public static void setOptions()
    {
        //get new options from API
        String SJSON = IRSAPI.getAvResearch();
        JsonObject json = JsonParser.parseString(SJSON).getAsJsonObject();
        //LOGGER.info(SJSON);

        //System.out.print(json);

        if (Objects.equals(json.get("success").getAsString(), "false"))
        {
            LOGGER.error("Failed to talk to API - " + json.get("codeMessage").getAsString());
            setCurrent();
            return;
        }

        //LOGGER.info(json.getAsString());

        Options = new ArrayList<>();
        JsonObject curobj;


        //LOGGER.info(String.valueOf(json.get("data").getAsJsonArray().size()));
        //LOGGER.info(json.get("data").getAsJsonArray().get(0).getAsJsonObject().get("stageIDName").getAsString());

        for (int i = 0; i < json.get("data").getAsJsonArray().size(); i++)
        {
            curobj = json.get("data").getAsJsonArray().get(i).getAsJsonObject();
            Options.add(new Research(curobj.get("stageID").getAsInt(), curobj.get("stageIDName").getAsString(), curobj.get("displayItemID").getAsString(), curobj.get("displayName").getAsString(), curobj.get("displayLore").getAsString()));
            //LOGGER.info(curobj.get("stageIDName").getAsString());
        }


        tickTimeForVote = (Config.TimeForVote.getAsInt() * 60) * 20;
        voteTicks = 0;
    }

    public static void vote(int id, UUID player)
    {
        if (playersVotes.contains(player)) return;

        for (int i = 0; i < Options.size(); i++)
        {
            if (Options.get(i).id == id)
            {
                playersVotes.add(player);
                Options.get(i).votes++;
                currentVotes++;
                if (currentVotes >= Config.VotesToLowerTime.getAsInt())
                {
                    tickTimeForVote = (Config.LowerTimeForVote.getAsInt() * 60) * 20;
                    voteTicks = 0;
                }
            }
        }
    }


}
