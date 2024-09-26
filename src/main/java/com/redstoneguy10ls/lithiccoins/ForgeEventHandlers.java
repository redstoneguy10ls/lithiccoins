package com.redstoneguy10ls.lithiccoins;


import com.redstoneguy10ls.lithiccoins.common.Capability.LocationCapability;
import com.redstoneguy10ls.lithiccoins.common.Capability.LocationHandler;
import com.redstoneguy10ls.lithiccoins.config.LithicConfig;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraftforge.eventbus.api.IEventBus;

public class ForgeEventHandlers {

    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addGenericListener(ItemStack.class, ForgeEventHandlers::attachItemCapabilities);
        bus.addListener(ForgeEventHandlers::onPlayerTick);
        //bus.addListener(ForgeEventHandlers::onServerTick);

    }





    private static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        final Player player = event.player;
        BlockPos blockPos = player.getOnPos();
        ChunkPos chunkPos = new ChunkPos(blockPos);

        Container container = player.getInventory();
            for (int i = 0; i < container.getContainerSize(); i++) {
                final ItemStack stack = container.getItem(i);
                if (!stack.isEmpty()) {
                    //System.out.println("get item in inventory using .getItem"+container.getItem(i));
                    stack.getCapability(LocationCapability.CAPABILITY).ifPresent(test ->
                    {
                        if (!test.getLocationSet()) {
                            test.setCreationLocation(chunkPos);
                            test.setCreationDate(Calendars.get().getTotalYears());
                            if(Helpers.getValueOrDefault(LithicConfig.SERVER.printName)) {
                                test.setName(player.getName().getString());
                            }

                        }
                    });
                }
            }


    }





    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        ItemStack stack = event.getObject();
        if (!stack.isEmpty())
        {

            if (Helpers.isItem(stack, LCTags.Items.STAMPED_COINS))
            {

                event.addCapability(LocationCapability.KEY, new LocationHandler(stack));

            }
        }
    }
}
