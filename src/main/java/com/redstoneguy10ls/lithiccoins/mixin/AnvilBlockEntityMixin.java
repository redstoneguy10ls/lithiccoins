/*
package com.redstoneguy10ls.lithiccoins.mixin;

import com.redstoneguy10ls.lithiccoins.recipes.MintRecipe;
import com.redstoneguy10ls.lithiccoins.test.dieAccessor;
import com.redstoneguy10ls.lithiccoins.util.ModTags;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.container.ISlotCallback;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = AnvilBlockEntity.class, remap = false, priority = 1)
public class AnvilBlockEntityMixin extends InventoryBlockEntity<AnvilBlockEntity.AnvilInventory> implements ISlotCallback, dieAccessor {

    private static final int SLOT_BOTTOM_DIE = 4;

    private static final int SLOT_COIN = 5;
    private static final int SLOT_TOP_DIE = 6;



    public AnvilBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, InventoryFactory<AnvilBlockEntity.AnvilInventory> inventoryFactory, Component defaultName) {
        super(type, pos, state, inventoryFactory, defaultName);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return slot != SLOT_BOTTOM_DIE || Helpers.isItem(stack.getItem(), ModTags.Items.BOTTOM_DIE);
    }

    @Override
    public boolean startMinting()
    {
        assert level != null;
        final ItemStack inputStack = inventory.getStackInSlot(SLOT_COIN);
        if(!inputStack.isEmpty())
        {
            final ItemStackInventory wrapper = new ItemStackInventory(inputStack);
            final MintRecipe recipe = MintRecipe.getRecipe(level, wrapper);
            if(recipe != null && recipe.matches(wrapper,level))
            {
                level.playSound(null, worldPosition, SoundEvents.ANVIL_DESTROY, SoundSource.PLAYERS, 0.4f, 1.0f);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean hasBottomDie()
    {
        return !inventory.getStackInSlot(SLOT_BOTTOM_DIE).isEmpty();
    }
    @Override
    public int getBottomDieSlot()
    {
        return SLOT_BOTTOM_DIE;
    }
}
*/

