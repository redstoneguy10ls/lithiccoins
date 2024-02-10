package com.redstoneguy10ls.lithiccoins.datagen;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import com.redstoneguy10ls.lithiccoins.items.ModItems;
import com.redstoneguy10ls.lithiccoins.items.coinMaterial;
import com.redstoneguy10ls.lithiccoins.items.stampTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LithicCoins.MOD_ID, existingFileHelper);
    }


    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        System.out.println(item.getId().getPath());
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LithicCoins.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder stampedCoins(Item item) {
        //System.out.println(item.getId().getPath());
        return withExistingParent(String.valueOf(item),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LithicCoins.MOD_ID,"item/" + item));
    }

    @Override
    protected void registerModels() {
        //simpleItem(ModItems.BLANK_COINS.get(coinMaterial.BISMUTH));

        for(stampTypes stamps : stampTypes.VALUES)
        {
            for(coinMaterial coins : coinMaterial.VALUES)
            {
                Item item = accept(ModItems.STAMPED_COINS.get(coins), stamps);
                stampedCoins(item);
            }
        }

        //ModItems.STAMPED_COINS.values().forEach(item -> );

        //ModItems.STAMPED_COINS.values().forEach(this::simpleItem);
        //ModItems.BLANK_COINS.values().forEach(item -> System.out.println(LithicCoins.MOD_ID + "item/" + item.getId().getPath()));

    }
    protected static <T extends ItemLike, R extends Supplier<T>, K> T accept(Map<K, R> map, K key)
    {

        if (map.containsKey(key))
        {
            //simpleItem((map.get(key).get()));
            //System.out.println((map.get(key).get()));
            return (map.get(key).get());
        }

        return null;
    }

}
