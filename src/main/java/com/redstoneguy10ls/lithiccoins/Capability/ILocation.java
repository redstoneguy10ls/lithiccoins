package com.redstoneguy10ls.lithiccoins.Capability;

import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

public interface ILocation extends INetworkLocation {

    //void setCreationLocation(BlockPos creationLocation);


   ChunkPos getCreationLocation();
   ChunkPos getDefualtChunkPos();

    public Boolean getLocationSet();

    long getCreationDate();

    void setCreationDate(long creationDate);

    void setCreationLocation(ChunkPos creationLocation);

    default void addTooltipInfo(ItemStack stack, List<Component> text)
    {

        //System.out.println("shit "+stack.getItem());
        //ChunkPos chunkpos = new ChunkPos(blockPos);

        text.add(Component.translatable("tooltip.lithiccoins.coins.tooltip2"));



    }

}
