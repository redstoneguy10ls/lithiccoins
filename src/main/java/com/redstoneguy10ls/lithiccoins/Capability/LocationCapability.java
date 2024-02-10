package com.redstoneguy10ls.lithiccoins.Capability;


import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodDefinition;
import net.dries007.tfc.common.capabilities.food.INetworkFood;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.ItemDefinition;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.crafting.conditions.ItemExistsCondition;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LocationCapability {
    public static final Capability<ILocation> CAPABILITY = Helpers.capability(new CapabilityToken<>() {});

    public static final Capability<INetworkLocation> NETWORK_CAPABILITY = Helpers.capability(new CapabilityToken<>() {});

    public static final ResourceLocation KEY = Helpers.identifier("location");
    //public static final DataManager<LocationDefinition> MANAGER = new DataManager<>(Helpers.identifier("food_items"), "food", FoodDefinition::new, FoodDefinition::new, FoodDefinition::encode, FoodCapability.Packet::new);
    //public static final IndirectHashCollection<Item, FoodDefinition> CACHE = IndirectHashCollection.create(FoodDefinition::getValidItems, MANAGER::getValues);


    @Nullable
    public static ILocation get(ItemStack stack)
    {
        return Helpers.getCapability(stack, CAPABILITY);
    }

    public static boolean has(ItemStack stack)
    {
        return stack.getCapability(CAPABILITY).isPresent();
    }
/*
    public static void applyLocation(ILocation instance)
    {
        instance.setCreationLocation(CalculateCreationLocation(instance));
    }

    private static BlockPos CalculateCreationLocation(ILocation instance)
    {
        instance.
    }
*/


    /*
    public static int getCreationX()
    {
        new ChunkPos();
    }
*/
}
