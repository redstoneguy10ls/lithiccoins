package com.redstoneguy10ls.lithiccoins.compat.jei;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCBlocks;
import com.redstoneguy10ls.lithiccoins.common.recipes.LCRecipeTypes;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import com.redstoneguy10ls.lithiccoins.compat.jei.category.MintingRecipeCategory;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@JeiPlugin
public class JEIIntegration implements IModPlugin {

    public static final IIngredientTypeWithSubtypes<Item, ItemStack> ITEM_STACK = VanillaTypes.ITEM_STACK;

    public static RecipeType<MintingRecipe> MINTING = type("minting", MintingRecipe.class);


    private static <T> RecipeType<T> type(String name, Class<T> tClass)
    {
        return RecipeType.create(LithicCoins.MOD_ID, name, tClass);
    }

    private static <C extends Container, T extends Recipe<C>> List<T> recipes(net.minecraft.world.item.crafting.RecipeType<T> type)
    {
        return ClientHelpers.getLevelOrThrow().getRecipeManager().getAllRecipesFor(type);
    }
    private static <C extends Container, T extends Recipe<C>> List<T> recipes(net.minecraft.world.item.crafting.RecipeType<T> type, Predicate<T> filter)
    {
        return recipes(type).stream().filter(filter).collect(Collectors.toList());
    }

    private static void addRecipeCatalyst(IRecipeCatalystRegistration registry, TagKey<Item> tag, RecipeType<?> recipeType) {
        Helpers.getAllTagValues(tag, ForgeRegistries.ITEMS).forEach(item -> registry.addRecipeCatalyst(new ItemStack(item),recipeType));
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

        registry.addRecipeCategories(
                new MintingRecipeCategory(MINTING,gui)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry)
    {
        registry.addRecipes(MINTING, recipes(LCRecipeTypes.MINTING.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        registry.addRecipeCatalyst(new ItemStack(LCBlocks.MINT.get()),MINTING);
    }

}
