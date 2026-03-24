package games.polarbearbytes.mobxp;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.input.KeyInputHandler;
import games.polarbearbytes.mobxp.networking.MobXPClientNetworking;
import games.polarbearbytes.mobxp.networking.MobXPUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.resources.Identifier;
import java.util.HashMap;

public class MobXPClient implements ClientModInitializer {
	public static final HashMap<String, MobXPData> mobXPData = new HashMap<>();
	@Override
	public void onInitializeClient() {
		MobXPClientNetworking.registerReceivers();
		KeyInputHandler.register();

		//On entering the world build the default mob list for the mob xp screen
		ClientEntityEvents.ENTITY_LOAD.register((entity,world)->{
			if( !entity.isAlwaysTicking() ) return;

			Registry<EntityType<?>> entityRegistry = world.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE);

			entityRegistry.forEach(entityType->{
				try {
					Identifier id = entityRegistry.getKey(entityType);
					Mob mobEntity = (Mob) entityRegistry.getValue(id).create(world, EntitySpawnReason.EVENT);
					if(mobEntity == null) return;

					MobXPData data = new MobXPData(id.toString(), -1, -1,-1, false, false, false);
					mobXPData.put(data.id(), data);
				} catch(Exception ignored){}
			});
		});
	}

	public static void updateData(MobXPData data){
		mobXPData.put( data.id(), data );
		ClientPlayNetworking.send(new MobXPUpdatePacket(data));
	}
}