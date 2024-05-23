package com.redstoneguy10ls.lithiccoins.common.items;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class coinItem extends Item {
    public coinItem(Properties pProperties) {
        super(pProperties);
    }




    /*
    @Override
    public InteractionResult useOn(UseOnContext pContext)
    {
        if(!pContext.getLevel().isClientSide())
        {
            BlockPos blockPos = pContext.getClickedPos();
            ChunkPos chunkPos = new ChunkPos(blockPos);

            ItemStack stack = new ItemStack(this);

            stack.getCapability(LocationCapability.CAPABILITY).ifPresent(test ->
            {
                test.setCreationLocation(chunkPos);
            });
            /*
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            boolean foundBlock = false;

            for(int i = 0; i <= positionClicked.getY() + 64; i++)
            {
                BlockState state = pContext.getLevel().getBlockState(positionClicked.below(i));

                if(isValuableBlock(state)){
                    outputValuableChunk(positionClicked.below(i),player,state.getBlock());
                    //outputValuableCoordinantes(positionClicked.below(i),player,state.getBlock());
                    foundBlock = true;
                    break;
                }
            }
            *//*
        }
        return InteractionResult.SUCCESS;
    }
    */

    private void outputValuableChunk(BlockPos blockPos, Player player, Block block) {
        ChunkPos chunkpos = new ChunkPos(blockPos);

        //String test = String.valueOf(chunkpos.x);

        player.sendSystemMessage(Component.literal("Found" + I18n.get(block.getDescriptionId()) +
                " at " + "(" + chunkpos.x + "," + chunkpos.z + ")"));

    }
    /*
    private void outputValuableCoordinantes(BlockPos blockPos, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found" + I18n.get(block.getDescriptionId()) +
                " at " + "(" + blockPos.getX() + "," + blockPos.getY() + "," + blockPos.getZ() + ")"));

    }

     */

    private boolean isValuableBlock(BlockState state) {
        return state.is(Blocks.IRON_BLOCK);
    }
    /*
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        final int test = 5;
        pTooltipComponents.add(Component.translatable("tooltip.lithiccoins.coins.tooltip",test));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    */
}
