package net.not_thefirst.oxidizing_tools.components;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.not_thefirst.oxidizing_tools.OxidizingTools;

public final class ModComponents {
    public static final ComponentType<Integer> HELD_TICKS = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(OxidizingTools.MOD_ID, "held_ticks"),
        ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    // Special UUID for tracking
    public static final ComponentType<String> ITEM_ID = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(OxidizingTools.MOD_ID, "item_id"),
        ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static void initialize() {}
}

