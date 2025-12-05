package com.redstoneguy10ls.lithiccoins.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.redstoneguy10ls.lithiccoins.common.container.WaxKnappingContainer;
import com.redstoneguy10ls.lithiccoins.util.LargeKnappingPattern;

import net.dries007.tfc.common.recipes.INoopInputRecipe;
import net.dries007.tfc.common.recipes.IRecipePredicate;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.KnappingType;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LargeKnappingRecipe implements INoopInputRecipe, IRecipePredicate<WaxKnappingContainer>
{
    public static final MapCodec<LargeKnappingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        KnappingType.MANAGER.byIdReferenceCodec().fieldOf("knapping_type").forGetter(c -> c.knappingType),
        LargeKnappingPattern.CODEC.forGetter(c -> c.pattern),
        Ingredient.CODEC.optionalFieldOf("ingredient").forGetter(c -> c.ingredient),
        ItemStack.CODEC.fieldOf("result").forGetter(c -> c.result)
    ).apply(i, LargeKnappingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LargeKnappingRecipe> STREAM_CODEC = StreamCodec.composite(
        KnappingType.MANAGER.byIdStreamCodec(), c -> c.knappingType,
        LargeKnappingPattern.STREAM_CODEC, c -> c.pattern,
        ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), c -> c.ingredient,
        ItemStack.STREAM_CODEC, c -> c.result,
        LargeKnappingRecipe::new
    );

    @Nullable
    public static LargeKnappingRecipe get(Level level, WaxKnappingContainer input)
    {
        return RecipeHelpers.unbox(RecipeHelpers.getHolder(level, LCRecipeTypes.LARGE_KNAPPING, input));
    }

    private final DataManager.Reference<KnappingType> knappingType;
    private final LargeKnappingPattern pattern;
    private final Optional<Ingredient> ingredient;
    private final ItemStack result;

    public LargeKnappingRecipe(DataManager.Reference<KnappingType> knappingType, LargeKnappingPattern pattern, Optional<Ingredient> ingredient, ItemStack result)
    {
        this.knappingType = knappingType;
        this.pattern = pattern;
        this.ingredient = ingredient;
        this.result = result;
    }


    @Override
    public boolean matches(WaxKnappingContainer input)
    {
        return input.getKnappingType() == knappingType.get()
            && input.getPattern().matches(getPattern())
            && matchesItem(input.getOriginalStack());
    }

    public boolean matchesItem(ItemStack stack)
    {
        return ingredient.isEmpty() || ingredient.get().test(stack);
    }

    public ItemStack assemble()
    {
        return result.copy();
    }

    public DataManager.Reference<KnappingType> knappingType()
    {
        return knappingType;
    }

    public LargeKnappingPattern getPattern()
    {
        return pattern;
    }

    @Nullable
    public Ingredient getIngredient()
    {
        return ingredient.orElse(null);
    }

    @Override
    public ItemStack getResultItem(@Nullable HolderLookup.Provider registries)
    {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return LCRecipeSerializers.WAX_KNAPPING.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return LCRecipeTypes.LARGE_KNAPPING.get();
    }
}
