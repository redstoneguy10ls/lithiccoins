package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class MoldedWaxItem extends Item
{
    private final StampType type;

    public MoldedWaxItem(StampType type, Properties pProperties)
    {
        super(pProperties);
        this.type = type;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new Tooltips.CoinImageTooltip(1, 1, type));
    }
}
