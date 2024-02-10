
package com.redstoneguy10ls.lithiccoins;

import com.redstoneguy10ls.lithiccoins.Capability.LocationCapability;
import net.dries007.tfc.client.ClientForgeEventHandler;
import net.dries007.tfc.client.ClimateRenderCache;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

public class ClientEventHandler {

    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ClientEventHandler::onItemTooltip);
        //bus.addListener(ClientEventHandler::onClientTick);

    }

    @SuppressWarnings("ConstantConditions")
    public static void onItemTooltip(ItemTooltipEvent event)
    {
        final ItemStack stack = event.getItemStack();
        final List<Component> text = event.getToolTip();
        if (!stack.isEmpty())
        {
            stack.getCapability(LocationCapability.CAPABILITY).ifPresent(cap -> cap.addTooltipInfo(stack, text));
            //stack.getCapability(LocationCapability.CAPABILITY).ifPresent(cap -> cap.serializeNBT());

        }
    }
    /*
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        final ItemStack stack = event.getItemStack();

        Level world = Minecraft.getInstance().level;
        if (event.phase == TickEvent.Phase.END && world != null && !Minecraft.getInstance().isPaused())
        {
            if()
        }
    }

     */


}

