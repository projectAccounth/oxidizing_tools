package net.not_thefirst.oxidizing_tools.registry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryTicks {
    // Map: Player UUID -> Map<ItemID, heldTicks>
    private static final Map<UUID, Map<String, Integer>> MAP = new HashMap<>();

    public static Map<String, Integer> get(UUID id) {
        return MAP.get(id);
    }

    public static Map<String, Integer> computeIfAbsent(UUID id) {
        return MAP.computeIfAbsent(id, k -> new HashMap<>());
    }

    public static void remove(UUID id) {
        MAP.remove(id);
    }

    public static void removeStack(UUID id, String itemId) {
        MAP.getOrDefault(id, Map.of()).remove(itemId);
    }

    public static int getTicks(UUID id, String itemId) {
        return MAP.getOrDefault(id, Map.of()).getOrDefault(itemId, 0);
    }

    public static int getOrCreateTicks(UUID id, String itemId) {
        return MAP
            .computeIfAbsent(id, k -> new HashMap<>())
            .computeIfAbsent(itemId, k -> 0);
    }

    public static void setTicks(UUID id, String itemId, int ticks) {
        computeIfAbsent(id).put(itemId, ticks);
    }
}
