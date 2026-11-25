package com.zyon.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class ZyonClient implements ClientModInitializer {
    public static final DiscoverySettings SETTINGS = new DiscoverySettings();
    public static final DiscoveryScanner SCANNER = new DiscoveryScanner();
    private static KeyBinding openMenu;

    @Override
    public void onInitializeClient() {
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.zyon.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new ZyonScreen());
                else if (client.currentScreen instanceof ZyonScreen) client.setScreen(null);
            }
            if (client.world != null && client.player != null && SETTINGS.anyScannerEnabled()) {
                SCANNER.tick(client);
            }
        });
    }
}
