package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class LCTags {
    public static class Blocks{

    }

    public static class Items {
        public static final TagKey<Item> STAMPED_COINS = tag("stamped_coins");

        public static final TagKey<Item> FIT_IN_PURSE = tag("fit_in_purse");
        public static final TagKey<Item> BOTTOM_DIE = tag("bottom_dies");
        public static final TagKey<Item> TOP_DIE = tag("top_dies");
        public static final TagKey<Item> BLANK_COIN = tag("blank_coins");
        public static final TagKey<Item> WAX_KNAPPABLE = tag("wax_knappable");

        public static final TagKey<Item> ANY_KNAPPING = tag("any_knapping");


        public static final TagKey<Item> T1_BOTTOM_DIES = tag("dies/bottom/tier_i_dies");
        public static final TagKey<Item> T2_BOTTOM_DIES = tag("dies/bottom/tier_ii_dies");
        public static final TagKey<Item> T3_BOTTOM_DIES = tag("dies/bottom/tier_iii_dies");
        public static final TagKey<Item> T4_BOTTOM_DIES = tag("dies/bottom/tier_iv_dies");
        public static final TagKey<Item> T5_BOTTOM_DIES = tag("dies/bottom/tier_v_dies");
        public static final TagKey<Item> T6_BOTTOM_DIES = tag("dies/bottom/tier_vi_dies");

        public static final TagKey<Item> T1_TOP_DIES = tag("dies/top/tier_i_dies");
        public static final TagKey<Item> T2_TOP_DIES = tag("dies/top/tier_ii_dies");
        public static final TagKey<Item> T3_TOP_DIES = tag("dies/top/tier_iii_dies");
        public static final TagKey<Item> T4_TOP_DIES = tag("dies/top/tier_iv_dies");
        public static final TagKey<Item> T5_TOP_DIES = tag("dies/top/tier_v_dies");
        public static final TagKey<Item> T6_TOP_DIES = tag("dies/top/tier_vi_dies");



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
