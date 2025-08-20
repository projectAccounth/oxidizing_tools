package net.not_thefirst.oxidizing_tools.networking.c2s;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.not_thefirst.oxidizing_tools.OxidizingTools;

public record GrindstoneUpdatePayloadC2S(boolean value) implements CustomPayload {
    public static final Id<GrindstoneUpdatePayloadC2S> ID = new Id<>(Identifier.of(OxidizingTools.MOD_ID, "grindstone_state_update"));
    public static final PacketCodec<RegistryByteBuf, GrindstoneUpdatePayloadC2S> CODEC =
        PacketCodec.tuple(PacketCodecs.BOOLEAN, GrindstoneUpdatePayloadC2S::value, GrindstoneUpdatePayloadC2S::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}