package com.redstoneguy10ls.lithiccoins.compat.jei.category;

import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.common.items.stampTypes;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.util.Metal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class MintingRecipeCategory extends BaseRecipeCategory<MintingRecipe> {

    public MintingRecipeCategory(RecipeType<MintingRecipe> type, IGuiHelper helper)
    {
        super(type,helper,helper.createBlankDrawable(118,26), new ItemStack(LCItems.BOTTOM_DIE.get(Metal.Default.RED_STEEL).get()));
    }
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MintingRecipe recipe, IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 5)
                .addIngredients(Ingredient.of(LCItems.TOP_DIE.get(stampTypes.MINER).get(Metal.Default.BLACK_STEEL).get()))
                .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 5)
                .addIngredients(recipe.getCoin())
                .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.CATALYST, 46, 5)
                .addIngredients(Ingredient.of(LCItems.BOTTOM_DIE.get(Metal.Default.RED_STEEL).get()))
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
}
