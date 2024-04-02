package com.redstoneguy10ls.lithiccoins;

import com.mojang.logging.LogUtils;
import com.redstoneguy10ls.lithiccoins.common.blocks.ModBlocks;
import com.redstoneguy10ls.lithiccoins.config.LithicConfig;
import com.redstoneguy10ls.lithiccoins.common.items.ModItems;
import com.redstoneguy10ls.lithiccoins.common.items.ModTabs;
//import com.redstoneguy10ls.lithiccoins.recipes.ModRecipeSerializers;
//import com.redstoneguy10ls.lithiccoins.recipes.ModRecipeTypes;
import com.redstoneguy10ls.lithiccoins.common.misc.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LithicCoins.MOD_ID)
public class LithicCoins
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "lithiccoins";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public LithicCoins()
    {
        LithicConfig.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModItems.ITEMS.register(modEventBus);
        ModTabs.CREATIVE_TABS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        ForgeEventHandlers.init();
        //ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        //ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);



        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init();
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
