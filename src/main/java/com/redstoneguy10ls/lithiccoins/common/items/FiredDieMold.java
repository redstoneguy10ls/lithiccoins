package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import net.dries007.tfc.common.items.MoldItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;
import java.util.function.Supplier;

public class FiredDieMold extends MoldItem
{
    private final StampType type;

    public FiredDieMold(StampType type, Supplier<Integer> capacity, TagKey<Fluid> fluidTag, Properties properties)
    {
        super(capacity, fluidTag, properties);
        this.type = type;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new Tooltips.CoinImageTooltip(1, 1, type));
    }
}
