package com.redstoneguy10ls.lithiccoins.mixin;


import com.redstoneguy10ls.lithiccoins.common.Capability.ItemStackCapabilitySync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin {

    @Inject(method = "writeItemStack", at = @At("RETURN"), remap = false)
    private void writeSyncableCapabilityData(ItemStack stack,boolean limitedTag, CallbackInfoReturnable<FriendlyByteBuf> cir)
    {
        ItemStackCapabilitySync.writeToNetwork(stack, (FriendlyByteBuf) (Object) this);
    }
    @Inject(method = "readItem", at = @At("RETURN"))
    private void readSyncableCapabilityData(CallbackInfoReturnable<ItemStack> cir)
    {
        ItemStackCapabilitySync.readFromNetwork(cir.getReturnValue(), (FriendlyByteBuf) (Object) this);
    }
}
