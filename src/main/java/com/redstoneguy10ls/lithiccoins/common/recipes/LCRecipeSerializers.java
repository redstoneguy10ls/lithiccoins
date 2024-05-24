package com.redstoneguy10ls.lithiccoins.common.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);

    public static final RegistryObject<MintingRecipe.Serializer> MINTING = register("minting", MintingRecipe.Serializer::new);

    public static final RegistryObject<WaxKnappingRecipe.Serializer> WAX_KNAPPING = register("wax_knapping", WaxKnappingRecipe.Serializer::new);


    private static <S extends RecipeSerializer<?>> RegistryObject<S> register(String name, Supplier<S> factory)
    {
        return RECIPE_SERIALIZERS.register(name, factory);
    }
}
