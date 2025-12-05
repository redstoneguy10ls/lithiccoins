package com.redstoneguy10ls.lithiccoins;

import com.mojang.logging.LogUtils;
import com.redstoneguy10ls.lithiccoins.client.ClientEventHandler;
import com.redstoneguy10ls.lithiccoins.client.ClientForgeEventHandler;
import com.redstoneguy10ls.lithiccoins.common.blockentities.LCBlockEntities;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCBlocks;
import com.redstoneguy10ls.lithiccoins.common.capability.LCBlockCapabilities;
import com.redstoneguy10ls.lithiccoins.common.capability.LCItemCapabilities;
import com.redstoneguy10ls.lithiccoins.common.component.LCComponents;
import com.redstoneguy10ls.lithiccoins.common.container.LCContainerTypes;
import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.common.recipes.LCRecipeSerializers;
import com.redstoneguy10ls.lithiccoins.common.recipes.LCRecipeTypes;
import com.redstoneguy10ls.lithiccoins.config.ServerConfig;
import com.redstoneguy10ls.lithiccoins.common.items.LCTabs;
import com.redstoneguy10ls.lithiccoins.common.misc.LCSounds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(LithicCoins.MOD_ID)
public class LithicCoins
{
    public static final String MOD_ID = "lithiccoins";
    private static final Logger LOGGER = LogUtils.getLogger();

    public LithicCoins(IEventBus bus, ModContainer mod)
    {
        mod.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        bus.addListener(LCBlockCapabilities::register);
        bus.addListener(LCItemCapabilities::register);

        ForgeEventHandler.init();

        LCComponents.COMPONENTS.register(bus);

        LCItems.ITEMS.register(bus);
        LCTabs.CREATIVE_TABS.register(bus);
        LCBlocks.BLOCKS.register(bus);
        LCBlockEntities.BLOCK_ENTITIES.register(bus);

        LCRecipeTypes.RECIPE_TYPES.register(bus);
        LCRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        LCSounds.SOUNDS.register(bus);
        LCContainerTypes.CONTAINERS.register(bus);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init(bus, mod);
            ClientForgeEventHandler.init();
        }
    }
}
