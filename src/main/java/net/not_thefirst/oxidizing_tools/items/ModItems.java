package net.not_thefirst.oxidizing_tools.items;
import net.minecraft.item.*;
import net.not_thefirst.oxidizing_tools.registry.ModToolMaterials;
import net.not_thefirst.oxidizing_tools.utilities.ItemUtils;

import java.util.*;
import java.util.function.BiFunction;

public class ModItems {
    private static final boolean USES_DEFAULT_NAMESPACE = true;

    public static final Map<Item, Item> NEXT_OXIDATION_STAGE = new HashMap<>();

    private static final String[] OXIDATION_STAGES = {
        "copper", "exposed_copper", "weathered_copper", "oxidized_copper"
    };

    private static final Object[][] TOOL_TYPES = {
            {"sword", (BiFunction<ToolMaterial, Item.Settings, Item>) (mat, settings) ->
                    new SwordItem(mat, 3.0F, -2.4F, settings)},
            {"pickaxe", (BiFunction<ToolMaterial, Item.Settings, Item>) (mat, settings) ->
                    new PickaxeItem(mat, 1.0F, -2.8F, settings )},
            {"axe", (BiFunction<ToolMaterial, Item.Settings, Item>) (mat, settings) ->
                    new AxeItem(mat, 7.0F, -3.2F, settings)},
            {"shovel", (BiFunction<ToolMaterial, Item.Settings, Item>) (mat, settings) ->
                    new ShovelItem(mat, 1.5F, -3.0F, settings )},
            {"hoe", (BiFunction<ToolMaterial, Item.Settings, Item>) (mat, settings) ->
                    new HoeItem(mat, -1.0F, -1.0F, settings)}
    };

    public static void initialize() {
        for (Object[] tool : TOOL_TYPES) {
            String type = (String) tool[0];
            @SuppressWarnings("unchecked")
            BiFunction<ToolMaterial, Item.Settings, Item> factory =
                    (BiFunction<ToolMaterial, Item.Settings, Item>) tool[1];

            Item prevItem = null;

            for (String stage : OXIDATION_STAGES) {
                String name = stage + "_" + type;
                Item current = ItemUtils.RegisterItem(name,
                        settings -> factory.apply(ModToolMaterials.COPPER_TOOL_MATERIAL, settings),
                        USES_DEFAULT_NAMESPACE);

                if (prevItem != null) {
                    NEXT_OXIDATION_STAGE.put(prevItem, current);
                }
                prevItem = current;
            }
        }
    }
}
