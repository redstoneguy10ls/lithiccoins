package com.redstoneguy10ls.lithiccoins.client;

import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import net.dries007.tfc.util.Helpers;

import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.Tooltips;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class CoinTooltip implements ClientTooltipComponent
{
    public static final Map<StampType, ResourceLocation> TEXTURE_LOCATION = Helpers.mapOf(StampType.class, type ->
        LCHelpers.identifier("textures/coin_preview/coin_" + type.name().toLowerCase() + "_preview.png"));


    private final Tooltips.CoinImageTooltip tooltip;

    public CoinTooltip(Tooltips.CoinImageTooltip tooltips)
    {
        this.tooltip = tooltips;
    }


    @Override
    public int getHeight()
    {
        return this.gridSizeY() * 12 + 2 + 4;
    }

    @Override
    public int getWidth(Font font)
    {
        return this.gridSizeX() * 18 + 2;
    }

    private StampType getType()
    {
        return tooltip.stamp();
    }


    private int gridSizeX()
    {
        return tooltip.width();
    }

    private int gridSizeY()
    {
        return tooltip.height();
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics graphics)
    {
        int maxX = this.gridSizeX();
        int maxY = this.gridSizeY();
        this.drawCoin(mouseX, mouseY, maxX, maxY, graphics);
    }

    private void drawCoin(int x, int y, int slotWidth, int slotHeight, GuiGraphics poseStack)
    {
        this.blit(poseStack, x,y, Texture.COIN);
    }


    private void blit(GuiGraphics graphics, int x, int y, Texture texture)
    {
        graphics.blit(TEXTURE_LOCATION.get(getType()), x, y, 0, (float) texture.x, (float) texture.y, texture.w, texture.h, 16, 16);
    }

    public enum Texture
    {
        COIN(0,0,16,16);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        Texture(int x, int y, int w, int h)
        {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
}
