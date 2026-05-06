package com.redstoneguy10ls.lithiccoins.datagen;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import com.redstoneguy10ls.lithiccoins.common.blocks.LCBlocks;
import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.common.items.StampType;
import com.redstoneguy10ls.lithiccoins.datagen.builders.AnvilRecipeBuilder;
import com.redstoneguy10ls.lithiccoins.datagen.builders.CastingRecipeBuilder;
import com.redstoneguy10ls.lithiccoins.datagen.builders.HeatingRecipeBuilder;
import com.redstoneguy10ls.lithiccoins.datagen.builders.KnappingRecipeBuilder;
import com.redstoneguy10ls.lithiccoins.datagen.builders.MintingRecipeBuilder;
import com.redstoneguy10ls.lithiccoins.datagen.builders.LargeKnappingRecipeBuilder;
import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import com.redstoneguy10ls.lithiccoins.util.LargeKnappingPattern;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

import net.dries007.tfc.util.DataGenerationHelpers;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCRecipeProvider extends RecipeProvider implements IConditionBuilder
{
    public LCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput)
    {
        heatingRecipes(recipeOutput);
        craftingRecipes(recipeOutput);
        castingRecipes(recipeOutput);
        knappingRecipes(recipeOutput);
        anvilRecipes(recipeOutput);

        for (StampType type : StampType.values())
        {
            for (LCMetal metal : LCMetal.values())
            {
                for (LCMetal.MetalType metalType : metal.containedMetals())
                {
                    if (metalType.fluid().isPresent())
                    {
                        if (metalType.modID().equals("tfc"))
                        {
                            MintingRecipeBuilder.mint(type, Ingredient.of(LCItems.BLANK_COINS.get(metal.material())), metalType.metalTier(), LCItems.STAMPED_COINS.get(type).get(metal.material()))
                                .save(recipeOutput, MOD_ID + ":" + type.name().toLowerCase() + "/" + metal.name().toLowerCase());
                        }
                        else
                        {
                            MintingRecipeBuilder.mint(type, Ingredient.of(LCItems.BLANK_COINS.get(metal.material())), metalType.metalTier(), LCItems.STAMPED_COINS.get(type).get(metal.material()))
                                .save(recipeOutput, MOD_ID + ":" + type.name().toLowerCase() + "/" + metal.name().toLowerCase() + "_" + metalType.modID());
                        }
                    }
                }
            }

            LargeKnappingRecipeBuilder.waxKnapping(PATTERNS.get(type), LCItems.MOLDED_WAX.get(type).get())
                .save(recipeOutput, MOD_ID + ":wax_die/" + type.name().toLowerCase());
        }
    }


    private void heatingRecipes(RecipeOutput recipeOutput)
    {
        for (LCMetal metal : LCMetal.values())
        {
            for (LCMetal.MetalType metalType : metal.containedMetals())
            {
                if (metalType.fluid().isPresent())
                {
                    if (metalType.modID().equals("tfc"))
                    {
                        HeatingRecipeBuilder.heat(LCItems.BLANK_COINS.get(metal.material()), metalType.fluid().get(), 25, metalType.meltingTemperature(), false)
                            .save(recipeOutput, MOD_ID + ":coin/blank/" + metal.name().toLowerCase());

                        HeatingRecipeBuilder.heat(LCTags.Items.STAMPED_COIN_MAP.get(metal.material()), metalType.fluid().get(), 20, metalType.meltingTemperature(), false)
                            .save(recipeOutput, MOD_ID + ":coin/stamped/" + metal.name().toLowerCase());
                    }
                    else
                    {
                        HeatingRecipeBuilder.heat(LCItems.BLANK_COINS.get(metal.material()), metalType.fluid().get(), 25, metalType.meltingTemperature(), false)
                            .save(recipeOutput.withConditions(metal.loadingCondition(metalType.modID())), MOD_ID + ":coin/blank/" + metal.name().toLowerCase() + "_" + metalType.modID());

                        HeatingRecipeBuilder.heat(LCTags.Items.STAMPED_COIN_MAP.get(metal.material()), metalType.fluid().get(), 20, metalType.meltingTemperature(), false)
                            .save(recipeOutput.withConditions(metal.loadingCondition(metalType.modID())), MOD_ID + ":coin/stamped/" + metal.name().toLowerCase() + "_" + metalType.modID());
                    }
                }
            }
        }


        for (Metal metal : Metal.values())
        {
            if (LCHelpers.isToolMetal(metal))
            {
                HeatingRecipeBuilder.heat(LCItems.BOTTOM_DIE.get(metal), metal, 100, toolMetalTemperatureMap.get(metal), true)
                    .save(recipeOutput, MOD_ID + ":bottom_die/" + metal.name().toLowerCase());

                HeatingRecipeBuilder.heat(LCTags.Items.TOP_DIE_METAL_MAP.get(metal), metal, 100, toolMetalTemperatureMap.get(metal), true)
                    .save(recipeOutput, MOD_ID + ":top_die/" + metal.name().toLowerCase());
            }
        }

        for (StampType type : StampType.values())
        {
            HeatingRecipeBuilder.heat(LCItems.UNFIRED_DIE_MOLD.get(type), LCItems.FIRED_DIE_MOLD.get(type), 1399)
                .save(recipeOutput, MOD_ID + ":ceramic/die_mold/" + type.name().toLowerCase());
        }

        HeatingRecipeBuilder.heat(LCItems.DIE_HOLDER, Metal.BRASS, 100, 930, false)
            .save(recipeOutput, MOD_ID + ":die_holder");

        HeatingRecipeBuilder.heat(LCItems.STAMP_HOLDER, Metal.BRASS, 50, 930, false)
            .save(recipeOutput, MOD_ID + ":stamp_holder");

        HeatingRecipeBuilder.heat(LCItems.UNFIRED_COIN_MOLD, LCItems.COIN_MOLD, 1399)
            .save(recipeOutput, MOD_ID + ":unfired_coin_mold");

        HeatingRecipeBuilder.heat(LCItems.UNFIRED_FIRE_COIN_MOLD, LCItems.FIRE_COIN_MOLD, 1399)
            .save(recipeOutput, MOD_ID + ":unfired_fire_coin_mold");
    }

    private void craftingRecipes(RecipeOutput recipeOutput)
    {
        for (StampType type : StampType.values())
        {
            recipe(recipeOutput, "crafting/die_mold/unfired/" + type.name().toLowerCase())
                .input('C', Items.CLAY_BALL)
                .input('W', LCItems.MOLDED_WAX.get(type))
                .pattern("CCC", "CWC", "CCC")
                .shaped(LCItems.UNFIRED_DIE_MOLD.get(type), 1);
        }

        recipe(recipeOutput, "crafting/mint")
            .input('U', LCItems.STAMP_HOLDER)
            .input('S', Items.STICK)
            .input('L', LCItems.DIE_HOLDER)
            .input('R', TFCTags.Items.STONES_SMOOTH)
            .pattern(" U ", "SLS", "RRR")
            .shaped(LCBlocks.MINT.asItem(), 1);

        recipe(recipeOutput, "crafting/coin_purse")
            .input('C', TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "rods/copper")))
            .input('L', Tags.Items.LEATHERS)
            .input('S', Tags.Items.STRINGS)
            .pattern(" C ", "LSL", " L ")
            .shaped(LCItems.COIN_PURSE, 1);
    }

    private void castingRecipes(RecipeOutput recipeOutput)
    {
        for (LCMetal metal : LCMetal.values())
        {
            for (LCMetal.MetalType metalType : metal.containedMetals())
            {
                if (metalType.fluid().isPresent())
                {
                    if (metalType.modID().equals("tfc"))
                    {
                        CastingRecipeBuilder.cast(LCItems.COIN_MOLD, metalType.fluid().get(), 100, LCItems.BLANK_COINS.get(metal.material()), 4, 1f)
                            .save(recipeOutput, MOD_ID + ":blank_coin/mold/" + metal.name().toLowerCase());

                        CastingRecipeBuilder.cast(LCItems.FIRE_COIN_MOLD, metalType.fluid().get(), 100, LCItems.BLANK_COINS.get(metal.material()), 4, 1f)
                            .save(recipeOutput, MOD_ID + ":blank_coin/fire_mold/" + metal.name().toLowerCase());
                    }
                    else
                    {
                        CastingRecipeBuilder.cast(LCItems.COIN_MOLD, metalType.fluid().get(), 100, LCItems.BLANK_COINS.get(metal.material()), 4, 1f)
                            .save(recipeOutput.withConditions(modLoaded(metalType.modID())), MOD_ID + ":blank_coin/mold/" + metal.name().toLowerCase() + "_" + metalType.modID());

                        CastingRecipeBuilder.cast(LCItems.FIRE_COIN_MOLD, metalType.fluid().get(), 100, LCItems.BLANK_COINS.get(metal.material()), 4, 1f)
                            .save(recipeOutput.withConditions(modLoaded(metalType.modID())), MOD_ID + ":blank_coin/fire_mold/" + metal.name().toLowerCase() + "_" + metalType.modID());
                    }
                }
            }
        }

        for (StampType type : StampType.values())
        {
            for (Metal metal : Metal.values())
            {
                if (LCHelpers.isToolMetal(metal))
                {
                    CastingRecipeBuilder.cast(LCItems.FIRED_DIE_MOLD.get(type), metal, LCItems.TOP_DIE.get(type).get(metal), 1f)
                        .save(recipeOutput, MOD_ID + ":die_mold/" + type.name().toLowerCase() + "/" + metal.name().toLowerCase());
                }
            }
        }
    }

    private void knappingRecipes(RecipeOutput recipeOutput)
    {
        KnappingRecipeBuilder.clayKnapping(LCItems.UNFIRED_COIN_MOLD, "XXXXX", "X X X", "XXXXX", "X X X", "XXXXX")
            .save(recipeOutput, MOD_ID + ":unfired_coin_mold");

        KnappingRecipeBuilder.fireClayKnapping(LCItems.UNFIRED_FIRE_COIN_MOLD, "XXXXX", "X X X", "XXXXX", "X X X", "XXXXX")
            .save(recipeOutput, MOD_ID + ":unfired_fire_coin_mold");
    }

    private void anvilRecipes(RecipeOutput recipeOutput)
    {
        for (Metal metal : Metal.values())
        {
            if (LCHelpers.isToolMetal(metal))
            {
                AnvilRecipeBuilder.die(metal)
                    .save(recipeOutput);
            }
        }

        AnvilRecipeBuilder.anvil(Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "sheets/brass"))), Metal.BRASS.tier(), List.of(ForgeRule.PUNCH_ANY, ForgeRule.PUNCH_LAST), false, LCItems.DIE_HOLDER)
            .save(recipeOutput);

        AnvilRecipeBuilder.anvil(Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "rods/brass"))), Metal.BRASS.tier(), List.of(ForgeRule.BEND_THIRD_LAST, ForgeRule.BEND_SECOND_LAST, ForgeRule.HIT_LAST), false, LCItems.STAMP_HOLDER)
            .save(recipeOutput);
    }




    /**
     * @return A builder for a new recipe with a name inferred from the output.
     */
    private DataGenerationHelpers.Builder recipe(RecipeOutput output, String customName)
    {
        return new DataGenerationHelpers.Builder((name, recipe) -> output.accept(LCHelpers.identifier(Objects.requireNonNullElse(name, customName).toLowerCase(Locale.ROOT)), recipe, null));
    }

    private static final Map<StampType, LargeKnappingPattern> PATTERNS = Helpers.mapOf(StampType.class, type ->
        switch (type)
        {
            case ANGLER -> LargeKnappingPattern.from(false, "XXXXXXXX", "XXXXX  X", "XXXX   X", "XXX  X X", "XX  XX X", "X  XX   ", "X XXXX X", "XXXXXXXX");
            case ARCHER -> LargeKnappingPattern.from(false, "XXXXXXXX", "XXX XXXX", "XXXX XXX", " XXX X X", "X       ", " XXX X X", "XXXX XXX", "XXX XXXX");
            case ARMS_UP -> LargeKnappingPattern.from(false, "XXXXXXXX", "X X  X X", "X X  X X", "XX    XX", "XXX  XXX", "XXX  XXX", "XX XX XX", "XX XX XX");
            case BEE -> LargeKnappingPattern.from(false, "XXXX  XX", "XXX  XXX", " X     X", "X X X X ", "X X X X ", " X     X", "XXX  XXX", "XXXX  XX");
            case BLADE -> LargeKnappingPattern.from(false, "XXXXXXXX", "XXXXX  X", "XXXX   X", "X X   XX", "XX   XXX", "XX  XXXX", "X XX XXX", "XXXXXXXX");
            case BREWER -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX X  XX", "XXX XXXX", "XX XX XX", "X    X X", "X  XXX X", "XX    XX", "XXXXXXXX");
            case BURN -> LargeKnappingPattern.from(false, "XXXX XXX", "XXX XXXX", "X X  XXX", "XX X XXX", "X  XX  X", "X   XX X", "XX    XX", "XXXXXXXX");
            case BUST -> LargeKnappingPattern.from(false, "XXXXXXXX", "X X  X X", "XX    XX", " X X XX ", " X    X ", "XXX  XXX", "X      X", "XX    XX");
            case DANGER -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX   XXX", "XXX XXXX", "XX X XXX", "XXX  XXX", "XXX  X X", "XXX   XX", "XX XX XX");
            case EAGLE -> LargeKnappingPattern.from(false, "XXXXXXXX", "X X   XX", "  X  X  ", "        ", "        ", "  X  X  ", "X X  X X", "XX X  XX");
            case EXPLORER -> LargeKnappingPattern.from(false, "XXXXXXXX", "XXX    X", "   XXX X", "X   XX X", "X  XX  X", "X  X  X ", "XX  XXXX", "XXXXXXXX");
            case FACE -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX    XX", "X      X", "X       ", "XX     X", "XXX   XX", "X      X", "        ");
            case FRIEND -> LargeKnappingPattern.from(false, "XXXXXXXX", "X      X", "XXXXXXXX", "X      X", "X XXXX X", "X  XX  X", "X  XX  X", "XXXXXXXX");
            case HEART -> LargeKnappingPattern.from(false, "XXXXXXXX", "X  XX  X", "        ", "        ", "X      X", "XX    XX", "XXX  XXX", "XXXXXXXX");
            case HEARTBREAK -> LargeKnappingPattern.from(false, "XXXXXXXX", "X  XX  X", "   X    ", "    X   ", "X  X   X", "XX  X XX", "XXX  XXX", "XXXXXXXX");
            case HOWL -> LargeKnappingPattern.from(false, "XXXXXXXX", "XXXXX XX", "XXXXX  X", "        ", "XX    XX", "XX XX XX", "XX XX XX", "XXXXXXXX");
            case MINER -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX     X", "X  X X  ", "XXXX XXX", "XXXX XXX", "XXXX XXX", "XXXX XXX", "XXXX XXX");
            case MOURNER -> LargeKnappingPattern.from(false, "XX XX XX", "XX    XX", "XXX  XXX", "XX    XX", "X X X  X", "X  X X X", "XX    XX", "XX XX XX");
            case PLENTY -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX    XX", "X  XX XX", "X XX XXX", "XX     X", "XX  XX X", "XX     X", "XXXXXXXX");
            case PRIZE -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX     X", "X XXXX  ", "X   X X ", "XX X X X", "XXX   XX", "XXXX XXX", "XXXXXXXX");
            case PUNCHED -> LargeKnappingPattern.from(false, "XXXXXXXX", "XXXXXXXX", "XXX  XXX", "XX    XX", "XX    XX", "XXX  XXX", "XXXXXXXX", "XXXXXXXX");
            case SHEAF -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX  X XX", "XXX  X X", "X  X  XX", "XX  X  X", "XXX  XXX", "XX    XX", "XXXXXXXX");
            case SHELTER -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX    XX", "X      X", "XX X XXX", "XXX  XXX", "XXXX XXX", "XXXX XXX", "XXXXXXXX");
            case SKULL -> LargeKnappingPattern.from(false, "XXXXXXXX", "XX    XX", "X      X", "X      X", "X X  X X", "XX XX XX", "XXXXXXXX", "XX    XX");
            case SYMBOL -> LargeKnappingPattern.from(false, "XXXX  XX", "XX XXX X", "X XX XXX", "XXX X X ", "XXXX XXX", "XXX X XX", "X XXXX X", "XXX   XX");
            case TRIFOIL -> LargeKnappingPattern.from(false, "XXXX  XX", "XXX  X X", "X X XX X", " XX  XXX", " X    XX", "X   X  X", "XXXXXX X", "XXXX  XX");
        }
    );

    private static final Map<Metal, Integer> toolMetalTemperatureMap = Helpers.mapOf(Metal.class, metal ->
        switch (metal)
        {
            case COPPER -> 1080;
            case BRONZE -> 950;
            case BLACK_BRONZE -> 1070;
            case BISMUTH_BRONZE -> 985;
            case WROUGHT_IRON -> 1535;
            case STEEL, RED_STEEL, BLUE_STEEL -> 1540;
            case BLACK_STEEL -> 1485;
            default -> 0;
        }
    );
}
