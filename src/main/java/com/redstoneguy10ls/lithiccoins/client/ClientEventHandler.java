
package com.redstoneguy10ls.lithiccoins.client;

import com.redstoneguy10ls.lithiccoins.client.render.MintBlockEntityRenderer;
import com.redstoneguy10ls.lithiccoins.client.screen.LargeKnappingScreen;
import com.redstoneguy10ls.lithiccoins.common.blockentities.LCBlockEntities;
import com.redstoneguy10ls.lithiccoins.common.component.LCComponents;
import com.redstoneguy10ls.lithiccoins.common.component.PurseComponent;
import com.redstoneguy10ls.lithiccoins.common.container.LCContainerTypes;
import com.redstoneguy10ls.lithiccoins.common.items.CoinPurseItem;
import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.Tooltips;

import net.dries007.tfc.client.model.ContainedFluidModel;

import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientEventHandler
{

    public static void init(IEventBus bus, ModContainer mod)
    {
        bus.addListener(ClientEventHandler::registerTooltipFactories);
        bus.addListener(ClientEventHandler::registerColorHandlerItems);
        bus.addListener(ClientEventHandler::registerEntityRenderers);
        bus.addListener(ClientEventHandler::registerMenuScreens);
        bus.addListener(ClientEventHandler::onClientSetup);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(LCBlockEntities.MINT.get(), ctx -> new MintBlockEntityRenderer());
    }

    public static void registerTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(Tooltips.CoinImageTooltip.class, CoinTooltip::new);
        event.register(Tooltips.PurseImageTooltip.class, PurseTooltip::new);
    }

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        event.register(ContainedFluidModel.COLOR, LCItems.FIRE_COIN_MOLD.get(), LCItems.COIN_MOLD.get());
        LCItems.FIRED_DIE_MOLD.values().forEach(reg -> event.register(ContainedFluidModel.COLOR, reg.get()));
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event)
    {
        event.register(LCContainerTypes.WAX_KNAPPING.get(), LargeKnappingScreen::new);
    }

    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ItemProperties.register(
                LCItems.COIN_PURSE.asItem(),
                LCHelpers.identifier("filled"),
                (stack, level, player, seed) ->
                {
                    final PurseComponent purse = stack.getOrDefault(LCComponents.PURSE, PurseComponent.EMPTY);
                    return purse.isEmpty() ? 0f : 1f;
                }
            );
        });
    }
}

