package com.redstoneguy10ls.lithiccoins.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class registries {

    public static final ResourceKey<Registry<String>> COIN_TIPS = createRegistryKey("coin_tips");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String pName) {
        return ResourceKey.createRegistryKey(new ResourceLocation(pName));
    }
}
