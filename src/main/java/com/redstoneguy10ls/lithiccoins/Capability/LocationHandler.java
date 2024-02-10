package com.redstoneguy10ls.lithiccoins.Capability;

import net.dries007.tfc.common.capabilities.food.DelegateFoodHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.lang.Boolean.FALSE;

public class LocationHandler implements ICapabilitySerializable<CompoundTag>, ILocation {
    private final ItemStack stack;
    private final LazyOptional<ILocation> capability;

    public static final ChunkPos DEFUALT_CHUNK_POS = new ChunkPos(29999999,29999999);

    protected ChunkPos creationLocation;

    protected boolean LocationSet;

    public static final long UNKNOWN_CREATION_DATE = -1;

    protected long creationDate;


    public LocationHandler(ItemStack itemStack) {
        stack = itemStack;
        this.capability = LazyOptional.of(() -> this);
        this.creationLocation = DEFUALT_CHUNK_POS;
        this.LocationSet = false;
        this.creationDate = UNKNOWN_CREATION_DATE;
    }

    @Override
    public ChunkPos getDefualtChunkPos()
    {
        return DEFUALT_CHUNK_POS;
    }
    @Override
    public ChunkPos getCreationLocation()
    {
        return creationLocation;
    }
    @Override
    public Boolean getLocationSet()
    {
        return LocationSet;
    }

    @Override
    public void setCreationLocation(ChunkPos creationLocation)
    {
        this.creationLocation = creationLocation;
        this.LocationSet = true;
    }

    @Override
    public long getCreationDate()
    {
        return creationDate;
    }
    @Override
    public void setCreationDate(long creationDate)
    {
        this.creationDate = creationDate;
        serializeNBT();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == LocationCapability.CAPABILITY || cap == LocationCapability.NETWORK_CAPABILITY)
        {
            return capability.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void addTooltipInfo(ItemStack stack, List<Component> text)
    {

        if(creationLocation == DEFUALT_CHUNK_POS)
        {
            text.add(Component.translatable("tooltip.lithiccoins.coins.tooltip2").withStyle(ChatFormatting.GRAY));
        }
        else {
            text.add(Component.translatable("tooltip.lithiccoins.coins.tooltip",creationLocation).withStyle(ChatFormatting.GRAY));
            text.add(Component.translatable("tooltip.lithiccoins.coins.tooltip_time",creationDate).withStyle(ChatFormatting.GRAY));
        }


    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putLong("CreationLocation", creationLocation.toLong());
        nbt.putBoolean("Location_Set", LocationSet);
        nbt.putLong("creationDate", getCreationDate());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        //long temp = nbt.contains("CreationLocation") ? (nbt.getLong("CreationLocation")) : DEFUALT_CHUNK_POS.toLong();
        creationLocation = new ChunkPos(nbt.contains("CreationLocation") ? (nbt.getLong("CreationLocation")) : DEFUALT_CHUNK_POS.toLong());
        LocationSet = nbt.contains("Location_Set") ? (nbt.getBoolean("Location_Set")) : FALSE;
        creationDate = nbt.contains("creationDate") ? nbt.getLong("creationDate") : UNKNOWN_CREATION_DATE;
    }

}
