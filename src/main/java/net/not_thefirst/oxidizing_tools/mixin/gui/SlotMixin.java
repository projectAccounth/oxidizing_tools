package net.not_thefirst.oxidizing_tools.mixin.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.not_thefirst.oxidizing_tools.OxidizingTools;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.registry.InMemoryTicks;
import net.not_thefirst.oxidizing_tools.ticking.OxidationTickHandler;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        // Stack going OUT
        if (player instanceof ServerPlayerEntity && !player.getWorld().isClient && !stack.isEmpty() && OxidationTickHandler.isStackEligible(stack)) {
            InMemoryTicks.removeStack(player.getUuid(), stack.get(ModComponents.ITEM_ID));
            OxidizingTools.LOGGER.info("Stack id {} will go out of slot, value {}", stack.get(ModComponents.ITEM_ID), stack.get(ModComponents.HELD_TICKS));
        }
    }
}
