
package com.redstoneguy10ls.lithiccoins.config;

import net.dries007.tfc.config.ConfigBuilder;
import net.dries007.tfc.util.Helpers;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public final class LithicConfig {

    public static final ServerConfig SERVER;

    private static final ForgeConfigSpec SERVER_SPEC;

    static
    {
        final Pair<ServerConfig, ForgeConfigSpec> pair = register(ModConfig.Type.SERVER, ServerConfig::new, "server");

        SERVER = pair.getKey();
        SERVER_SPEC = pair.getRight();
    }
    public static void init() {}

    public static boolean isServerConfigLoaded()
    {
        return SERVER_SPEC.isLoaded();
    }

    private static <C> Pair<C, ForgeConfigSpec> register(ModConfig.Type type, Function<ConfigBuilder, C> factory, String prefix)
    {
        final Pair<C, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> factory.apply(new ConfigBuilder(builder, prefix)));
        if (!Helpers.BOOTSTRAP_ENVIRONMENT) ModLoadingContext.get().registerConfig(type, specPair.getRight());
        return specPair;
    }
}
