
package com.redstoneguy10ls.lithiccoins.common.blocks;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class LCStateProperties {
    public static final IntegerProperty HAS_BOTTOM_DIE = IntegerProperty.create("has_bottom_die",0,9);
    public static final IntegerProperty HAS_TOP_DIE = IntegerProperty.create("has_top_die",0,9);
    public static final IntegerProperty COIN_TYPE = IntegerProperty.create("coin",0,26);
    public static final BooleanProperty HIT = BooleanProperty.create("hit");


}
