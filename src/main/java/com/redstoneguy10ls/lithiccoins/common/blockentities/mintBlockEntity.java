package com.redstoneguy10ls.lithiccoins.common.blockentities;

import com.redstoneguy10ls.lithiccoins.common.Capability.LocationCapability;
import com.redstoneguy10ls.lithiccoins.common.blocks.mintBlock;
import com.redstoneguy10ls.lithiccoins.common.items.TopDies;
import com.redstoneguy10ls.lithiccoins.common.recipes.LCRecipeTypes;
import com.redstoneguy10ls.lithiccoins.common.recipes.MintingRecipe;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.container.ISlotCallback;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class mintBlockEntity extends InventoryBlockEntity<mintBlockEntity.mintInventory> implements ISlotCallback {

    public static final int SLOT_TOPDIE = 0;
    public static final int SLOT_BOTTOMDIE = 1;

    public static final int SLOT_COIN = 2;
    public static final int SLOT_OUTPUT = 3;

    public boolean hit = false;

    public int hittimer = 0;

    private boolean needsStateUpdate = false;

    private static final Component NAME = Component.translatable(MOD_ID + ".block_entity.mint");

    public mintBlockEntity(BlockPos pos, BlockState state)
    {
        super(LCBlockEntities.MINT.get(), pos, state, mintInventory::new, NAME);

    }
    public mintBlockEntity(BlockEntityType<? extends mintBlockEntity> type, BlockPos pos, BlockState state, InventoryFactory<mintInventory> inventoryFactory, Component defaultName)
    {
        super(type,pos,state,inventoryFactory, defaultName);

    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, mintBlockEntity mint)
    {
        final ServerLevel serverLevel = (ServerLevel) level;


        //mint.checkForLastTickSync();
        if(mint.needsStateUpdate)
        {
            //mint.updateTopDie();
            //mint.updateBottomDie();
            //mint.updateCoins();
            mint.updateHit();
        }

        clientTick(level, pos, state, mint);

    }
    public static void clientTick(Level level, BlockPos pos, BlockState state, mintBlockEntity mint)
    {
        if(mint.hittimer > 0)
        {
            mint.hittimer--;
        }
        if(mint.hittimer == 0)
        {
            mint.hit = false;
            mint.updateHit();
        }
    }


/*

    public void updateTopDie()
    {
        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        BlockState newState = Helpers.setProperty(state, mintBlock.HAS_TOP_DIE, hasTopDie());
        if(hasTopDie() != state.getValue(mintBlock.HAS_TOP_DIE))
        {
            level.setBlockAndUpdate(worldPosition, newState);
        }
    }
    public void updateBottomDie()
    {
        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        BlockState newState = Helpers.setProperty(state, mintBlock.HAS_BOTTOM_DIE, hasBottomDie());
        if(hasBottomDie() != state.getValue(mintBlock.HAS_BOTTOM_DIE))
        {
            level.setBlockAndUpdate(worldPosition, newState);
        }
        needsStateUpdate = false;

    }

    public void updateCoins()
    {
        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        BlockState newState = Helpers.setProperty(state, mintBlock.COIN_TYPE, hasCoin());
        if(hasCoin() != state.getValue(mintBlock.COIN_TYPE))
        {
            level.setBlockAndUpdate(worldPosition, newState);
        }
        needsStateUpdate = false;

    }
    */
    public void updateHit()
    {
        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        BlockState newState = Helpers.setProperty(state, mintBlock.HIT, hasHit());
        if(hasHit() != state.getValue(mintBlock.HIT))
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
       if(slot == SLOT_BOTTOMDIE || slot == SLOT_TOPDIE)
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
        switch (slot)
        {
            case 0:
                return Helpers.isItem(stack.getItem(), LCTags.Items.TOP_DIE);
            case 1:
                return Helpers.isItem(stack.getItem(), LCTags.Items.BOTTOM_DIE);
            case 3:
                return Helpers.isItem(stack.getItem(), LCTags.Items.BLANK_COIN);
            default:
                return true;
        }
    }

    @Override
    public void loadAdditional(CompoundTag nbt)
    {
        super.loadAdditional(nbt);
        needsStateUpdate = true;
    }

    @Override
    public void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
    }

    @Override
    public boolean canInteractWith(Player player)
    {
        return super.canInteractWith(player);
    }


    public int getTopDieTier(){return LCHelpers.getTier(inventory.getStackInSlot(SLOT_TOPDIE).getItem());}
    public int getBottomDieTier(){return LCHelpers.getTier(inventory.getStackInSlot(SLOT_BOTTOMDIE).getItem());}


    public int hasTopDie(){return LCHelpers.getMetalInInt(inventory.getStackInSlot(SLOT_TOPDIE).getItem());}
    public int hasBottomDie(){return LCHelpers.getMetalInInt(inventory.getStackInSlot(SLOT_BOTTOMDIE).getItem());}
    public int hasCoin(){return LCHelpers.getCoinInInt(inventory.getStackInSlot(SLOT_COIN));}

    public void setHit(boolean value){hit = value;}
    public boolean hasHit(){return hit;}

    public InteractionResult minting(Player player)
    {
        final ItemStack top = inventory.getStackInSlot(SLOT_TOPDIE);
        final ItemStack coin = inventory.getStackInSlot(SLOT_COIN);
        final ItemStack bottom = inventory.getStackInSlot(SLOT_BOTTOMDIE);

        if(top.isEmpty() || coin.isEmpty() || bottom.isEmpty())
        {
            return InteractionResult.PASS;
        }

        assert level != null;

        final ItemStackInventory wrapper = new ItemStackInventory(coin);
        final int dies = LCHelpers.getStampTypesInInt(top);
        final MintingRecipe recipe = MintingRecipe.getRecipe(level,wrapper, dies);
        //final MintingRecipe recipe = level.getRecipeManager().getRecipeFor(LCRecipeTypes.MINTING.get(), inventory, level).orElse(null);
        if(recipe != null)
        {
            if(getTopDieTier() < recipe.getTier() || getBottomDieTier() < recipe.getTier())
            {
                if(getTopDieTier() < recipe.getTier() && getBottomDieTier() < recipe.getTier())
                {
                    player.displayClientMessage(Component.translatable("lithiccoins.tooltip.lithiccoins.mint.both_die_to_low"), true);
                }
                else if(getTopDieTier() < recipe.getTier())
                {
                    player.displayClientMessage(Component.translatable("lithiccoins.tooltip.lithiccoins.mint.top_die_to_low"), true);
                }
                else if(getBottomDieTier() < recipe.getTier())
                {
                    player.displayClientMessage(Component.translatable("lithiccoins.tooltip.lithiccoins.mint.bottom_die_to_low"), true);
                }
                return InteractionResult.FAIL;
            }
            if(recipe.getTopDie()[0].getId() == LCHelpers.getStampTypesInInt(inventory.getStackInSlot(SLOT_TOPDIE)))
            {
                final ItemStack result = recipe.assemble(inventory,level.registryAccess());

                ChunkPos chunkPos = new ChunkPos(getBlockPos());

                result.getCapability(LocationCapability.CAPABILITY).ifPresent(test ->
                {
                    if (!test.getLocationSet()) {
                        test.setCreationLocation(chunkPos);
                        test.setCreationDate(Calendars.get().getTotalYears());

                    }
                });


                if(inventory.getStackInSlot(SLOT_OUTPUT).isEmpty())
                {
                    inventory.setStackInSlot(SLOT_OUTPUT, result);
                }
                else {
                    inventory.getStackInSlot(SLOT_OUTPUT).grow(1);
                }



                inventory.getStackInSlot(SLOT_COIN).shrink(1);

                final ItemStack topDie = inventory.getStackInSlot(SLOT_TOPDIE);
                final ItemStack bottomDie = inventory.getStackInSlot(SLOT_BOTTOMDIE);
                Helpers.damageItem(topDie, 1);
                Helpers.damageItem(bottomDie, 1);
                setAndUpdateSlots(SLOT_TOPDIE);
                setAndUpdateSlots(SLOT_BOTTOMDIE);
                markForSync();

                return InteractionResult.SUCCESS;
            }
            else {
                return InteractionResult.FAIL;
            }

        }
        return InteractionResult.PASS;
    }

    public static class mintInventory extends InventoryItemHandler implements MintingRecipe.Inventory{

        private final mintBlockEntity mint;

        public mintInventory(InventoryBlockEntity<mintInventory> mint) {
            super(mint, 4);
            this.mint = (mintBlockEntity) mint;
        }

        @Override
        public ItemStack getItem() {
            return null;
        }

        @Override
        public int getTier() {
            return 0;
        }
    }

}
