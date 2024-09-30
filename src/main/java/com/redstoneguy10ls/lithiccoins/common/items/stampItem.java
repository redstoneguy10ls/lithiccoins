package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.tooltips;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

import java.util.Optional;

public class stampItem extends TieredItem {
    public stampItem(Tier tier , Properties properties){super(tier, properties);}



    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new tooltips.CoinImageTooltip(1, 1, LCHelpers.getStamptype(stack.getItem())));
    }
}
