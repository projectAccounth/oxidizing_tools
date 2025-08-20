package net.not_thefirst.oxidizing_tools.ticking;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.not_thefirst.oxidizing_tools.OxidizingTools;
import net.not_thefirst.oxidizing_tools.components.ItemIdHelper;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.registry.InMemoryTicks;
import net.not_thefirst.oxidizing_tools.registry.OxidationRegistry;
import net.not_thefirst.oxidizing_tools.utilities.ItemUtils;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

public class OxidationTickHandler {

    public static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            Map<String, Integer> playerTicks = InMemoryTicks.computeIfAbsent(player.getUuid());

            tickStack(player, player.getMainHandStack(), playerTicks, Hand.MAIN_HAND, -1);
            tickStack(player, player.getOffHandStack(), playerTicks, Hand.OFF_HAND, -1);

            for (int slot = 0; slot < player.getInventory().armor.size(); slot++) {
                ItemStack stack = player.getInventory().armor.get(slot);
                tickStack(player, stack, playerTicks, null, slot);
            }
        }
    }

    public static boolean isStackEligible(ItemStack stack) {
        return OxidationRegistry.isEligible(stack);
    }

    private static void tickStack(ServerPlayerEntity player, ItemStack stack, Map<String, Integer> playerTicks, @Nullable Hand hand, int armorSlot) {
        if (stack.isEmpty()) return;
        if (!OxidationRegistry.isEligibleForward(stack)) return;

        String itemId = ItemIdHelper.ensureId(stack);
        int ticks = playerTicks.getOrDefault(itemId, 0) + 1;
        playerTicks.put(itemId, ticks);

        if (ticks >= OxidationRegistry.STAGE_DURATION_TICKS) {
            ItemStack newStack = ItemUtils.SwapInPlace(
                stack,
                OxidationRegistry.getNext(stack.getItem())
            );
            newStack.set(ModComponents.HELD_TICKS, 0);
            ItemIdHelper.ensureId(newStack);

            // Replace depending on where it was
            if (hand != null) {
                player.setStackInHand(hand, newStack);
            } else if (armorSlot >= 0) {
                player.getInventory().armor.set(armorSlot, newStack);
            }
            playerTicks.put(ItemIdHelper.getId(newStack), 0);
        }
    }

    public static void saveAllTicksToNBT(ServerPlayerEntity player) {
        Map<String, Integer> playerTicks = InMemoryTicks.get(player.getUuid());
        if (playerTicks == null) return;

        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (stack.isEmpty() || !OxidationRegistry.isEligibleForward(stack)) continue;

            String itemId = ItemIdHelper.getId(stack);
            if (itemId == null) continue;

            int ticks = playerTicks.getOrDefault(itemId, 0);
            stack.set(ModComponents.HELD_TICKS, ticks);
            OxidizingTools.LOGGER.info("Saved for stack id {} value {}", itemId, ticks);
        }
    }

    public static void onPlayerLogout(ServerPlayerEntity player) {
        saveAllTicksToNBT(player);
        InMemoryTicks.remove(player.getUuid());
    }

    // Restore ticks from component into memory
    public static void loadTicksFromNBT(ServerPlayerEntity player) {
        Map<String, Integer> playerTicks = InMemoryTicks.computeIfAbsent(player.getUuid());
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (stack.isEmpty()) continue;

            String itemId = ItemIdHelper.ensureId(stack);
            int ticks = stack.contains(ModComponents.HELD_TICKS) 
                ? stack.get(ModComponents.HELD_TICKS) 
                : 0;
            playerTicks.put(itemId, ticks);
        }
    }
}
