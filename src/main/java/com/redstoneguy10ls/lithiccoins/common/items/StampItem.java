package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

import java.util.Optional;

public class StampItem extends TieredItem
{
    private final StampType type;

    public StampItem(StampType type, Tier tier, Properties properties)
    {
        super(tier, properties);
        this.type = type;
    }

    public StampType getType()
    {
        return this.type;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new Tooltips.CoinImageTooltip(1, 1, type));
    }
}
