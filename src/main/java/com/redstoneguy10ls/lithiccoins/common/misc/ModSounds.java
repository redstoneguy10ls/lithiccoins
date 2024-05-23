package com.redstoneguy10ls.lithiccoins.common.misc;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import net.dries007.tfc.util.Helpers;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LithicCoins.MOD_ID);

    public static final RegistryObject<SoundEvent> COINPURSE_ADD = create("item.coinpurse.some_add1");


    private static RegistryObject<SoundEvent> create(String name)
    {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Helpers.identifier(name)));
    }
}
