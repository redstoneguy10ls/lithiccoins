package com.redstoneguy10ls.lithiccoins.common.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.redstoneguy10ls.lithiccoins.common.blockentities.LCBlockEntities;
import com.redstoneguy10ls.lithiccoins.common.blockentities.mintBlockEntity;
import com.redstoneguy10ls.lithiccoins.common.misc.LCSounds;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.client.IHighlightHandler;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import static com.redstoneguy10ls.lithiccoins.common.blockentities.mintBlockEntity.*;


public class mintBlock extends DeviceBlock implements IHighlightHandler {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    //public static final IntegerProperty HAS_TOP_DIE = LCStateProperties.HAS_TOP_DIE;
    //public static final IntegerProperty HAS_BOTTOM_DIE = LCStateProperties.HAS_BOTTOM_DIE;
    //public static final IntegerProperty COIN_TYPE = LCStateProperties.COIN_TYPE;
    public static final BooleanProperty HIT = LCStateProperties.HIT;



    //to whoever decided to look at this jank ass code
    //im sorry
    private static final VoxelShape TOP_DIE_N = box(5.5, 14.5, 6.5, 10.5, 15.5, 11.5);
    private static final VoxelShape TOP_DIE_W = box(6.5, 14.5, 5.5, 11.5, 15.5, 10.5);
    private static final VoxelShape TOP_DIE_E = box(4.5, 14.5, 5.5, 9.5, 15.5, 10.5);
    private static final VoxelShape TOP_DIE_S = box(5.5, 14.5, 4.5, 10.5, 15.5, 9.5);
    private static final VoxelShape BOTTOM_DIE_N = Block.box(4.5, 9.5, 5.5, 11.5, 11, 12.5);
    private static final VoxelShape BOTTOM_DIE_E = box(3.5D, 9.5D, 4.5D, 10.5D, 11D, 11.5D);
    private static final VoxelShape BOTTOM_DIE_S = box(4.5D, 9.5D, 3.5D, 11.5D, 11D, 10.5D);
    private static final VoxelShape BOTTOM_DIE_W = box(5.5D, 9.5D, 4.5D, 12.5D, 11D, 11.5D);
    private static final VoxelShape BASE_SHAPE = box(0D, 0D, 0D, 16D, 10D, 16D);
    private static final VoxelShape BASE_SHAPE_N = Shapes.join(BASE_SHAPE,BOTTOM_DIE_N,BooleanOp.OR);
    private static final VoxelShape BASE_SHAPE_W = Shapes.join(BASE_SHAPE,BOTTOM_DIE_W,BooleanOp.OR);
    private static final VoxelShape BASE_SHAPE_E = Shapes.join(BASE_SHAPE,BOTTOM_DIE_E,BooleanOp.OR);
    private static final VoxelShape BASE_SHAPE_S = Shapes.join(BASE_SHAPE,BOTTOM_DIE_S,BooleanOp.OR);

    private static final AABB TOP_DIE_N_AABB = TOP_DIE_N.bounds().inflate(0.01D);
    private static final AABB TOP_DIE_W_AABB = TOP_DIE_W.bounds().inflate(0.01D);
    private static final AABB TOP_DIE_E_AABB = TOP_DIE_E.bounds().inflate(0.01D);
    private static final AABB TOP_DIE_S_AABB = TOP_DIE_S.bounds().inflate(0.01D);
    private static final AABB BOTTOM_DIE_N_AABB = BOTTOM_DIE_N.bounds().inflate(0.01D);
    private static final AABB BOTTOM_DIE_E_AABB = BOTTOM_DIE_E.bounds().inflate(0.01D);
    private static final AABB BOTTOM_DIE_S_AABB = BOTTOM_DIE_S.bounds().inflate(0.01D);
    private static final AABB BOTTOM_DIE_W_AABB = BOTTOM_DIE_W.bounds().inflate(0.01D);




    public mintBlock(ExtendedProperties properties) {
        super(properties, InventoryRemoveBehavior.DROP);
        //registerDefaultState(getStateDefinition().any().setValue(HAS_BOTTOM_DIE, 0));
        //registerDefaultState(getStateDefinition().any().setValue(HAS_TOP_DIE, 0));
        //registerDefaultState(getStateDefinition().any().setValue(COIN_TYPE, 0));
        registerDefaultState(getStateDefinition().any().setValue(HIT, false));

    }
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BASE_SHAPE;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch(state.getValue(FACING)){
            case WEST:
                return Shapes.join(BASE_SHAPE_W,TOP_DIE_W,BooleanOp.OR);
            case EAST:
                return Shapes.join(BASE_SHAPE_E,TOP_DIE_E,BooleanOp.OR);
            case SOUTH:
                return Shapes.join(BASE_SHAPE_S,TOP_DIE_S,BooleanOp.OR);
            default:
                return Shapes.join(BASE_SHAPE_N,TOP_DIE_N, BooleanOp.OR);

        }
    }

    private static SelectionPlace getPlayerSelection(BlockGetter level, BlockPos pos, Player player, BlockHitResult result)
    {
        return level.getBlockEntity(pos, LCBlockEntities.MINT.get())
                .flatMap(mint -> mint.getCapability(Capabilities.ITEM)
                    .map(inventory -> {
                         final ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
                         final Vec3 hit = result.getLocation();
                         if(TOP_DIE_N_AABB.move(pos).contains(hit))
                         {
                             return SelectionPlace.TOPDIE_N;
                         }
                         else if (TOP_DIE_E_AABB.move(pos).contains(hit)) {
                             return SelectionPlace.TOPDIE_E;
                         }
                         else if (TOP_DIE_S_AABB.move(pos).contains(hit)) {
                             return SelectionPlace.TOPDIE_S;
                         }
                         else if (TOP_DIE_W_AABB.move(pos).contains(hit)) {
                             return SelectionPlace.TOPDIE_W;
                         }
                        if(BOTTOM_DIE_N_AABB.move(pos).contains(hit))
                        {
                            return SelectionPlace.BOTTOMDIE_N;
                        }
                        else if (BOTTOM_DIE_E_AABB.move(pos).contains(hit)) {
                            return SelectionPlace.BOTTOMDIE_E;
                        }
                        else if (BOTTOM_DIE_S_AABB.move(pos).contains(hit)) {
                            return SelectionPlace.BOTTOMDIE_S;
                        }
                        else if (BOTTOM_DIE_W_AABB.move(pos).contains(hit)) {
                            return SelectionPlace.BOTTOMDIE_W;
                        }
                        return SelectionPlace.BASE;
                    }))
                    .orElse(SelectionPlace.BASE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(level.getBlockEntity(pos) instanceof final mintBlockEntity mint)
        {
            final ItemStack heldStack = player.getItemInHand(hand);
            final SelectionPlace selection = getPlayerSelection(level,pos,player,hit);

            if(Helpers.isItem(heldStack, TFCTags.Items.HAMMERS))
            {
                if(!mint.hasHit())
                {
                    mint.setHit(true);
                    mint.hittimer = 5;
                    attemptMint(level, mint, player,pos);
                    return InteractionResult.SUCCESS;
                }

            }else
            {
                return mint.getCapability(Capabilities.ITEM).map(inventory -> switch (selection)
                        {
                            case BASE -> coinstuff(state,level,mint,inventory,player,heldStack,2);
                            case TOPDIE_N, TOPDIE_W, TOPDIE_E, TOPDIE_S -> lookTest(state,level,mint,inventory,player,heldStack,SLOT_TOPDIE);
                            case BOTTOMDIE_N, BOTTOMDIE_W, BOTTOMDIE_E, BOTTOMDIE_S -> lookTest(state,level,mint,inventory,player,heldStack,SLOT_BOTTOMDIE);
                        })
                        .orElse(InteractionResult.PASS);
            }

        }
        return InteractionResult.PASS;
    }

    private static InteractionResult attemptMint(Level level, mintBlockEntity mint, Player player,BlockPos pos)
    {
        final InteractionResult mintResult = mint.minting(player);
        if(mintResult == InteractionResult.SUCCESS)
        {

            if(level instanceof ServerLevel server)
            {
                final double x = pos.getX() + Mth.nextDouble(level.random, 0.2, 0.8);
                final double z = pos.getZ() + Mth.nextDouble(level.random, 0.2, 0.8);
                final double y = pos.getY() + Mth.nextDouble(level.random, 0.8, 1.0);
                server.sendParticles(TFCParticles.SPARK.get(), x, y, z, 8, 0, 0, 0, 0.2f);
            }
                level.playSound(null,pos, LCSounds.MINT_HIT.get(), SoundSource.PLAYERS,0.6f,1.0f);
            return InteractionResult.SUCCESS;
        } else if (mintResult == InteractionResult.FAIL) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private static InteractionResult coinstuff(BlockState state, Level level, mintBlockEntity mint, IItemHandler inventory, Player player, ItemStack stack, int slot)
    {
            if ((player.isShiftKeyDown() || Helpers.isItem(stack, LCTags.Items.BLANK_COIN))) {
                if(mint.hasBottomDie() > 0)
                {
                    //state.setValue(COIN_TYPE, LCHelpers.getCoinInInt(stack));
                    //mint.updateCoins();
                    return insertOrExtract(level, mint, inventory, player, stack, slot);
                }
            }
            else {
                //state.setValue(COIN_TYPE, LCHelpers.getCoinInInt(stack));
                //mint.updateCoins();
                return insertOrExtract(level, mint, inventory, player, ItemStack.EMPTY, SLOT_OUTPUT);
            }
            return InteractionResult.PASS;
    }

    private static InteractionResult lookTest(BlockState state,Level level, mintBlockEntity mint, IItemHandler inventory, Player player, ItemStack stack, int slot){
        if(slot == SLOT_BOTTOMDIE)
        {
                if ((player.isShiftKeyDown() || Helpers.isItem(stack, LCTags.Items.BOTTOM_DIE))) {
                    return insertOrExtract(level, mint, inventory, player, stack, slot);
                }
        }
        if (slot == SLOT_TOPDIE) {
                if ((player.isShiftKeyDown() || Helpers.isItem(stack, LCTags.Items.TOP_DIE))) {
                    return insertOrExtract(level, mint, inventory, player, stack, slot);
                }
        }
        if(slot >= 2)
        {
            coinstuff(state,level,mint,inventory,player,stack,2);
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult insertOrExtract(Level level, mintBlockEntity mint, IItemHandler inventory, Player player, ItemStack stack, int slot)
    {
        if(!stack.isEmpty())
        {
            player.setItemInHand(InteractionHand.MAIN_HAND, inventory.insertItem(slot, stack, false));
        }
        else
        {
            ItemHandlerHelper.giveItemToPlayer(player, inventory.extractItem(slot, inventory.getStackInSlot(slot).getCount(), false));
            if(slot == SLOT_BOTTOMDIE)
            {
                insertOrExtract(level,mint,inventory,player,ItemStack.EMPTY,SLOT_COIN);
            }
        }
        mint.setAndUpdateSlots(slot);
        mint.markForSync();
        return InteractionResult.sidedSuccess(level.isClientSide);
    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        //super.createBlockStateDefinition(builder.add(HAS_TOP_DIE));
        //super.createBlockStateDefinition(builder.add(HAS_BOTTOM_DIE));
        super.createBlockStateDefinition(builder.add(FACING));
        //super.createBlockStateDefinition(builder.add(COIN_TYPE));
        super.createBlockStateDefinition(builder.add(HIT));
    }
    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public boolean drawHighlight(Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, PoseStack poseStack, MultiBufferSource multiBufferSource, Vec3 vec3) {
        return false;
    }

    enum SelectionPlace
    {
        BASE(BASE_SHAPE),
        TOPDIE_N(TOP_DIE_N),
        BOTTOMDIE_N(BOTTOM_DIE_N),
        TOPDIE_W(TOP_DIE_W),
        BOTTOMDIE_W(BOTTOM_DIE_W),
        TOPDIE_E(TOP_DIE_E),
        BOTTOMDIE_E(BOTTOM_DIE_E),
        TOPDIE_S(TOP_DIE_S),
        BOTTOMDIE_S(BOTTOM_DIE_S);

        final VoxelShape shape;

        SelectionPlace(VoxelShape shape){this.shape= shape;}

    }
}

