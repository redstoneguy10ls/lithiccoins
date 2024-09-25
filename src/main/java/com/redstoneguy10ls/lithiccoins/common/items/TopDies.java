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
    BUST(7),
    DANGER(8),
    EAGLE(9),
    EXPLORER(10),
    FRIEND(11),
    HEART(12),
    HEARTBREAK(13),
    HOWL(14),
    MINER(15),
    MOURNER(16),
    PLENTY(17),
    PRIZE(18),
    PUNCHED(19),
    SHEAF(20),
    SHELTER(21),
    SKULL(22),
    SYMBOL(23),
    TRIFOIL(24),
    FACE(25),
    BEE(26);
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
