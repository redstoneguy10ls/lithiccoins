package com.redstoneguy10ls.lithiccoins.common.recipes;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.redstoneguy10ls.lithiccoins.common.blockentities.MintBlockEntity;
import com.redstoneguy10ls.lithiccoins.common.items.StampType;

import net.dries007.tfc.common.recipes.ISimpleRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;

import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MintingRecipe implements ISimpleRecipe<MintBlockEntity.MintInventory>
{
    public static final MapCodec<MintingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        StampType.CODEC.fieldOf("stamp_type").forGetter(c -> c.type),
        Ingredient.CODEC.fieldOf("coin").forGetter(c -> c.coin),
        Codec.INT.optionalFieldOf("tier", 0).forGetter(c -> c.tier),
        ItemStackProvider.CODEC.fieldOf("output").forGetter(c -> c.output)
    ).apply(i, MintingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MintingRecipe> STREAM_CODEC = StreamCodec.composite(
        StampType.STREAM_CODEC, c -> c.type,
        Ingredient.CONTENTS_STREAM_CODEC, c -> c.coin,
        ByteBufCodecs.VAR_INT, c -> c.tier,
        ItemStackProvider.STREAM_CODEC, c -> c.output,
        MintingRecipe::new
    );

    public static final BiMap<ResourceLocation, MintingRecipe> CACHE = IndirectHashCollection.createForRecipeId(LCRecipeTypes.MINTING);

    @Nullable
    public static MintingRecipe byId(ResourceLocation id)
    {
        return CACHE.get(id);
    }

    @Nullable
    public static ResourceLocation getId(MintingRecipe recipe)
    {
        return CACHE.inverse().get(recipe);
    }


    private final StampType type;
    private final Ingredient coin;
    private final int tier;
    private final ItemStackProvider output;


    public MintingRecipe(StampType type, Ingredient coin, int tier, ItemStackProvider output)
    {
        this.type = type;
        this.coin = coin;
        this.tier = tier;
        this.output = output;
    }


    @Override
    public boolean matches(MintBlockEntity.MintInventory inventory, Level level)
    {
        return coin.test(inventory.getItem(MintBlockEntity.SLOT_COIN))
            && this.type == inventory.getType()
            && Helpers.isItem(inventory.getItem(MintBlockEntity.SLOT_BOTTOM_DIE), LCTags.Items.BOTTOM_DIE_TIER_MAP.get(this.tier))
            && Helpers.isItem(inventory.getItem(MintBlockEntity.SLOT_TOP_DIE), LCTags.Items.TOP_DIE_TIER_MAP.get(this.tier));
    }

    @Override
    public ItemStack assemble(MintBlockEntity.MintInventory inventory, HolderLookup.Provider provider)
    {
        return output.getSingleStack(inventory.getItem(MintBlockEntity.SLOT_COIN));
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return LCRecipeSerializers.MINTING.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return LCRecipeTypes.MINTING.get();
    }

    public Ingredient getCoin()
    {
        return this.coin;
    }

    public int getTier()
    {
        return this.tier;
    }

    public StampType getStampType()
    {
        return this.type;
    }

    @Override
    public ItemStack getResultItem(@Nullable HolderLookup.Provider registries)
    {
        return output.stack();
    }
}
