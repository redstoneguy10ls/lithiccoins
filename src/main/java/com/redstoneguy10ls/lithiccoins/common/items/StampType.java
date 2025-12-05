package com.redstoneguy10ls.lithiccoins.common.items;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import net.dries007.tfc.network.StreamCodecs;

public enum StampType implements StringRepresentable
{
    ANGLER,
    ARCHER,
    ARMS_UP,
    BEE,
    BLADE,
    BREWER,
    BURN,
    BUST,
    DANGER,
    EAGLE,
    EXPLORER,
    FACE,
    FRIEND,
    HEART,
    HEARTBREAK,
    HOWL,
    MINER,
    MOURNER,
    PLENTY,
    PRIZE,
    PUNCHED,
    SHEAF,
    SHELTER,
    SKULL,
    SYMBOL,
    TRIFOIL;

    public static final Codec<StampType> CODEC = StringRepresentable.fromEnum(StampType::values);
    public static final StreamCodec<ByteBuf, StampType> STREAM_CODEC = StreamCodecs.forEnum(StampType::values);

    @Override
    public String getSerializedName()
    {
        return this.name().toLowerCase();
    }
}
