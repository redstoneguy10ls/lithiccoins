package com.redstoneguy10ls.lithiccoins.common.misc;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistryHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);


    public static final Id COINPURSE_ADD = register("item.coinpurse.some_add1");

    public static final Id COINPURSE_EMPTY_ADD = register("item.coinpurse.empty_add");

    public static final Id MINT_HIT = register("block.minting_die.hit");



    private static Id register(String name)
    {
        return new Id(SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Helpers.identifier(name))));
    }

    public record Id(DeferredHolder<SoundEvent, SoundEvent> holder) implements RegistryHolder<SoundEvent, SoundEvent> {}
}
