package games.polarbearbytes.mobxp.input;

import com.mojang.blaze3d.platform.InputConstants;
import games.polarbearbytes.mobxp.gui.screens.MobXPListScreen;
import games.polarbearbytes.mobxp.networking.MobXPDataRequestPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Keybinding class
 */
public class KeyInputHandler {
    public static final KeyMapping.Category KEY_CATEGORY_KEYS = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("mobxp","keys"));
    public static final String KEY_TOGGLE_SCREEN = "key.mobxp.openxpscreen";

    public static KeyMapping toggleXPScreenKey;

    /**
     * Register the actual actions for the keybinds
     */
    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(toggleXPScreenKey.consumeClick()){
                if(client.player == null) return;
                MobXPListScreen screen = new MobXPListScreen();
                Minecraft.getInstance().setScreen(screen);
                ClientPlayNetworking.send(new MobXPDataRequestPacket());
            }
        });
    }

    /**
     * Register the keybinds
     */
    public static void register() {
        toggleXPScreenKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_TOGGLE_SCREEN,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_5,
                KEY_CATEGORY_KEYS
        ));

        registerKeyInputs();
    }
}

