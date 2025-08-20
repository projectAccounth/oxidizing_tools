package net.not_thefirst.oxidizing_tools.registry;

import java.util.EnumMap;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.not_thefirst.oxidizing_tools.OxidizingTools;

public class ModToolMaterials {
    private static TagKey<Item> of(String id, boolean usesDefaultNamespace) {
		return TagKey.of(RegistryKeys.ITEM, usesDefaultNamespace ? Identifier.ofVanilla(id) : Identifier.of(OxidizingTools.MOD_ID, id));
	}

    public static final RegistryKey<EquipmentAsset> ASSET_KEY_COPPER = EquipmentAssetKeys.register("copper");

    public static final ToolMaterial COPPER_TOOL_MATERIAL = new ToolMaterial(
        BlockTags.INCORRECT_FOR_STONE_TOOL,  // ~ Stone on this one 
        200,                               
        5.8F,                              
        1.0F,                              
        14,                                
        of("copper_tool_materials", true)
    );
    
    public static ArmorMaterial COPPER_ARMOR_MATERIAL = new ArmorMaterial(11, // durability multiplier 
        Util.make(new EnumMap<EquipmentType, Integer>(EquipmentType.class), map -> {
            map.put(EquipmentType.BOOTS, 2);
            map.put(EquipmentType.LEGGINGS, 3);
            map.put(EquipmentType.CHESTPLATE, 4);
            map.put(EquipmentType.HELMET, 3);
            map.put(EquipmentType.BODY, 4);
        }), 
        8, // enchantability (constant)
        SoundEvents.ITEM_ARMOR_EQUIP_IRON, 
        0.0F, 0.0F, // toughness and knockback resistant (none)
        of("copper_tool_materials", true), ASSET_KEY_COPPER
    );

    public static void initialize() {
        
    }
}