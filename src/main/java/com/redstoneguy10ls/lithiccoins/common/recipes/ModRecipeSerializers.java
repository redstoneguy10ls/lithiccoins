/*
package com.redstoneguy10ls.lithiccoins.recipes;

import net.dries007.tfc.common.recipes.QuernRecipe;
import net.dries007.tfc.common.recipes.SimpleItemRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;


public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);

    public static final RegistryObject<SimpleItemRecipe.Serializer<MintRecipe>> MINT = register("mint", () -> new SimpleItemRecipe.Serializer<>(MintRecipe::new));


    private static <S extends RecipeSerializer<?>> RegistryObject<S> register(String name, Supplier<S> factory)
    {
        return RECIPE_SERIALIZERS.register(name, factory);
    }

}
*/