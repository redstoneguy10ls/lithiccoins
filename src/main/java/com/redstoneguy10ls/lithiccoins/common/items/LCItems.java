package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.common.component.LCComponents;
import com.redstoneguy10ls.lithiccoins.common.component.MintingComponent;
import com.redstoneguy10ls.lithiccoins.common.component.PurseComponent;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    // Coins
    public static final Map<CoinMaterial, Id> BLANK_COINS = Helpers.mapOf(CoinMaterial.class, material ->
        register("blank_coin/" + material.name(),
            () -> new CoinItem(new Item.Properties().component(LCComponents.MINTING, MintingComponent.EMPTY)))
    );

    public static final Map<StampType, Map<CoinMaterial, Id>> STAMPED_COINS = Helpers.mapOf(StampType.class, type ->
        Helpers.mapOf(CoinMaterial.class, material ->
            register("coin/" + type.name() + "/" + material.name(),
                () -> new CoinItem(new Item.Properties().component(LCComponents.MINTING, new MintingComponent(0, 0, Optional.empty())))))
    );

    // Dies
    public static final Map<StampType, Map<Metal, Id>> TOP_DIE = Helpers.mapOf(StampType.class, type ->
        Helpers.mapOf(Metal.class, LCHelpers::isToolMetal, metal ->
            register("top_die/" + type.name() + "/" + metal.name(),
                 () -> new StampItem(type, metal.toolTier(), new Item.Properties().rarity(metal.rarity()))))
    );

    public static final Map<Metal, Id> BOTTOM_DIE = Helpers.mapOf(Metal.class, LCHelpers::isToolMetal, metal ->
        register("bottom_die/" + metal.name(),
            () -> new TieredItem(metal.toolTier(), new Item.Properties().rarity(metal.rarity())))
    );

    public static final Map<StampType, Id> MOLDED_WAX = Helpers.mapOf(StampType.class, type ->
        register("wax/" + type.name(),
            () -> new MoldedWaxItem(type, new Item.Properties()))
    );

    // Die Molds
    public static final Map<StampType, Id> UNFIRED_DIE_MOLD = Helpers.mapOf(StampType.class, type ->
        register("ceramic/die_mold/unfired/" + type.name(),
            () -> new UnfiredDieMold(type, new Item.Properties()))
    );

    public static final Map<StampType, Id> FIRED_DIE_MOLD = Helpers.mapOf(StampType.class, type ->
        register("ceramic/die_mold/fired/" + type.name(),
            () -> new FiredDieMold(type, TFCConfig.SERVER.moldIngotCapacity, TFCTags.Fluids.USABLE_IN_INGOT_MOLD, new Item.Properties()))
    );

    // Coin Molds
    public static final Id UNFIRED_COIN_MOLD = register("ceramic/unfired_coin_mold");
    public static final Id COIN_MOLD = register("ceramic/coin_mold",
        () -> new MoldItem(TFCConfig.SERVER.moldIngotCapacity, LCTags.Fluids.USABLE_IN_COIN_MOLD, new Item.Properties())
    );

    public static final Id UNFIRED_FIRE_COIN_MOLD = register("ceramic/unfired_fire_coin_mold");
    public static final Id FIRE_COIN_MOLD = register("ceramic/fire_coin_mold",
        () -> new MoldItem(TFCConfig.SERVER.moldFireIngotCapacity, LCTags.Fluids.USABLE_IN_COIN_MOLD, new Item.Properties())
    );

    // Misc
    public static final Id COIN_PURSE = register("coin_purse",
        () -> new CoinPurseItem(new Item.Properties().stacksTo(1).component(LCComponents.PURSE, PurseComponent.EMPTY))
    );

    public static final Id STAMP_HOLDER = register("stamp_holder");

    public static final Id DIE_HOLDER = register("die_holder");



    private static Id register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static Id register(String name, Supplier<Item> item)
    {
        return new Id(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }

    public record Id(DeferredHolder<Item, Item> holder) implements RegistryHolder<Item, Item>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get();
        }
    }
}
