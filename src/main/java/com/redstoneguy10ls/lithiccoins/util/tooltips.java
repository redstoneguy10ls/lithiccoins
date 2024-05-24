package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.items.stampTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;

import java.util.List;

public final class tooltips {
    public record CoinImageTooltip(int width, int height, stampTypes stamp) implements TooltipComponent{

    }
}
