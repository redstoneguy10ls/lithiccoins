package com.redstoneguy10ls.lithiccoins.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;


public class LargeKnappingPattern
{

    /**
     * AYO big props to the tfc devs for this, as this is pretty
     * much all directly copied from them and their class
     * KnappingPattern
     */

    public static final int MAX_WIDTH = 8;
    public static final int MAX_HEIGHT = 8;

    public static final MapCodec<LargeKnappingPattern> CODEC = RecordCodecBuilder.<Prototype>mapCodec(i -> i.group(
        Codec.STRING.listOf(1, 8).fieldOf("pattern").forGetter(c -> c.pattern),
        Codec.BOOL.optionalFieldOf("default_on").forGetter(c -> c.defaultOn)
    ).apply(i, LargeKnappingPattern.Prototype::new)).flatXmap(LargeKnappingPattern::readPattern, LargeKnappingPattern::writePattern);

    public static final StreamCodec<ByteBuf, LargeKnappingPattern> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, c -> c.width,
        ByteBufCodecs.VAR_INT, c -> c.height,
        ByteBufCodecs.VAR_LONG, c -> c.data,
        ByteBufCodecs.BOOL, c -> c.defaultOn,
        LargeKnappingPattern::new
    );

    public static LargeKnappingPattern from(boolean defaultOn, String... pattern)
    {
        return readPattern(new Prototype(List.of(pattern), Optional.of(defaultOn))).getOrThrow();
    }

    private static DataResult<LargeKnappingPattern> readPattern(Prototype proto)
    {
        final int height = proto.pattern.size();
        if (height == 0 || height > MAX_HEIGHT) return DataResult.error(() -> "Invalid pattern: must have [1, " + MAX_HEIGHT + "] rows");

        final int width = proto.pattern.getFirst().length();
        if (width == 0 || width > MAX_WIDTH) return DataResult.error(() -> "Invalid pattern: must have [1, " + MAX_WIDTH + "] columns");
        if ((height != MAX_HEIGHT || width != MAX_HEIGHT) && proto.defaultOn.isEmpty()) return DataResult.error(() -> "default_on is required if the pattern is not " + MAX_WIDTH + "x" + MAX_HEIGHT);

        final LargeKnappingPattern pattern = new LargeKnappingPattern(width, height, proto.defaultOn.orElse(false));
        for (int r = 0; r < height; ++r)
        {
            String row = proto.pattern.get(r);
            if (r > 0 && width != row.length()) return DataResult.error(() -> "Invalid pattern: each row must be the same width");
            for (int c = 0; c < width; c++)
            {
                pattern.set(r * width + c, row.charAt(c) != ' '); // on = anything that isn't ' '
            }
        }
        return DataResult.success(pattern);
    }

    private static DataResult<Prototype> writePattern(LargeKnappingPattern pattern)
    {
        final List<String> array = new ArrayList<>();
        for (int r = 0; r < pattern.height; ++r)
        {
            final StringBuilder row = new StringBuilder();
            for (int c = 0; c < pattern.width; c++)
            {
                row.append(pattern.get(r * pattern.width + c) ? '#' : ' ');
            }
            array.add(row.toString());
        }
        return DataResult.success(new Prototype(array, pattern.height == MAX_HEIGHT && pattern.width == MAX_WIDTH ? Optional.empty() : Optional.of(pattern.defaultOn)));
    }


    private final int width;
    private final int height;
    private final boolean defaultOn;
    private long data; // on = 1, off = 0

    public LargeKnappingPattern()
    {
        this(MAX_WIDTH, MAX_HEIGHT, false);
    }

    public LargeKnappingPattern(int width, int height, boolean defaultOn)
    {
        this(width, height, 0xFFFFFFFFFFFFFFFFL >> (MAX_HEIGHT * MAX_WIDTH - width * height), defaultOn);
    }

    private LargeKnappingPattern(int width, int height, long data, boolean defaultOn)
    {
        this.width = width;
        this.height = height;
        this.data = data;
        this.defaultOn = defaultOn;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean defaultIsOn()
    {
        return defaultOn;
    }

    public void setAll(boolean value)
    {
        data = value ? 0xFFFFFFFFL >> (MAX_HEIGHT * MAX_WIDTH - width * height) : 0;
    }

    public void set(int x, int y, boolean value)
    {
        set(x + y * width, value);
    }

    public void set(long pattern)
    {
        data = pattern;
    }

    public void set(int index, boolean value)
    {
        assert index >= 0 && index < 64;
        if (value)
        {
            data |= 1L << index;
        }
        else
        {
            data &= ~(1L << index);
        }
    }

    public boolean get(int x, int y)
    {
        return get(x + y * width);
    }

    public boolean get(int index)
    {
        assert index >= 0 && index < 64;
        return ((data >> index) & 0b1) == 1;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (other instanceof LargeKnappingPattern pattern)
        {
            final long mask = 0xFFFFFFFFFFFFFFFFL >> (MAX_HEIGHT * MAX_WIDTH - width * height);
            return width == pattern.width && height == pattern.height && defaultOn == pattern.defaultOn && (data & mask) == (pattern.data & mask);
        }
        return false;
    }

    /**
     * Used to check if a craft matrix matches another one.
     *
     * @param other Another craft matrix
     * @return if 'other' is a subset of the current craft matrix (i.e. other is found somewhere within the current matrix)
     */
    public boolean matches(LargeKnappingPattern other)
    {
        // Check all possible shifted positions
        for (int dx = 0; dx <= this.width - other.width; dx++)
        {
            for (int dy = 0; dy <= this.height - other.height; dy++)
            {
                if (matches(other, dx, dy, false) || matches(other, dx, dy, true))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matches(LargeKnappingPattern other, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < this.width; x++)
        {
            for (int y = 0; y < this.height; y++)
            {
                int patternIdx = y * width + x;
                if (x < startX || y < startY || x - startX >= other.width || y - startY >= other.height)
                {
                    // If the current position in the matrix is outside the pattern, the value should be set by other.empty
                    if (get(patternIdx) != other.defaultOn)
                    {
                        return false;
                    }
                }
                else
                {
                    // Otherwise, the value must equal the value in the pattern
                    int otherIdx;
                    if (mirror)
                    {
                        otherIdx = (y - startY) * other.width + (other.width - 1 - (x - startX));
                    }
                    else
                    {
                        otherIdx = (y - startY) * other.width + (x - startX);
                    }

                    if (get(patternIdx) != other.get(otherIdx))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param defaultOn The value of {@code defaultOn}, or {@code Optional.empty()} to indicate the pattern is full-size and it should not be written
     */
    record Prototype(List<String> pattern, Optional<Boolean> defaultOn) {}
}
