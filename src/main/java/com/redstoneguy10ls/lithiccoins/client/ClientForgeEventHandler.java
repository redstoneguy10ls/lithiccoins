package com.redstoneguy10ls.lithiccoins.client;

import java.util.List;

import com.redstoneguy10ls.lithiccoins.common.component.MintingComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ClientForgeEventHandler
{

    public static void init()
    {
        final IEventBus bus = NeoForge.EVENT_BUS;

        bus.addListener(ClientForgeEventHandler::onItemTooltip);
    }

    public static void onItemTooltip(ItemTooltipEvent event)
    {
        final ItemStack stack = event.getItemStack();
        final List<Component> tooltip = event.getToolTip();

        if (!stack.isEmpty())
        {
            MintingComponent.addTooltipInfo(stack, tooltip);
        }
    }
}
