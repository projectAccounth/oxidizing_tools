package net.not_thefirst.oxidizing_tools.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.not_thefirst.oxidizing_tools.items.ModItems;

public class OxidationRegistry {
    public static final Map<Item, Item> OXIDATION_CHAIN = new HashMap<>();
    public static final int STAGE_DURATION_TICKS = 20 * 60 * 3; // 3 mins

    public static void initialize() {
        OXIDATION_CHAIN.clear();
        OXIDATION_CHAIN.putAll(ModItems.NEXT_OXIDATION_STAGE);
    }
}
