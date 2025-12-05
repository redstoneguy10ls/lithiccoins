package com.redstoneguy10ls.lithiccoins.common.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.util.registry.RegistryHolder;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCRecipeTypes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MOD_ID);


    public static final Id<MintingRecipe> MINTING = register("minting");

    public static final Id<LargeKnappingRecipe> LARGE_KNAPPING = register("large_knapping");



    private static <R extends Recipe<?>> Id<R> register(String name)
    {
        return new Id<>(RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString()
            {
                return name;
            }
        }));
    }

    public record Id<T extends Recipe<?>>(DeferredHolder<RecipeType<?>, RecipeType<T>> holder)
        implements RegistryHolder<RecipeType<?>, RecipeType<T>> {}
}
