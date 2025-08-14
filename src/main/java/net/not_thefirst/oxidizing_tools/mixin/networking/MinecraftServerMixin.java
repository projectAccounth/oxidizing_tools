package net.not_thefirst.oxidizing_tools.mixin.networking;

import net.minecraft.server.MinecraftServer;
import net.not_thefirst.oxidizing_tools.ticking.OxidationTickHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onServerTick(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (server.isPaused()) return;

        OxidationTickHandler.onServerTick(server);
    }
}
