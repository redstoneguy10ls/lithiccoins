package com.redstoneguy10ls.lithiccoins.common.capability;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;


import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class PurseCapability {
    public static final Capability<IPurse> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static final ResourceLocation KEY = new ResourceLocation(MOD_ID, "purse");
}
