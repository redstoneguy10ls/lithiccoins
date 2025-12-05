package com.redstoneguy10ls.lithiccoins.common.component;

import java.util.List;
import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

public record MintingComponent(
    long mintingLocation,
    long mintingDate,
    Optional<String> engraver
)
{
    public static final Codec<MintingComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.LONG.fieldOf("minting_location").forGetter(c -> c.mintingLocation),
        Codec.LONG.fieldOf("minting_data").forGetter(c -> c.mintingDate),
        Codec.sizeLimitedString(32).optionalFieldOf("engraver").forGetter(c -> c.engraver)
    ).apply(i, MintingComponent::new));

    public static final StreamCodec<ByteBuf, MintingComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_LONG, c -> c.mintingLocation,
        ByteBufCodecs.VAR_LONG, c -> c.mintingDate,
        ByteBufCodecs.optional(ByteBufCodecs.stringUtf8(32)), c -> c.engraver,
        MintingComponent::new
    );

    public static final MintingComponent EMPTY = new MintingComponent(ChunkPos.INVALID_CHUNK_POS, Long.MIN_VALUE, Optional.empty());


    public static void addTooltipInfo(ItemStack stack, List<Component> tooltips)
    {
        final @Nullable MintingComponent component = stack.get(LCComponents.MINTING);
        if (component != null)
        {
            if (component.mintingLocation == ChunkPos.INVALID_CHUNK_POS)
            {
                Component.translatable("lithiccoins.tooltip.lithiccoins.coins.tooltip_unminted").withStyle(ChatFormatting.GRAY);
            }
            else
            {
                tooltips.add(Component.translatable("lithiccoins.tooltip.lithiccoins.coins.tooltip_minted", new ChunkPos(component.mintingLocation).toString()).withStyle(ChatFormatting.GRAY));
                tooltips.add(Component.translatable("lithiccoins.tooltip.lithiccoins.coins.tooltip_time", component.mintingDate).withStyle(ChatFormatting.GRAY));

                component.engraver.ifPresent(s -> tooltips.add(Component.translatable("lithiccoins.tooltip.lithiccoins.coins.tooltip_engraver", s).withStyle(ChatFormatting.GRAY)));
            }
        }
    }

    public static void set(ItemStack stack, long mintingLocation, long mintingDate, @Nullable Player player)
    {
        stack.set(LCComponents.MINTING, new MintingComponent(
            mintingLocation,
            mintingDate,
            player == null
                ? Optional.empty()
                : Optional.of(player.getName().getString())
        ));
    }

    @Override
    public boolean equals(Object obj)
    {
        return this == obj || (obj instanceof MintingComponent that
            && this.mintingLocation == that.mintingLocation
            && this.mintingDate == that.mintingDate
            && this.engraver.equals(that.engraver)
        );
    }
}
