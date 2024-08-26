package com.redstoneguy10ls.lithiccoins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.redstoneguy10ls.lithiccoins.common.blockentities.MintBlockEntity;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCStateProperties;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import net.minecraftforge.items.IItemHandler;

public class MintBlockEntityRenderer implements BlockEntityRenderer<MintBlockEntity> {

    @Override
    public void render(MintBlockEntity mint, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        final Level level = mint.getLevel();
        final IItemHandler inventory = Helpers.getCapability(mint, Capabilities.ITEM);

        if (inventory == null || level == null)
        {
            return;
        }

        final ItemStack topdie = inventory.getStackInSlot(MintBlockEntity.SLOT_TOPDIE);
        final ItemStack coin = inventory.getStackInSlot(MintBlockEntity.SLOT_COIN);
        final ItemStack bottom = inventory.getStackInSlot(MintBlockEntity.SLOT_BOTTOMDIE);
        final ItemStack output = inventory.getStackInSlot(MintBlockEntity.SLOT_OUTPUT);

        final BlockState blockState = mint.getLevel().getBlockState(mint.getBlockPos());

        final boolean hasHit = blockState.hasProperty(LCStateProperties.HIT) ? blockState.getValue(LCStateProperties.HIT).booleanValue() : false;

        final int rotation = blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() : 0;
        final int coinType = blockState.hasProperty(LCStateProperties.COIN_TYPE) ? blockState.getValue(LCStateProperties.COIN_TYPE) : 0;

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

            stack.pushPose();
            stack.translate(((7.125f+1f)/16f), (10f/16), (6.75f/16f));

            if(rotation == 1)
            {
                stack.translate(((1f)/16),0,((0.25f+1f)/16));
            }
            if(rotation == 2)
            {
                stack.translate(0,0,(2.25f/16f));
            }
            if(rotation == 3)
            {
                stack.translate(-((1f)/16),0,((0.25f+1f)/16));
            }

            //stack.translate(((7.5f+1f)/16f), (13.5f/16), ((6.5f+1f)/16f));
            //stack.translate(0,0,0);

            //stack.translate(0.5075F, (15.0f/16.0f), 0.563f);
            stack.scale(0.9F, 0.9F, 0.9F);
            //stack.mulPose(Axis.XP.rotationDegrees(90f));
            //stack.mulPose(Axis.XP.rotationDegrees(90f* rotation + 270F));
            //stack.mulPose(Axis.ZP.rotationDegrees(90f * rotation + 270F));
            Minecraft.getInstance().getItemRenderer().renderStatic(bottom, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, mint.getLevel(), 0);
            stack.popPose();


        }
        if(!topdie.isEmpty())
        {

                stack.pushPose();
                stack.translate(((7.125f+1f)/16f), (15f/16), (6.75f/16f));

                if(rotation == 1)
                {
                    stack.translate(((1f)/16),0,((0.25f+1f)/16));
                }
                if(rotation == 2)
                {
                    stack.translate(0,0,(2.25f/16f));
                }
                if(rotation == 3)
                {
                    stack.translate(-((1f)/16),0,((0.25f+1f)/16));
                }
                if(hasHit)
                {
                    stack.translate(0,-(1f/16f),0);
                }

                //stack.translate(((7.5f+1f)/16f), (13.5f/16), ((6.5f+1f)/16f));
                //stack.translate(0,0,0);

                //stack.translate(0.5075F, (15.0f/16.0f), 0.563f);
                stack.scale(0.8F, 0.8F, 0.8F);
                //stack.mulPose(Axis.XP.rotationDegrees(90f));
                //stack.mulPose(Axis.XP.rotationDegrees(90f* rotation + 270F));
                //stack.mulPose(Axis.ZP.rotationDegrees(90f * rotation + 270F));
                Minecraft.getInstance().getItemRenderer().renderStatic(topdie, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, mint.getLevel(), 0);
                stack.popPose();


        }

        if(!coin.isEmpty())
        {
            stack.pushPose();
            stack.translate(((7.25f+1f)/16f), (13.5f/16), (7.125f/16f));
            stack.mulPose(Axis.XP.rotationDegrees(90f));
            rotateTranslate(stack, rotation);

            //stack.translate(((7.5f+1f)/16f), (13.5f/16), ((6.5f+1f)/16f));
            //stack.translate(0,0,0);

            //stack.translate(0.5075F, (15.0f/16.0f), 0.563f);
            stack.scale(0.3F, 0.3F, 1F);
            //stack.mulPose(Axis.XP.rotationDegrees(90f));
            //stack.mulPose(Axis.XP.rotationDegrees(90f* rotation + 270F));
            //stack.mulPose(Axis.ZP.rotationDegrees(90f * rotation + 270F));
            Minecraft.getInstance().getItemRenderer().renderStatic(coin, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, mint.getLevel(), 0);
            stack.popPose();
        }

    }
    public void rotateTranslate(PoseStack stack,int rotation)
    {
        if(rotation == 1)//west
        {
            //xzy
            stack.translate(((1f-(0.125/2))/16),((0.125f+1f)/16),0);
        }
        if(rotation == 2)//north
        {
            //xzy
            stack.translate(0,((2.25f-(0.125/2))/16f),0);
        }
        if(rotation == 3)//east
        {
            //xzy
            stack.translate(-((1f)/16),((0.2f-(0.125/2)+1f)/16),0);
        }
    }
}

