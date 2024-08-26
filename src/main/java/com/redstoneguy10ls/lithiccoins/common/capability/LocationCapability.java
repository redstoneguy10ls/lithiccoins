package com.redstoneguy10ls.lithiccoins.common.capability;


import com.redstoneguy10ls.lithiccoins.util.LCHelpers;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.Nullable;

public class LocationCapability {
    public static final Capability<ILocation> CAPABILITY = Helpers.capability(new CapabilityToken<>() {});

    public static final Capability<INetworkLocation> NETWORK_CAPABILITY = Helpers.capability(new CapabilityToken<>() {});

    public static final ResourceLocation KEY = LCHelpers.identifier("location");
    //public static final DataManager<LocationDefinition> MANAGER = new DataManager<>(Helpers.identifier("food_items"), "food", FoodDefinition::new, FoodDefinition::new, FoodDefinition::encode, FoodCapability.Packet::new);
    //public static final IndirectHashCollection<Item, FoodDefinition> CACHE = IndirectHashCollection.create(FoodDefinition::getValidItems, MANAGER::getValues);


    @Nullable
    public static ILocation get(ItemStack stack)
    {
        return Helpers.getCapability(stack, CAPABILITY);
    }

    public static boolean has(ItemStack stack)
    {
        return stack.getCapability(CAPABILITY).isPresent();
    }




/*
    public static void applyLocation(ILocation instance)
    {
        instance.setCreationLocation(CalculateCreationLocation(instance));
    }

    private static BlockPos CalculateCreationLocation(ILocation instance)
    {
        instance.
    }
*/


    /*
    public static int getCreationX()
    {
        new ChunkPos();
    }
*/
}
