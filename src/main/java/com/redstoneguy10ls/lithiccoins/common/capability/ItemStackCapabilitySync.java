package com.redstoneguy10ls.lithiccoins.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class ItemStackCapabilitySync {

    public static boolean hasSyncableCapability(ItemStack stack)
    {
        return stack.getCapability(LocationCapability.NETWORK_CAPABILITY).isPresent();
    }
    public static void writeToNetwork(ItemStack stack, FriendlyByteBuf buffer)
    {
        synchronized (stack)
        {
            if(hasSyncableCapability(stack))
            {
                buffer.writeBoolean(true);
                writeToNetwork(LocationCapability.NETWORK_CAPABILITY, stack, buffer);
            }
            else
            {
                buffer.writeBoolean(false);
            }

        }
    }
    public static void readFromNetwork(ItemStack stack, FriendlyByteBuf buffer)
    {
        if(buffer.readBoolean())
        {
            readFromNetwork(LocationCapability.NETWORK_CAPABILITY, stack, buffer);
        }
    }
    private static void writeToNetwork(Capability<? extends INBTSerializable<CompoundTag>> capability, ItemStack stack, FriendlyByteBuf buffer)
    {
        buffer.writeNbt(stack.getCapability(capability)
                .map(INBTSerializable::serializeNBT)
                .orElse(null));
    }
    private static void readFromNetwork(Capability<? extends INBTSerializable<CompoundTag>> capability, ItemStack stack, FriendlyByteBuf buffer)
    {
        final CompoundTag tag = buffer.readNbt();
        if (tag != null)
        {
            stack.getCapability(capability).ifPresent(cap -> cap.deserializeNBT(tag));
        }
    }
}
