package com.redstoneguy10ls.lithiccoins.common.container;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.ItemStackContainerProvider;
import net.dries007.tfc.util.KnappingType;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCContainerTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);


    public static final RegistryObject<MenuType<WaxKnappingContainer>> WAX_KNAPPING = register("wax_knapping", (windowId, playerInventory, buffer) -> {
        final KnappingType knappingType = KnappingType.MANAGER.getOrThrow(buffer.readResourceLocation());
        final ItemStackContainerProvider.Info info = ItemStackContainerProvider.read(buffer, playerInventory);
        return WaxKnappingContainer.create(info.stack(), knappingType, info.hand(), info.slot(), playerInventory, windowId);
    });




    private static <T extends InventoryBlockEntity<?>, C extends BlockEntityContainer<T>> RegistryObject<MenuType<C>> registerBlock(String name, Supplier<BlockEntityType<T>> type, BlockEntityContainer.Factory<T, C> factory)
    {
        return RegistrationHelpers.registerBlockEntityContainer(CONTAINERS, name, type, factory);
    }
    private static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(String name, IContainerFactory<C> factory)
    {
        return RegistrationHelpers.registerContainer(CONTAINERS, name, factory);
    }

}
