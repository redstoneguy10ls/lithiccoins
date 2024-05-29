package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    public static final Map<coinMaterial, RegistryObject<Item>> BLANK_COINS = Helpers.mapOfKeys(coinMaterial.class, type ->
            register("blank_coin/" + type.name())
    );

    public static final Map<coinMaterial, RegistryObject<Item>> DISPLAY_COINS = Helpers.mapOfKeys(coinMaterial.class, type ->
            register("blank_coin/display/" + type.name())
    );

    public static final Map<coinMaterial, Map<stampTypes, RegistryObject<Item>>> STAMPED_COINS =
            Helpers.mapOfKeys(coinMaterial.class, coins ->
                    Helpers.mapOfKeys(stampTypes.class, stamps -> register("coin/"+stamps.name()+"/"+coins.name(),
                            () -> new coinItem(new Item.Properties())))
            );

    //DIES
    public static final Map<TopDies, Map<Metal.Default, RegistryObject<Item>>> TOP_DIE =
            Helpers.mapOfKeys(TopDies.class, stamps ->
                    Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metals -> register("top_die/"+stamps.name()+"/"+metals.name(),
                             () -> new stampItem(metals.toolTier(), new Item.Properties())))
            );
    public static final Map<Metal.Default, RegistryObject<Item>> BOTTOM_DIE = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("bottom_die/" + metal.name(), () -> new TieredItem(metal.toolTier(), new Item.Properties()))
    );

    public static final Map<Metal.Default, RegistryObject<Item>> DISPLAY_TOP_DIES = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metal ->
            register("display/top_dies/" + metal.name(), () -> new TieredItem(metal.toolTier(), new Item.Properties()))
    );

    //WAX STUFF
    public static final RegistryObject<Item> WAX_DIE = register("wax/wax_die");
    public static final Map<stampTypes, RegistryObject<Item>> MOLDED_WAX = Helpers.mapOfKeys(stampTypes.class, stamps ->
            register("wax/"+stamps.name(), () -> new moldedWaxItem(new Item.Properties())));

    public static final Map<stampTypes, RegistryObject<Item>> UNFIRED_DIE_MOLD = Helpers.mapOfKeys(stampTypes.class, stamps ->
            register("ceramic/die_mold/unfired/"+stamps.name(), () -> new unfiredCoinDieMold(new Item.Properties())));

    public static final Map<stampTypes, RegistryObject<Item>> FIRED_DIE_MOLD =
            Helpers.mapOfKeys(stampTypes.class, stamps ->
                    register("ceramic/die_mold/fired/"+stamps.name(), () ->
                    new firedCoinDieMold(TFCConfig.SERVER.moldIngotCapacity, TFCTags.Fluids.USABLE_IN_INGOT_MOLD, new Item.Properties())));




    public static final RegistryObject<Item> COIN_PURSE = register("coin_purse", () -> new coinPurseItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> STAMP_HOLDER = register("stamp_holder");
    public static final RegistryObject<Item> DIE_HOLDER = register("die_holder");



    //MOLDS
    public static final RegistryObject<Item> UNFIRED_COIN_MOLD = register("ceramic/unfired_coin_mold");
    public static final RegistryObject<Item> COIN_MOLD = register("ceramic/coin_mold",
            () -> new MoldItem(TFCConfig.SERVER.moldIngotCapacity, LCTags.Fluids.USABLE_IN_COIN_MOLD, new Item.Properties()));

    public static final RegistryObject<Item> UNFIRED_FIRE_COIN_MOLD = register("ceramic/unfired_fire_coin_mold");
    public static final RegistryObject<Item> FIRE_COIN_MOLD = register("ceramic/fire_coin_mold",
            () -> new MoldItem(TFCConfig.SERVER.moldFireIngotCapacity, LCTags.Fluids.USABLE_IN_COIN_MOLD, new Item.Properties()));

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
