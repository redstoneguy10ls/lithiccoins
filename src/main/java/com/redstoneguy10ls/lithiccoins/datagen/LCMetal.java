package com.redstoneguy10ls.lithiccoins.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.redstoneguy10ls.lithiccoins.common.items.CoinMaterial;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.AndCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.OrCondition;

import net.dries007.tfc.util.Metal;

public enum LCMetal
{
    ALUMINUM(CoinMaterial.ALUMINUM, new MetalType("aluminum", "tfc_ie_addon", 1740f, 1), new MetalType("dfc_aluminum", "dfc", 1540f, 3)),
    BISMUTH(CoinMaterial.BISMUTH, Metal.BISMUTH, 270f, 1),
    BISMUTH_BRONZE(CoinMaterial.BISMUTH_BRONZE, Metal.BISMUTH_BRONZE, 985f, 2),
    BLACK_BRONZE(CoinMaterial.BLACK_BRONZE, Metal.BLACK_BRONZE, 1070f, 2),
    BLACK_STEEL(CoinMaterial.BLACK_STEEL, Metal.BLACK_STEEL, 1485f, 5),
    BLUE_STEEL(CoinMaterial.BLUE_STEEL, Metal.BLUE_STEEL, 1540f, 6),
    BRASS(CoinMaterial.BRASS, Metal.BRASS, 930f, 2),
    BRONZE(CoinMaterial.BRONZE, Metal.BRONZE, 950f, 2),
    CAST_IRON(CoinMaterial.CAST_IRON, Metal.CAST_IRON, 1535f, 1),
    CHROMIUM(CoinMaterial.CHROMIUM, new MetalType("chromium", "firmalife", 0f, 1)), //TODO: Temperature
    CONSTANTAN(CoinMaterial.CONSTANTAN, new MetalType("electrum", "tfc_ie_addon", 1250f, 2)),
    COPPER(CoinMaterial.COPPER, Metal.COPPER, 1080f, 1),
    ELECTRUM(CoinMaterial.ELECTRUM, new MetalType("electrum", "tfc_ie_addon", 1000f, 3)),
    GOLD(CoinMaterial.GOLD, Metal.GOLD, 1060f, 1),
    LEAD(CoinMaterial.LEAD, new MetalType("lead", "tfc_ie_addon", 330f, 2), new MetalType("dfc_lead", "dfc", 370f, 1)),
    NICKEL(CoinMaterial.NICKEL, Metal.NICKEL, 1453f, 1),
    RED_STEEL(CoinMaterial.RED_STEEL, Metal.RED_STEEL, 1540f, 6),
    ROSE_GOLD(CoinMaterial.ROSE_GOLD, Metal.ROSE_GOLD, 960f, 1),
    SILVER(CoinMaterial.SILVER, Metal.SILVER, 961f, 1),
    STAINLESS_STEEL(CoinMaterial.STAINLESS_STEEL, new MetalType("stainless_steel", "firmalife", 0f, 4)), //TODO: Temperature
    STEEL(CoinMaterial.STEEL, Metal.STEEL, 1540f, 4),
    STERLING_SILVER(CoinMaterial.STERLING_SILVER, Metal.STERLING_SILVER, 950f, 1),
    TIN(CoinMaterial.TIN, Metal.TIN, 230f, 1),
    URANIUM(CoinMaterial.URANIUM, new MetalType("uranium", "tfc_ie_addon", 1130f, 3)),
    WROUGHT_IRON(CoinMaterial.WROUGHT_IRON, Metal.WROUGHT_IRON, 1535f, 3),
    ZINC(CoinMaterial.ZINC, Metal.ZINC, 420f, 1);



    private final CoinMaterial material;
    private final List<MetalType> containedMetals;

    LCMetal(CoinMaterial material, MetalType metal)
    {
        this.material = material;
        this.containedMetals = List.of(metal);
    }

    LCMetal(CoinMaterial material, Metal metal, float meltingTemperature, int metalTier)
    {
        this.material = material;
        this.containedMetals = List.of(new MetalType(metal, meltingTemperature, metalTier));
    }

    LCMetal(CoinMaterial material, MetalType... containedMetals)
    {
        this.material = material;
        this.containedMetals = List.of(containedMetals);
    }


    public CoinMaterial material()
    {
        return this.material;
    }

    public List<MetalType> containedMetals()
    {
        return this.containedMetals;
    }

    /**
     * To ensure we don't load multiple recipes if multiple compatible mods are loaded, we use the natural order of the {@code MetalType}s in a given {@code LCMetal} to define a mod hierarchy
     *
     * @param modID The ID of a given mod
     * @return A nested condition ensuring the given modID is loaded, and all mods that are higher in the hierarchy than this mod are not loaded
     */
    public ICondition loadingCondition(String modID)
    {
        List<ICondition> notLoadedMods = new ArrayList<>();

        for (MetalType metalType : this.containedMetals)
        {
            if (metalType.modID.equals(modID)) break;
            notLoadedMods.add(new ModLoadedCondition(metalType.modID));
        }

        if (!notLoadedMods.isEmpty())
        {
            return new AndCondition(List.of(new NotCondition(new OrCondition(notLoadedMods)), new ModLoadedCondition(modID)));
        }
        else
        {
            return new ModLoadedCondition(modID);
        }
    }


    public record MetalType(Optional<Fluid> fluid, String modID, float meltingTemperature, int metalTier)
    {
        public MetalType(String name, String modID, float meltingTemperature, int metalTier)
        {
            this(BuiltInRegistries.FLUID.getOptional(ResourceLocation.fromNamespaceAndPath(modID, "metal/" + name)), modID, meltingTemperature, metalTier);
        }

        public MetalType(Metal metal, float meltingTemperature, int metalTier)
        {
            this(metal.getSerializedName(), "tfc", meltingTemperature, metalTier);
        }

    }
}
