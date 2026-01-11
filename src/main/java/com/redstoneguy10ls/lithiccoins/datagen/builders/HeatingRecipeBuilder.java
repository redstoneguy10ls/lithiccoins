package com.redstoneguy10ls.lithiccoins.datagen.builders;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;

public class HeatingRecipeBuilder implements RecipeBuilder
{
    private final Ingredient ingredient;
    private final ItemStackProvider resultProvider;
    private final Item result;
    private final FluidStack outputFluid;
    private final float temperature;
    private final boolean useDurability;
    private final Factory<?> factory;

    public HeatingRecipeBuilder(Ingredient ingredient, ItemStack resultStack, FluidStack outputFluid, float temperature, boolean useDurability, Factory<?> factory)
    {
        this.ingredient = ingredient;
        this.resultProvider = ItemStackProvider.of(resultStack);
        this.result = resultStack.getItem();
        this.outputFluid = outputFluid;
        this.temperature = temperature;
        this.useDurability = useDurability;
        this.factory = factory;
    }


    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't want to lock recipes behind any criterion, we make this a no-op
     */
    @Override
    public HeatingRecipeBuilder unlockedBy(String name, Criterion<?> criterion)
    {
        return this;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't divide Heating recipes into groups, we make this a no-op
     */
    @Override
    public HeatingRecipeBuilder group(@Nullable String groupName)
    {
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }



    public static HeatingRecipeBuilder heat(ItemLike ingredient, ItemLike result, float temperature)
    {
        return new HeatingRecipeBuilder(Ingredient.of(ingredient), new ItemStack(result), FluidStack.EMPTY, temperature, false, HeatingRecipe::new);
    }

    public static HeatingRecipeBuilder heat(ItemLike ingredient, Metal metal, int units, float temperature, boolean useDurability)
    {
        return new HeatingRecipeBuilder(Ingredient.of(ingredient), ItemStack.EMPTY, new FluidStack(TFCFluids.METALS.get(metal).getSource(), units), temperature, useDurability, HeatingRecipe::new);
    }

    public static HeatingRecipeBuilder heat(TagKey<Item> ingredient, Metal metal, int units, float temperature, boolean useDurability)
    {
        return new HeatingRecipeBuilder(Ingredient.of(ingredient), ItemStack.EMPTY, new FluidStack(TFCFluids.METALS.get(metal).getSource(), units), temperature, useDurability, HeatingRecipe::new);
    }

    public static HeatingRecipeBuilder heat(ItemLike ingredient, Fluid fluid, int units, float temperature, boolean useDurability)
    {
        return new HeatingRecipeBuilder(Ingredient.of(ingredient), ItemStack.EMPTY, new FluidStack(fluid, units), temperature, useDurability, HeatingRecipe::new);
    }

    public static HeatingRecipeBuilder heat(TagKey<Item> ingredient, Fluid fluid, int units, float temperature, boolean useDurability)
    {
        return new HeatingRecipeBuilder(Ingredient.of(ingredient), ItemStack.EMPTY, new FluidStack(fluid, units), temperature, useDurability, HeatingRecipe::new);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation)
    {
        HeatingRecipe heatingRecipe = this.factory.create(this.ingredient, this.resultProvider, this.outputFluid, this.temperature, this.useDurability);
        recipeOutput.accept(resourceLocation.withPrefix("heating/"), heatingRecipe, null);
    }


    public interface Factory<T extends HeatingRecipe>
    {
        T create(Ingredient ingredient, ItemStackProvider outputItem, FluidStack outputFluid, float temperature, boolean useDurability);
    }
}
