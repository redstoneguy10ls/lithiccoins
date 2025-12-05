package com.redstoneguy10ls.lithiccoins.compat.jei.category;

import com.redstoneguy10ls.lithiccoins.compat.jei.LCJEIIntegration;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;

import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.common.component.food.FoodCapability;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public abstract class BaseRecipeCategory<T> extends AbstractRecipeCategory<T>
{
    public static final ResourceLocation ICONS = LCHelpers.identifier("textures/gui/jei/icons.png");

    public static RegistryAccess registryAccess()
    {
        return ClientHelpers.getLevelOrThrow().registryAccess();
    }

    protected final IDrawableStatic slot;
    protected final IDrawableStatic arrow;
    protected final IDrawableAnimated arrowAnimated;

    public BaseRecipeCategory(RecipeType<T> type, IGuiHelper helper, int width, int height, ItemStack icon)
    {
        super(type, Component.translatable(MOD_ID + ".jei." + type.getUid().getPath()), helper.createDrawableIngredient(LCJEIIntegration.ITEM_STACK, FoodCapability.setNonDecaying(icon)), width, height);
        this.slot = helper.getSlotDrawable();

        this.arrow = helper.createDrawable(ICONS, 0, 14, 22, 16);
        IDrawableStatic arrowAnimated = helper.createDrawable(ICONS, 22, 14, 22, 16);
        this.arrowAnimated = helper.createAnimatedDrawable(arrowAnimated, 80, IDrawableAnimated.StartDirection.LEFT, false);
    }
}
