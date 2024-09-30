package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.tooltips;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class moldedWaxItem extends Item {

    public moldedWaxItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new tooltips.CoinImageTooltip(1, 1, LCHelpers.getStamptype(stack.getItem())));
    }
}
