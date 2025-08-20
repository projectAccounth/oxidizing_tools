package net.not_thefirst.oxidizing_tools.mixin.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.not_thefirst.oxidizing_tools.components.CustomGrindstoneData;
import net.not_thefirst.oxidizing_tools.registry.OxidationRegistry;
import net.not_thefirst.oxidizing_tools.utilities.TickSaveUtils;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Inject(method = "onSlotClick", at = @At("HEAD"))
    private void beforeOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo cir) {
        if (player == null || player.getWorld().isClient) return;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        // Check slot validity
        if (slotIndex < 0) return;

        Slot slot = ((ScreenHandler)(Object)this).getSlot(slotIndex);
        if (slot == null) return;

        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) return;

        TickSaveUtils.saveStackTicksToComponent(serverPlayer, stack);
    }

    @Inject(method = "onButtonClick", at = @At("HEAD"))
    private void beforeOnButtonClick(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> ci) {
        if (player == null || player.getWorld().isClient) return;

        ScreenHandler handler = ((ScreenHandler)(Object)this);

        if (id == 0 && handler instanceof GrindstoneScreenHandler gHandler) {
            ((CustomGrindstoneData)(Object) gHandler).toggleCustomMode();
            ci.setReturnValue(true);
        }
    }
}
