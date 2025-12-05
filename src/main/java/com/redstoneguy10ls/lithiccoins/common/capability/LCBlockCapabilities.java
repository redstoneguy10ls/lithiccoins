package com.redstoneguy10ls.lithiccoins.common.capability;

import com.redstoneguy10ls.lithiccoins.common.blockentities.LCBlockEntities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.capabilities.BlockCapabilities;

public class LCBlockCapabilities
{
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(BlockCapabilities.ITEM, LCBlockEntities.MINT.get(), InventoryBlockEntity::getSidedInventory);
    }
}
