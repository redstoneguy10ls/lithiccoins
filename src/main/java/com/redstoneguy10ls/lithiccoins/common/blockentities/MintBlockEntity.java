package com.redstoneguy10ls.lithiccoins.common.blockentities;

import com.redstoneguy10ls.lithiccoins.common.blocks.MintBlock;
import com.redstoneguy10ls.lithiccoins.common.component.MintingComponent;
import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import com.redstoneguy10ls.lithiccoins.common.recipes.LCRecipeTypes;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import com.redstoneguy10ls.lithiccoins.config.ServerConfig;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.capabilities.DelegateItemHandler;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.container.ISlotCallback;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class MintBlockEntity extends InventoryBlockEntity<MintBlockEntity.MintInventory> implements ISlotCallback
{
    public static final int SLOT_TOP_DIE = 0;
    public static final int SLOT_BOTTOM_DIE = 1;
    public static final int SLOT_COIN = 2;
    public static final int SLOT_OUTPUT = 3;

    public boolean hit = false;
    private int hitTimer = 0;
    private boolean needsStateUpdate = false;

    public MintBlockEntity(BlockPos pos, BlockState state)
    {
        super(LCBlockEntities.MINT.get(), pos, state, MintInventory::new, MOD_ID);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MintBlockEntity mint)
    {
        if(mint.needsStateUpdate)
        {
            mint.updateHit();
        }

        if(mint.hitTimer > 0)
        {
            mint.hitTimer--;
        }
        else if(mint.hitTimer == 0)
        {
            mint.hit = false;
            mint.updateHit();
        }
    }

    public void updateHit()
    {
        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        BlockState newState = Helpers.setProperty(state, MintBlock.HIT, hasHit());
        if(hasHit() != state.getValue(MintBlock.HIT))
        {
            level.setBlockAndUpdate(worldPosition, newState);
        }
        needsStateUpdate = false;
    }


    @Override
    public void setAndUpdateSlots(int slot)
    {
        super.setAndUpdateSlots(slot);
        needsStateUpdate = true;
    }



    @Override
    public int getSlotStackLimit(int slot)
    {
       if(slot == SLOT_BOTTOM_DIE || slot == SLOT_TOP_DIE)
       {
           return 1;
       }
       else
       {
           return 64;
       }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return switch (slot)
        {
            case SLOT_TOP_DIE -> Helpers.isItem(stack.getItem(), LCTags.Items.TOP_DIE);
            case SLOT_BOTTOM_DIE -> Helpers.isItem(stack.getItem(), LCTags.Items.BOTTOM_DIE);
            case SLOT_COIN -> Helpers.isItem(stack.getItem(), LCTags.Items.BLANK_COIN) && inventory.hasBottomDie();
            default -> true;
        };
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        needsStateUpdate = true;
        super.loadAdditional(nbt, provider);
    }

    public void setHitTimer(int ticks)
    {
        this.hitTimer = ticks;
        this.hit = hitTimer > 0;
    }

    public boolean hasHit()
    {
        return hit;
    }

    public InteractionResult tryMinting(Player player)
    {
        final ItemStack topDie = inventory.getStackInSlot(SLOT_TOP_DIE);
        final ItemStack bottomDie = inventory.getStackInSlot(SLOT_BOTTOM_DIE);
        final ItemStack coin = inventory.getStackInSlot(SLOT_COIN);

        if(topDie.isEmpty() || bottomDie.isEmpty() || coin.isEmpty())
        {
            return InteractionResult.PASS;
        }

        assert level != null;

        MintingRecipe recipe = level.getRecipeManager()
            .getRecipeFor(LCRecipeTypes.MINTING.get(), inventory, level)
            .map(RecipeHolder::value)
            .orElse(null);

        // To give players useful feedback in case the dies are of a too low tier, we give them an info message
        // However, we can not know the required tier of the recipe at this point, so the message has to be generic
        if (recipe == null)
        {
            player.displayClientMessage(Component.translatable("lithiccoins.tooltip.lithiccoins.mint.dies_to_low"), true);
            return InteractionResult.FAIL;
        }
        else
        {
            final ItemStack result = recipe.assemble(inventory, level.registryAccess());
            MintingComponent.set(result, ChunkPos.asLong(getBlockPos()), Calendars.get().getCalendarYear(), ServerConfig.engraveName.getAsBoolean() ? player : null);

            if(inventory.getStackInSlot(SLOT_OUTPUT).isEmpty())
            {
                inventory.setStackInSlot(SLOT_OUTPUT, result);
            }
            else
            {
                inventory.getStackInSlot(SLOT_OUTPUT).grow(1);
            }

            inventory.getStackInSlot(SLOT_COIN).shrink(1);

            Helpers.damageItem(topDie, level);
            Helpers.damageItem(bottomDie, level);
            setAndUpdateSlots(SLOT_TOP_DIE);
            setAndUpdateSlots(SLOT_BOTTOM_DIE);
            markForSync();

            return InteractionResult.SUCCESS;
        }
    }

    public static class MintInventory implements RecipeInput, DelegateItemHandler, INBTSerializable<CompoundTag>
    {
        private final MintBlockEntity mint;
        private final ItemStackHandler inventory;

        public MintInventory(InventoryBlockEntity<MintInventory> entity)
        {
            this.mint = (MintBlockEntity) entity;
            this.inventory = new InventoryItemHandler(entity, 4);
        }

        @Override
        public IItemHandlerModifiable getItemHandler()
        {
            return inventory;
        }

        @Override
        public ItemStack getItem(int i)
        {
            return inventory.getStackInSlot(i);
        }

        @Override
        public int size()
        {
            return 1;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack)
        {
            this.getItemHandler().setStackInSlot(slot, stack);
            mint.setAndUpdateSlots(slot);
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider)
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.put("inventory", inventory.serializeNBT(provider));
            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
        {
            inventory.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        }

        public StampType getType()
        {
            return LCHelpers.getStamptype(inventory.getStackInSlot(SLOT_TOP_DIE).getItem());
        }

        public boolean hasBottomDie()
        {
            return !inventory.getStackInSlot(SLOT_BOTTOM_DIE).isEmpty();
        }
    }
}
