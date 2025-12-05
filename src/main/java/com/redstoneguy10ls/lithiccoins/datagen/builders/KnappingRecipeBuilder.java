package com.redstoneguy10ls.lithiccoins.datagen.builders;

import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.recipes.KnappingRecipe;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.KnappingPattern;
import net.dries007.tfc.util.data.KnappingType;

public class KnappingRecipeBuilder implements RecipeBuilder
{
    private final DataManager.Reference<KnappingType> knappingType;
    private final KnappingPattern pattern;
    private final Optional<Ingredient> ingredient;
    private final ItemStack resultStack;
    private final Item result;
    private final Factory<?> factory;


    public KnappingRecipeBuilder(DataManager.Reference<KnappingType> knappingType, KnappingPattern pattern, Optional<Ingredient> ingredient, ItemLike result, Factory<?> factory)
    {
        this(knappingType, pattern, ingredient, new ItemStack(result), factory);
    }

    public KnappingRecipeBuilder(DataManager.Reference<KnappingType> knappingType, KnappingPattern pattern, Optional<Ingredient> ingredient, ItemStack result, Factory<?> factory)
    {
        this.knappingType = knappingType;
        this.pattern = pattern;
        this.ingredient = ingredient;
        this.resultStack = result;
        this.result = result.getItem();
        this.factory = factory;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't want to lock recipes behind any criterion, we make this a no-op
     */
    @Override
    public KnappingRecipeBuilder unlockedBy(String name, Criterion<?> criterion)
    {
        return this;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't divide knapping recipes into groups, we make this a no-op
     */
    @Override
    public KnappingRecipeBuilder group(@Nullable String group)
    {
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }


    public static KnappingRecipeBuilder clayKnapping(ItemLike result, String... pattern)
    {
        return new KnappingRecipeBuilder(KnappingType.MANAGER.getReference(Helpers.identifier("clay")), KnappingPattern.from(false, pattern), Optional.empty(), result, KnappingRecipe::new);
    }

    public static KnappingRecipeBuilder fireClayKnapping(ItemLike result, String... pattern)
    {
        return new KnappingRecipeBuilder(KnappingType.MANAGER.getReference(Helpers.identifier("fire_clay")), KnappingPattern.from(false, pattern), Optional.empty(), result, KnappingRecipe::new);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation)
    {
        KnappingRecipe knappingRecipe = this.factory.create(this.knappingType, this.pattern, this.ingredient, this.resultStack);
        recipeOutput.accept(resourceLocation.withPrefix("knapping/"), knappingRecipe, null);
    }


    public interface Factory<T extends KnappingRecipe>
    {
        T create(DataManager.Reference<KnappingType> knappingType, KnappingPattern pattern, Optional<Ingredient> ingredient, ItemStack result);
    }
}
