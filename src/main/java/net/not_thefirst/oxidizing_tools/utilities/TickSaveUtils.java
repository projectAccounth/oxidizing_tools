package net.not_thefirst.oxidizing_tools.utilities;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.not_thefirst.oxidizing_tools.OxidizingTools;
import net.not_thefirst.oxidizing_tools.components.ItemIdHelper;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.registry.InMemoryTicks;

public final class TickSaveUtils {
    public static void saveStackTicksToComponent(ServerPlayerEntity player, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        // Ensure the stack has an item id so storage is stable
        String id = ItemIdHelper.ensureId(stack);

        // Read current in-memory ticks for this player+itemid (0 if none)
        int ticks = InMemoryTicks.getTicks(player.getUuid(), id);

        // Save into the stack's component so it travels with the ItemStack / ItemEntity
        stack.set(ModComponents.HELD_TICKS, ticks);

        OxidizingTools.LOGGER.info("Stack with id {} saved with value {}", id, ticks);
    }

    public static void loadFromStack(ServerPlayerEntity player, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        String id = ItemIdHelper.ensureId(stack);
        Integer ticks = stack.get(ModComponents.HELD_TICKS);

        if (ticks == null) {
            ticks = 0;
            stack.set(ModComponents.HELD_TICKS, 0);
        }
        
        InMemoryTicks.setTicks(player.getUuid(), id, ticks);
    }
}
