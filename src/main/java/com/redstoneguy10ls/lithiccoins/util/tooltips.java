package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.items.stampTypes;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public final class tooltips {
    
    public record CoinImageTooltip(int width, int height, stampTypes stamp) implements TooltipComponent {
    
    }
}
