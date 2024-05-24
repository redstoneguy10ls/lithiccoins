package com.redstoneguy10ls.lithiccoins.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.redstoneguy10ls.lithiccoins.common.items.TopDies;
import com.redstoneguy10ls.lithiccoins.common.items.stampTypes;
import net.dries007.tfc.common.recipes.ISimpleRecipe;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.common.recipes.inventory.EmptyInventory;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class MintingRecipe implements ISimpleRecipe<MintingRecipe.Inventory> {

    public static final IndirectHashCollection<Item, MintingRecipe> CACHE = IndirectHashCollection.createForRecipe(MintingRecipe::getValidItems, LCRecipeTypes.MINTING);

    @Nullable
    public static MintingRecipe getRecipe(Level world, ItemStackInventory wrapper,int style)
    {
        for (MintingRecipe recipe : CACHE.getAll(wrapper.getStack().getItem()))
        {
            if (recipe.match(wrapper, world))
            {
                if(recipe.matchDie(style))
                {
                    return recipe;
                }
            }
        }
        return null;
    }

    private final ResourceLocation id;
    private final TopDies[] topDie;
    private final Ingredient coin;
    private final int tier;
    private final ItemStackProvider output;


    public MintingRecipe(ResourceLocation id, TopDies[] topDie, Ingredient coin, int tier, ItemStackProvider output) {
        this.id = id;
        this.topDie = topDie;
        this.coin = coin;
        this.tier = tier;
        this.output = output;
    }
    public int getTier()
    {
        return tier;
    }

    public Collection<Item> getValidItems() {
        return Arrays.stream(this.getCoin().getItems()).map(ItemStack::getItem).collect(Collectors.toSet());
    }

    @Override
    public ItemStack getResultItem(@Nullable RegistryAccess access)
    {
        return output.getEmptyStack();
    }

    @Override
    public ResourceLocation getId() {return id;}

    @Override
    public RecipeSerializer<?> getSerializer(){return LCRecipeSerializers.MINTING.get();}

    @Override
    public RecipeType<?> getType() {return LCRecipeTypes.MINTING.get();}

    public Ingredient getCoin() {return coin;}
    public TopDies[] getTopDie() {return topDie;}

    @Override
    public boolean matches(Inventory inventory, Level Level) {
        return coin.test(inventory.getItem());
    }

    public boolean matchDie(int die)
    {
        if(die == this.topDie[0].getId())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public boolean match(ItemStackInventory wrapper, Level level)
    {
        return this.getCoin().test(wrapper.getStack());
    }

    @Override
    public ItemStack assemble(Inventory inventory, RegistryAccess registryAccess) {
        return output.getStack(inventory.getItem());
    }

    public interface Inventory extends EmptyInventory
    {
        ItemStack getItem();

        int getTier();
    }

    public static class Serializer extends RecipeSerializerImpl<MintingRecipe>
    {

        @Override
        public MintingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final JsonArray topJson = JsonHelpers.getAsJsonArray(json, "top_dies");
            final TopDies[] top_Dies = new TopDies[topJson.size()];
            for(int i = 0; i < top_Dies.length; i++)
            {
                top_Dies[i] = JsonHelpers.getEnum(topJson.get(i), TopDies.class);
            }

            final Ingredient coin = Ingredient.fromJson(JsonHelpers.get(json,"blank_coin"));
            final int tier = JsonHelpers.getAsInt(json, "tier", -1);
            final ItemStackProvider output = ItemStackProvider.fromJson(JsonHelpers.getAsJsonObject(json,"result"));
            return new MintingRecipe(recipeId,top_Dies,coin,tier,output);

        }

        @Override
        public @Nullable MintingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            final TopDies[] top_Dies = Helpers.decodeArray(buffer, TopDies[]::new, TopDies::fromNetwork);
            final Ingredient coin = Ingredient.fromNetwork(buffer);
            final int tier = buffer.readVarInt();
            final ItemStackProvider output = ItemStackProvider.fromNetwork(buffer);
            return new MintingRecipe(recipeId,top_Dies,coin,tier,output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MintingRecipe recipe) {
            Helpers.encodeArray(buffer, recipe.topDie, TopDies::toNetwork);
            recipe.coin.toNetwork(buffer);
            buffer.writeVarInt(recipe.tier);
            recipe.output.toNetwork(buffer);
        }
    }

}
