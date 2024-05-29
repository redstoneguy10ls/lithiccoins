package com.redstoneguy10ls.lithiccoins.compat.jei.category;

import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.common.items.TopDies;
import com.redstoneguy10ls.lithiccoins.common.items.stampTypes;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import com.redstoneguy10ls.lithiccoins.util.tooltips;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.dries007.tfc.util.Metal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MintingRecipeCategory extends BaseRecipeCategory<MintingRecipe> {

    public MintingRecipeCategory(RecipeType<MintingRecipe> type, IGuiHelper helper)
    {
        super(type,helper,helper.createBlankDrawable(118,26), new ItemStack(LCItems.BOTTOM_DIE.get(Metal.Default.RED_STEEL).get()));
    }
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MintingRecipe recipe, IFocusGroup focuses)
    {
        //topdie(recipe);

        final List<ItemStack> topdies = new ArrayList<>(topdie(recipe));


        builder.addSlot(RecipeIngredientRole.INPUT, 6, 5)
                .addIngredients(VanillaTypes.ITEM_STACK, topdies)
                .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 5)
                .addIngredients(recipe.getCoin())
                .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.INPUT, 46, 5)
                .addIngredients(bottom_teir(recipe))
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
    public List<ItemStack> topdie(MintingRecipe recipe)
    {
        final List<ItemStack> topdies = new ArrayList<>();
        //System.out.println(recipe.getTopDie()[0].name());
        int int1 = recipe.getTopDie()[0].getId();

        for(TopDies dies : TopDies.VALUES)
        {

            if(int1 == dies.getId())
            {
                top_tier(recipe).forEach(teirs -> topdies.add( new ItemStack(LCItems.TOP_DIE.get(dies).get(teirs).get()) ));
            }

        }
        return topdies;
    }

    public Ingredient bottom_teir(MintingRecipe recipe)
    {
        switch(recipe.getTier())
        {
            case 2: return Ingredient.of(LCTags.Items.T2_BOTTOM_DIES);
            case 3: return Ingredient.of(LCTags.Items.T3_BOTTOM_DIES);
            case 4: return Ingredient.of(LCTags.Items.T4_BOTTOM_DIES);
            case 5: return Ingredient.of(LCTags.Items.T5_BOTTOM_DIES);
            case 6: return Ingredient.of(LCTags.Items.T6_BOTTOM_DIES);
            default: return Ingredient.of(LCTags.Items.T1_BOTTOM_DIES);
        }

    }

    public List<Metal.Default> top_tier(@NotNull MintingRecipe recipe)
    {
        final List<Metal.Default> list = new ArrayList<>();
        final int recipeTier = recipe.getTier();
        for(int i = 7; i >= recipeTier; i--)
        {
            if(i == 6){list.add(Metal.Default.BLUE_STEEL); list.add(Metal.Default.RED_STEEL);}
            if(i == 5){list.add(Metal.Default.BLACK_STEEL);}
            if(i == 4){list.add(Metal.Default.STEEL);}
            if(i == 3){list.add(Metal.Default.WROUGHT_IRON);}
            if(i == 2){list.add(Metal.Default.BLACK_BRONZE); list.add(Metal.Default.BRONZE); list.add(Metal.Default.BISMUTH_BRONZE);}
            if(i == 1){list.add(Metal.Default.COPPER);}
        }
        return list;
    }


}
