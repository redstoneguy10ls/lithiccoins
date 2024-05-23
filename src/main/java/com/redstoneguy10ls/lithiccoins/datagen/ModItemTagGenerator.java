package com.redstoneguy10ls.lithiccoins.datagen;

import com.redstoneguy10ls.lithiccoins.LithicCoins;
import com.redstoneguy10ls.lithiccoins.common.items.ModItems;
import com.redstoneguy10ls.lithiccoins.common.items.coinMaterial;
import com.redstoneguy10ls.lithiccoins.common.items.stampTypes;
import com.redstoneguy10ls.lithiccoins.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider,
           CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, LithicCoins.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        for(stampTypes stamps : stampTypes.VALUES)
        {
            for(coinMaterial coins : coinMaterial.VALUES)
            {
                Item item = accept(ModItems.STAMPED_COINS.get(coins), stamps);
                this.tag(ModTags.Items.STAMPED_COINS)
                        .add(item);
            }
        }

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
