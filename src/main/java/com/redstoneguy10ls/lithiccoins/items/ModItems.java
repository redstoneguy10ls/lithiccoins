package com.redstoneguy10ls.lithiccoins.items;

import com.redstoneguy10ls.lithiccoins.util.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    public static final Map<coinMaterial, RegistryObject<Item>> BLANK_COINS = Helpers.mapOfKeys(coinMaterial.class, type ->
            register("blank_coin/" + type.name())
    );


    public static final Map<coinMaterial, Map<stampTypes, RegistryObject<Item>>> STAMPED_COINS =
            Helpers.mapOfKeys(coinMaterial.class, coins ->
                    Helpers.mapOfKeys(stampTypes.class, stamps -> register("coin/"+stamps.name()+"/"+coins.name(),
                            () -> new coinItem(new Item.Properties())))
            );
    public static final Map<stampTypes, Map<stampMaterials, RegistryObject<Item>>> STAMPS =
            Helpers.mapOfKeys(stampTypes.class, stamps ->
                    Helpers.mapOfKeys(stampMaterials.class, metals -> register("top_die/"+stamps.name()+"/"+metals.name(),
                             () -> new Item(new Item.Properties().defaultDurability(250))))
            );

    public static final Map<stampMaterials, RegistryObject<Item>> BOTTOM_DIE = Helpers.mapOfKeys(stampMaterials.class, metal ->
            register("bottom_die/" + metal.name())
    );

    public static final RegistryObject<Item> UNFIRED_COIN_MOLD = register("ceramic/unfired_coin_mold");
    public static final RegistryObject<Item> COIN_MOLD = register("ceramic/coin_mold",
            () -> new MoldItem(TFCConfig.SERVER.moldFireIngotCapacity, ModTags.Fluids.USABLE_IN_COIN_MOLD, new Item.Properties()));

    public static final RegistryObject<Item> UNFIRED_FIRE_COIN_MOLD = register("ceramic/unfired_fire_coin_mold");
    public static final RegistryObject<Item> FIRE_COIN_MOLD = register("ceramic/fire_coin_mold",
            () -> new MoldItem(TFCConfig.SERVER.moldFireIngotCapacity, TFCTags.Fluids.USABLE_IN_INGOT_MOLD, new Item.Properties()));

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
