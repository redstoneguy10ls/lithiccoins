package com.redstoneguy10ls.lithiccoins.common.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface LCEmptyInventory extends Container
{
    @Override
    @Deprecated
    default int getContainerSize()
    {
        return 0;
    }

    @Override
    @Deprecated
    default boolean isEmpty()
    {
        return true;
    }

    @Override
    @Deprecated
    default ItemStack getItem(int index)
    {
        return ItemStack.EMPTY;
    }

    @Override
    @Deprecated
    default ItemStack removeItem(int index, int count)
    {
        return ItemStack.EMPTY;
    }

    @Override
    @Deprecated
    default ItemStack removeItemNoUpdate(int index)
    {
        return ItemStack.EMPTY;
    }

    @Override
    @Deprecated
    default void setItem(int index, ItemStack stack) {}

    @Override
    @Deprecated
    default void setChanged() {}

    @Override // Not deprecated, because it's implemented by containers
    default boolean stillValid(Player player)
    {
        return true;
    }

    @Override
    @Deprecated
    default void clearContent() {}
}

