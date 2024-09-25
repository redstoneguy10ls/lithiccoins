package com.redstoneguy10ls.lithiccoins.util;

import com.google.common.base.Function;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.KnappingPattern;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;


public class LCKnappingPattern{

    /**
     * AYO big props to the tfc devs for this, as this is pretty
     * much all directly copied from them and their class
     * KnappingPattern
     */

    public static final int MAX_WIDTH = 8;
    public static final int MAX_HEIGHT = 8;


    public static @NotNull LCKnappingPattern fromJson(@NotNull JsonObject json)
    {
        final JsonArray array = json.getAsJsonArray("pattern");
        final boolean empty = GsonHelper.getAsBoolean(json, "outside_slot_required", true);

        final int height = array.size();
        if (height > MAX_HEIGHT) throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        if (height == 0) throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

        final int width = GsonHelper.convertToString(array.get(0), "pattern[ 0 ]").length();
        if (width > MAX_WIDTH) throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");

        final LCKnappingPattern pattern = new LCKnappingPattern(width, height, empty);
        for (int r = 0; r < height; ++r)
        {
            String row = GsonHelper.convertToString(array.get(r), "pattern[" + r + "]");
            if (r > 0 && width != row.length()) throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
            for (int c = 0; c < width; c++)
            {
                pattern.set(r * width + c, row.charAt(c) != ' ');
            }
        }
        return pattern;
    }

    public static LCKnappingPattern fromNetwork(FriendlyByteBuf buffer)
    {
        final boolean[] pat = new boolean[64];
        for(int i = 0; i < 64; i++)
        {
            pat[i] = buffer.readBoolean();

        }

        final int width = buffer.readVarInt();
        final int height = buffer.readVarInt();
        final boolean empty = buffer.readBoolean();
        return new LCKnappingPattern(width, height, pat, empty);
    }

    private final int width;
    private final int height;
    private final boolean empty;
    private int data; // on = 1, off = 0

    private boolean[] pat;

    public LCKnappingPattern(){this(MAX_WIDTH,MAX_HEIGHT, false);}

    public LCKnappingPattern(int width, int height, boolean empty)
    {

        this(width, height, new boolean[(height*width)], empty);
    }

    private LCKnappingPattern(int width, int height, boolean[] pat, boolean empty)
    {
        this.width = width;
        this.height = height;
        this.pat = pat;
        this.empty = empty;
    }
    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isOutsideSlotRequired()
    {
        return empty;
    }

    public void setAll(boolean value)
    {
        for(int i = 0; i < 64; i++)
        {
            pat[i] = value;

        }
        //data = value ? (1 << (width * height)) - 1 : 0;
    }

    public void sets(int x, int y, boolean value)
    {
        set(x + y * width, value);
    }

    public void set(int index, boolean value)
    {
        pat[index] = value;
        /*
        assert index >= 0 && index < 32;
        if (value)
        {
            data |= 1 << index;
        }
        else
        {
            data &= ~(1 << index);
        }

         */
    }

    public boolean get(int x, int y)
    {
        return get(x + y * width);
    }

    public boolean get(int index)
    {
        return pat[index];
        //assert index >= 0 && index < 32;
        //return ((data >> index) & 0b1) == 1;
    }
    public void toNetwork(@NotNull FriendlyByteBuf buffer)
    {
        for(int i = 0; i < 64; i++)
        {
            buffer.writeBoolean(pat[i]);
        }
        buffer.writeVarInt(width);
        buffer.writeVarInt(height);
        buffer.writeBoolean(empty);
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (other instanceof LCKnappingPattern p)
        {
            final int mask = (1 << (width * height)) - 1;
            return width == p.width && height == p.height && empty == p.empty && (data & mask) == (p.data & mask);
        }
        return false;
    }

    public boolean matches(@NotNull LCKnappingPattern other)
    {
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
    private boolean matches(LCKnappingPattern other, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < this.width; x++)
        {
            for (int y = 0; y < this.height; y++)
            {
                int patternIdx = y * width + x;
                if (x < startX || y < startY || x - startX >= other.width || y - startY >= other.height)
                {
                    // If the current position in the matrix is outside the pattern, the value should be set by other.empty
                    if (get(patternIdx) != other.empty)
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

}
