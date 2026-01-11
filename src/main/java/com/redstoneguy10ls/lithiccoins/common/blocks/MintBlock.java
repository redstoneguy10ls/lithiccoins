package com.redstoneguy10ls.lithiccoins.common.blocks;

import java.util.Map;
import com.redstoneguy10ls.lithiccoins.common.blockentities.MintBlockEntity;
import com.redstoneguy10ls.lithiccoins.common.misc.LCSounds;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import static com.redstoneguy10ls.lithiccoins.common.blockentities.MintBlockEntity.*;


public class MintBlock extends DeviceBlock
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HIT = LCStateProperties.HIT;

    // I hope this code is a bit less jank ass
    private static final VoxelShape BASE_SHAPE = box(0D, 0D, 0D, 16D, 10D, 16D);

    private static final Map<Direction, VoxelShape> TOP_DIE_SHAPE = Helpers.mapOf(Direction.class, direction ->
        switch (direction)
        {
            case SOUTH -> box(5.5, 14.5, 4.5, 10.5, 15.5, 9.5);
            case WEST -> box(6.5, 14.5, 5.5, 11.5, 15.5, 10.5);
            case EAST -> box(4.5, 14.5, 5.5, 9.5, 15.5, 10.5);
            default -> box(5.5, 14.5, 6.5, 10.5, 15.5, 11.5);
        }
    );

    private static final Map<Direction, VoxelShape> BOTTOM_DIE_SHAPE = Helpers.mapOf(Direction.class, direction ->
        switch (direction)
        {
            case SOUTH -> box(4.5D, 9.5D, 3.5D, 11.5D, 11D, 10.5D);
            case WEST -> box(5.5D, 9.5D, 4.5D, 12.5D, 11D, 11.5D);
            case EAST -> box(3.5D, 9.5D, 4.5D, 10.5D, 11D, 11.5D);
            default -> box(4.5, 9.5, 5.5, 11.5, 11, 12.5);
        }
    );



    public MintBlock(ExtendedProperties properties)
    {
        super(properties, InventoryRemoveBehavior.DROP);
        registerDefaultState(getStateDefinition().any().setValue(HIT, false));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return BASE_SHAPE;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        Direction direction = state.getValue(FACING);
        return Shapes.join(Shapes.join(BASE_SHAPE, TOP_DIE_SHAPE.get(direction), BooleanOp.OR), BOTTOM_DIE_SHAPE.get(direction), BooleanOp.OR);
    }

    /**
     * T
     * @return The part of the mint block that a player has attempted to hit
     */
    private MintPart getSelectedPart(BlockState state, BlockPos pos, BlockHitResult hitResult)
    {
        Vec3 hit = hitResult.getLocation();
        Direction direction = state.getValue(FACING);

        if (TOP_DIE_SHAPE.get(direction).bounds().inflate(0.01).move(pos).contains(hit))
        {
            return MintPart.TOP_DIE;
        }
        else if (BOTTOM_DIE_SHAPE.get(direction).bounds().inflate(0.01).move(pos).contains(hit))
        {
            return MintPart.BOTTOM_DIE;
        }
        else
        {
            return MintPart.BASE;
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (level.getBlockEntity(pos) instanceof final MintBlockEntity mint)
        {
            final ItemStack heldStack = player.getItemInHand(hand);

            // If the player is holding a hammer, we want to attempt to mint a coin
            if (Helpers.isItem(heldStack, TFCTags.Items.TOOLS_HAMMER))
            {
                if (!mint.hasHit())
                {
                    mint.setHitTimer(5);
                    attemptMinting(level, mint, player,pos);
                    return ItemInteractionResult.SUCCESS;
                }

            }
            // Otherwise, we want to try with the inventory of the mint, if possible
            else
            {
                final MintPart selectedPart = getSelectedPart(state, pos, hitResult);

                if (player.isShiftKeyDown() || Helpers.isItem(stack, selectedPart.validItemTag()))
                {
                    insertOrExtract(mint, player, stack, selectedPart.slotIndex());
                }
                else if (Helpers.isItem(stack, MintPart.BASE.validItemTag()))
                {
                    insertOrExtract(mint, player, stack, MintPart.BASE.slotIndex());
                }
                else
                {
                    insertOrExtract(mint, player, ItemStack.EMPTY, SLOT_OUTPUT);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static void attemptMinting(Level level, MintBlockEntity mint, Player player, BlockPos pos)
    {
        final InteractionResult mintResult = mint.tryMinting(player);
        if (mintResult == InteractionResult.SUCCESS)
        {
            if (level instanceof ServerLevel server)
            {
                final double x = pos.getX() + Mth.nextDouble(level.random, 0.2, 0.8);
                final double z = pos.getZ() + Mth.nextDouble(level.random, 0.2, 0.8);
                final double y = pos.getY() + Mth.nextDouble(level.random, 0.8, 1.0);
                server.sendParticles(TFCParticles.SPARK.get(), x, y, z, 8, 0, 0, 0, 0.2f);
            }

            level.playSound(null, pos, LCSounds.MINT_HIT.get(), SoundSource.PLAYERS,0.6f,1.0f);
        }
    }
    
    private static void insertOrExtract(MintBlockEntity mint, Player player, ItemStack stack, int slot)
    {
        IItemHandler inventory = mint.getInventory();
        
        if (!stack.isEmpty())
        {
            // Prevent the creation of incorrectly-minted coins via stamp-switching
            if (slot == MintPart.TOP_DIE.slotIndex())
            {
                ItemHandlerHelper.giveItemToPlayer(player, inventory.extractItem(slot, inventory.getStackInSlot(slot).getCount(), false));
            }

            player.setItemInHand(InteractionHand.MAIN_HAND, inventory.insertItem(slot, stack, false));
        }
        else
        {
            // Prevent 'flying' coins
            if (slot == MintPart.BOTTOM_DIE.slotIndex())
            {
                insertOrExtract(mint, player, ItemStack.EMPTY, MintPart.BASE.slotIndex());
            }

            // Prevent the creation of incorrectly-minted coins via stamp-switching
            if (slot == MintPart.TOP_DIE.slotIndex())
            {
                insertOrExtract(mint, player, ItemStack.EMPTY, SLOT_OUTPUT);
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
            {
                ItemHandlerHelper.giveItemToPlayer(player, inventory.extractItem(slot, inventory.getStackInSlot(slot).getCount(), false));
            }
        }

        mint.setAndUpdateSlots(slot);
        mint.markForSync();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(FACING));
        super.createBlockStateDefinition(builder.add(HIT));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }



    protected enum MintPart
    {
        BASE(LCTags.Items.BLANK_COIN, SLOT_COIN),
        TOP_DIE(LCTags.Items.TOP_DIE, SLOT_TOP_DIE),
        BOTTOM_DIE(LCTags.Items.BOTTOM_DIE, SLOT_BOTTOM_DIE);

        private final TagKey<Item> validItemTag;
        private final int slotIndex;

        MintPart(TagKey<Item> validItemTag, int slotIndex)
        {
            this.validItemTag = validItemTag;
            this.slotIndex = slotIndex;
        }

        public TagKey<Item> validItemTag()
        {
            return this.validItemTag;
        }

        public int slotIndex()
        {
            return this.slotIndex;
        }
    }
}
