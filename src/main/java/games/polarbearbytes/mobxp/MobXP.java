package games.polarbearbytes.mobxp;

import games.polarbearbytes.mobxp.commands.MobXPCommands;
import games.polarbearbytes.mobxp.networking.MobXPServerNetworking;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobXP implements ModInitializer {
	public static final String MOD_ID = "mobxp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MobXPServerNetworking.register();
		CommandRegistrationCallback.EVENT.register(MobXPCommands::register);
	}

	public static boolean hasManageXPPermission(ServerPlayer player, MinecraftServer server) {
		boolean singlePlayerOP = server.isSingleplayer() && server.getPlayerList().isOp(player.nameAndId());
		boolean serverPlayerOP = !server.isSingleplayer() && server.getPlayerList().isOp(player.nameAndId());
		return singlePlayerOP || serverPlayerOP || Permissions.check(player, MobXP.MOD_ID+".manageXP");
	}
}