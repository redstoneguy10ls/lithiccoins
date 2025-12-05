package com.redstoneguy10ls.lithiccoins.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue numberOfStacksInCoinPurse = BUILDER
        .comment("")
        .comment(" Number of stacks that can be put into a coin purse")
        .defineInRange("numberOfStacksInCoinPurse", 4, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue engraveName = BUILDER
        .comment("")
        .comment(" Engrave the name of the player minting a coin onto the it")
        .define("engraveName",false);



    public static final ModConfigSpec SPEC = BUILDER.build();
}
