package folk.sisby.picohud;

import com.mojang.blaze3d.platform.InputUtil;
import folk.sisby.picohud.compat.SeasonsCompat;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.config.QuiltConfig;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PicoHudClient implements ClientModInitializer, HudRenderCallback {
	public static final String ID = "picohud";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static boolean SHOW_OVERLAY = false;
	public static boolean SEASONS_COMPAT = false;

	public static final PicoHudConfig CONFIG = QuiltConfig.create(ID, "config", PicoHudConfig.class);

	public static KeyBind showOverlayKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBind(
		"key.picohud.show",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_LEFT_ALT,
		"category.picohud.picohud"
	));

	public static final List<MutableText> DIRECTIONS = List.of(
		Text.translatable("picohud.directions.south"),
		Text.translatable("picohud.directions.southwest"),
		Text.translatable("picohud.directions.west"),
		Text.translatable("picohud.directions.northwest"),
		Text.translatable("picohud.directions.north"),
		Text.translatable("picohud.directions.northeast"),
		Text.translatable("picohud.directions.east"),
		Text.translatable("picohud.directions.southeast")
	);

	public static final List<MutableText> DIRECTION_AXES = List.of(
		Text.literal("[=+]"),
		Text.literal("[-+]"),
		Text.literal("[-=]"),
		Text.literal("[--]"),
		Text.literal("[=-]"),
		Text.literal("[+-]"),
		Text.literal("[+=]"),
		Text.literal("[++]")
	);

	@Override
	public void onInitializeClient(ModContainer mod) {
		ClientTickEvents.END.register(client -> {
			while (CONFIG.useKeyToggle && showOverlayKeybinding.wasPressed()) {
				SHOW_OVERLAY = !SHOW_OVERLAY;
			}
		});
		HudRenderCallback.EVENT.register(this);
		if (QuiltLoader.isModLoaded("seasons")) SEASONS_COMPAT = true;
		LOGGER.info("[PicoHUD] Initialized.");
	}

	@Override
	public void onHudRender(MatrixStack matrixStack, float tickDelta) {
		if (!CONFIG.useKeyToggle) {
			SHOW_OVERLAY = showOverlayKeybinding.isPressed();
		}

		MinecraftClient client = MinecraftClient.getInstance();
		if (!SHOW_OVERLAY || !MinecraftClient.isHudEnabled() || client.options.debugEnabled) return;
		World clientWorld = MinecraftClient.getInstance().world;
		Entity cameraEntity = client.getCameraEntity();
		if (clientWorld == null || cameraEntity == null) return;

		matrixStack.push();

		if (CONFIG.showCoordinates) {
			MutableText coordinateText = Text.translatable("picohud.hud.coordinates", (int) cameraEntity.getX(), (int) cameraEntity.getY(), (int) cameraEntity.getZ());
			client.textRenderer.drawWithShadow(matrixStack, coordinateText, 5, 5, 0xFFFFFF);
		}

		if (CONFIG.showDirectionCardinal || CONFIG.showDirectionAxes) {
			int direction = (int) ((((cameraEntity.getYaw(tickDelta) * 2 + 45) % 720) + 720) % 720 / 90);
			MutableText directionText = Text.literal("");
			if (CONFIG.showDirectionCardinal) directionText.append(DIRECTIONS.get(direction)).append(" ");
			if (CONFIG.showDirectionAxes) directionText.append(DIRECTION_AXES.get(direction));
			client.textRenderer.drawWithShadow(matrixStack, directionText, 5, 17, 0xFFFFFF);
		}

		if (CONFIG.showDayTime && !clientWorld.getDimension().hasFixedTime()) {
			long time = clientWorld.getTimeOfDay();
			String timeOfDay = String.format("%d:%02d", (((6000 + time) % 24000) / 1000), time % 1000 > 500 ? 30 : 0);
			MutableText timeText = SEASONS_COMPAT ?
				Text.translatable("picohud.hud.time.seasons", SeasonsCompat.getSeasonText(clientWorld), SeasonsCompat.getDayOfSeason(clientWorld), (SeasonsCompat.getYear(clientWorld) > 1 ? String.format("Y%d ", SeasonsCompat.getYear(clientWorld)) : "") + timeOfDay) :
				Text.translatable("picohud.hud.time.default", 1 + (time  / 24000), timeOfDay);
			client.textRenderer.drawWithShadow(matrixStack, timeText, 5, 29, 0xFFFFFF);
		}

		matrixStack.pop();
	}
}
