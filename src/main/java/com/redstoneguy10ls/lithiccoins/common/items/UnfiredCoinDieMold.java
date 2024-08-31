package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class UnfiredCoinDieMold extends Item {
    public UnfiredCoinDieMold(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new Tooltips.CoinImageTooltip(1,1, LCHelpers.getStamptype(stack.getItem())));
    }
}
