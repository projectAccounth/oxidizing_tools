package net.not_thefirst.oxidizing_tools.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.not_thefirst.oxidizing_tools.networking.c2s.GrindstoneUpdatePayloadC2S;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    private static final Text DEFAULT_TITLE = Text.translatable("container.grindstone_title");
    private static final Text CUSTOM_TITLE = Text.translatable("container.grindstone_custom_title");

    // Constants
    private static final Identifier[] TAB_TOP_UNSELECTED_TEXTURES = new Identifier[] {
        Identifier.ofVanilla("container/creative_inventory/tab_top_unselected_1"),
        Identifier.ofVanilla("container/creative_inventory/tab_top_unselected_2")
    };
    private static final Identifier[] TAB_TOP_SELECTED_TEXTURES = new Identifier[] {
        Identifier.ofVanilla("container/creative_inventory/tab_top_selected_1"),
        Identifier.ofVanilla("container/creative_inventory/tab_top_selected_2")
    };
    private static final Identifier AXE_TEMPLATE = Identifier.ofVanilla("container/slot/axe");

    // Fields
    @Unique
    private boolean isCustomMode = false;

    @Unique
    private final CyclingSlotIcon templateSlotIcon = new CyclingSlotIcon(1);

    // Helper Methods
    @Unique
    private boolean isClickInTab(int index, double mouseX, double mouseY) {
        return mouseX >= 27 * index && mouseX <= 27 * index + 26 && mouseY >= -32 && mouseY <= 0;
    }

    @Unique
    private void renderTabIcon(DrawContext context, int index, boolean isEnabled) {
        @SuppressWarnings("rawtypes")
        HandledScreen screen = (HandledScreen) (Object) this;
        if (!(screen instanceof GrindstoneScreen)) return;

        HandledScreenAccessor accessor = (HandledScreenAccessor) screen;

        int backgroundHeight = accessor.getBackgroundHeight();
        int backgroundWidth = accessor.getBackgroundWidth();

        int x = (screen.width - backgroundWidth) / 2;
        int y = (screen.height - backgroundHeight) / 2;

        int i = Math.clamp(index, 0, 1);
        int j = x + 27 * i;
        int k = y - 28;

        Identifier texture = isEnabled ? TAB_TOP_SELECTED_TEXTURES[i] : TAB_TOP_UNSELECTED_TEXTURES[i];

        context.drawGuiTexture(RenderLayer::getGuiTextured, texture, j, k, 26, 32);
        context.getMatrices().push();
        context.getMatrices().translate(0.0F, 0.0F, 100.0F);
        j += 5;
        k += 9;

        Item item = i == 0 ? Items.ENCHANTED_BOOK : Items.HONEYCOMB;
        ItemStack itemStack = new ItemStack(item);

        context.drawItem(itemStack, j, k);
        context.drawStackOverlay(screen.getTextRenderer(), itemStack, j, k);
        context.getMatrices().pop();
    }

    // Initialization
    @Inject(method = "init", at = @At("TAIL"))
    private void addToggleButton(CallbackInfo ci) {
        @SuppressWarnings("rawtypes")
        HandledScreen screen = (HandledScreen) (Object) this;
        if (!(screen instanceof GrindstoneScreen)) return;

        isCustomMode = false;
    }

    // Rendering
    @Inject(method = "renderBackground", at = @At("HEAD"))
    private void beforeRenderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        @SuppressWarnings("rawtypes")
        HandledScreen screen = (HandledScreen) (Object) this;
        if (!(screen instanceof GrindstoneScreen)) return;

        renderTabIcon(context, isCustomMode ? 0 : 1, false);
    }

    @Inject(method = "renderBackground", at = @At("TAIL"))
    private void afterRenderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        @SuppressWarnings("rawtypes")
        HandledScreen screen = (HandledScreen) (Object) this;
        if (!(screen instanceof GrindstoneScreen)) return;

        HandledScreenAccessor accessor = (HandledScreenAccessor) (Object) screen;

        int backgroundHeight = accessor.getBackgroundHeight();
        int backgroundWidth = accessor.getBackgroundWidth();

        int x = (screen.width - backgroundWidth) / 2;
        int y = (screen.height - backgroundHeight) / 2;

        renderTabIcon(context, isCustomMode ? 1 : 0, true);
        this.templateSlotIcon.render(accessor.getHandler(), context, delta, x, y);
    }

    // Mouse Interaction
    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void onMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
        @SuppressWarnings("rawtypes")
        HandledScreen screen = (HandledScreen) (Object) this;
        if (!(screen instanceof GrindstoneScreen)) return;

        HandledScreenAccessor accessor = (HandledScreenAccessor) screen;

        if (button == 0) {
            int backgroundHeight = accessor.getBackgroundHeight();
            int backgroundWidth = accessor.getBackgroundWidth();

            int x = (screen.width - backgroundWidth) / 2;
            int y = (screen.height - backgroundHeight) / 2;

            if (isClickInTab(0, mouseX - x, mouseY - y)) {
                isCustomMode = false;
                ClientPlayNetworking.send(new GrindstoneUpdatePayloadC2S(isCustomMode));
                ci.setReturnValue(true);
            } else if (isClickInTab(1, mouseX - x, mouseY - y)) {
                isCustomMode = true;
                ClientPlayNetworking.send(new GrindstoneUpdatePayloadC2S(isCustomMode));
                ci.setReturnValue(true);
            }
        }
    }

    // Tick Handling
    @Inject(method = "handledScreenTick", at = @At("HEAD"))
    private void handleTick(CallbackInfo ci) {
        @SuppressWarnings("rawtypes")
        HandledScreen screen = (HandledScreen) (Object) this;
        if (!(screen instanceof GrindstoneScreen)) return;

        ScreenTitleMixin accessor = (ScreenTitleMixin)(Object) screen;
        accessor.setTitle(isCustomMode ? CUSTOM_TITLE : DEFAULT_TITLE);

        this.templateSlotIcon.updateTexture(isCustomMode ? List.of(AXE_TEMPLATE) : List.of());
    }
}