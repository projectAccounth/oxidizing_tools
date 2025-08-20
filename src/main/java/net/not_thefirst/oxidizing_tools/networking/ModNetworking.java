package net.not_thefirst.oxidizing_tools.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.not_thefirst.oxidizing_tools.components.CustomGrindstoneData;
import net.not_thefirst.oxidizing_tools.networking.c2s.GrindstoneUpdatePayloadC2S;

public class ModNetworking {
    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(
            GrindstoneUpdatePayloadC2S.ID,
            GrindstoneUpdatePayloadC2S.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(GrindstoneUpdatePayloadC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                if (player.currentScreenHandler instanceof GrindstoneScreenHandler handler) {
                    ((CustomGrindstoneData)(Object) handler).setCustomMode(payload.value());
                }
            });
        });
    }
}
