package com.redstoneguy10ls.lithiccoins.compat.jei;

import com.electronwill.nightconfig.core.utils.ObservedMap;
import com.redstoneguy10ls.lithiccoins.LithicCoins;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCBlocks;
import com.redstoneguy10ls.lithiccoins.common.recipes.LCRecipeTypes;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import com.redstoneguy10ls.lithiccoins.common.recipes.WaxKnappingRecipe;
import com.redstoneguy10ls.lithiccoins.compat.jei.category.MintingRecipeCategory;
import com.redstoneguy10ls.lithiccoins.compat.jei.category.WaxKnappingRecipeCategory;
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
import net.dries007.tfc.common.recipes.KnappingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.compat.jei.category.KnappingRecipeCategory;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.KnappingType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;



@JeiPlugin
public class JEIIntegration implements IModPlugin {

    public static final IIngredientTypeWithSubtypes<Item, ItemStack> ITEM_STACK = VanillaTypes.ITEM_STACK;
    private static final Map<ResourceLocation, RecipeType<WaxKnappingRecipe>> KNAPPING_TYPES = new HashMap<>();

    public static RecipeType<MintingRecipe> MINTING = type("minting", MintingRecipe.class);

    public static RecipeType<WaxKnappingRecipe> getKnappingType(KnappingType type)
    {
        return KNAPPING_TYPES.computeIfAbsent(type.getId(), key -> type(key.getPath() + "_knapping", WaxKnappingRecipe.class));
    }

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


        for (KnappingType knappingType : KnappingType.MANAGER.getValues())
        {
            final RecipeType<WaxKnappingRecipe> recipeType = getKnappingType(knappingType);
            registry.addRecipeCategories(new WaxKnappingRecipeCategory<>(recipeType, gui, knappingType));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry)
    {
        registry.addRecipes(MINTING, recipes(LCRecipeTypes.MINTING.get()));

        KNAPPING_TYPES.forEach((id, type) -> registry.addRecipes(type, recipes(LCRecipeTypes.WAX_KNAPPING.get(), r -> r.getKnappingType().getId().toString().replace("_knapping", "").equals(id.toString()))));

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        registry.addRecipeCatalyst(new ItemStack(LCBlocks.MINT.get()),MINTING);

        for (KnappingType knappingType : KnappingType.MANAGER.getValues())
        {
            final RecipeType<WaxKnappingRecipe> recipeType = getKnappingType(knappingType);
            for (ItemStack item : knappingType.inputItem().ingredient().getItems())
            {
                registry.addRecipeCatalyst(item, recipeType);
            }
        }
    }

}
