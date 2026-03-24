package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

/**
 * Packet from client with mob xp details to update the config to
 * @param data
 */
public record MobXPUpdatePacket(MobXPData data) implements CustomPacketPayload {
    public static final Type<MobXPUpdatePacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MobXP.MOD_ID, "mob_xp_update_packet"));
    public static final Type<MobXPUpdatePacket> PAYLOAD_TYPE = new Type<>(Identifier.fromNamespaceAndPath(MobXP.MOD_ID, "mob_xp_update_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MobXPUpdatePacket> PACKET_CODEC = StreamCodec.composite(
            MobXPData.PACKET_CODEC,
            MobXPUpdatePacket::data,
            MobXPUpdatePacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
