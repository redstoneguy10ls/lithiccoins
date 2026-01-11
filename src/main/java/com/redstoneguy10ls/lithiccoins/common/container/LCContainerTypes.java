package com.redstoneguy10ls.lithiccoins.common.container;

import net.dries007.tfc.common.container.ItemStackContainerProvider;
import net.dries007.tfc.util.data.KnappingType;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCContainerTypes
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MOD_ID);


    public static final Id<WaxKnappingContainer> WAX_KNAPPING = register("large_knapping", (windowId, playerInventory, buffer) -> {
        final KnappingType knappingType = KnappingType.MANAGER.getOrThrow(buffer.readResourceLocation());
        final ItemStackContainerProvider.Info info = ItemStackContainerProvider.read(buffer, playerInventory);
        return WaxKnappingContainer.create(info.stack(), knappingType, info.hand(), info.slot(), playerInventory, windowId);
    });



    private static <C extends AbstractContainerMenu> Id<C> register(String name, net.neoforged.neoforge.network.IContainerFactory<C> factory)
    {
        return new Id<>(RegistrationHelpers.registerContainer(CONTAINERS, name, factory));
    }

    public record Id<T extends AbstractContainerMenu>(DeferredHolder<MenuType<?>, MenuType<T>> holder)
        implements RegistryHolder<MenuType<?>, MenuType<T>> {}
}
