package net.not_thefirst.oxidizing_tools.mixin.networking;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.not_thefirst.oxidizing_tools.components.ItemIdHelper;
import net.not_thefirst.oxidizing_tools.registry.InMemoryTicks;
import net.not_thefirst.oxidizing_tools.registry.OxidationRegistry;
import net.not_thefirst.oxidizing_tools.ticking.OxidationTickHandler;
import net.not_thefirst.oxidizing_tools.utilities.TickSaveUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onPlayerLogout(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        OxidationTickHandler.onPlayerLogout(player);
    }

    @Inject(method = "dropPlayerItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", 
        at = @At("HEAD"))
    private void beforeDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (!stack.isEmpty() && OxidationRegistry.isEligibleForward(stack)) {
            TickSaveUtils.saveStackTicksToComponent(((ServerPlayerEntity)(Object)this), stack);
            String id = ItemIdHelper.ensureId(stack);
            InMemoryTicks.removeStack(((ServerPlayerEntity)(Object)this).getUuid(), id);
        }
    }

}
