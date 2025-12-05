package com.redstoneguy10ls.lithiccoins.common.blocks;

import com.redstoneguy10ls.lithiccoins.common.blockentities.LCBlockEntities;
import com.redstoneguy10ls.lithiccoins.common.blockentities.MintBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;

import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);


    public static final Id<MintBlock> MINT = register("mint",
            () -> new MintBlock(ExtendedProperties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0F, 2.0F)
                    .sound(SoundType.BASALT)
                    .noOcclusion()
                    .blockEntity(LCBlockEntities.MINT)
                    .serverTicks(MintBlockEntity::serverTick)));



    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new Id<>(RegistrationHelpers.registerBlock(LCBlocks.BLOCKS, LCItems.ITEMS, name, blockSupplier, blockItemFactory));
    }

    public record Id<T extends Block>(DeferredHolder<Block, T> holder) implements RegistryHolder<Block, T>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get().asItem();
        }
    }
}
