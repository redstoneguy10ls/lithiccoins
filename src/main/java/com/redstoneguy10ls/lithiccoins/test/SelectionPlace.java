package com.redstoneguy10ls.lithiccoins.test;

import net.minecraft.world.phys.shapes.VoxelShape;

public enum SelectionPlace {
    DIE_WEST(test.WEST),
    DIE_NORTH(test.NORTH),
    DIE_SOUTH(test.SOUTH),
    DIE_EAST(test.EAST),
    BASE_X(test.SHAPE_X),
    BASE_Z(test.SHAPE_Z);

    final VoxelShape shape;
    SelectionPlace(VoxelShape shape)
    {
        this.shape = shape;
    }
}
