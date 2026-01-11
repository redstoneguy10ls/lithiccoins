package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.common.component.LCComponents;
import com.redstoneguy10ls.lithiccoins.common.component.PurseComponent;
import com.redstoneguy10ls.lithiccoins.common.misc.LCSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class CoinPurseItem extends Item
{
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public CoinPurseItem(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player)
    {
        if (stack.getCount() == 1 && action == ClickAction.SECONDARY)
        {
            PurseComponent purse = stack.get(LCComponents.PURSE);
            if (purse == null)
            {
                return false;
            }
            else
            {
                ItemStack itemstack = slot.getItem();
                PurseComponent.Mutable mutable = new PurseComponent.Mutable(purse);
                if (itemstack.isEmpty())
                {
                    this.playRemoveOneSound(player);

                    ItemStack itemstack1 = mutable.removeOne();
                    if (itemstack1 != null)
                    {
                        ItemStack itemstack2 = slot.safeInsert(itemstack1);
                        mutable.tryInsert(itemstack2);
                    }
                }
                else if (itemstack.canFitInsideContainerItems())
                {
                    int i = mutable.tryTransfer(slot, player);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(LCComponents.PURSE, mutable.toImmutable());
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access)
    {
        if (stack.getCount() != 1)
        {
            return false;
        }
        else if (action == ClickAction.SECONDARY && slot.allowModification(player))
        {
            PurseComponent purse = stack.get(LCComponents.PURSE);
            if (purse == null)
            {
                return false;
            }
            else
            {
                PurseComponent.Mutable mutable = new PurseComponent.Mutable(purse);
                if (other.isEmpty())
                {
                    ItemStack itemstack = mutable.removeOne();
                    if (itemstack != null)
                    {
                        this.playRemoveOneSound(player);
                        access.set(itemstack);
                    }
                }
                else
                {
                    int i = mutable.tryInsert(other);
                    if (i > 0)
                    {
                        this.playInsertSound(player);
                    }
                }

                stack.set(LCComponents.PURSE, mutable.toImmutable());
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (dropContents(itemstack, player))
        {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        else
        {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    private static boolean dropContents(ItemStack stack, Player player)
    {
        PurseComponent purse = stack.get(LCComponents.PURSE);
        if (purse != null && !purse.isEmpty())
        {
            if (player instanceof ServerPlayer)
            {
                purse.contents().forEach(entry -> player.drop(entry, true));
            }

            stack.set(LCComponents.PURSE, PurseComponent.EMPTY);
            return true;
        }
        else
        {
            return false;
        }
    }

    private static int getContentWeight(ItemStack stack)
    {
        PurseComponent purse = stack.get(LCComponents.PURSE);
        return purse != null ? purse.weight() : 0;
    }



    /**
     * Adding the `X/Y` tooltip, indicating how full the purse is
     */
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        PurseComponent purse = stack.get(LCComponents.PURSE);
        if (purse != null)
        {
            tooltipComponents.add(purse.getTooltipComponent());
        }
    }

    /**
     * Adding the tooltip image of the contained items
     */
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        final PurseComponent purse = stack.get(LCComponents.PURSE);
        if (purse != null)
        {
            return purse.getTooltipImage();
        }

        return Optional.empty();
    }

    /**
     * We add a bar indicating the fullness of the purse, if it isn't empty
     */
    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        final PurseComponent purse = stack.getOrDefault(LCComponents.PURSE, PurseComponent.EMPTY);
        return !purse.isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        final PurseComponent purse = stack.getOrDefault(LCComponents.PURSE, PurseComponent.EMPTY);
        return Math.min(1 + Mth.mulAndTruncate(purse.weightFraction(), 12), 13);
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return BAR_COLOR;
    }

    /**
     * Sounds that play when coins are added / removed from the purse
     */
    private void playRemoveOneSound(Entity entity)
    {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity)
    {
        if(getContentWeight(new ItemStack(this)) == 0)
        {
            entity.playSound(LCSounds.COINPURSE_EMPTY_ADD.get(), 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
        }
        else
        {
            entity.playSound(LCSounds.COINPURSE_ADD.get(), 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
        }
    }

    private void playDropContentsSound(Entity pEntity)
    {
        pEntity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }
}
