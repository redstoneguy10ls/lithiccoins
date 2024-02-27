package com.redstoneguy10ls.lithiccoins.recipes;

import net.dries007.tfc.common.recipes.SimpleItemRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MintRecipe extends SimpleItemRecipe {

    public static final IndirectHashCollection<Item, MintRecipe> CACHE = IndirectHashCollection.createForRecipe(MintRecipe::getValidItems, ModRecipeTypes.MINT);

    @Nullable
    public static MintRecipe getRecipe(Level world, ItemStackInventory wrapper)
    {
        for (MintRecipe recipe : CACHE.getAll(wrapper.getStack().getItem()))
        {
            if(recipe.matches(wrapper, world))
            {
                return recipe;
            }
        }
        return null;
    }
    public MintRecipe(ResourceLocation id, Ingredient ingredient, ItemStackProvider result)
    {
        super(id,ingredient,result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MINT.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MINT.get();
    }
}
