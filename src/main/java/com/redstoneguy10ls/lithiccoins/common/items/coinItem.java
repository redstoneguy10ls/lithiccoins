package com.redstoneguy10ls.lithiccoins.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class coinItem extends Item {
    public coinItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {


        int rand = player.getRandom().nextIntBetweenInclusive(1, 100);
        if(rand <= 50)
        {
            player.displayClientMessage(Component.translatable("lithiccoins.tooltip.lithiccoins.coins.tails").withStyle(ChatFormatting.BOLD), true);
        }
        else
        {
            player.displayClientMessage(Component.translatable("lithiccoins.tooltip.lithiccoins.coins.heads").withStyle(ChatFormatting.BOLD), true);

        }


        player.getCooldowns().addCooldown(this, 10);

        return super.use(pLevel, player, pUsedHand);
    }




}
