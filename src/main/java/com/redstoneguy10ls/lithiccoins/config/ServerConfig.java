package com.redstoneguy10ls.lithiccoins.config;


import net.dries007.tfc.config.ConfigBuilder;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public final ForgeConfigSpec.IntValue numberOfStacksInCoinPurse;


    ServerConfig(ConfigBuilder builder) {
        builder.push("general");

        numberOfStacksInCoinPurse = builder.comment("How many stacks can be added to a coin purse").define("numberOfStacksInCoinPurse", 4, 1, Integer.MAX_VALUE);
    }
}
