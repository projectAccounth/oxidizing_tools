package net.not_thefirst.oxidizing_tools.components;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;

public class ItemIdHelper {

    /**
     * getOrCreate for IDs
     */
    public static String ensureId(ItemStack stack) {
        if (!stack.contains(ModComponents.ITEM_ID)) {
            String id = UUID.randomUUID().toString();
            stack.set(ModComponents.ITEM_ID, id);
            return id;
        }
        return stack.get(ModComponents.ITEM_ID);
    }

    /**
     * Returns the ID
     */
    public static @Nullable String getId(ItemStack stack) {
        return stack.contains(ModComponents.ITEM_ID) ? stack.get(ModComponents.ITEM_ID) : null;
    }

    /**
     * Generates a new ID for this stack, overriding existing
     */
    public static String regenerateId(ItemStack stack) {
        String id = UUID.randomUUID().toString();
        stack.set(ModComponents.ITEM_ID, id);
        return id;
    }
}
