/*
package com.redstoneguy10ls.lithiccoins.mixin;


import com.redstoneguy10ls.lithiccoins.blocks.ModBlockStateProperties;
import com.redstoneguy10ls.lithiccoins.test.SelectionPlace;
import com.redstoneguy10ls.lithiccoins.test.dieAccessor;
import com.redstoneguy10ls.lithiccoins.util.ModTags;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.blockentities.QuernBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.common.blocks.devices.Tiered;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import com.redstoneguy10ls.lithiccoins.mixin.AnvilBlockEntityMixin;

import java.util.concurrent.atomic.AtomicBoolean;


import static com.redstoneguy10ls.lithiccoins.test.SelectionPlace.DIE_WEST;
import static com.redstoneguy10ls.lithiccoins.test.test.*;
import static net.dries007.tfc.common.blocks.devices.AnvilBlock.FACING;

@Mixin(value = AnvilBlock.class, remap = false)
public abstract class AnvilBlockMixin extends DeviceBlock implements Tiered{


    //@Shadow @Final
    //public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    //X



    public AnvilBlockMixin(ExtendedProperties properties, InventoryRemoveBehavior removeBehavior) {
        super(properties, removeBehavior);

    }



    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {

        switch (state.getValue(FACING)){
            case NORTH:
                return Shapes.join(NORTH,SHAPE_Z,BooleanOp.OR);
            case WEST:
                return Shapes.join(WEST,SHAPE_X,BooleanOp.OR);
            case EAST:
                return Shapes.join(EAST,SHAPE_X,BooleanOp.OR);
            case SOUTH:
                return Shapes.join(SOUTH,SHAPE_Z,BooleanOp.OR);
            default:
                return SOUTH;
        }

        //return state.getValue(FACING).getAxis() == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }


    private static SelectionPlace getPlayerSelection(BlockGetter level, BlockPos pos, Player player, BlockHitResult result)
    {
        AtomicBoolean hasbottomdie = new AtomicBoolean(false);
        return level.getBlockEntity(pos, TFCBlockEntities.ANVIL.get())
                .flatMap(anvil -> anvil.getCapability(Capabilities.ITEM)
                        .map(inventory ->{
                            final ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
                            final Vec3 hit = result.getLocation();
                            if(anvil instanceof dieAccessor)
                            {
                                final int temp = 4;//((dieAccessor) anvil).getBottomDieSlot();
                                hasbottomdie.set(((dieAccessor) anvil).hasBottomDie());
                                if(hasbottomdie.get())
                                {
                                    if(!held.isEmpty() || !inventory.getStackInSlot(temp).isEmpty() && WEST_AABB.move(pos).contains(hit))
                                    {
                                        return DIE_WEST;
                                    } else if (!held.isEmpty() || !inventory.getStackInSlot(temp).isEmpty() && NORTH_AABB.move(pos).contains(hit)) {
                                        return SelectionPlace.DIE_NORTH;
                                    }
                                    else if (!held.isEmpty() || !inventory.getStackInSlot(temp).isEmpty() && EAST_AABB.move(pos).contains(hit)) {
                                        return SelectionPlace.DIE_EAST;
                                    }
                                    else if (!held.isEmpty() || !inventory.getStackInSlot(temp).isEmpty() && SOUTH_AABB.move(pos).contains(hit)) {
                                        return SelectionPlace.DIE_SOUTH;
                                    }
                                }
                            }
                            return SelectionPlace.BASE_X;
                         }

                        ))
                .orElse(SelectionPlace.BASE_X);

    }




    private static InteractionResult insertOrExtract(Level level, AnvilBlockEntity anvil, IItemHandler inventory, Player player, ItemStack stack, int slot)
    {
        if(!stack.isEmpty())
        {
            player.setItemInHand(InteractionHand.MAIN_HAND, inventory.insertItem(slot,stack,false));
        }
        else
        {
            final int temp = ((dieAccessor) anvil).getBottomDieSlot();
            ItemHandlerHelper.giveItemToPlayer(player,inventory.extractItem(slot,inventory.getStackInSlot(slot).getCount(), false));
            if (slot == temp)
            {
                insertOrExtract(level,anvil,inventory,player,ItemStack.EMPTY,temp);
            }
        }
        anvil.setAndUpdateSlots(slot);
        anvil.markForSync();
        return InteractionResult.sidedSuccess(level.isClientSide);
    }




    private static InteractionResult interactWithAnvil(Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        final AnvilBlockEntity anvil = level.getBlockEntity(pos, TFCBlockEntities.ANVIL.get()).orElse(null);
        if (anvil == null)
        {
            return InteractionResult.PASS;
        }
        final IItemHandler inventory = anvil.getCapability(Capabilities.ITEM).resolve().orElse(null);
        if (inventory == null)
        {
            return InteractionResult.PASS;
        }
        if (player.isShiftKeyDown())
        {
            final ItemStack playerStack = player.getItemInHand(hand);

            final SelectionPlace selection = getPlayerSelection(level, pos, player, hit);
            boolean lookingAtDie = false;
            switch(selection)
            {
                case DIE_WEST -> lookingAtDie = true;
                case DIE_EAST -> lookingAtDie = true;
                case DIE_NORTH -> lookingAtDie = true;
                case DIE_SOUTH -> lookingAtDie = true;
            }

            if (playerStack.isEmpty()) // Extraction requires held item to be empty
            {
                for (int slot : AnvilBlockEntity.SLOTS_BY_HAND_EXTRACT)
                {
                    final ItemStack anvilStack = inventory.getStackInSlot(slot);
                    if (!anvilStack.isEmpty())
                    {
                        // Give the item to player in the main hand
                        ItemStack result = inventory.extractItem(slot, 1, false);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            else if (Helpers.isItem(playerStack, TFCTags.Items.HAMMERS)) // Attempt welding or minting with a hammer in hand
            {
                if(lookingAtDie)//if looking at a die, then attempt to mint,
                {
                    attemptMint(level,pos,anvil);
                }
                else//else run the normal tfc weld code
                {
                    final InteractionResult weldResult = anvil.weld(player);
                    if (weldResult == InteractionResult.SUCCESS)
                    {
                        // Welding occurred
                        if (level instanceof ServerLevel server)
                        {
                            final double x = pos.getX() + Mth.nextDouble(level.random, 0.2, 0.8);
                            final double z = pos.getZ() + Mth.nextDouble(level.random, 0.2, 0.8);
                            final double y = pos.getY() + Mth.nextDouble(level.random, 0.8, 1.0);
                            server.sendParticles(TFCParticles.SPARK.get(), x, y, z, 8, 0, 0, 0, 0.2f);
                        }
                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.6f, 1.0f);
                        return InteractionResult.SUCCESS;
                    }
                    else if (weldResult == InteractionResult.FAIL)
                    {
                        // Welding was attempted, but failed for some reason - player was alerted and action was consumed.
                        // Returning fail here causes the off hand to still attempt to be used?
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            else
            {
                if(lookingAtDie)
                {
                    final ItemStack heldStack = player.getItemInHand(hand);
                    if(Helpers.isItem(heldStack, ModTags.Items.BOTTOM_DIE))
                    {
                        insertOrExtract(level,anvil,inventory,player,heldStack,4);
                    }

                }
                // Try and insert an item
                final ItemStack insertStack = playerStack.copy();
                for (int slot : AnvilBlockEntity.SLOTS_BY_HAND_INSERT)
                {
                    final ItemStack resultStack = inventory.insertItem(slot, insertStack, false);
                    if (insertStack.getCount() > resultStack.getCount())
                    {
                        // At least one item was inserted (and so remainder < attempt)
                        player.setItemInHand(hand, resultStack);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }



        return InteractionResult.PASS;
    }

    @NotNull
    private static InteractionResult attemptMint(Level level, BlockPos pos, AnvilBlockEntity anvil)
    {
        AtomicBoolean startMinting = new AtomicBoolean(false);
        if(anvil instanceof dieAccessor)
        {
            startMinting.set(((dieAccessor) anvil).startMinting());
        }
        return startMinting.get() ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.FAIL;
    }

    private static final BooleanProperty HAS_BOTTOM_DIE = ModBlockStateProperties.HAS_BOTTOM_DIE;
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(HAS_BOTTOM_DIE));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return AnvilBlockMixin.interactWithAnvil(level, pos, player, hand, hit);
    }



    @Override
    public int getTier() {
        return 0;
    }

    //Block.box(11.5, 11, 6, 15.5, 17, 10)
    //Shapes.join(Block.box(11.5, 11, 6, 15.5, 16, 10), Block.box(0, 0, 3, 16, 11, 13), BooleanOp.OR)

}
*/