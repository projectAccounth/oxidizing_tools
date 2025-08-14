package net.not_thefirst.oxidizing_tools.mixin.player;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.not_thefirst.oxidizing_tools.OxidizingTools;
import net.not_thefirst.oxidizing_tools.components.ItemIdHelper;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.registry.InMemoryTicks;
import net.not_thefirst.oxidizing_tools.ticking.OxidationTickHandler;
import net.not_thefirst.oxidizing_tools.utilities.TickSaveUtils;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Unique
    private int insertSlot = -1;

    @Inject(method = "setStack", at = @At("HEAD"))
    private void beforeSetStack(int slot, ItemStack newStack, CallbackInfo ci) {
        PlayerInventory inv = (PlayerInventory)(Object)this;
        PlayerEntity player = inv.player;
        if (player == null || player.getWorld().isClient) return;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (!OxidationTickHandler.isStackEligible(newStack)) return;

        ItemStack oldStack = inv.getStack(slot);
        if (oldStack.isEmpty()) return;

        String oldId = ItemIdHelper.getId(oldStack);
        String newId = ItemIdHelper.getId(newStack);

        // Skip if it's actually the same stack/item
        if (Objects.equals(oldId, newId)) return;

        // Save ticks to component before removal
        TickSaveUtils.saveStackTicksToComponent(serverPlayer, oldStack);

        // Remove from memory
        if (oldId != null) {
            InMemoryTicks.removeStack(serverPlayer.getUuid(), oldId);
            OxidizingTools.LOGGER.info(
                "Stack with id {} will be replaced, value {}",
                oldId, oldStack.get(ModComponents.HELD_TICKS)
            );
        }
    }

    @Inject(method = "setStack", at = @At("TAIL"))
    private void afterSetStack(int slot, ItemStack newStack, CallbackInfo ci) {
        PlayerInventory inv = (PlayerInventory)(Object)this;
        PlayerEntity player = inv.player;
        if (player == null || player.getWorld().isClient) return;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        if (newStack.isEmpty()) return;

        // Load ticks from memory if they exist
        TickSaveUtils.loadFromStack(serverPlayer, newStack);
        OxidizingTools.LOGGER.info(
            "Stack with id {} got in, value {}",
            ItemIdHelper.getId(newStack),
            newStack.get(ModComponents.HELD_TICKS)
        );
    }

    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", 
            at = @At("HEAD"))
    private void beforeInsert(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = ((PlayerInventory)(Object)this).player;
        if (player.getWorld().isClient || stack.isEmpty() || !OxidationTickHandler.isStackEligible(stack)) return;

        // Preserve ticks before the stack gets copied
        String itemId = ItemIdHelper.ensureId(stack);
        InMemoryTicks.removeStack(player.getUuid(), itemId);

        insertSlot = slot;
        if (insertSlot == -1) insertSlot = ((PlayerInventory)(Object)this).getEmptySlot();        
        
        OxidizingTools.LOGGER.info("Pickup starting for stack id {}, saved ticks {}, slot {}", itemId, stack.get(ModComponents.HELD_TICKS), insertSlot);

        if (insertSlot >= 0) {
            TickSaveUtils.loadFromStack((ServerPlayerEntity)player, stack); 
            OxidizingTools.LOGGER.info(
                "Pickup finished for stack id {}, loaded ticks {}", 
                ItemIdHelper.getId(stack), stack.get(ModComponents.HELD_TICKS)
            ); 
        }
    }
}
