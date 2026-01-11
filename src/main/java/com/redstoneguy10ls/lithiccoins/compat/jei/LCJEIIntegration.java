package com.redstoneguy10ls.lithiccoins.compat.jei;

import com.redstoneguy10ls.lithiccoins.client.screen.LargeKnappingScreen;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCBlocks;
import com.redstoneguy10ls.lithiccoins.common.recipes.LCRecipeTypes;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import com.redstoneguy10ls.lithiccoins.common.recipes.LargeKnappingRecipe;
import com.redstoneguy10ls.lithiccoins.compat.jei.category.MintingRecipeCategory;
import com.redstoneguy10ls.lithiccoins.compat.jei.category.LargeKnappingRecipeCategory;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.util.data.KnappingType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;


@JeiPlugin
public class LCJEIIntegration implements IModPlugin
{
    public static final IIngredientTypeWithSubtypes<Item, ItemStack> ITEM_STACK = VanillaTypes.ITEM_STACK;
    private static final Map<KnappingType, RecipeType<LargeKnappingRecipe>> LARGE_KNAPPING_TYPES = new HashMap<>();

    public static RecipeType<MintingRecipe> MINTING = type("minting", MintingRecipe.class);

    public static RecipeType<LargeKnappingRecipe> getKnappingType(Map.Entry<ResourceLocation, KnappingType> entry)
    {
        return LARGE_KNAPPING_TYPES.computeIfAbsent(entry.getValue(), key -> type(entry.getKey().getPath() + "_large_knapping", LargeKnappingRecipe.class));
    }

    private static <T> RecipeType<T> type(String name, Class<T> tClass)
    {
        return RecipeType.create(MOD_ID, name, tClass);
    }

    private static <C extends RecipeInput, T extends Recipe<C>> List<T> recipes(Supplier<net.minecraft.world.item.crafting.RecipeType<T>> type)
    {
        return recipes(type, e -> true);
    }

    private static <C extends RecipeInput, T extends Recipe<C>> List<T> recipes(Supplier<net.minecraft.world.item.crafting.RecipeType<T>> type, Predicate<T> filter)
    {
        return ClientHelpers.getLevelOrThrow().getRecipeManager()
            .getAllRecipesFor(type.get())
            .stream()
            .map(RecipeHolder::value)
            .filter(filter)
            .toList();
    }

    @Override
    public ResourceLocation getPluginUid()
    {
        return LCHelpers.identifier("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        final IGuiHelper gui = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new MintingRecipeCategory(MINTING,gui));

        for (var entry : KnappingType.MANAGER.getElements().entrySet())
        {
            registry.addRecipeCategories(new LargeKnappingRecipeCategory<>(getKnappingType(entry), gui, entry.getValue()));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry)
    {
        registry.addRecipes(MINTING, recipes(LCRecipeTypes.MINTING));

        LARGE_KNAPPING_TYPES.forEach((knappingType, recipeType) -> registry.addRecipes(recipeType, recipes(LCRecipeTypes.LARGE_KNAPPING, r -> r.knappingType().get().equals(knappingType))));

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        registry.addRecipeCatalyst(new ItemStack(LCBlocks.MINT.asItem()), MINTING);

        for (var entry : KnappingType.MANAGER.getElements().entrySet())
        {
            final RecipeType<LargeKnappingRecipe> recipeType = getKnappingType(entry);
            for (ItemStack item : entry.getValue().inputItem().ingredient().getItems())
            {
                registry.addRecipeCatalyst(item, recipeType);
            }
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry)
    {
        registry.addRecipeClickArea(LargeKnappingScreen.class, 97, 44, 22, 15, LARGE_KNAPPING_TYPES.values().toArray(RecipeType[]::new));
    }
}
