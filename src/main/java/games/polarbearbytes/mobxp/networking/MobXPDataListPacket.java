package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Network packet for transmitting the mob xp details list to client
 * @param mobXPDataList
 */
public record MobXPDataListPacket(List<MobXPData> mobXPDataList) implements CustomPacketPayload {
    public static final Type<MobXPDataListPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MobXP.MOD_ID, "mob_xp_data_packet"));
    public static final Type<MobXPDataListPacket> PAYLOAD_TYPE = new Type<>(Identifier.fromNamespaceAndPath(MobXP.MOD_ID, "mob_xp_data_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MobXPDataListPacket> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(
                    ArrayList::new,
                    MobXPData.PACKET_CODEC
            ), MobXPDataListPacket::mobXPDataList,
            MobXPDataListPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
