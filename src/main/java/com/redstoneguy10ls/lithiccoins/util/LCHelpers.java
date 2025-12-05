package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.items.StampItem;
import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import net.dries007.tfc.util.Metal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCHelpers {

    public static ResourceLocation identifier(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    /**
     * Helper for getting the {@link StampType} of an assumed {@link StampItem}.
     * Returns a default value if the given item is <strong>not</strong> a StampItem
     */
    public static StampType getStamptype(Item item)
    {
        // Since we store the StampType in the StampItem class, this is a simple look-up
        if (item instanceof StampItem stampItem)
        {
            return stampItem.getType();
        }
        // But if the given item isn't a StampItem (which should never happen), we need to give back a default value
        else
        {
            return StampType.BEE;
        }
    }

    public static boolean isToolMetal(Metal metal)
    {
        return metal.tier() != 0;
    }
}
