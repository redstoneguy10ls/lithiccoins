package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.common.capability.LocationHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CoinItem extends Item {
    public CoinItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player player, @NotNull InteractionHand pUsedHand) {


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


    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new LocationHandler(stack);
    }


}
