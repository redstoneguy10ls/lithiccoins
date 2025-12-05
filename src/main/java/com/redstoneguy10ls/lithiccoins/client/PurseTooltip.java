package com.redstoneguy10ls.lithiccoins.client;

import com.redstoneguy10ls.lithiccoins.common.component.PurseComponent;
import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * This is pretty much just taken from {@link ClientBundleTooltip}
 */
public class PurseTooltip implements ClientTooltipComponent
{
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
    private static final int MARGIN_Y = 4;
    private static final int BORDER_WIDTH = 1;
    private static final int SLOT_SIZE_X = 18;
    private static final int SLOT_SIZE_Y = 20;
    private final PurseComponent contents;

    public PurseTooltip(Tooltips.PurseImageTooltip tooltip)
    {
        this.contents = tooltip.contents();
    }

    public int getHeight()
    {
        return this.backgroundHeight() + MARGIN_Y;
    }

    public int getWidth(Font font)
    {
        return this.backgroundWidth();
    }

    private int backgroundWidth()
    {
        return this.gridSizeX() * SLOT_SIZE_X + 2 * BORDER_WIDTH;
    }

    private int backgroundHeight()
    {
        return this.gridSizeY() * SLOT_SIZE_Y + 2 * BORDER_WIDTH;
    }

    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics)
    {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
        boolean flag = contents.isFull();
        int k = 0;

        for (int l = 0; l < j; l++)
        {
            for(int i1 = 0; i1 < i; i1++)
            {
                int j1 = x + i1 * SLOT_SIZE_X + BORDER_WIDTH;
                int k1 = y + l * SLOT_SIZE_Y + BORDER_WIDTH;
                this.renderSlot(j1, k1, k++, flag, guiGraphics, font);
            }
        }

    }

    private void renderSlot(int x, int y, int itemIndex, boolean isPurseFull, GuiGraphics guiGraphics, Font font)
    {
        if (itemIndex >= this.contents.size())
        {
            this.blit(guiGraphics, x, y, isPurseFull ? Texture.BLOCKED_SLOT : Texture.SLOT);
        }
        else
        {
            ItemStack itemstack = this.contents.getItemUnsafe(itemIndex);
            this.blit(guiGraphics, x, y, Texture.SLOT);
            guiGraphics.renderItem(itemstack, x + BORDER_WIDTH, y + BORDER_WIDTH, itemIndex);
            guiGraphics.renderItemDecorations(font, itemstack, x + BORDER_WIDTH, y + BORDER_WIDTH);
            if (itemIndex == 0)
            {
                AbstractContainerScreen.renderSlotHighlight(guiGraphics, x + BORDER_WIDTH, y + BORDER_WIDTH, 0);
            }
        }

    }

    private void blit(GuiGraphics guiGraphics, int x, int y, Texture texture)
    {
        guiGraphics.blitSprite(texture.sprite, x, y, 0, texture.w, texture.h);
    }

    private int gridSizeX()
    {
        return Math.max(2, (int) Math.ceil(Math.sqrt((double) this.contents.size() + 1.0)));
    }

    private int gridSizeY()
    {
        return (int) Math.ceil(((double) this.contents.size() + 1.0) / (double) this.gridSizeX());
    }

    enum Texture
    {
        BLOCKED_SLOT(ResourceLocation.withDefaultNamespace("container/bundle/blocked_slot"), SLOT_SIZE_X, SLOT_SIZE_Y),
        SLOT(ResourceLocation.withDefaultNamespace("container/bundle/slot"), SLOT_SIZE_X, SLOT_SIZE_Y);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        Texture(ResourceLocation sprite, int w, int h)
        {
            this.sprite = sprite;
            this.w = w;
            this.h = h;
        }
    }
}
