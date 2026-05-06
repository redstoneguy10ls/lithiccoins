package com.redstoneguy10ls.lithiccoins.common.misc;

import net.dries007.tfc.util.registry.RegistryHolder;

import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);


    public static final Id COINPURSE_ADD_FEW = register("item.coin_purse.add_few");
    public static final Id COINPURSE_ADD_SOME = register("item.coin_purse.add_some");
    public static final Id COINPURSE_ADD_MANY = register("item.coin_purse.add_many");
    public static final Id COINPURSE_REMOVE = register("item.coin_purse.remove");

    public static final Id MINT_HIT = register("block.mint.hit");



    private static Id register(String name)
    {
        return new Id(SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(LCHelpers.identifier(name))));
    }

    public record Id(DeferredHolder<SoundEvent, SoundEvent> holder) implements RegistryHolder<SoundEvent, SoundEvent> {}
}
