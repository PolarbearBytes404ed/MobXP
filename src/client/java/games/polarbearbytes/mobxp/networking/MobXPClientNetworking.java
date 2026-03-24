package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXPClient;
import games.polarbearbytes.mobxp.gui.screens.MobXPListScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

/**
 * Class for handling all the networking requests and actions to take
 */
public class MobXPClientNetworking {
    /**
     * Register all the packets that the client might receive
     */
    public static void registerReceivers(){
        ClientPlayNetworking.registerGlobalReceiver(MobXPDataListPacket.PAYLOAD_TYPE, MobXPClientNetworking::onDataListPacket);
    }

    /**
     * Action for handling the incoming data list packet, filter the list by entities that are instances of MobEntity (actual mobs)
     *
     * @param packet {@link MobXPDataListPacket} packet containing the mob xp details list
     * @param context Client context
     */
    public static void onDataListPacket(MobXPDataListPacket packet, Context context){
        context.client().execute(()->{
            packet.mobXPDataList().forEach(data->{
                try {
                    Registry<EntityType<?>> entityRegistry = context.player().registryAccess().lookupOrThrow(Registries.ENTITY_TYPE);
                    Mob entity = (Mob) entityRegistry.getValue(Identifier.parse(data.id())).create(context.player().level(), EntitySpawnReason.EVENT);
                    if(entity != null){
                        MobXPClient.mobXPData.put(data.id(),data);
                    }
                } catch(Exception ignored) {}
            });
            Screen currentScreen = context.client().screen;
            if(currentScreen instanceof MobXPListScreen) {
                ((MobXPListScreen) currentScreen).updateList(MobXPClient.mobXPData);
            }
        });
    }
}
