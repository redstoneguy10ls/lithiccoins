package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.items.StampTypes;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class CoinTooltip implements ClientTooltipComponent {

    public static final Map<StampTypes, ResourceLocation> TEXTURE_LOCATION = Helpers.mapOfKeys(StampTypes.class, type ->
            LCHelpers.identifier("textures/coin_preview/coin_"+type.name().toLowerCase()+"_preview.png"));

/*
    public static final ResourceLocation ANGLER = LCHelpers.identifier("textures/entity/coin_angler_preview.png");
    public static final ResourceLocation ARCHER = LCHelpers.identifier("textures/entity/coin_archer_preview.png");
    public static final ResourceLocation ARMS_UP = LCHelpers.identifier("textures/entity/coin_arms_up_preview.png");
    public static final ResourceLocation BLADE = LCHelpers.identifier("textures/entity/coin_blade_preview.png");
    public static final ResourceLocation BREWER = LCHelpers.identifier("textures/entity/coin_brewer_preview.png");
    public static final ResourceLocation BURN = LCHelpers.identifier("textures/entity/coin_burn_preview.png");
    public static final ResourceLocation DANGER = LCHelpers.identifier("textures/entity/coin_danger_preview.png");
    public static final ResourceLocation EAGLE = LCHelpers.identifier("textures/entity/coin_eagle_preview.png");
    public static final ResourceLocation EXPLORER = LCHelpers.identifier("textures/entity/coin_explorer_preview.png");
    public static final ResourceLocation FRIEND = LCHelpers.identifier("textures/entity/coin_friend_preview.png");
    public static final ResourceLocation HEART = LCHelpers.identifier("textures/entity/coin_heart_preview.png");
    public static final ResourceLocation HEARTBREAK = LCHelpers.identifier("textures/entity/coin_heartbreak_preview.png");
    public static final ResourceLocation HOWL = LCHelpers.identifier("textures/entity/coin_howl_preview.png");
    public static final ResourceLocation MINER = LCHelpers.identifier("textures/entity/coin_miner_preview.png");
    public static final ResourceLocation MOURNER = LCHelpers.identifier("textures/entity/coin_mourner_preview.png");
    public static final ResourceLocation PLENTY = LCHelpers.identifier("textures/entity/coin_plenty_preview.png");
    public static final ResourceLocation PRIZE = LCHelpers.identifier("textures/entity/coin_prize_preview.png");
    public static final ResourceLocation SHEAF = LCHelpers.identifier("textures/entity/coin_sheaf_preview.png");
    public static final ResourceLocation SHELTER = LCHelpers.identifier("textures/entity/coin_shelter_preview.png");
    public static final ResourceLocation SKULL = LCHelpers.identifier("textures/entity/coin_skull_preview.png");
    public static final ResourceLocation TRIFOIL = LCHelpers.identifier("textures/entity/coin_trifoil_preview.png");
*/
    //public static final ResourceLocation TEXTURE_LOCATION = LCHelpers.identifier("textures/entity/coin_angler_preview.png");

    private final Tooltips.CoinImageTooltip tooltip;

    public CoinTooltip(Tooltips.CoinImageTooltip tooltips) {
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
    private StampTypes getType()
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
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics graphics) {
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
        //graphics.blit(TEXTURE_LOCATION, x, y, 0, (float) texture.x, (float) texture.y, texture.w, texture.h, 16, 16);
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
