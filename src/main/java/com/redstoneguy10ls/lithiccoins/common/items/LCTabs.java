package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCBlocks;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.SelfTests;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class LCTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LithicCoins.MOD_ID);


    public static final LCTabs.CreativeTabHolder EVERYTHING_ELSE =
            register("everything_else", () -> new ItemStack(LCBlocks.MINT.get()), LCTabs::fillTab);

    public static final LCTabs.CreativeTabHolder COINS =
            register("coins", () -> new ItemStack(LCItems.BLANK_COINS.get(coinMaterial.GOLD).get()), LCTabs::fillCoinTab);


    private static void fillCoinTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        LCItems.BLANK_COINS.values().forEach(reg -> accept(out, reg));

        for(stampTypes stamps : stampTypes.VALUES)
        {
            for(coinMaterial mats : coinMaterial.values())
            {
                accept(out,LCItems.STAMPED_COINS.get(mats), stamps);
            }
            //accept(out,LCItems.STAMPED_COINS.get(coinMaterial.GOLD), stamps);

        }
    }

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        accept(out, LCBlocks.MINT);
        accept(out, LCItems.DIE_HOLDER);
        accept(out, LCItems.STAMP_HOLDER);

        accept(out, LCItems.FIRE_COIN_MOLD);
        accept(out, LCItems.UNFIRED_FIRE_COIN_MOLD);
        accept(out, LCItems.COIN_MOLD);
        accept(out, LCItems.UNFIRED_COIN_MOLD);
        accept(out, LCItems.COIN_PURSE);
        accept(out, LCItems.WAX_DIE);

        LCItems.MOLDED_WAX.values().forEach(reg -> accept(out, reg));
        LCItems.UNFIRED_DIE_MOLD.values().forEach(reg -> accept(out, reg));
        LCItems.FIRED_DIE_MOLD.values().forEach(reg -> accept(out, reg));




        for(TopDies stamps : TopDies.VALUES)
        {

                for(Metal.Default mats : Metal.Default.values())
                {
                    accept(out, LCItems.TOP_DIE.get(stamps), mats);
                }

            //accept(out, LCItems.TOP_DIE.get(stamps), Metal.Default.STEEL);
        }
        LCItems.BOTTOM_DIE.values().forEach(reg -> accept(out, reg));


    }

    private static <T extends ItemLike, R extends Supplier<T>> void accept(CreativeModeTab.Output out, R reg)
    {
        if (reg.get().asItem() == Items.AIR)
        {
            TerraFirmaCraft.LOGGER.error("BlockItem with no Item added to creative tab: " + reg);
            SelfTests.reportExternalError();
            return;
        }
        out.accept(reg.get());
    }

    private static <T extends ItemLike, R extends Supplier<T>, K> void accept(CreativeModeTab.Output out, Map<K, R> map, K key)
    {
        if (map.containsKey(key))
        {
            out.accept(map.get(key).get());
        }
    }
    private static <T extends ItemLike, R extends Supplier<T>, K1, K2> void accept(CreativeModeTab.Output out, Map<K1, Map<K2, R>> map, K1 key1, K2 key2)
    {
        if (map.containsKey(key1) && map.get(key1).containsKey(key2))
        {
            out.accept(map.get(key1).get(key2).get());
        }
    }

    private static LCTabs.CreativeTabHolder register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final RegistryObject<CreativeModeTab> reg = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("lithiccoins.creative_tab." + name))
                .displayItems(displayItems)
                .build());
        return new LCTabs.CreativeTabHolder(reg, displayItems);
    }

    private static <T> void consumeOurs(IForgeRegistry<T> registry, Consumer<T> consumer)
    {
        for (T value : registry)
        {
            if (Objects.requireNonNull(registry.getKey(value)).getNamespace().equals(LithicCoins.MOD_ID))
            {
                consumer.accept(value);
            }
        }
    }

    public record CreativeTabHolder(RegistryObject<CreativeModeTab> tab, CreativeModeTab.DisplayItemsGenerator generator) {}
}
