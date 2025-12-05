package com.redstoneguy10ls.lithiccoins.common.capability;

import java.util.stream.Stream;
import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import net.dries007.tfc.common.capabilities.ItemCapabilities;


public class LCItemCapabilities
{
    public static void register(RegisterCapabilitiesEvent event)
    {
        final ItemLike[] molds = Stream.concat(
            LCItems.FIRED_DIE_MOLD.values().stream(),
            Stream.of(
                LCItems.COIN_MOLD,
                LCItems.FIRE_COIN_MOLD
            )
        ).toArray(ItemLike[]::new);

        event.registerItem(ItemCapabilities.MOLD, ItemCapabilities::forMold, molds);
        event.registerItem(ItemCapabilities.HEAT, ItemCapabilities::forMold, molds);
        event.registerItem(ItemCapabilities.FLUID, ItemCapabilities::forMold, molds);
    }
}
