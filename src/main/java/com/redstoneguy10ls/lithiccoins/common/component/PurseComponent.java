package com.redstoneguy10ls.lithiccoins.common.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import com.redstoneguy10ls.lithiccoins.config.ServerConfig;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import net.dries007.tfc.common.component.item.ItemComponent;
import net.dries007.tfc.util.Helpers;

public record PurseComponent(
    List<ItemStack> contents
)
{
    public static final Codec<PurseComponent> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(PurseComponent::new, PurseComponent::contents);

    public static final StreamCodec<RegistryFriendlyByteBuf, PurseComponent> STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC
        .apply(ByteBufCodecs.list())
        .map(PurseComponent::new, PurseComponent::contents);

    public PurseComponent(List<ItemStack> contents)
    {
        this.contents = new ArrayList<>(contents);
    }

    private static final Supplier<Integer> MAX_WEIGHT = () -> 64 * ServerConfig.numberOfStacksInCoinPurse.get();
    public static final PurseComponent EMPTY = new PurseComponent(List.of());

    @Override
    public boolean equals(Object obj)
    {
        return this == obj || (obj instanceof PurseComponent that && ItemComponent.equals(this.contents, that.contents));
    }

    @Override
    public int hashCode()
    {
        return ItemComponent.hashCode(this.contents);
    }



    public boolean isEmpty()
    {
        return this.contents.isEmpty();
    }

    public int weight()
    {
        return this.contents.stream().mapToInt(ItemStack::getCount).sum();
    }

    public Fraction weightFraction()
    {
        return Fraction.getFraction(weight(), MAX_WEIGHT.get());
    }

    public int size()
    {
        return this.contents.size();
    }

    public boolean isFull()
    {
        return weight() == MAX_WEIGHT.get();
    }

    public ItemStack getItemUnsafe(int index)
    {
        return this.contents.get(index);
    }



    public Component getTooltipComponent()
    {
        return Component.translatable("item.minecraft.bundle.fullness", weight(), MAX_WEIGHT.get()).withStyle(ChatFormatting.GRAY);
    }

    public Optional<TooltipComponent> getTooltipImage()
    {
        return Optional.of(new Tooltips.PurseImageTooltip(this));
    }


    /**
     * Mutating the contents of the component directly is a bad idea, as it will lead to interesting behaviour like globally changing what {@code PurseComponent.EMPTY} is
     * <p>
     * Thus, we make use of the {@code Mutable} dummy class in which we do every mutating action, before casting it back into the immutable {@code PurseComponent}
     */
    public static class Mutable
    {
        private final List<ItemStack> contents;

        public Mutable(PurseComponent component)
        {
            this.contents = new ArrayList<>(component.contents);
        }

        public Mutable clearItems() {
            this.contents.clear();
            return this;
        }

        public int weight()
        {
            return this.contents.stream().mapToInt(ItemStack::getCount).sum();
        }

        private int findStackIndex(ItemStack stack)
        {
            if (stack.isStackable())
            {
                for (int i = 0; i < this.contents.size(); ++i)
                {
                    final ItemStack compare = this.contents.get(i);
                    if (compare.getCount() != compare.getMaxStackSize() && ItemStack.isSameItemSameComponents(compare, stack))
                    {
                        return i;
                    }
                }

            }
            return -1;
        }

        private int getMaxAmountToAdd(ItemStack stack)
        {
            return Math.min(MAX_WEIGHT.get() - this.weight(), stack.getCount());
        }

        public int tryInsert(ItemStack stack)
        {
            if (!stack.isEmpty() && stack.canFitInsideContainerItems() && Helpers.isItem(stack, LCTags.Items.FIT_IN_PURSE))
            {
                int amount = getMaxAmountToAdd(stack);
                if (amount == 0)
                {
                    return 0;
                }
                else
                {
                    int i = findStackIndex(stack);
                    if (i != -1)
                    {
                        // We want to ensure every stack in the purse only contains as many coins as a stack can usually hold
                        // This leads to this slightly more complicated solution compared to what vanilla bundles do, as those don't allow more than one stack of items in total
                        ItemStack baseStack = this.contents.remove(i);
                        ItemStack changedStack = baseStack.copyWithCount(Math.min(baseStack.getMaxStackSize(), baseStack.getCount() + amount));

                        final int change = changedStack.getCount() - baseStack.getCount();
                        stack.shrink(change);
                        this.contents.addFirst(changedStack);

                        if (amount - change > 0)
                        {
                            this.contents.addFirst(stack.split(amount - change));
                        }
                    }
                    else
                    {
                        this.contents.addFirst(stack.split(amount));
                    }

                    return amount;
                }
            }
            else
            {
                return 0;
            }
        }

        public int tryTransfer(Slot slot, Player player)
        {
            ItemStack stack = slot.getItem();
            return tryInsert(slot.safeTake(stack.getCount(), getMaxAmountToAdd(stack), player));
        }

        public @Nullable ItemStack removeOne()
        {
            if (this.contents.isEmpty())
            {
                return null;
            }
            else
            {
                return (this.contents.removeFirst()).copy();
            }
        }

        public PurseComponent toImmutable()
        {
            return new PurseComponent(List.copyOf(this.contents));
        }
    }
}
