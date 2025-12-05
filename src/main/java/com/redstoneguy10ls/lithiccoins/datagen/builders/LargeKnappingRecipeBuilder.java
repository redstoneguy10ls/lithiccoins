package com.redstoneguy10ls.lithiccoins.datagen.builders;

import java.util.Optional;
import com.redstoneguy10ls.lithiccoins.common.recipes.LargeKnappingRecipe;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.LargeKnappingPattern;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.KnappingType;

public class LargeKnappingRecipeBuilder implements RecipeBuilder
{
    private final DataManager.Reference<KnappingType> knappingType;
    private final LargeKnappingPattern pattern;
    private final Optional<Ingredient> ingredient;
    private final ItemStack resultStack;
    private final Item result;
    private final Factory<?> factory;


    public LargeKnappingRecipeBuilder(DataManager.Reference<KnappingType> knappingType, LargeKnappingPattern pattern, Optional<Ingredient> ingredient, ItemLike result, Factory<?> factory)
    {
        this(knappingType, pattern, ingredient, new ItemStack(result), factory);
    }

    public LargeKnappingRecipeBuilder(DataManager.Reference<KnappingType> knappingType, LargeKnappingPattern pattern, Optional<Ingredient> ingredient, ItemStack result, Factory<?> factory)
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
    public LargeKnappingRecipeBuilder unlockedBy(String name, Criterion<?> criterion)
    {
        return this;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't divide large knapping recipes into groups, we make this a no-op
     */
    @Override
    public LargeKnappingRecipeBuilder group(@Nullable String group)
    {
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }

    public static LargeKnappingRecipeBuilder waxKnapping(LargeKnappingPattern pattern, ItemLike result)
    {
        return new LargeKnappingRecipeBuilder(KnappingType.MANAGER.getReference(LCHelpers.identifier("wax")), pattern, Optional.empty(), result, LargeKnappingRecipe::new);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation)
    {
        LargeKnappingRecipe largeKnappingRecipe = this.factory.create(this.knappingType, this.pattern, this.ingredient, this.resultStack);
        recipeOutput.accept(resourceLocation.withPrefix("large_knapping/"), largeKnappingRecipe, null);
    }


    public interface Factory<T extends LargeKnappingRecipe>
    {
        T create(DataManager.Reference<KnappingType> knappingType, LargeKnappingPattern pattern, Optional<Ingredient> ingredient, ItemStack result);
    }
}
