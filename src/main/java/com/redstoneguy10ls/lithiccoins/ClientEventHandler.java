
package com.redstoneguy10ls.lithiccoins;

import com.redstoneguy10ls.lithiccoins.client.render.MintBlockEntityRenderer;
import com.redstoneguy10ls.lithiccoins.client.screen.WaxKanppingScreen;
import com.redstoneguy10ls.lithiccoins.common.capability.IPurse;
import com.redstoneguy10ls.lithiccoins.common.capability.LocationCapability;
import com.redstoneguy10ls.lithiccoins.common.capability.PurseCapability;
import com.redstoneguy10ls.lithiccoins.common.blockentities.LCBlockEntities;
import com.redstoneguy10ls.lithiccoins.common.container.LCContainerTypes;
import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.util.CoinTooltip;
import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import net.dries007.tfc.client.model.ContainedFluidModel;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class ClientEventHandler {

    public static void init()
    {
        final IEventBus bus2 = MinecraftForge.EVENT_BUS;
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::onTooltipFactoryRegistry);
        bus.addListener(ClientEventHandler::clientSetup);
        bus2.addListener(ClientEventHandler::onItemTooltip);
        bus.addListener(ClientEventHandler::registerColorHandlerItems);
        bus.addListener(ClientEventHandler::regusterEntityRenderers);
        //bus2.addListener(ClientEventHandler::onGatherTooltipComponents);


    }

    public static void regusterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(LCBlockEntities.MINT.get(), ctx -> new MintBlockEntityRenderer());
    }

    public static void onTooltipFactoryRegistry(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(Tooltips.CoinImageTooltip.class, CoinTooltip::new);
    }


    public static void clientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {

            MenuScreens.register(LCContainerTypes.WAX_KNAPPING.get(), WaxKanppingScreen::new);

            ItemProperties.register(LCItems.COIN_PURSE.get(), new ResourceLocation(MOD_ID, "filled"),
                    (stack, a, b, c) -> stack.getCapability(PurseCapability.CAPABILITY).map(IPurse::hasItem).orElse(false) ? 1f: 0f);
        });
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
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event){

        event.getTooltipElements().add(Either.right(new tooltips.CoinImageTooltip(LCHelpers.identifier("textures/entity/coin_angler_preview.png"))));
    }
*/


    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        event.register(new ContainedFluidModel.Colors(), LCItems.FIRE_COIN_MOLD.get(), LCItems.COIN_MOLD.get());

        LCItems.FIRED_DIE_MOLD.values().forEach(reg -> event.register(new ContainedFluidModel.Colors(), reg.get()));
    }



}

