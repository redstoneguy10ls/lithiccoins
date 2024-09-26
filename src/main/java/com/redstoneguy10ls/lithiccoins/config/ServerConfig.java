package com.redstoneguy10ls.lithiccoins.config;


import net.dries007.tfc.config.ConfigBuilder;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public final ForgeConfigSpec.IntValue numberOfStacksInCoinPurse;
    public final ForgeConfigSpec.BooleanValue printName;


    ServerConfig(ConfigBuilder builder) {
        builder.push("general");

        numberOfStacksInCoinPurse = builder.comment("How many stacks can be added to a coin purse").define("numberOfStacksInCoinPurse", 4, 1, Integer.MAX_VALUE);
        printName = builder.comment("Should the coins display the name of the person who minted them").define("CoinsPrintName",false);
    }
}
