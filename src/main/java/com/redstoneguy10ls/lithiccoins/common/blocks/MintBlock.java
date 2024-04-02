package com.redstoneguy10ls.lithiccoins.common.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.client.IHighlightHandler;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class MintBlock extends DeviceBlock implements IHighlightHandler {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape WEST = Stream.of(
            box(3, 0, 3, 13, 2, 13),
            box(7, 1, 11, 9, 11, 13),
            box(6, 4, 5.9375, 7, 5.5, 13),
            box(9, 4, 5.9375, 10, 5.5, 13),
            box(7, 4, 5.9375, 9, 5.5, 7),
            box(5.5, 8.5, 5.5, 10.5, 10, 6.5),
            box(5.5, 8.5, 6.5, 7, 10, 13),
            box(9, 8.5, 6.5, 10.5, 10, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape NORTH = Stream.of(
            box(3, 0, 3, 13, 2, 13),
            box(3, 1, 7, 5, 11, 9),
            box(3, 4, 6, 10.0625, 5.5, 7),
            box(3, 4, 9, 10.0625, 5.5, 10),
            box(9, 4, 7, 10.0625, 5.5, 9),
            box(9.5, 8.5, 5.5, 10.5, 10, 10.5),
            box(3, 8.5, 5.5, 9.5, 10, 7),
            box(3, 8.5, 9, 9.5, 10, 10.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape EAST = Stream.of(
            box(3, 0, 3, 13, 2, 13),
            box(7, 1, 3, 9, 11, 5),
            box(9, 4, 3, 10, 5.5, 10.0625),
            box(6, 4, 3, 7, 5.5, 10.0625),
            box(7, 4, 9, 9, 5.5, 10.0625),
            box(5.5, 8.5, 9.5, 10.5, 10, 10.5),
            box(9, 8.5, 3, 10.5, 10, 9.5),
            box(5.5, 8.5, 3, 7, 10, 9.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SOUTH = Stream.of(
            box(3, 0, 3, 13, 2, 13),
            box(11, 1, 7, 13, 11, 9),
            box(5.9375, 4, 9, 13, 5.5, 10),
            box(5.9375, 4, 6, 13, 5.5, 7),
            box(5.9375, 4, 7, 7, 5.5, 9),
            box(5.5, 8.5, 5.5, 6.5, 10, 10.5),
            box(6.5, 8.5, 9, 13, 10, 10.5),
            box(6.5, 8.5, 5.5, 13, 10, 7)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();



    //public static final IntegerProperty HAS_BOTTOM_DIE =


    public MintBlock(ExtendedProperties properties) {
        super(properties, InventoryRemoveBehavior.DROP);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        switch (state.getValue(FACING)) {
            case NORTH:
                return NORTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
            case SOUTH:
                return SOUTH;
            default:
                return SOUTH;
        }
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(FACING));
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
}
