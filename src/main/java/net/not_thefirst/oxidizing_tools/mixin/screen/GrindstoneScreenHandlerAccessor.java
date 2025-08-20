package net.not_thefirst.oxidizing_tools.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

@Mixin(GrindstoneScreenHandler.class)
public interface GrindstoneScreenHandlerAccessor {
    @Accessor("input")
    Inventory getInput();
    
    @Accessor("context")
    ScreenHandlerContext getContext();
}