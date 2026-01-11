package com.redstoneguy10ls.lithiccoins.common.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.util.registry.RegistryHolder;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);

    public static final Id<MintingRecipe> MINTING = register("minting", MintingRecipe.CODEC, MintingRecipe.STREAM_CODEC);

    public static final Id<LargeKnappingRecipe> WAX_KNAPPING = register("large_knapping", LargeKnappingRecipe.CODEC, LargeKnappingRecipe.STREAM_CODEC);



    private static <R extends Recipe<?>> Id<R> register(String name, MapCodec<R> codec, StreamCodec<RegistryFriendlyByteBuf, R> stream)
    {
        return register(name, new RecipeSerializerImpl<>(codec, stream));
    }

    private static <R extends Recipe<?>> Id<R> register(String name, RecipeSerializer<R> serializer)
    {
        return new Id<>(RECIPE_SERIALIZERS.register(name, () -> serializer));
    }

    public record Id<T extends Recipe<?>>(DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> holder)
        implements RegistryHolder<RecipeSerializer<?>, RecipeSerializer<T>> {}
}
