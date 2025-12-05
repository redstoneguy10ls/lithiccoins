package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.common.blocks.LCBlocks;
import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public final class LCTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final Id EVERYTHING_ELSE =
        register("everything_else", () -> new ItemStack(LCBlocks.MINT.asItem()), LCTabs::fillTab);

    public static final Id COINS =
        register("coins", () -> new ItemStack(LCItems.BLANK_COINS.get(CoinMaterial.GOLD).get()), LCTabs::fillCoinTab);


    private static void fillCoinTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        LCItems.BLANK_COINS.values().forEach(reg -> out.accept(reg));

        for(StampType type : StampType.values())
        {
            LCItems.STAMPED_COINS.get(type).values().forEach(reg -> out.accept(reg));
        }
    }

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        out.accept(LCBlocks.MINT);
        out.accept(LCItems.DIE_HOLDER);
        out.accept(LCItems.STAMP_HOLDER);

        out.accept(LCItems.FIRE_COIN_MOLD);
        out.accept(LCItems.UNFIRED_FIRE_COIN_MOLD);
        out.accept(LCItems.COIN_MOLD);
        out.accept(LCItems.UNFIRED_COIN_MOLD);
        out.accept(LCItems.COIN_PURSE);

        LCItems.MOLDED_WAX.values().forEach(out::accept);
        LCItems.UNFIRED_DIE_MOLD.values().forEach(out::accept);
        LCItems.FIRED_DIE_MOLD.values().forEach(out::accept);

        for(StampType type : StampType.values())
        {
            LCItems.TOP_DIE.get(type).values().forEach(out::accept);
        }

        LCItems.BOTTOM_DIE.values().forEach(out::accept);
    }


    private static Id register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final var holder = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
            .icon(icon)
            .title(Component.translatable("lithiccoins.creative_tab." + name))
            .displayItems(displayItems)
            .build());
        return new Id(holder, displayItems);
    }

    private static void accept(CreativeModeTab.Output out, DecorationBlockHolder decoration)
    {
        out.accept(decoration.stair());
        out.accept(decoration.slab());
        out.accept(decoration.wall());
    }

    public record Id(DeferredHolder<CreativeModeTab, CreativeModeTab> tab, CreativeModeTab.DisplayItemsGenerator generator) {}
}
