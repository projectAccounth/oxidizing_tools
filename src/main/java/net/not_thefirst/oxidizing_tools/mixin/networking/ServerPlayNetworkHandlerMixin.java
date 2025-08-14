package net.not_thefirst.oxidizing_tools.mixin.networking;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.not_thefirst.oxidizing_tools.OxidizingTools;
import net.not_thefirst.oxidizing_tools.components.ItemIdHelper;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.registry.InMemoryTicks;
import net.not_thefirst.oxidizing_tools.ticking.OxidationTickHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Inject(method = "onPlayerLoaded", at = @At("TAIL"))
    private void onPlayerJoin(CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;

        PlayerInventory inv = player.getInventory();

        for (int slot = 0; slot < inv.size(); slot++) {
            ItemStack stack = inv.getStack(slot);
            if (!stack.isEmpty()) {
                String itemId = ItemIdHelper.ensureId(stack);

                if (stack.contains(ModComponents.HELD_TICKS)) {
                    int ticks = stack.get(ModComponents.HELD_TICKS);
                    InMemoryTicks.setTicks(player.getUuid(), itemId, ticks);

                    OxidizingTools.LOGGER.info("Loaded for stack id {} value {}", itemId, ticks);
                }
            }
        }
    }

    @Unique
    private int oxi_oldSlot = -1;

    @Inject(method = "onUpdateSelectedSlot", at = @At("HEAD"))
    private void captureOldSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        this.oxi_oldSlot = player.getInventory().selectedSlot; // capture before change
    }

    @Inject(method = "onUpdateSelectedSlot", at = @At("TAIL"))
    private void afterHotbarChange(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        PlayerInventory inv = player.getInventory();

        int oldSlot = this.oxi_oldSlot;
        int newSlot = inv.selectedSlot;

        if (oldSlot == newSlot) return;

        ItemStack oldStack = inv.getStack(oldSlot);
        ItemStack newStack = inv.getStack(newSlot);

        // Save old stack's ticks
        if (!oldStack.isEmpty() && OxidationTickHandler.isStackEligible(oldStack)) {
            String oldId = ItemIdHelper.ensureId(oldStack);

            // First write memory ticks to the stack itself
            int memTicks = InMemoryTicks.getOrCreateTicks(player.getUuid(), oldId);
            oldStack.set(ModComponents.HELD_TICKS, memTicks);

            // Keep memory in sync too (optional redundancy)
            InMemoryTicks.setTicks(player.getUuid(), oldId, memTicks);
        }

        // Restore new stack's ticks
        if (!newStack.isEmpty() && OxidationTickHandler.isStackEligible(newStack)) {
            String newId = ItemIdHelper.ensureId(newStack);
            int ticks = InMemoryTicks.getOrCreateTicks(player.getUuid(), newId);
            if (ticks > 0) {
                newStack.set(ModComponents.HELD_TICKS, ticks);
            }
        }
    }
}
