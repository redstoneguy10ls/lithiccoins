
package com.redstoneguy10ls.lithiccoins;

import com.redstoneguy10ls.lithiccoins.Capability.LocationCapability;
import com.redstoneguy10ls.lithiccoins.items.ModItems;
import net.dries007.tfc.client.ClientForgeEventHandler;
import net.dries007.tfc.client.ClimateRenderCache;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class ClientEventHandler {

    public static void init()
    {
        final IEventBus bus2 = MinecraftForge.EVENT_BUS;
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus2.addListener(ClientEventHandler::onItemTooltip);
        bus.addListener(ClientEventHandler::registerColorHandlerItems);


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

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        event.register(new ContainedFluidModel.Colors(), ModItems.FIRE_COIN_MOLD.get(), ModItems.COIN_MOLD.get());
    }



}

