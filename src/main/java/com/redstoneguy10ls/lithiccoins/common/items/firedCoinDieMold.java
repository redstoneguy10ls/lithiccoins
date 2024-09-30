package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.tooltips;
import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.util.Metal;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Optional;
import java.util.function.IntSupplier;

public class firedCoinDieMold extends MoldItem {
    public firedCoinDieMold(Metal.ItemType type, Properties properties) {
        super(type, properties);
    }

    public firedCoinDieMold(ForgeConfigSpec.IntValue capacity, TagKey<Fluid> fluidTag, Properties properties) {
        super(capacity, fluidTag, properties);
    }

    public firedCoinDieMold(IntSupplier capacity, TagKey<Fluid> fluidTag, Properties properties) {
        super(capacity, fluidTag, properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new tooltips.CoinImageTooltip(1, 1, LCHelpers.getStamptype(stack.getItem())));
    }
}
