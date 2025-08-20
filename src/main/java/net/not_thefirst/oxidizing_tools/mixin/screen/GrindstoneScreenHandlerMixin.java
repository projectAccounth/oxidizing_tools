package net.not_thefirst.oxidizing_tools.mixin.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.not_thefirst.oxidizing_tools.components.CustomGrindstoneData;
import net.not_thefirst.oxidizing_tools.registry.OxidationRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneScreenHandler.class)
public abstract class GrindstoneScreenHandlerMixin implements CustomGrindstoneData {

    @Shadow
    private Inventory input;

    @Shadow
    private Inventory result;

    @Unique
    private boolean customMode = false;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void injectUpdateResult(CallbackInfo ci) {
        if (customMode) {
            // Custom logic for updating the result
            ItemStack top = input.getStack(0);
            ItemStack bottom = input.getStack(1);
            result.setStack(0, getCustomOutputStack(top, bottom));
            ci.cancel(); // Prevent vanilla logic from running
        }
    }

    @Unique
    private ItemStack getCustomOutputStack(ItemStack top, ItemStack bottom) {
        if (top.isEmpty() || bottom.isEmpty()) return ItemStack.EMPTY;
        if (!isAxe(bottom)) return ItemStack.EMPTY;

        ItemStack output = ItemStack.EMPTY;
        
        if (OxidationRegistry.isOxidized(top)) {
            output = OxidationRegistry.getPrevious(top);
        } else if (OxidationRegistry.isWaxed(top)) {
            output = OxidationRegistry.getUnwaxed(top);
        }

        if (output.isDamageable()) {
            int newDamage = Math.min(output.getDamage() + 6, output.getMaxDamage());
            output.setDamage(newDamage);
        }

        return output;
    }

    @Unique
    private boolean isAxe(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }

    @Unique
    public void toggleCustomMode() {
        setCustomMode(!this.customMode);
    }

    @Unique
    public void setCustomMode(boolean mode) {
        this.customMode = mode;
        ((GrindstoneScreenHandler)(Object) this).onContentChanged(input);
    }

    @Unique
    public boolean isCustomMode() {
        return this.customMode;
    }
}