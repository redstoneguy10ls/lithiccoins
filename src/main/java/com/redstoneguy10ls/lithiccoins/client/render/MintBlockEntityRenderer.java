package com.redstoneguy10ls.lithiccoins.client.render;

import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.redstoneguy10ls.lithiccoins.common.blockentities.MintBlockEntity;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCStateProperties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;

public class MintBlockEntityRenderer implements BlockEntityRenderer<MintBlockEntity>
{
    private static final Map<Integer, Vec3> startPosition = Map.of(
        0, new Vec3(0.875d, 0.625d, 0.875d),
        1, new Vec3(0.875d, 0.625d, 0.125d),
        2, new Vec3(0.125d, 0.625d, 0.125d),
        3, new Vec3(0.125d, 0.625d, 0.875d)
    );

    private static final Map<Integer, Vec3> baseOffset = Map.of(
        0, new Vec3(0d, 0d, -0.046875d),
        1, new Vec3(-0.046875d, 0d, 0d),
        2, new Vec3(0d, 0d, 0.046875d),
        3, new Vec3(0.046875d, 0d, 0d)
    );

    @Override
    public void render(MintBlockEntity mint, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        final Level level = mint.getLevel();
        final IItemHandler inventory = mint.getInventory();

        if (inventory == null || level == null)
        {
            return;
        }

        final BlockState blockState = level.getBlockState(mint.getBlockPos());
        final Direction direction = blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? blockState.getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.UP;

        // This function may attempt to run in the same tick a player breaks a mint.
        // In that case, the block will be air, so we don't need to render anything
        if (direction == Direction.UP) return;


        final ItemStack topDie = inventory.getStackInSlot(MintBlockEntity.SLOT_TOP_DIE);
        final ItemStack bottomDie = inventory.getStackInSlot(MintBlockEntity.SLOT_BOTTOM_DIE);
        final ItemStack coin = inventory.getStackInSlot(MintBlockEntity.SLOT_COIN);
        final ItemStack output = inventory.getStackInSlot(MintBlockEntity.SLOT_OUTPUT);
        final boolean hasHit = blockState.hasProperty(LCStateProperties.HIT) && blockState.getValue(LCStateProperties.HIT);

        final ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        // Rendering coins in the output
        if(!output.isEmpty())
        {
            for(int i = 0; i < output.getCount(); i++)
            {
                int rot = (intFromDirection(direction) + i / 16) % 4;
                Vec3 coinPos = startPosition.get(rot).add(baseOffset.get(rot).scale((i % 16)));

                stack.pushPose();
                stack.translate(coinPos.x, coinPos.y, coinPos.z);

                switch ((Math.floorDiv(i, 16) + intFromDirection(direction)) % 4)
                {
                    case 1 -> stack.mulPose(Axis.YP.rotationDegrees(90f));
                    case 2 -> stack.mulPose(Axis.YP.rotationDegrees(180f));
                    case 3 -> stack.mulPose(Axis.YP.rotationDegrees(270f));
                }

                stack.mulPose(Axis.XP.rotationDegrees(75f));

                stack.scale(0.125f, 0.125f, 0.125f);
                renderer.renderStatic(output, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, level, 0);

                stack.popPose();
            }
        }

        // Rendering the bottom die
        if(!bottomDie.isEmpty())
        {
            stack.pushPose();
            stack.translate(((7.125f+1f)/16f), (10f/16), (6.75f/16f));

            switch (direction)
            {
                case WEST -> stack.translate(((1f)/16),0,((0.25f+1f)/16));
                case NORTH -> stack.translate(0,0,(2.25f/16f));
                case EAST -> stack.translate(-((1f)/16),0,((0.25f+1f)/16));
            }

            stack.scale(0.9F, 0.9F, 0.9F);
            renderer.renderStatic(bottomDie, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, level, 0);

            stack.popPose();
        }

        // Rendering the coins in the input
        if(!coin.isEmpty())
        {
            stack.pushPose();
            stack.translate(((7.25f+1f)/16f), (13.5f/16), (7.125f/16f));
            stack.mulPose(Axis.XP.rotationDegrees(90f));

            switch (direction)
            {
                case WEST -> stack.translate(((1f-(0.125/2))/16),((0.125f+1f)/16),0);
                case NORTH -> stack.translate(0,((2.25f-(0.125/2))/16f),0);
                case EAST -> stack.translate(-((1f)/16),((0.2f-(0.125/2)+1f)/16),0);
            }

            stack.scale(0.3f, 0.3f, 1f);
            renderer.renderStatic(coin, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, level, 0);

            stack.popPose();
        }

        // Rendering the top die
        if(!topDie.isEmpty())
        {
            stack.pushPose();
            stack.translate(((7.125f+1f)/16f), (15f/16), (6.75f/16f));

            switch (direction)
            {
                case WEST -> stack.translate(((1f)/16),0,((0.25f+1f)/16));
                case NORTH -> stack.translate(0,0,(2.25f/16f));
                case EAST -> stack.translate(-((1f)/16),0,((0.25f+1f)/16));
            }

            if(hasHit)
            {
                stack.translate(0,-(1f/16f),0);
            }

            stack.scale(0.8F, 0.8F, 0.8F);
            renderer.renderStatic(topDie, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, level, 0);

            stack.popPose();
        }
    }

    /**
     * The integer values associated with directions via {@link Direction#get2DDataValue()} are not useful for us, as they increase CCW instead of CW.
     * This is a helper method to map the directions to integers as we require them, allowing us to lower the computation costs of displaying coins in proper rotation
     */
    private int intFromDirection(Direction direction)
    {
        return switch (direction)
        {
            case NORTH -> 2;
            case WEST -> 3;
            case SOUTH -> 0;
            case EAST -> 1;
            default -> -1;
        };
    }
}

