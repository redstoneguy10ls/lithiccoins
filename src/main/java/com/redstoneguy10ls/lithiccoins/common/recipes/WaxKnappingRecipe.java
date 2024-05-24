package com.redstoneguy10ls.lithiccoins.common.recipes;

import com.google.gson.JsonObject;
import com.redstoneguy10ls.lithiccoins.util.LCKnappingPattern;
import net.dries007.tfc.common.container.KnappingContainer;
import net.dries007.tfc.common.recipes.ISimpleRecipe;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.KnappingPattern;
import net.dries007.tfc.util.KnappingType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class WaxKnappingRecipe implements ISimpleRecipe<KnappingContainer.Query> {


    private final ResourceLocation id;
    private final LCKnappingPattern pattern;
    private final ItemStack result;
    private final @Nullable Ingredient ingredient;
    private final Supplier<KnappingType> knappingType;

    public WaxKnappingRecipe(ResourceLocation id, LCKnappingPattern pattern, ItemStack result, @Nullable Ingredient ingredient, Supplier<KnappingType> knappingType) {
        this.id = id;
        this.pattern = pattern;
        this.result = result;
        this.ingredient = ingredient;
        this.knappingType = knappingType;
    }


    @Override
    public boolean matches(Query query, Level level)
    {
        return query.container().getKnappingType() == knappingType.get()
                && query.container().getPattern().matches(getPattern())
                && matchesItem(query.container().getOriginalStack());
    }

    public boolean matchesItem(ItemStack stack)
    {
        return ingredient == null || ingredient.test(stack);
    }
    @Override
    public ItemStack getResultItem(@Nullable RegistryAccess access)
    {
        return result;
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer(){return LCRecipeSerializers.WAX_KNAPPING.get();}

    @Override
    public RecipeType<?> getType(){return LCRecipeTypes.WAX_KNAPPING.get();}

    public LCKnappingPattern getPattern()
    {
        return pattern;
    }

    @Nullable
    public Ingredient getIngredient()
    {
        return ingredient;
    }

    public static class Serializer extends RecipeSerializerImpl<WaxKnappingRecipe>
    {

        @Override
        public WaxKnappingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final Supplier<KnappingType> knappingType = JsonHelpers.getReference(json, "knapping_type", KnappingType.MANAGER);
            final ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            final @Nullable Ingredient ingredient = json.has("ingredient") ? Ingredient.fromJson(json.get("ingredient")) : null;
            final LCKnappingPattern pattern = LCKnappingPattern.fromJson(json);
            return new WaxKnappingRecipe(recipeId, pattern, result, ingredient, knappingType)
        }

        @Override
        public @Nullable WaxKnappingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WaxKnappingRecipe recipe) {

        }
    }

}
