package com.redstoneguy10ls.lithiccoins.common.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.util.registry.RegistryHolder;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.*;

public class LCComponents
{
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MOD_ID);


    // Minting Component, holding all relevant info regarding minted or unminted coins
    public static final Id<MintingComponent> MINTING = register("minting", MintingComponent.CODEC, MintingComponent.STREAM_CODEC);

    // Purse Component, similar to bundles but with config-based size
    public static final Id<PurseComponent> PURSE = register("purse", PurseComponent.CODEC, PurseComponent.STREAM_CODEC);



    private static <T> Id<T> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec)
    {
        return new Id<>(COMPONENTS.register(name, () -> new DataComponentType.Builder<T>()
            .persistent(codec)
            .networkSynchronized(streamCodec)
            .build()));
    }

    public record Id<T>(DeferredHolder<DataComponentType<?>, DataComponentType<T>> holder)
        implements RegistryHolder<DataComponentType<?>, DataComponentType<T>> {}
}
