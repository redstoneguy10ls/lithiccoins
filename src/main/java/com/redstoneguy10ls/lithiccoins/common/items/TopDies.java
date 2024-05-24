package com.redstoneguy10ls.lithiccoins.common.items;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public enum TopDies {
    ANGLER(1),
    ARCHER(2),
    ARMS_UP(3),
    BLADE(4),
    BREWER(5),
    BURN(6),
    DANGER(7),
    EAGLE(8),
    EXPLORER(9),
    FRIEND(10),
    HEART(11),
    HEARTBREAK(12),
    HOWL(13),
    MINER(14),
    MOURNER(15),
    PLENTY(16),
    PRIZE(17),
    SHEAF(18),
    SHELTER(19),
    SKULL(20),
    TRIFOIL(21);
    public static final TopDies[] VALUES = values();

    static
    {
        assert VALUES.length < Byte.MAX_VALUE;
    }

    private final int numb;

    TopDies(int id) {
        this.numb = id;
    }
    public int getId()
    {
        return numb;
    }

    @Nullable
    public static TopDies valueOf(int id)
    {
        return id >= 0 && id < VALUES.length ? VALUES[id] : null;
    }

    public static TopDies fromNetwork(FriendlyByteBuf buffer)
    {
        final TopDies die = valueOf(buffer.readByte());
        return die;
    }
    public void toNetwork(FriendlyByteBuf buffer)
    {
        buffer.writeByte(ordinal());
    }

}
