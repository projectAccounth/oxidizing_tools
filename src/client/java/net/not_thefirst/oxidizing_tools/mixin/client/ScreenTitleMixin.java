package net.not_thefirst.oxidizing_tools.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Mixin(Screen.class)
public abstract class ScreenTitleMixin {
    @Shadow @Mutable @Final protected Text title;

    public void setTitle(Text newTitle) {
        this.title = newTitle;
    }
}
