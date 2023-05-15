package folk.sisby.picohud;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class PicoHudKeybindings {
	public static KeyBind showOverlayKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBind(
		"key.picohud.show",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_LEFT_ALT,
		"category.picohud.picohud"
	));

	public static void initializeKeybindings() {
		ClientTickEvents.END.register(client -> {
			while (showOverlayKeybinding.wasPressed()) {
				// Do toggle stuff
			}
		});
	}
}
