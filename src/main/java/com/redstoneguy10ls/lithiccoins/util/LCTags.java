package com.redstoneguy10ls.lithiccoins.util;

import java.util.Map;
import com.redstoneguy10ls.lithiccoins.common.items.CoinMaterial;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

public class LCTags
{
    public static class Items
    {
        public static final TagKey<Item> STAMPED_COINS = tag("stamped_coins");

        public static final TagKey<Item> FIT_IN_PURSE = tag("fit_in_purse");
        public static final TagKey<Item> BOTTOM_DIE = tag("bottom_dies");
        public static final TagKey<Item> TOP_DIE = tag("top_dies");
        public static final TagKey<Item> BLANK_COIN = tag("blank_coins");
        public static final TagKey<Item> WAX_KNAPPING = tag("wax_knapping");

        public static final TagKey<Item> FORCE_LARGE_KNAPPING = tag("force_large_knapping");

        public static final Map<Integer, TagKey<Item>> BOTTOM_DIE_TIER_MAP = Map.of(
            1, tag("bottom_die/tier_i_dies"),
            2, tag("bottom_die/tier_ii_dies"),
            3, tag("bottom_die/tier_iii_dies"),
            4, tag("bottom_die/tier_iv_dies"),
            5, tag("bottom_die/tier_v_dies"),
            6, tag("bottom_die/tier_vi_dies")
        );

        public static final Map<Integer, TagKey<Item>> TOP_DIE_TIER_MAP = Map.of(
            1, tag("top_die/tier_i_dies"),
            2, tag("top_die/tier_ii_dies"),
            3, tag("top_die/tier_iii_dies"),
            4, tag("top_die/tier_iv_dies"),
            5, tag("top_die/tier_v_dies"),
            6, tag("top_die/tier_vi_dies")
        );

        public static final Map<CoinMaterial, TagKey<Item>> STAMPED_COIN_MAP = Helpers.mapOf(CoinMaterial.class, material ->
            tag("stamped_coin/" + material.name().toLowerCase())
        );

        public static final Map<Metal, TagKey<Item>> TOP_DIE_METAL_MAP = Helpers.mapOf(Metal.class, LCHelpers::isToolMetal, metal ->
            switch (metal)
            {
                case BRONZE -> tag("top_die/bronze");
                case BLACK_BRONZE -> tag("top_die/black_bronze");
                case BISMUTH_BRONZE -> tag("top_die/bismuth_bronze");
                case WROUGHT_IRON -> tag("top_die/wrought_iron");
                case STEEL -> tag("top_die/steel");
                case BLACK_STEEL -> tag("top_die/black_steel");
                case RED_STEEL -> tag("top_die/red_steel");
                case BLUE_STEEL -> tag("top_die/blue_steel");
                default -> tag("top_die/copper");
            }
        );


        private static TagKey<Item> tag(String name)
        {
            return ItemTags.create(LCHelpers.identifier(name));
        }
    }

    public static class Fluids
    {
        public static final TagKey<Fluid> USABLE_IN_COIN_MOLD = create("usable_in_coin_mold");


        private static TagKey<Fluid> create(String name){
            return FluidTags.create(LCHelpers.identifier(name));
        }
    }


}
