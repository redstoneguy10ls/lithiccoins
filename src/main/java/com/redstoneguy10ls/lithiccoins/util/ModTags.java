package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static class Blocks{

    }

    public static class Items {
        public static final TagKey<Item> STAMPED_COINS = tag("stamped_coins");
        public static final TagKey<Item> BOTTOM_DIE = tag("bottom_dies");

        private static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(LithicCoins.MOD_ID, name));
        }

    }

    public static class Fluids
    {
        public static final TagKey<Fluid> USABLE_IN_COIN_MOLD  = create("usable_in_coin_mold");

        private static TagKey<Fluid> create(String name){
            return FluidTags.create(new ResourceLocation(LithicCoins.MOD_ID, name));
        }
    }


}
