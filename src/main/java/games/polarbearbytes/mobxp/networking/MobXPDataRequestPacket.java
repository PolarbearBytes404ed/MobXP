package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXP;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;


/**
 * Packet from client requesting the mob xp details list
 */
public record MobXPDataRequestPacket() implements CustomPacketPayload {
    public static final Type<MobXPDataRequestPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MobXP.MOD_ID, "mob_xp_data_request_packet"));
    public static final Type<MobXPDataRequestPacket> PAYLOAD_TYPE = new Type<>(Identifier.fromNamespaceAndPath(MobXP.MOD_ID, "mob_xp_data_request_packet"));

    public static final MobXPDataRequestPacket INSTANCE = new MobXPDataRequestPacket();

    public static final StreamCodec<RegistryFriendlyByteBuf, MobXPDataRequestPacket> PACKET_CODEC = StreamCodec.unit( INSTANCE );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}