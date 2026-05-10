package net.torchednova.researchmod.ChestUI;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.torchednova.researchmod.ResearchMod;
import net.torchednova.researchmod.research.Research;
import net.torchednova.researchmod.research.ResearchController;
import net.torchednova.researchmod.utils.Utils;
import static net.torchednova.researchmod.ResearchMod.LOGGER;

import java.awt.*;
import java.util.List;

public class ResearchUI extends ChestMenu {
    private final SimpleContainer container;
    private int pagenum = 1;


    public ResearchUI (int id, Inventory playerInventory, SimpleContainer cont)
    {
        super(MenuType.GENERIC_9x6, id, playerInventory, cont, 6);
        pagenum = 1;
        this.container = cont;

        drawMenu(playerInventory.player.getServer(), playerInventory.player);

    }

    private void drawMenu(MinecraftServer server, Player player)
    {
        this.container.clearContent();

        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "blue_wool"));
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&fExit"));
        CompoundTag tag = new CompoundTag();
        tag.putString("vote:tag", "Exit");
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        this.container.setItem(49, itemStack);


        if (ResearchController.Options.size() > 4)
        {
            item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "red_wool"));
            itemStack = new ItemStack(item);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&fPrevious"));
            tag = new CompoundTag();
            tag.putString("vote:tag", "Prev");
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            this.container.setItem(45, itemStack);

            item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "green_wool"));
            itemStack = new ItemStack(item);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&fNext"));
            tag = new CompoundTag();
            tag.putString("vote:tag", "Next");
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            this.container.setItem(53, itemStack);
        }



        if (ResearchController.current == null || ResearchController.current.name == null)
        {
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

            item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "clock"));
            itemStack = new ItemStack(item);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&fTime Left to Vote"));
            tag = new CompoundTag();
            tag.putString("vote:tag", "cheese");
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            ItemLore im = new ItemLore(List.of(Utils.Chat("&f" + msg)));
            itemStack.set(DataComponents.LORE, im);
            this.container.setItem(4, itemStack);


            if (ResearchController.playersVotes.contains(player.getUUID()))
            {
                displayVotedPage(player.getServer());
            }
            else
            {
                displayMainPage(player.getServer());
            }

        }
        else
        {
            item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "barrier"));
            itemStack = new ItemStack(item);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&4Not Currently Voting"));
            tag = new CompoundTag();
            tag.putString("vote:tag", "cheese");
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            this.container.setItem(22, itemStack);
        }
    }


    private void displayVotedPage(MinecraftServer server)
    {
        int optionCount = ResearchController.Options.size();
        if (optionCount > 4) optionCount = 4;
        if (((pagenum) * 4 > ResearchController.Options.size()))
        {
            optionCount = ResearchController.Options.size() % 4;
            //LOGGER.info(String.valueOf(optionCount) + " | " + String.valueOf(ResearchController.Options.size()));
        }

        int spaces = optionCount - 1;


        int ii = 0;
        int offset = 17 + ((9 - (optionCount + spaces)) / 2);

        Item item;
        ItemStack itemStack;
        ItemLore im;
        CompoundTag tag;

        item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "lime_stained_glass_pane"));
        itemStack = new ItemStack(item);
        itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f"));
        tag = new CompoundTag();
        tag.putString("vote:tag", "cheese");
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        this.container.setItem(offset, itemStack);
        offset++;

        drawBorder(offset - 10, optionCount + (spaces + 2));

        for (int i = 0; i < optionCount; i++)
        {
            String[] name = ResearchController.Options.get(i+ (4 * (pagenum - 1))).itemID.split(":");
            item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(name[0], name[1]));
            itemStack = new ItemStack(item);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f" + ResearchController.Options.get(i+ (4 * (pagenum - 1))).displayname));
            im = new ItemLore(List.of(Utils.Chat("&f" + ResearchController.Options.get(i+ (4 * (pagenum - 1))).lore), Utils.Chat("( " + (ResearchController.Options.get(i+ (4 * (pagenum - 1))).votes / ResearchController.currentVotes) * 100 + " Percent Chance)")));
            itemStack.set(DataComponents.LORE, im);
            tag = new CompoundTag();
            tag.putString("vote:tag", String.valueOf(ResearchController.Options.get(i+ (4 * (pagenum - 1))).id));
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            this.container.setItem(i + offset + ii, itemStack);

            if (ii < spaces + 1)
            {
                item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "lime_stained_glass_pane"));
                itemStack = new ItemStack(item);
                itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f"));
                tag = new CompoundTag();
                tag.putString("vote:tag", "cheese");
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                this.container.setItem(i + offset + ii + 1, itemStack);
                ii++;
            }

            if (1 + i + (4 * (pagenum - 1)) >= ResearchController.Options.size()) return;
            if (i > 3) return;
        }

        return;
    }


    private void displayMainPage(MinecraftServer s)
    {

        Item item;
        ItemStack itemStack;
        CompoundTag tag;
        ItemLore im;

        //LOGGER.info(String.valueOf(ResearchController.Options.size()));
        int optionCount = ResearchController.Options.size();
        if (((pagenum) * 4 > ResearchController.Options.size()))
        {
            optionCount = ResearchController.Options.size() % 4;
            //LOGGER.info(String.valueOf(optionCount) + " | " + String.valueOf(ResearchController.Options.size()));
        }
        if (optionCount > 4) optionCount = 4;
        int spaces = optionCount - 1;
        int offset = 17 + ((9 - (optionCount + spaces)) / 2);

        int ii = 0;

        item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "lime_stained_glass_pane"));
        itemStack = new ItemStack(item);
        itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f"));
        tag = new CompoundTag();
        tag.putString("vote:tag", "cheese");
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        this.container.setItem(offset, itemStack);
        offset++;

        drawBorder(offset - 10, optionCount + (spaces + 2));

        for (int i = 0; i < optionCount; i++)
        {
            String[] name = ResearchController.Options.get(i+ (4 * (pagenum - 1))).itemID.split(":");
            //item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "dirt"));
            item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(name[0], name[1]));
            //if (item == null) item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "dirt"));
            itemStack = new ItemStack(item);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f" + ResearchController.Options.get(i+ (4 * (pagenum - 1))).displayname));
            im = new ItemLore(List.of(Utils.Chat("&f" + ResearchController.Options.get(i+ (4 * (pagenum - 1))).lore), Utils.Chat("&fVotes: " + ResearchController.Options.get(i).votes)));
            itemStack.set(DataComponents.LORE, im);
            tag = new CompoundTag();
            tag.putString("vote:tag", String.valueOf(ResearchController.Options.get(i+ (4 * (pagenum - 1))).id));
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            this.container.setItem(i + offset + ii, itemStack);

            if (ii < spaces + 1)
            {
                item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "lime_stained_glass_pane"));
                itemStack = new ItemStack(item);
                itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f"));
                tag = new CompoundTag();
                tag.putString("vote:tag", "cheese");
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                this.container.setItem(i + offset + ii + 1, itemStack);
                ii++;
            }

            if (1 + i + (4 * (pagenum - 1)) >= ResearchController.Options.size()) return;
            if (i > 3) return;
        }

    }

    private void drawBorder(int start, int len)
    {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "lime_stained_glass_pane"));
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f"));
        CompoundTag tag = new CompoundTag();
        tag.putString("vote:tag", "cheese");
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        for (int i = 0; i < len; i++)
        {
            this.container.setItem(start + i, itemStack);
            this.container.setItem(start + i + 18, itemStack);
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int button)
    {
        //CreateShopDisplay.LOGGER.info("Clicked");
        return super.clickMenuButton(player, button);
    }

    @Override
    public void clicked(int slotIndex, int dragType, ClickType clickType, Player player)
    {

        ItemStack is = container.getItem(slotIndex);
        if (is.isEmpty()) return;
        CustomData data = is.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;
        CompoundTag yag = data.copyTag();
        String name = yag.getString("vote:tag");
        ResearchMod.LOGGER.info(name);

        if (name.isEmpty()) return;
        if (player.getServer() == null) return;


        if (name.equalsIgnoreCase("Next"))
        {
            if (!((pagenum) * 4 >= ResearchController.Options.size()))
            {
                pagenum++;
                drawMenu(player.getServer(), player);
            }
        }
        else if (name.equalsIgnoreCase("Prev"))
        {
            if(pagenum > 1)
            {
                pagenum--;
                drawMenu(player.getServer(), player);
            }
        }
        else if(name.equals("Exit"))
        {
            player.closeContainer();
        }
        else if(name.equals("cheese"))
        {

        }
        else {
            int nameInt = -1;
            try {
                nameInt = Integer.parseInt(name);
            }
            catch(Exception e) {
                return;
            }


            if (ResearchController.playersVotes.contains(player.getUUID())) {
                ResearchMod.LOGGER.info("Already Voted");
                return;

            }

            //LOGGER.info(String.valueOf(ResearchController.Finshed.size()));
            for (int i = 0; i < ResearchController.Finshed.size(); i++)
            {
                if (ResearchController.Finshed.get(i).id == nameInt) {
                    ResearchMod.LOGGER.info("That research is already finished");
                    return;
                }
            }

            ResearchController.playerVote(player, nameInt);
            player.closeContainer();
            if (player instanceof ServerPlayer)
            {

                Research r = ResearchController.getResearchOption(nameInt);
                if (r == null) return;
                CommandSourceStack con = ((ServerPlayer)player).createCommandSourceStack();
                con.sendSuccess(
                        () -> Component.literal("You have voted for " + r.displayname),
                        false
                );
            }

        }
        return;
    }
}
