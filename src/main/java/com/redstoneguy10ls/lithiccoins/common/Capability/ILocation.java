package com.redstoneguy10ls.lithiccoins.common.Capability;

import net.minecraft.network.chat.Component;
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

    void funy(ItemStack stack);

    default void addTooltipInfo(ItemStack stack, List<Component> text)
    {

        //System.out.println("shit "+stack.getItem());
        //ChunkPos chunkpos = new ChunkPos(blockPos);

        text.add(Component.translatable("lithiccoins.tooltip.lithiccoins.coins.tooltip2"));



    }

}
