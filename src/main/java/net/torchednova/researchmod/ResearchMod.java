package net.torchednova.researchmod;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.torchednova.researchmod.api.IRSAPI;
import net.torchednova.researchmod.commands.ResearchBuy;
import net.torchednova.researchmod.commands.ResearchVote;
import net.torchednova.researchmod.commands.SetCountCount;
import net.torchednova.researchmod.commands.resetprogress;
import net.torchednova.researchmod.research.ResearchController;
import net.torchednova.researchmod.savedata.TargetDataStorage;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.neonetwork.neolib.textures.NeoTexture;
import xyz.neonetwork.neolib.utilities.NeoNotify;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ResearchMod.MODID)
public class ResearchMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "researchmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ResearchMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);


        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    private void onServerTick(ServerTickEvent.Post event)
    {
        if(ResearchController.voteTicks != -1 && ResearchController.currentVotes != 0)
        {
            //LOGGER.info(ResearchController.voteTicks + " | " + ResearchController.tickTimeForVote);
            ResearchController.voteTicks++;
            if (ResearchController.voteTicks >= ResearchController.tickTimeForVote) {
                new Thread(() -> { ResearchController.SelectResearch(event.getServer()); }).start();
            }
        }

        ResearchController.timenotchecked++;
        if (ResearchController.timenotchecked >= ResearchController.tickstillcheck)
        {
            new Thread(() -> { ResearchController.checkIfDone(event.getServer()); }).start();
            ResearchController.timenotchecked = 0;
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player p = event.getEntity();
        if (ResearchController.voteTicks != -1) {
            LOGGER.info(String.valueOf(ResearchController.voteTicks));
            if (p instanceof ServerPlayer) {
                int timeleft = ((ResearchController.tickTimeForVote - ResearchController.voteTicks) / 20);
                String msg = "";
                if ((ResearchController.voteTicks == 0 || ResearchController.voteTicks == -1) && ResearchController.currentVotes == 0)
                {
                    msg = "Timer will start when the first person has voted";
                }
                else if (timeleft < 60)
                {
                    msg = String.valueOf(timeleft) + " Seconds Left";
                }
                else if (timeleft > 60 && timeleft < 3600)
                {
                    msg = String.valueOf(timeleft / 60) + " Minutes Left";
                }
                else
                {
                    msg = String.valueOf((timeleft / 60) /60) + " Hours Left";
                }
                CommandSourceStack css = p.createCommandSourceStack();
                String finalMsg = msg;
                css.sendSuccess(
                        () -> Component.literal("Voting is open for the next research, you have " + finalMsg),
                        false
                );
            }
        }
        else
        {
            if (p instanceof ServerPlayer sp) {
                NeoNotify.sendToast(sp, Component.literal("Voting is currently open"), Component.literal("for new research"), NeoTexture.RESEARCH);
            }
        }
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        IRSAPI.init(TargetDataStorage.loadAPI(event.getServer()), TargetDataStorage.loadDispAPI(event.getServer()));

        ResearchController.init(event.getServer());

    }

    @SubscribeEvent
    public void onRegisterCommnads(RegisterCommandsEvent event)
    {
        ResearchVote.register(event.getDispatcher());
        resetprogress.register(event.getDispatcher());
        ResearchBuy.register(event.getDispatcher());
        SetCountCount.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        ResearchController.closing(event.getServer());
    }
}
