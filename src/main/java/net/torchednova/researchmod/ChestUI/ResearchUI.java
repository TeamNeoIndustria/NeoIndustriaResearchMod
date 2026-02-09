package net.torchednova.researchmod.ChestUI;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
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
import net.torchednova.researchmod.research.ResearchController;
import net.torchednova.researchmod.utils.Utils;

import java.util.List;

public class ResearchUI extends ChestMenu {
    private final SimpleContainer container;
    private int pagenum = 1;


    public ResearchUI (int id, Inventory playerInventory, SimpleContainer cont)
    {
        super(MenuType.GENERIC_9x6, id, playerInventory, cont, 6);
        pagenum = 1;
        this.container = cont;

        this.container.clearContent();

        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "blue_wool"));
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&fExit"));
        CompoundTag tag = new CompoundTag();
        tag.putString("vote:tag", "Exit");
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        cont.setItem(49, itemStack);

        if (ResearchController.current != null)
        {
            if (ResearchController.playersVotes.contains(playerInventory.player.getUUID()))
            {
                item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "barrier"));
                itemStack = new ItemStack(item);
                itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&You Have already voted"));
                tag = new CompoundTag();
                tag.putString("vote:tag", "null");
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                cont.setItem(22, itemStack);
                return;
            }
            displayMainPage(playerInventory.player.getServer());
        }

    }

    private void displayMainPage(MinecraftServer s)
    {
        this.container.clearContent();

        Item item;
        ItemStack itemStack;
        CompoundTag tag;
        ItemLore im;

        int offset = 0;
        for (int i = 0; i < ResearchController.Options.size(); i++)
        {
            String[] name = ResearchController.Options.get(i).itemID.split(":");
            item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(name[0], name[1]));
            itemStack = new ItemStack(item);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&f" + ResearchController.Options.get(i).name));
            im = new ItemLore(List.of(Utils.Chat("&f"+ List.of(Utils.Chat("&f" + ResearchController.Options.get(i).lore)))));
            itemStack.set(DataComponents.LORE, im);
            tag = new CompoundTag();
            tag.putString("vote:tag", "V" + ResearchController.Options.get(i).id);
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            this.container.setItem(i + offset, itemStack);
            if (i > 0 && (i % 9 != 0))
            {
                offset += 9;
            }
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

        if (name.isEmpty()) return;
        if (player.getServer() == null) return;

        if (name.equalsIgnoreCase("Next"))
        {

        }
        else if (name.equalsIgnoreCase("Previous"))
        {

        }
        else if(name.equals("Exit"))
        {

        }
        else {

        }
        return;
    }
}
