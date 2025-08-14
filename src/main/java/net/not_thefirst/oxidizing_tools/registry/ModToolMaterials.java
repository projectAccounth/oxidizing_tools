package net.not_thefirst.oxidizing_tools.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.not_thefirst.oxidizing_tools.OxidizingTools;

public class ModToolMaterials {
    private static TagKey<Item> of(String id, boolean usesDefaultNamespace) {
		return TagKey.of(RegistryKeys.ITEM, usesDefaultNamespace ? Identifier.ofVanilla(id) : Identifier.of(OxidizingTools.MOD_ID, id));
	}
    public static final ToolMaterial COPPER_TOOL_MATERIAL = new ToolMaterial(
        BlockTags.INCORRECT_FOR_GOLD_TOOL,  // ~ Gold on this one 
        200,                               
        5.8F,                              
        1.0F,                              
        14,                                
        of("copper_tool_materials", true)
    );

    public static void initialize() {}
}