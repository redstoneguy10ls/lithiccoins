package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Blocks{

    }

    public static class Items {
        public static final TagKey<Item> STAMPED_COINS = tag("stamped_coins");
        private static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(LithicCoins.MOD_ID, name));
        }

    }

}
