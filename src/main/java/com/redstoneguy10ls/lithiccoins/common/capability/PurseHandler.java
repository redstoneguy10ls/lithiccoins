package com.redstoneguy10ls.lithiccoins.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PurseHandler implements IPurse, ICapabilitySerializable<CompoundTag> {

    private final LazyOptional<IPurse> capability;
    private final ItemStack stack;
    boolean hasItem;

    public PurseHandler(ItemStack stack) {
        capability = LazyOptional.of(() -> this);
        this.stack = stack;
        hasItem = false;
    }

    @Override
    public boolean hasItem() {
        return hasItem;
    }

    @Override
    public void setHasItem(boolean exists) {
        hasItem = exists;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PurseCapability.CAPABILITY)
        {
            return capability.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
