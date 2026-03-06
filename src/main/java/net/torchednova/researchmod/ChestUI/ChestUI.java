package net.torchednova.researchmod.ChestUI;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.torchednova.researchmod.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChestUI {
    public static SimpleContainer createContainer(int size) {
        SimpleContainer container = new SimpleContainer(size) {

            @Override
            public boolean canPlaceItem(int slot, @NotNull ItemStack stack)
            {
                return false;
            }

            @Override
            public boolean canTakeItem(@NotNull Container target, int slot, @NotNull ItemStack stack)
            {
                return false;
            }

        };

        return container;
    }

    public static void openMenu(Player player, SimpleContainer cont) {
        player.openMenu(new SimpleMenuProvider((id, inv, p) -> new ResearchUI(id, inv, cont), Component.literal("Vote for the next Research")));
    }

    public static void openVoteMenu(Player player, int size, ServerLevel sl)
    {
        SimpleContainer cont = createContainer(size);


        /*
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "barrier"));
            ItemStack itemStack = new ItemStack(item);
            ItemLore itemLore = new ItemLore(List.of(Utils.Chat("&fCurrently no stores are active")));

            itemStack.set(DataComponents.LORE, itemLore);
            itemStack.set(DataComponents.ITEM_NAME, Utils.Chat("&fYou can create you store to be added to this list"));
            cont.setItem(22, itemStack);
         */


        openMenu(player, cont);

    }
}
