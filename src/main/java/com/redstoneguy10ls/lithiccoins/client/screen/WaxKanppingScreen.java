package com.redstoneguy10ls.lithiccoins.client.screen;

import com.redstoneguy10ls.lithiccoins.common.container.WaxKnappingContainer;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.LCKnappingPattern;
import net.dries007.tfc.client.screen.ScreenParticle;
import net.dries007.tfc.client.screen.TFCContainerScreen;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.KnappingType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WaxKanppingScreen extends TFCContainerScreen<WaxKnappingContainer> {
    public static final ResourceLocation BACKGROUND = Helpers.identifier("textures/gui/knapping.png");

    private final ResourceLocation buttonLocation;
    @Nullable
    private final ResourceLocation buttonDisabledLocation;
    private final List<ScreenParticle> particles = new ArrayList<>();

    public static ResourceLocation getHighTexture(ItemStack stack)
    {
        return getButtonLocation(stack.getItem(), false);
    }

    @Nullable
    public static ResourceLocation getLowTexture(KnappingType type, ItemStack stack)
    {
        return type.usesDisabledTexture() ? getButtonLocation(stack.getItem(), true) : null;
    }

    public static ResourceLocation getButtonLocation(Item item, boolean disabled)
    {
        ResourceLocation buttonAssetPath = ForgeRegistries.ITEMS.getKey(item);
        assert buttonAssetPath != null;
        return LCHelpers.identifier("textures/gui/knapping/" + buttonAssetPath.getPath() + (disabled ? "_disabled" : "") + ".png");
    }

    public WaxKanppingScreen(WaxKnappingContainer container, Inventory inv, Component name)
    {
        super(container, inv, name, BACKGROUND);
        imageHeight = 186;
        inventoryLabelY += 22;
        titleLabelY -= 2;

        final ItemStack stack = container.getOriginalStack();

        buttonLocation = getHighTexture(stack);
        buttonDisabledLocation = getLowTexture(container.getKnappingType(), stack);
    }
    @Override
    protected void init()
    {
        super.init();
        for(int x = 0; x < LCKnappingPattern.MAX_WIDTH; x++)
        {
            for(int y = 0; y < LCKnappingPattern.MAX_HEIGHT; y++)
            {
                int bx = (width - getXSize()) / 2 + 12 + 10 * x;
                int by = (height - getYSize()) / 2 + 12 + 10 * y;
                addRenderableWidget(new WaxKnappingButton(x + 8 * y, bx, by, 10, 10, buttonLocation, menu.getKnappingType().clickSound(), this::spawnParticles));
            }
        }
    }

    private void spawnParticles(Button button)
    {
        if(button instanceof WaxKnappingButton knappingButton && menu.getKnappingType().spawnsParticles() && TFCConfig.CLIENT.enableScreenParticles.get() && Minecraft.useFancyGraphics())
        {
            final RandomSource random = Minecraft.getInstance().font.random;
            final int amount = Mth.nextInt(random, 0, 3);
            for (int i = 0; i < amount; i++)
            {
                final var particle = new ScreenParticle(knappingButton.getTexture(), button.getX(), button.getY(), Mth.nextFloat(random, -0.1f, 0.1f), Mth.nextFloat(random, 1.2f, 1.5f), 16, 16, random);
                particles.add(particle);
            }
        }
    }

    @Override
    protected void containerTick()
    {
        super.containerTick();
        for (ScreenParticle particle : particles)
        {
            particle.tick();
        }
        particles.removeIf(ScreenParticle::shouldBeRemoved);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY)
    {
        // Check if the container has been updated
        if (menu.requiresReset())
        {
            for (Renderable widget : renderables)
            {
                if (widget instanceof WaxKnappingButton button)
                {
                    button.visible = menu.getPattern().get(button.id);
                }
            }
            menu.setRequiresReset(false);
        }

        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        for (Renderable widget : renderables)
        {
            if (widget instanceof WaxKnappingButton button)
            {
                if (button.visible) // Active button
                {
                    graphics.blit(buttonLocation, button.getX(), button.getY(), 0, 0, 10, 10, 10, 10);
                }
                else if (buttonDisabledLocation != null) // Disabled / background texture
                {
                    graphics.blit(buttonDisabledLocation, button.getX(), button.getY(), 0, 0, 10, 10, 10, 10);
                }
            }
        }
    }
    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        for (ScreenParticle particle : particles)
        {
            particle.render(poseStack);
        }
    }

    @Override
    public boolean mouseDragged(double x, double y, int clickType, double dragX, double dragY)
    {
        if (clickType == 0)
        {
            mouseClicked(x, y, clickType);
        }
        return super.mouseDragged(x, y, clickType, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int clickType)
    {
        if (clickType == 0)
        {
            undoAccidentalButtonPress(x, y);
        }
        return super.mouseClicked(x, y, clickType);
    }
    private void undoAccidentalButtonPress(double x, double y)
    {
        for (Renderable widget : renderables)
        {
            if (widget instanceof WaxKnappingButton button && button.isMouseOver(x, y))
            {
                menu.getPattern().set(button.id, true);
            }
        }
    }


}
