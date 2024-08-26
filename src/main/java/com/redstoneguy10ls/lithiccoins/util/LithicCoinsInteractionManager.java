package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.container.WaxKnappingContainer;
import net.dries007.tfc.common.container.ItemStackContainerProvider;
import net.dries007.tfc.util.*;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

public final class LithicCoinsInteractionManager {

	public static void init() {
		InteractionManager.register(Ingredient.of(LCTags.Items.ANY_KNAPPING), false, true, (stack, context) -> {
			final Player player = context.getPlayer();
			if (player != null && context.getClickedPos().equals(BlockPos.ZERO)) {
				final KnappingType type = KnappingType.get(player);
				if (type != null) {
					if (player instanceof ServerPlayer serverPlayer) {
						final ItemStackContainerProvider provider = new ItemStackContainerProvider(
								(stack1, hand, slot, playerInventory, windowId) -> WaxKnappingContainer.create(stack1, type, hand, slot,
										playerInventory, windowId), Component.translatable("tfc.screen.knapping"));
						provider.openScreen(serverPlayer, context.getHand(), buffer -> buffer.writeResourceLocation(type.getId()));
					}
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		});
	}
}