package com.redstoneguy10ls.lithiccoins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.redstoneguy10ls.lithiccoins.common.blockentities.mintBlockEntity;
import com.redstoneguy10ls.lithiccoins.common.blocks.mintBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.IItemHandler;

public class mintBlockEntityRenderer implements BlockEntityRenderer<mintBlockEntity> {

    @Override
    public void render(mintBlockEntity mint, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        final Level level = mint.getLevel();
        final IItemHandler inventory = Helpers.getCapability(mint, Capabilities.ITEM);

        if (inventory == null || level == null)
        {
            return;
        }

        final ItemStack topdie = inventory.getStackInSlot(mintBlockEntity.SLOT_TOPDIE);
        final ItemStack coin = inventory.getStackInSlot(mintBlockEntity.SLOT_COIN);
        final ItemStack bottom = inventory.getStackInSlot(mintBlockEntity.SLOT_BOTTOMDIE);
        final ItemStack output = inventory.getStackInSlot(mintBlockEntity.SLOT_OUTPUT);

        if(!output.isEmpty())
        {
            for(int i = 0; i < output.getCount(); i++)
            {
                double yPos = 0.625D;
                stack.pushPose();
                switch (Math.floorDiv(i, 16))
                {
                    case 0 ->
                    {
                        stack.translate(0.125D, yPos, 0.125D + (0.046875D * i));
                        stack.mulPose(Axis.XP.rotationDegrees(75F));
                    }
                    case 1 ->
                    {
                        stack.translate(0.125D + (0.046875D * (i - 16)), yPos, 0.875D);
                        stack.mulPose(Axis.YP.rotationDegrees(90F));
                        stack.mulPose(Axis.XP.rotationDegrees(75F));
                    }
                    case 2 ->
                    {
                        stack.translate(0.875D, yPos, 0.875D - (0.046875D * (i - 32)));
                        stack.mulPose(Axis.YP.rotationDegrees(180F));
                        stack.mulPose(Axis.XP.rotationDegrees(75F));
                    }
                    case 3 ->
                    {
                        stack.translate(0.875D - (0.046875D * (i - 48)), yPos, 0.125D);
                        stack.mulPose(Axis.YP.rotationDegrees(270F));
                        stack.mulPose(Axis.XP.rotationDegrees(75F));
                    }
                    default ->
                    {
                        stack.translate(0.5D, 1.0D, 0.5D);
                        float degrees = (level.getGameTime() + partialTicks) * 4F;
                        stack.mulPose(Axis.YP.rotationDegrees(degrees));
                    }
                }

                stack.scale(0.125F, 0.125F, 0.125F);
                Minecraft.getInstance().getItemRenderer().renderStatic(output, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, mint.getLevel(), 0);

                stack.popPose();
            }
        }

        if(!bottom.isEmpty())
        {
            final float center = 0.5F;

            stack.pushPose();
            stack.translate(center, 0.705D, center);

            switch(mint.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING))
            {
                case SOUTH:
                    stack.translate(0.51f - center - 0.055, 0.001F, -0.06);
                case WEST:
                    stack.translate(0.625f - center, 0.001F, 0.5f-center);
                case EAST:
                    stack.translate(-0.56f +center, 0.001F, -0.5f+center-0.06);
                case NORTH:
                    stack.translate(0.5f - center, -0.1F, 0.06);

            }
            stack.scale(1F, 1F, 1F);
            Minecraft.getInstance().getItemRenderer().renderStatic(bottom, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, mint.getLevel(), 0);
            stack.popPose();
        }

    }
}
