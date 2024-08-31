package com.redstoneguy10ls.lithiccoins.common.items;

import com.redstoneguy10ls.lithiccoins.common.capability.LocationCapability;
import com.redstoneguy10ls.lithiccoins.common.misc.LCSounds;
import com.redstoneguy10ls.lithiccoins.config.LithicConfig;
import com.redstoneguy10ls.lithiccoins.util.LCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.openjdk.nashorn.internal.objects.NativeMath.floor;

public class CoinPurseItem extends Item {

    private static final String TAG_ITEMS = "Items";
    public static final int MAX_WEIGHT = 64 * Helpers.getValueOrDefault(LithicConfig.SERVER.numberOfStacksInCoinPurse);
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public CoinPurseItem(Properties pProperties) {
        super(pProperties);
    }

/*
    public static void getFullnessDisplay(ItemStack pStack) {
        final IPurse purse = pStack.getCapability(PurseCapability.CAPABILITY).resolve().orElse(null);
        if((getContentWeight(pStack) / MAX_WEIGHT) > 0)
        {
            purse.setHasItem(true);
        }
        else
        {
            purse.setHasItem(false);
        }
    }

 */

    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pStack.getCount() != 1 || pAction != ClickAction.SECONDARY) {
            return false;
        }
        else {
            ItemStack itemstack = pSlot.getItem();
            if (itemstack.isEmpty()) {
                this.playRemoveOneSound(pPlayer);
                removeOne(pStack).ifPresent((p_150740_) -> {
                    add(pStack, pSlot.safeInsert(p_150740_));
                });
            }
            else if (itemstack.getItem().canFitInsideContainerItems()) {
                itemstack.getCapability(LocationCapability.CAPABILITY).ifPresent(test ->
                        {
                            if(test.getLocationSet() && (Helpers.isItem(itemstack, LCTags.Items.FIT_IN_PURSE))) {
                                int i = (MAX_WEIGHT - getContentWeight(pStack)) / getWeight(itemstack);
                                int j = add(pStack, pSlot.safeTake(itemstack.getCount(), i, pPlayer));
                                if (j > 0) {
                                    this.playInsertSound(pPlayer);
                                }
                            }
                });
            }
            //getFullnessDisplay(pStack);
            return true;
        }
    }

    //for clicking on the purse with items
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (pStack.getCount() != 1) return false;
        if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            if (pOther.isEmpty()) {
                removeOne(pStack).ifPresent((coin) -> {
                    this.playRemoveOneSound(pPlayer);
                    pAccess.set(coin);
                });
            } else {
                int i = add(pStack, pOther);
                if (i > 0) {
                    this.playInsertSound(pPlayer);
                    pOther.shrink(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }
    
    //drops out all items when used on a block
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (dropContents(itemstack, pPlayer)) {
            this.playDropContentsSound(pPlayer);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public boolean isBarVisible(ItemStack pStack) {
        return getContentWeight(pStack) > 0;
    }

    public int getBarWidth(ItemStack pStack) {
        return Math.min(1 + 12 * getContentWeight(pStack) / MAX_WEIGHT, 13);
    }

    public int getBarColor(ItemStack pStack) {
        return BAR_COLOR;
    }

    //why is this called add? because mojang said it
    //but no like this is finding how much items are in the "bundle"
    //and reporting it back
    private static int add(ItemStack CoinStack, ItemStack pInsertedStack) {
        if (!pInsertedStack.isEmpty() && pInsertedStack.getItem().canFitInsideContainerItems() && (Helpers.isItem(pInsertedStack, LCTags.Items.FIT_IN_PURSE))) {
            CompoundTag nbt = CoinStack.getOrCreateTag();
            if (!nbt.contains("Items")) {
                nbt.put("Items", new ListTag());
            }

            int coinsInPurse = getContentWeight(CoinStack);
            int j = getWeight(pInsertedStack);//weight of stack being added

            int k = Math.min(pInsertedStack.getCount(), (MAX_WEIGHT - coinsInPurse) / j);
            if (k == 0) {
                return 0;
            } else {
                ListTag listtag = nbt.getList("Items", 10);


                Optional<CompoundTag> optional = getMatchingItem(pInsertedStack, listtag);

                if (optional.isPresent()) {
                    CompoundTag nbt1 = optional.get();
                    ItemStack itemstack = ItemStack.of(nbt1);
                    if(itemstack.getCount() >= 64)
                    {
                        ItemStack itemstack1 = pInsertedStack.copyWithCount(k);
                        CompoundTag nbt2 = new CompoundTag();
                        itemstack1.save(nbt2);
                        listtag.add(0, (Tag)nbt2);
                    }else {
                        itemstack.grow(k);
                        /*
                    ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(itemstack.getItem());
                    nbt1.putString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
                    nbt1.putInt("Count", itemstack.getCount());

                    if (itemstack.getTag() != null) {
                        nbt1.put("tag", itemstack.getTag().copy());
                    }

                    CompoundTag cnbt = itemstack.serializeNBT();
                    if (cnbt != null && !cnbt.isEmpty()) {
                        nbt1.put("ForgeCaps", cnbt);
                    }

                 */
                        itemstack.save(nbt1);
                        listtag.remove(nbt1);
                        listtag.add(0, (Tag)nbt1);
                    }
                } else {
                    ItemStack itemstack1 = pInsertedStack.copyWithCount(k);
                    CompoundTag nbt2 = new CompoundTag();
                    /*
                    ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(itemstack1.getItem());
                    nbt2.putString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
                    nbt2.putInt("Count", itemstack1.getCount());
                    if (itemstack1.getTag() != null) {
                        nbt2.put("tag", itemstack1.getTag().copy());
                    }

                    CompoundTag cnbt = itemstack1.serializeNBT();
                    if (cnbt != null && !cnbt.isEmpty()) {
                        nbt2.put("ForgeCaps", cnbt);
                    }

                    */
                    itemstack1.save(nbt2);
                    listtag.add(0, (Tag)nbt2);
                }

                return k;
            }
        } else {
            return 0;
        }
    }




    private static Optional<CompoundTag> getMatchingItem(ItemStack pStack, ListTag pList) {
        return pStack.is(LCItems.COIN_PURSE.get()) ? Optional.empty() : pList.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((p_186350_) -> {
            return ItemStack.isSameItemSameTags(ItemStack.of(p_186350_), pStack);
        }).findFirst();
    }

    private static int getWeight(ItemStack pStack) {
        if (pStack.is(LCItems.COIN_PURSE.get())) {
            return 4 + getContentWeight(pStack);
        } else {
            return 64 / pStack.getMaxStackSize();
        }
    }

    private static int getContentWeight(ItemStack pStack) {
        return getContents(pStack).mapToInt((coins) -> {
            return getWeight(coins) * coins.getCount();
        }).sum();
    }

    private static Stream<ItemStack> getContents(ItemStack pStack) {
        CompoundTag nbt = pStack.getTag();
        if (nbt == null) {
            return Stream.empty();
        } else {
            ListTag listtag = nbt.getList("Items", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    private static Optional<ItemStack> removeOne(ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        if (!nbt.contains("Items")) {
            return Optional.empty();
        }
        else {
            ListTag listtag = nbt.getList("Items", 10);
            if (listtag.isEmpty()) {
                return Optional.empty();
            }
            else {
                int i = 0;
                CompoundTag nbt1 = listtag.getCompound(0);//the last item added to the purse
                ItemStack itemstack = ItemStack.of(nbt1);//converting to item stack
                if(itemstack.getCount() >64)
                {
                    itemstack.shrink(64);
                    nbt1.putInt("Count", 64);
                    listtag.setTag(0,nbt1);
                }
                else{
                    listtag.remove(0);
                }
                if (listtag.isEmpty()) {
                    pStack.removeTagKey("Items");
                }

                return Optional.of(itemstack);
            }
        }
    }

    private static boolean dropContents(ItemStack pStack, Player pPlayer) {
        CompoundTag nbt = pStack.getOrCreateTag();
        if (!nbt.contains("Items")) {
            return false;
        } else {
            if (pPlayer instanceof ServerPlayer) {
                ListTag listtag = nbt.getList("Items", 10);

                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag nbt1 = listtag.getCompound(i);
                    ItemStack itemstack = ItemStack.of(nbt1);
                    pPlayer.drop(itemstack, true);
                }
            }

            pStack.removeTagKey("Items");
            return true;
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(pStack).forEach(nonnulllist::add);

        //final VesselLike coin = VesselLike.get(pStack);

        //int x = (int)Math.ceil(0.5*Helpers.getValueOrDefault(LithicConfig.SERVER.numberOfStacksInCoinPurse));

        //return Helpers.getTooltipImage(coin,x,x,0,Integer.MAX_VALUE);
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(pStack)));


    }



    /**
     * Allows items to add custom lines of information to the mouseover description.
     */
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(pStack), MAX_WEIGHT).withStyle(ChatFormatting.GRAY));
    }

    public void onDestroyed(ItemEntity pItemEntity) {
        ItemUtils.onContainerDestroyed(pItemEntity, getContents(pItemEntity.getItem()));
    }


    private void playRemoveOneSound(Entity pEntity) {

            pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);

    }

    private void playInsertSound(Entity pEntity) {
        if(getWeight(new ItemStack(this.asItem())) == 0)
        {
            pEntity.playSound(LCSounds.COINPURSE_EMPTY_ADD.get(), 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
        }
        else {
            pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);

        }
    }

    private void playDropContentsSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }
/*
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new PurseHandler(stack);
    }

 */

}
