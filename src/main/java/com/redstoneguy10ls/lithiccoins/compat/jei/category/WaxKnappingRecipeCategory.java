package com.redstoneguy10ls.lithiccoins.compat.jei.category;

import com.redstoneguy10ls.lithiccoins.client.screen.WaxKanppingScreen;
import com.redstoneguy10ls.lithiccoins.common.recipes.WaxKnappingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.dries007.tfc.compat.jei.JEIIntegration;
import net.dries007.tfc.compat.jei.category.BaseRecipeCategory;
import net.dries007.tfc.util.KnappingType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class WaxKnappingRecipeCategory<T extends WaxKnappingRecipe> extends BaseRecipeCategory<T>
{
    private static final String INPUT_SLOT_NAME = "input";

    private final KnappingType knappingType;
    private final IGuiHelper helper;

    public WaxKnappingRecipeCategory(RecipeType<T> type, IGuiHelper helper, KnappingType knappingType)
    {
        super(type, helper, helper.createBlankDrawable(155, 82), knappingType.jeiIcon());

        this.knappingType = knappingType;
        this.helper = helper;
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlots, GuiGraphics stack, double mouseX, double mouseY)
    {
        arrow.draw(stack, 106, 33);
        arrowAnimated.draw(stack, 106, 33);
        IDrawable high = getTexture(recipeSlots, false);
        IDrawable low = getTexture(recipeSlots, true);

        final int height = recipe.getPattern().getHeight();
        final int width = recipe.getPattern().getWidth();
        final boolean osr = recipe.getPattern().isOutsideSlotRequired();
        final int offsetX = Math.floorDiv(8 - width, 2);
        final int offsetY = Math.floorDiv(8 - height, 2);

        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                final int yd = y - offsetY;
                final int xd = x - offsetX;
                if (0 <= yd && yd < height && 0 <= xd && xd < width)
                {
                    if (recipe.getPattern().get(xd, yd))
                    {
                        if (high != null)
                        {
                            high.draw(stack, 21 + x * 10, 1 + y * 10);
                        }
                    }
                    else if (low != null)
                    {
                        low.draw(stack, 21 + x * 10, 1 + y * 10);
                    }
                }
                else
                {
                    // out of bounds
                    if (osr)
                    {
                        if (high != null)
                        {
                            high.draw(stack, 21 + x * 10, 1 + y * 10);
                        }
                    }
                    else if (low != null)
                    {
                        low.draw(stack, 21 + x * 10, 1 + y * 10);
                    }
                }
            }
        }


    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses)
    {
        final List<ItemStack> inputs = recipe.getIngredient() != null ? Arrays.asList(recipe.getIngredient().getItems()) : collapse(recipe.getKnappingType().inputItem());
        final IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 0, 33);
        inputSlot.addItemStacks(inputs).setSlotName(INPUT_SLOT_NAME);
        inputSlot.setBackground(slot, -1, -1);

        final IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 33);
        outputSlot.addItemStack(recipe.getResultItem(registryAccess()));
        outputSlot.setBackground(slot, -1, -1);
    }

    @Nullable
    private IDrawable getTexture(IRecipeSlotsView slots, boolean disabled)
    {
        if (disabled && !knappingType.usesDisabledTexture())
        {
            return null;
        }
        return slots.findSlotByName(INPUT_SLOT_NAME)
                .flatMap(slot -> slot.getDisplayedIngredient(JEIIntegration.ITEM_STACK))
                .map(displayed -> {
                    final ResourceLocation high = WaxKanppingScreen.getButtonLocation(displayed.getItem(), disabled);
                    return helper.drawableBuilder(high, 0, 0, 10, 10).setTextureSize(10, 10).build();
                })
                .orElse(null);
    }

}
