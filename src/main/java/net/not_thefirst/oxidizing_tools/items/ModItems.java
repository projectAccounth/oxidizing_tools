package net.not_thefirst.oxidizing_tools.items;
import net.minecraft.item.*;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.not_thefirst.oxidizing_tools.registry.ModToolMaterials;
import net.not_thefirst.oxidizing_tools.registry.OxidationRegistry;
import net.not_thefirst.oxidizing_tools.utilities.ItemUtils;

import java.util.*;
import java.util.function.BiFunction;

public class ModItems {
    private static final boolean USES_DEFAULT_NAMESPACE = true;
    public static final Map<Item, Item> WAXED_ITEMS = new HashMap<>();

    public static final Item COPPER_AXE = registerAxeItem("copper");
    public static final Item EXPOSED_COPPER_AXE = registerAxeItem("exposed_copper");
    public static final Item WEATHERED_COPPER_AXE = registerAxeItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_AXE = registerAxeItem("oxidized_copper");

    public static final Item COPPER_PICKAXE = registerPickaxeItem("copper");
    public static final Item EXPOSED_COPPER_PICKAXE = registerPickaxeItem("exposed_copper");
    public static final Item WEATHERED_COPPER_PICKAXE = registerPickaxeItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_PICKAXE = registerPickaxeItem("oxidized_copper");

    public static final Item COPPER_SWORD = registerSwordItem("copper");
    public static final Item EXPOSED_COPPER_SWORD = registerSwordItem("exposed_copper");
    public static final Item WEATHERED_COPPER_SWORD = registerSwordItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_SWORD = registerSwordItem("oxidized_copper");

    public static final Item COPPER_SHOVEL = registerShovelItem("copper");
    public static final Item EXPOSED_COPPER_SHOVEL = registerShovelItem("exposed_copper");
    public static final Item WEATHERED_COPPER_SHOVEL = registerShovelItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_SHOVEL = registerShovelItem("oxidized_copper");

    public static final Item COPPER_HOE = registerHoeItem("copper");
    public static final Item EXPOSED_COPPER_HOE = registerHoeItem("exposed_copper");
    public static final Item WEATHERED_COPPER_HOE = registerHoeItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_HOE = registerHoeItem("oxidized_copper");

    public static final Item COPPER_HELMET = registerHelmetItem("copper");
    public static final Item EXPOSED_COPPER_HELMET = registerHelmetItem("exposed_copper");
    public static final Item WEATHERED_COPPER_HELMET = registerHelmetItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_HELMET = registerHelmetItem("oxidized_copper");

    public static final Item COPPER_CHESTPLATE = registerChestplateItem("copper");
    public static final Item EXPOSED_COPPER_CHESTPLATE = registerChestplateItem("exposed_copper");
    public static final Item WEATHERED_COPPER_CHESTPLATE = registerChestplateItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_CHESTPLATE = registerChestplateItem("oxidized_copper");

    public static final Item COPPER_LEGGINGS = registerLeggingsItem("copper");
    public static final Item EXPOSED_COPPER_LEGGINGS = registerLeggingsItem("exposed_copper");
    public static final Item WEATHERED_COPPER_LEGGINGS = registerLeggingsItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_LEGGINGS = registerLeggingsItem("oxidized_copper");

    public static final Item COPPER_BOOTS = registerBootsItem("copper");
    public static final Item EXPOSED_COPPER_BOOTS = registerBootsItem("exposed_copper");
    public static final Item WEATHERED_COPPER_BOOTS = registerBootsItem("weathered_copper");
    public static final Item OXIDIZED_COPPER_BOOTS = registerBootsItem("oxidized_copper");

    // === Helpers for registering tool types ===
    private static Item registerSwordItem(String stage) {
        return registerTool(stage, "sword", 
            (mat, settings) -> new SwordItem(mat, 3.0F, -2.4F, settings));
    }

    private static Item registerPickaxeItem(String stage) {
        return registerTool(stage, "pickaxe", 
            (mat, settings) -> new PickaxeItem(mat, 1.0F, -2.8F, settings));
    }

    private static Item registerAxeItem(String stage) {
        return registerTool(stage, "axe", 
            (mat, settings) -> new AxeItem(mat, 7.0F, -3.2F, settings));
    }

    private static Item registerShovelItem(String stage) {
        return registerTool(stage, "shovel", 
            (mat, settings) -> new ShovelItem(mat, 1.5F, -3.0F, settings));
    }

    private static Item registerHoeItem(String stage) {
        return registerTool(stage, "hoe", 
            (mat, settings) -> new HoeItem(mat, -1.0F, -1.0F, settings));
    }

    private static Item registerHelmetItem(String stage) {
        return registerArmor(stage, "helmet", 
            (mat, settings) -> new ArmorItem(ModToolMaterials.COPPER_ARMOR_MATERIAL, EquipmentType.HELMET, settings));
    }


    private static Item registerChestplateItem(String stage) {
        return registerArmor(stage, "chestplate", 
            (mat, settings) -> new ArmorItem(ModToolMaterials.COPPER_ARMOR_MATERIAL, EquipmentType.CHESTPLATE, settings));
    }


    private static Item registerLeggingsItem(String stage) {
        return registerArmor(stage, "leggings", 
            (mat, settings) -> new ArmorItem(ModToolMaterials.COPPER_ARMOR_MATERIAL, EquipmentType.LEGGINGS, settings));
    }


    private static Item registerBootsItem(String stage) {
        return registerArmor(stage, "boots", 
            (mat, settings) -> new ArmorItem(ModToolMaterials.COPPER_ARMOR_MATERIAL, EquipmentType.BOOTS, settings));
    }


    // === Core registration logic ===
    private static Item registerTool(String stage, String type, 
                                     BiFunction<ToolMaterial, Item.Settings, Item> factory) {
        String baseName = stage + "_" + type;

        // main oxidizable version
        Item normal = ItemUtils.RegisterItem(baseName,
            settings -> factory.apply(ModToolMaterials.COPPER_TOOL_MATERIAL, settings),
            USES_DEFAULT_NAMESPACE);

        // waxed counterpart (not in chain)
        Item waxed = ItemUtils.RegisterItem("waxed_" + baseName,
            settings -> factory.apply(ModToolMaterials.COPPER_TOOL_MATERIAL, settings),
            USES_DEFAULT_NAMESPACE);

        WAXED_ITEMS.put(waxed, normal);
        return normal;
    }

    private static Item registerArmor(String stage, String type,
                                      BiFunction<ArmorMaterial, Item.Settings, Item> factory) {
                            
        String baseName = stage + "_" + type;
        Item normal = ItemUtils.RegisterItem(baseName, 
            settings -> factory.apply(ModToolMaterials.COPPER_ARMOR_MATERIAL, settings), 
            USES_DEFAULT_NAMESPACE);
        
        Item waxed = ItemUtils.RegisterItem("waxed_" + baseName,
            settings -> factory.apply(ModToolMaterials.COPPER_ARMOR_MATERIAL, settings),
            USES_DEFAULT_NAMESPACE);
        
        WAXED_ITEMS.put(waxed, normal);
        return normal;
    }

    // === Called in ModInitializer ===
    public static void initialize() {
        OxidationRegistry.registerChain(
            COPPER_AXE, EXPOSED_COPPER_AXE, WEATHERED_COPPER_AXE, OXIDIZED_COPPER_AXE
        );
        OxidationRegistry.registerChain(
            COPPER_PICKAXE, EXPOSED_COPPER_PICKAXE, WEATHERED_COPPER_PICKAXE, OXIDIZED_COPPER_PICKAXE
        );
        OxidationRegistry.registerChain(
            COPPER_SWORD, EXPOSED_COPPER_SWORD, WEATHERED_COPPER_SWORD, OXIDIZED_COPPER_SWORD
        );
        OxidationRegistry.registerChain(
            COPPER_SHOVEL, EXPOSED_COPPER_SHOVEL, WEATHERED_COPPER_SHOVEL, OXIDIZED_COPPER_SHOVEL
        );
        OxidationRegistry.registerChain(
            COPPER_HOE, EXPOSED_COPPER_HOE, WEATHERED_COPPER_HOE, OXIDIZED_COPPER_HOE
        );
        OxidationRegistry.registerChain(
            COPPER_HELMET, EXPOSED_COPPER_HELMET, WEATHERED_COPPER_HELMET, OXIDIZED_COPPER_HELMET
        );
        OxidationRegistry.registerChain(
            COPPER_CHESTPLATE, EXPOSED_COPPER_CHESTPLATE, WEATHERED_COPPER_CHESTPLATE, OXIDIZED_COPPER_CHESTPLATE
        );
        OxidationRegistry.registerChain(
            COPPER_LEGGINGS, EXPOSED_COPPER_LEGGINGS, WEATHERED_COPPER_LEGGINGS, OXIDIZED_COPPER_LEGGINGS
        );
        OxidationRegistry.registerChain(
            COPPER_BOOTS, EXPOSED_COPPER_BOOTS, WEATHERED_COPPER_BOOTS, OXIDIZED_COPPER_BOOTS
        );
    }
}