package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.common.items.StampItem;
import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

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

    public static int getTier(ItemStack stack)
    {
        return switch (stack.getMaxDamage())
        {
            case 600 -> 1;
            case 1200, 1300, 1460 -> 2;
            case 2200 -> 3;
            case 3300 -> 4;
            case 4200 -> 5;
            case 6500 -> 6;
            default -> 0;
        };
    }

    public static boolean isToolMetal(Metal metal)
    {
        return metal.tier() != 0;
    }
}
