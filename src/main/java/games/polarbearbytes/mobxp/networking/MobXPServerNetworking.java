package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

import static games.polarbearbytes.mobxp.MobXP.hasManageXPPermission;

public class MobXPServerNetworking {
    public static void register(){
        registerPackets();
        registerReceivers();
    }

    protected static void registerPackets(){
        PayloadTypeRegistry.serverboundPlay().register(MobXPDataRequestPacket.PAYLOAD_TYPE, MobXPDataRequestPacket.PACKET_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(MobXPDataListPacket.PAYLOAD_TYPE, MobXPDataListPacket.PACKET_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MobXPUpdatePacket.PAYLOAD_TYPE, MobXPUpdatePacket.PACKET_CODEC);
    }
    protected static void registerReceivers(){
        ServerPlayNetworking.registerGlobalReceiver(MobXPDataRequestPacket.PAYLOAD_TYPE, MobXPServerNetworking::onDataRequest);
        ServerPlayNetworking.registerGlobalReceiver(MobXPUpdatePacket.PAYLOAD_TYPE, MobXPServerNetworking::onUpdateRequest);
    }

    protected static void onDataRequest(MobXPDataRequestPacket payload, Context context){
        MinecraftServer server = context.server();
        ServerPlayer player = context.player();

        if(!hasManageXPPermission(player, server)) {
            player.sendSystemMessage(Component.translatable(
                    "mobxp.strings.nopermissions"
            ));
        } else {
            List<MobXPData> dataList = MobXPStateManager.get(server).getList();
            ServerPlayNetworking.send(player,new MobXPDataListPacket( dataList ));
        }
    }

    protected static void onUpdateRequest(MobXPUpdatePacket payload, Context context){
        MinecraftServer server = context.server();
        ServerPlayer player = context.player();

        if(!hasManageXPPermission(player, server)) {
            player.sendSystemMessage(Component.translatable(
                    "mobxp.strings.nopermissions"
            ));
        } else {
            MobXPData data = payload.data();
            MobXPStateManager.get(server).updateState(data);
        }
    }
}
