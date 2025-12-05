package com.redstoneguy10ls.lithiccoins;

import com.redstoneguy10ls.lithiccoins.common.container.WaxKnappingContainer;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

import net.dries007.tfc.common.container.ItemStackContainerProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.KnappingType;

public class ForgeEventHandler
{
    public static void init()
    {
        IEventBus bus = NeoForge.EVENT_BUS;

        bus.addListener(EventPriority.HIGH, ForgeEventHandler::onPlayerRightClickItem);
        bus.addListener(EventPriority.HIGH, ForgeEventHandler::onUseItemOnBlock);
    }

    private static InteractionResult ifKnappableOpenScreen(Player player, ItemStack stack, InteractionHand hand, Level level)
    {
        if (player != null)
        {
            final KnappingType type = KnappingType.get(stack);

            if (type != null && Helpers.isItem(stack, LCTags.Items.LARGE_KNAPPABLE))
            {
                if (player instanceof ServerPlayer serverPlayer)
                {
                    final ItemStackContainerProvider provider = new ItemStackContainerProvider((stack1, hand1, slot, playerInventory, windowId) -> WaxKnappingContainer.create(stack1, type, hand1, slot, playerInventory, windowId), Component.translatable("tfc.screen.knapping"));
                    provider.openScreen(serverPlayer, hand, buffer -> buffer.writeResourceLocation(KnappingType.MANAGER.getIdOrThrow(type)));
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        if (ifKnappableOpenScreen(event.getEntity(), event.getItemStack(), event.getHand(), event.getLevel()) != InteractionResult.PASS)
        {
            event.setCanceled(true);
        }
    }

    public static void onUseItemOnBlock(UseItemOnBlockEvent event)
    {
        if (ifKnappableOpenScreen(event.getPlayer(), event.getItemStack(), event.getHand(), event.getLevel()) != InteractionResult.PASS)
        {
            event.setCanceled(true);
        }
    }
}
