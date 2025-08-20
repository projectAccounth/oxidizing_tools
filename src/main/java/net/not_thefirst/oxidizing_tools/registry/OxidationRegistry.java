package net.not_thefirst.oxidizing_tools.registry;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.not_thefirst.oxidizing_tools.items.ModItems;
import net.not_thefirst.oxidizing_tools.utilities.ItemUtils;

public class OxidationRegistry {
    public static final Map<Item, Item> FORWARD = new HashMap<>();
    public static final Map<Item, Item> BACKWARD = new HashMap<>();
    public static final Map<Item, Item> WAXED_LIST = new HashMap<>();
    public static final int STAGE_DURATION_TICKS = 20 * 60; // 1 min

    @Nullable
    public static ItemStack getUnwaxed(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        Item itemEntry =  WAXED_LIST.get(stack.getItem());
        if (itemEntry == null) return ItemStack.EMPTY;
        return ItemUtils.SwapInPlace(stack, itemEntry);
    }

    public static boolean isWaxed(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return WAXED_LIST.containsKey(stack.getItem());
    }

    public static boolean isOxidized(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return BACKWARD.containsKey(stack.getItem());
    }
    

    public static void registerChain(Item... chain) {
        for (int i = 0; i < chain.length - 1; i++) {
            Item curr = chain[i];
            Item next = chain[i + 1];
            FORWARD.put(curr, next);
            BACKWARD.put(next, curr);
        }
    }

    @Nullable
    public static Item getNext(Item item) {
        return FORWARD.get(item);
    }

    @Nullable
    public static Item getPrevious(Item item) {
        return BACKWARD.get(item);
    }

    @Nullable
    public static ItemStack getPrevious(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        Item itemEntry =  getPrevious(stack.getItem());
        if (itemEntry == null) return ItemStack.EMPTY;
        return ItemUtils.SwapInPlace(stack, itemEntry);
    }

    public static boolean isEligible(ItemStack stack) {
        return isEligibleBackward(stack) || isEligibleBackward(stack);
    }

    public static boolean isEligibleForward(ItemStack stack) {
        return FORWARD.containsKey(stack.getItem());
    }

    public static boolean isEligibleBackward(ItemStack stack) {
        return BACKWARD.containsKey(stack.getItem());
    }

    public static void initialize() {
        WAXED_LIST.clear();
        WAXED_LIST.putAll(ModItems.WAXED_ITEMS);
    }
}