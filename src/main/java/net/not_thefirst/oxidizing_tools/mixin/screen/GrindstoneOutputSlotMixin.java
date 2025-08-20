package net.not_thefirst.oxidizing_tools.mixin.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.WorldEvents;
import net.not_thefirst.oxidizing_tools.components.CustomGrindstoneData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.screen.GrindstoneScreenHandler$4") // the OUTPUT slot class
public abstract class GrindstoneOutputSlotMixin extends Slot {

    public GrindstoneOutputSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(method = "onTakeItem", at = @At("HEAD"), cancellable = true)
    private void onTakeItemCustom(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        ScreenHandler screenHandler = player.currentScreenHandler; // owning handler
        if (!(screenHandler instanceof GrindstoneScreenHandler handler)) return;
        if (((CustomGrindstoneData) handler).isCustomMode()) {
            // Custom "take result" logic
            Inventory input = ((GrindstoneScreenHandlerAccessor) handler).getInput();

            // consume slot 0 only
            input.setStack(0, ItemStack.EMPTY);

            ItemStack bottom = input.getStack(1);

            int newDamage = Math.min(input.getStack(1).getDamage() + 5, bottom.getMaxDamage());
            bottom.setDamage(newDamage);

            // optionally spawn XP + world event (copying vanilla behavior)
            ((GrindstoneScreenHandlerAccessor) handler).getContext().run((world, pos) -> {
                world.syncWorldEvent(WorldEvents.GRINDSTONE_USED, pos, 0);
            });

            ci.cancel(); // skip vanilla
        }
    }
}