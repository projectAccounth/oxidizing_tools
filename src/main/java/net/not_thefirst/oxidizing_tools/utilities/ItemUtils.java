package net.not_thefirst.oxidizing_tools.utilities;

import java.util.function.Function;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.not_thefirst.oxidizing_tools.OxidizingTools;
import net.not_thefirst.oxidizing_tools.components.ModComponents;

public class ItemUtils {
    private static final Item.Settings DEFAULT_SETTINGS = new Item.Settings();

    public static void IncrementHeldTicks(ItemStack stack) {
        int ticks = stack.getOrDefault(ModComponents.HELD_TICKS, 0);
        stack.set(ModComponents.HELD_TICKS, ticks + 1);
    }

    /**
    *   @brief Swaps the current ItemStack's Item with an another Item instance, while preserving the underlying data.
    *   Only the model and the translation name (name that is not custom) will be changed respectively.
    */
    public static ItemStack SwapInPlace(ItemStack oldStack, Item newItem) {
        ItemStack newStack = new ItemStack(newItem, oldStack.getCount());

        ComponentMap components = oldStack.getComponents();
        ComponentMap defaultComp = newItem.getComponents();

        for (ComponentType<?> type : components.getTypes()) {
            if (type == DataComponentTypes.ITEM_MODEL) continue;
            if (type == DataComponentTypes.CUSTOM_NAME && oldStack.getCustomName() == null) continue;

            @SuppressWarnings("unchecked")
            ComponentType<Object> objType = (ComponentType<Object>) type;
            Object value = components.get(objType);

            if (value != null) {
                newStack.set(objType, value);
            }
        }
        
        newStack.set(DataComponentTypes.ITEM_MODEL, defaultComp.get(DataComponentTypes.ITEM_MODEL));

        return newStack;
    }

    public static Item RegisterItem(String name, Function<Item.Settings, Item> itemFactory, boolean usesDefaultNamespace) {
        RegistryKey<Item> itemKey = RegistryKey.of(
            RegistryKeys.ITEM,
            Identifier.of(usesDefaultNamespace ? "minecraft" : OxidizingTools.MOD_ID,
            name
        ));
        Item item = itemFactory.apply(DEFAULT_SETTINGS.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

}
