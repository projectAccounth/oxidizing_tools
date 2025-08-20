package net.not_thefirst.oxidizing_tools.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.item.ItemStack;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.registry.OxidationRegistry;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract ItemStack copy();
    @Shadow public abstract boolean isEmpty();

    @Inject(method = "copy", at = @At("RETURN"))
    private void copyComponents(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack self = (ItemStack)(Object)this;
        if (!OxidationRegistry.isEligibleForward(self)) return;
        
        ItemStack copy = cir.getReturnValue();

        if (!OxidationRegistry.isEligibleForward(copy)) return;
        if (!self.isEmpty() && !copy.isEmpty()) {
            if (self.contains(ModComponents.ITEM_ID)) {
                copy.set(ModComponents.ITEM_ID, self.get(ModComponents.ITEM_ID));
            }
            if (self.contains(ModComponents.HELD_TICKS)) {
                copy.set(ModComponents.HELD_TICKS, self.get(ModComponents.HELD_TICKS));
            }
        }
    }

    @Inject(method = "split", at = @At("RETURN"))
    private void splitComponents(int amount, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack self = (ItemStack)(Object)this;
        if (!OxidationRegistry.isEligibleForward(self)) return;

        ItemStack split = cir.getReturnValue();

        if (!OxidationRegistry.isEligibleForward(split)) return;
        if (!self.isEmpty() && !split.isEmpty()) {
            if (self.contains(ModComponents.ITEM_ID)) {
                split.set(ModComponents.ITEM_ID, self.get(ModComponents.ITEM_ID));
            }
            if (self.contains(ModComponents.HELD_TICKS)) {
                split.set(ModComponents.HELD_TICKS, self.get(ModComponents.HELD_TICKS));
            }
        }
    }
}
