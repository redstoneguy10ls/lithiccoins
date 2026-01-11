package com.redstoneguy10ls.lithiccoins.datagen.builders;

import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;

public class MintingRecipeBuilder implements RecipeBuilder
{
    private final StampType type;
    private final Ingredient coin;
    private final int tier;
    private final ItemStackProvider resultProvider;
    private final Item result;
    private final Factory<?> factory;

    public MintingRecipeBuilder(StampType type, Ingredient coin, int tier, ItemLike result, Factory<?> factory)
    {
        this(type, coin, tier, new ItemStack(result), factory);
    }

    public MintingRecipeBuilder(StampType type, Ingredient coin, int tier, ItemStack stackResult, Factory<?> factory)
    {
        this.type = type;
        this.coin = coin;
        this.tier = tier;
        this.resultProvider = ItemStackProvider.of(stackResult);
        this.result = stackResult.getItem();
        this.factory = factory;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't want to lock recipes behind any criterion, we make this a no-op
     */
    @Override
    public MintingRecipeBuilder unlockedBy(String name, Criterion<?> criterion)
    {
        return this;
    }

    /**
     * Required implementation by {@link RecipeBuilder}, but as we don't divide minting recipes into groups, we make this a no-op
     */
    @Override
    public MintingRecipeBuilder group(@Nullable String groupName)
    {
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }

    public static MintingRecipeBuilder mint(StampType type, Ingredient ingredient, int tier, ItemLike result)
    {
        return new MintingRecipeBuilder(type, ingredient, tier, result, MintingRecipe::new);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation)
    {
        MintingRecipe mintingRecipe = this.factory.create(this.type, this.coin, this.tier, this.resultProvider);
        recipeOutput.accept(resourceLocation.withPrefix("minting/"), mintingRecipe, null);
    }


    public interface Factory<T extends MintingRecipe>
    {
        T create(StampType type, Ingredient coin, int tier, ItemStackProvider output);
    }
}
