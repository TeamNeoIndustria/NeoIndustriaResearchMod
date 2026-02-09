package net.torchednova.researchmod.research;



import net.torchednova.researchmod.Config;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ResearchController {
    public static ArrayList<Research> Finshed;
    public static ArrayList<Research> Options;
    public static Research current;

    public static int currentVotes = 0;

    public static ArrayList<UUID> playersVotes;

    public static int tickTimeForVote = 0;
    public static int voteTicks = 0;



    public static void init()
    {
        Finshed = new ArrayList<>();
        Options = new ArrayList<>();
        current = null;
        playersVotes = new ArrayList<>();
    }

    public static void newOptions()
    {
        playersVotes = new ArrayList<>();
        Options = new ArrayList<>();

    }

    public static void finishedCurrentResearch()
    {
        Finshed.add(current);
        current = null;
        newOptions();
    }

    public static void SelectResearch()
    {
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

        if (selected != null)
        {
            current = selected;
            currentVotes = 0;
            Options.clear();

            //make call to API to set
        }

    }

    public static void setOptions()
    {
        //get new options from API

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
