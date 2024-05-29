package com.redstoneguy10ls.lithiccoins.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.screen.button.KnappingButton;
import net.dries007.tfc.network.PacketHandler;
import net.dries007.tfc.network.ScreenButtonPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.PacketDistributor;

public class WaxKnappingButton extends Button {

    public int id;
    private final ResourceLocation texture;
    private final SoundEvent sound;
    public WaxKnappingButton(int id, int x, int y, int width, int height, ResourceLocation texture, SoundEvent sound)
    {
        this(id, x, y, width, height, texture, sound, button -> {});
    }

    public WaxKnappingButton(int id, int x, int y, int width, int height, ResourceLocation texture, SoundEvent sound, OnPress onPress)
    {
        super(x, y, width, height, Component.empty(), onPress, RenderHelpers.NARRATION);
        this.id = id;
        this.texture = texture;
        this.sound = sound;
    }
    @Override
    public void onPress()
    {
        onPress.onPress(this);
        if (active)
        {
            visible = false;
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new ScreenButtonPacket(id, null));
            playDownSound(Minecraft.getInstance().getSoundManager());
        }
    }

    @Override
    public void playDownSound(SoundManager handler)
    {
        handler.play(SimpleSoundInstance.forUI(sound, 1.0F));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        if (visible)
        {
            int x = getX();
            int y = getY();
            RenderSystem.setShaderTexture(0, texture);
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

            graphics.blit(texture, x, y, 0, 0, 10, 10, 10, 10);
        }
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }
}
