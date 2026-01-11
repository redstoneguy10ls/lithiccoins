package com.redstoneguy10ls.lithiccoins.compat.jei.category;

import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.dries007.tfc.util.Metal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MintingRecipeCategory extends BaseRecipeCategory<MintingRecipe> {

    public MintingRecipeCategory(RecipeType<MintingRecipe> type, IGuiHelper helper)
    {
        super(type, helper, 118, 26, new ItemStack(LCItems.BOTTOM_DIE.get(Metal.RED_STEEL).get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MintingRecipe recipe, IFocusGroup focuses)
    {
        final List<ItemStack> topDies = new ArrayList<>(topDie(recipe));

        builder.addSlot(RecipeIngredientRole.INPUT, 6, 5)
            .addIngredients(VanillaTypes.ITEM_STACK, topDies)
            .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 5)
            .addIngredients(recipe.getCoin())
            .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.INPUT, 46, 5)
            .addIngredients(bottomTier(recipe))
            .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 5)
            .addItemStack(recipe.getResultItem(registryAccess()))
            .setBackground(slot, -1, -1);
    }

    @Override
    public void draw(MintingRecipe recipe, IRecipeSlotsView recipeSlots, GuiGraphics stack, double mouseX, double mouseY)
    {
        arrow.draw(stack, 68, 5);
        arrowAnimated.draw(stack, 68, 5);
    }

    //Ingredient
    public List<ItemStack> topDie(MintingRecipe recipe)
    {
        final List<ItemStack> topDies = new ArrayList<>();
        StampType type = recipe.getStampType();

        topTier(recipe).forEach(tier -> topDies.add(new ItemStack(LCItems.TOP_DIE.get(type).get(tier).get())));

        return topDies;
    }

    public Ingredient bottomTier(MintingRecipe recipe)
    {
        return Ingredient.of(LCTags.Items.BOTTOM_DIE_TIER_MAP.getOrDefault(recipe.getTier(), LCTags.Items.BOTTOM_DIE_TIER_MAP.get(1)));
    }

    public List<Metal> topTier(@NotNull MintingRecipe recipe)
    {
        final List<Metal> list = new ArrayList<>();
        final int recipeTier = recipe.getTier();
        for(int i = 7; i >= recipeTier; i--)
        {
            if(i == 6){list.add(Metal.BLUE_STEEL); list.add(Metal.RED_STEEL);}
            if(i == 5){list.add(Metal.BLACK_STEEL);}
            if(i == 4){list.add(Metal.STEEL);}
            if(i == 3){list.add(Metal.WROUGHT_IRON);}
            if(i == 2){list.add(Metal.BLACK_BRONZE); list.add(Metal.BRONZE); list.add(Metal.BISMUTH_BRONZE);}
            if(i == 1){list.add(Metal.COPPER);}
        }
        return list;
    }


}
