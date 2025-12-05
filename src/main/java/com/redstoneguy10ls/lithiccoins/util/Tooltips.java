package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.component.PurseComponent;
import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public final class Tooltips
{
    public record CoinImageTooltip(int width, int height, StampType stamp) implements TooltipComponent {}
    public record PurseImageTooltip(PurseComponent contents) implements TooltipComponent {}
}
