package com.redstoneguy10ls.lithiccoins.items;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.SelfTests;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ModTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LithicCoins.MOD_ID);


    public static final ModTabs.CreativeTabHolder COINS =
            register("coins", () -> new ItemStack(ModItems.BLANK_COINS.get(coinMaterial.GOLD).get()), ModTabs::fillTab);

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        ModItems.BLANK_COINS.values().forEach(reg -> accept(out, reg));
        /*
        for(stampTypes stamps : stampTypes.VALUES)
        {
                accept(out,ModItems.STAMPED_COINS.get(coinMaterial.GOLD), stamps);

        }
        */

        accept(out, ModItems.FIRE_COIN_MOLD);
        accept(out, ModItems.UNFIRED_FIRE_COIN_MOLD);
        accept(out, ModItems.COIN_MOLD);
        accept(out, ModItems.UNFIRED_COIN_MOLD);

        /*
        for(stampTypes stamps : stampTypes.VALUES)
        {
            for(stampMaterials mats : stampMaterials.VALUES)
            {
                accept(out, ModItems.STAMPS.get(stamps), mats);

            }
        }
        ModItems.BOTTOM_DIE.values().forEach(reg -> accept(out, reg));
        */

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


    private static ModTabs.CreativeTabHolder register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final RegistryObject<CreativeModeTab> reg = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("lithiccoins.creative_tab." + name))
                .displayItems(displayItems)
                .build());
        return new ModTabs.CreativeTabHolder(reg, displayItems);
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
