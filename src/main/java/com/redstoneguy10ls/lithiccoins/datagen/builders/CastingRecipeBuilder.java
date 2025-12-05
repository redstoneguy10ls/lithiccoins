package com.redstoneguy10ls.lithiccoins.datagen.builders;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.recipes.CastingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;

public class CastingRecipeBuilder implements RecipeBuilder
{
    private final Ingredient ingredient;
    private final SizedFluidIngredient fluidIngredient;
    private final ItemStackProvider resultProvider;
    private final Item result;
    private final float chance;
    private final CastingRecipeBuilder.Factory<?> factory;

    public CastingRecipeBuilder(Ingredient ingredient, SizedFluidIngredient fluidIngredient, ItemStack resultStack, float chance, Factory<?> factory)
    {
        this.ingredient = ingredient;
        this.fluidIngredient = fluidIngredient;
        this.resultProvider = ItemStackProvider.of(resultStack);
        this.result = resultStack.getItem();
        this.chance = chance;
        this.factory = factory;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't want to lock recipes behind any criterion, we make this a no-op
     */
    @Override
    public CastingRecipeBuilder unlockedBy(String name, Criterion<?> criterion)
    {
        return this;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't divide Casting recipes into groups, we make this a no-op
     */
    @Override
    public CastingRecipeBuilder group(@Nullable String groupName)
    {
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }

    public static CastingRecipeBuilder cast(ItemLike ingredient, Metal metal, ItemLike result, float chance)
    {
        return cast(ingredient, metal, 100, result, 1, chance);
    }

    public static CastingRecipeBuilder cast(ItemLike ingredient, Metal metal, int units, ItemLike result, int count, float chance)
    {
        return new CastingRecipeBuilder(Ingredient.of(ingredient), SizedFluidIngredient.of(TFCFluids.METALS.get(metal).getSource(), units), new ItemStack(result, count), chance, CastingRecipe::new);
    }

    public static CastingRecipeBuilder cast(ItemLike ingredient, Fluid fluid, int units, ItemLike result, int count, float chance)
    {
        return new CastingRecipeBuilder(Ingredient.of(ingredient), SizedFluidIngredient.of(fluid, units), new ItemStack(result, count), chance, CastingRecipe::new);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation)
    {
        CastingRecipe castingRecipe = this.factory.create(this.ingredient, this.fluidIngredient, this.resultProvider, this.chance);
        recipeOutput.accept(resourceLocation.withPrefix("casting/"), castingRecipe, null);
    }


    public interface Factory<T extends CastingRecipe>
    {
        T create(Ingredient ingredient, SizedFluidIngredient fluidIngredient, ItemStackProvider resultProvider, float chance);
    }
}
