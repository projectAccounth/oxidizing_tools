package net.not_thefirst.oxidizing_tools.mixin.item;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.not_thefirst.oxidizing_tools.components.ItemIdHelper;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.registry.InMemoryTicks;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {
    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void appendHeldTime(Item.TooltipContext tooltipContext,
                                @Nullable PlayerEntity player,
                                TooltipType tooltipType,
                                CallbackInfoReturnable<List<Text>> cir) {
        ItemStack self = (ItemStack)(Object)this;

        if (player == null) return;

        String itemId = ItemIdHelper.getId(self);
        int ticks = 0;

        if (itemId != null) {
            ticks = InMemoryTicks.getTicks(player.getUuid(), itemId);
        }

        // Fallback
        if (ticks == 0 && self.contains(ModComponents.HELD_TICKS)) {
            ticks = self.get(ModComponents.HELD_TICKS);
        }

        if (ticks > 0) {
            int seconds = ticks / 20;
            List<Text> lines = new ArrayList<>(cir.getReturnValue());
            lines.add(Text.literal("Held time: " + seconds + "s"));
            cir.setReturnValue(lines);
        }
    }
}
