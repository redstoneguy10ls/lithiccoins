package com.redstoneguy10ls.lithiccoins.mixin;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCChainBlock;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.common.blocks.devices.Tiered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.phys.shapes.CollisionContext;

import static net.minecraft.world.level.block.Block.box;

@Mixin(value = AnvilBlock.class, remap = false)
public abstract class AnvilBlockMixin extends DeviceBlock implements Tiered{


    @Shadow @Final
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    //X
    private static final VoxelShape WEST = box(11.5, 11, 6, 15.5, 16, 10);

    private static final VoxelShape NORTH = box(6, 11, 11.5, 10, 16, 15.5);

    //X
    private static final VoxelShape EAST = box(0.5, 11, 6, 4.5, 16, 10);

    private static final VoxelShape SOUTH = box(6, 11, 0.5, 10, 16, 4.5);

    private static final VoxelShape SHAPE_X = box(0, 0, 3, 16, 11, 13);
    private static final VoxelShape SHAPE_Z = box(3, 0, 0, 13, 11, 16);
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
    //Block.box(11.5, 11, 6, 15.5, 17, 10)
    //Shapes.join(Block.box(11.5, 11, 6, 15.5, 16, 10), Block.box(0, 0, 3, 16, 11, 13), BooleanOp.OR)

}
