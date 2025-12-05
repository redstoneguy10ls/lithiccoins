package com.redstoneguy10ls.lithiccoins.compat.jei.category;

import java.util.Arrays;
import com.redstoneguy10ls.lithiccoins.compat.jei.LCJEIIntegration;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import com.redstoneguy10ls.lithiccoins.client.screen.LargeKnappingScreen;
import com.redstoneguy10ls.lithiccoins.common.recipes.LargeKnappingRecipe;
import net.dries007.tfc.util.data.KnappingType;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.Nullable;

public class LargeKnappingRecipeCategory<T extends LargeKnappingRecipe> extends BaseRecipeCategory<T>
{
    private static final String INPUT_SLOT_NAME = "input";

    private final KnappingType knappingType;
    private final IGuiHelper helper;

    public LargeKnappingRecipeCategory(RecipeType<T> type, IGuiHelper helper, KnappingType knappingType)
    {
        super(type, helper, 155, 82, knappingType.icon());

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
        final boolean osr = recipe.getPattern().defaultIsOn();
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
        final SizedIngredient inputItem = recipe.getIngredient() != null
            // If this knapping recipe has an ingredient, we need to apply the count of the type's ingredient to it
            // See TerraFirmaCraft#2725
            ? new SizedIngredient(recipe.getIngredient(), recipe.knappingType().get().inputItem().count())
            : recipe.knappingType().get().inputItem();

        final IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 0, 33);
        inputSlot.addItemStacks(Arrays.stream(inputItem.ingredient().getItems()).toList()).setSlotName(INPUT_SLOT_NAME);
        inputSlot.setBackground(slot, -1, -1);

        final IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 33);
        outputSlot.addItemStack(recipe.getResultItem(registryAccess()));
        outputSlot.setBackground(slot, -1, -1);
    }

    @Nullable
    private IDrawable getTexture(IRecipeSlotsView slots, boolean disabled)
    {
        if (disabled && !knappingType.hasOffTexture())
        {
            return null;
        }
        return slots.findSlotByName(INPUT_SLOT_NAME)
            .flatMap(slot -> slot.getDisplayedIngredient(LCJEIIntegration.ITEM_STACK))
            .map(displayed -> {
                final ResourceLocation high = LargeKnappingScreen.getButtonLocation(displayed.getItem(), disabled);
                return helper.drawableBuilder(high, 0, 0, 10, 10).setTextureSize(10, 10).build();
            })
            .orElse(null);
    }

}
