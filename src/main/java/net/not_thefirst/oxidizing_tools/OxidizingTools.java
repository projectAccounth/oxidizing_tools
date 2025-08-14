package net.not_thefirst.oxidizing_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.not_thefirst.oxidizing_tools.components.ModComponents;
import net.not_thefirst.oxidizing_tools.items.ModItems;
import net.not_thefirst.oxidizing_tools.registry.ModToolMaterials;
import net.not_thefirst.oxidizing_tools.registry.OxidationRegistry;

public class OxidizingTools implements ModInitializer {
    public static final String MOD_ID = "oxidizing_tools";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModComponents.initialize();
        OxidationRegistry.initialize();
        ModToolMaterials.initialize();
    }
}
