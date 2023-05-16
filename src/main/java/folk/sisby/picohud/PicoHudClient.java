package folk.sisby.picohud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PicoHudClient implements ClientModInitializer, HudRenderCallback {
	public static final String ID = "picohud";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static boolean SHOW_OVERLAY = false;

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

	@Override
	public void onInitializeClient(ModContainer mod) {
		PicoHudKeybindings.initializeKeybindings();
		HudRenderCallback.EVENT.register(this);

		LOGGER.info("[PicoHUD] Initialized.");
	}

	@Override
	public void onHudRender(MatrixStack matrixStack, float tickDelta) {
		SHOW_OVERLAY = PicoHudKeybindings.showOverlayKeybinding.isPressed();

		if (!SHOW_OVERLAY || !MinecraftClient.isHudEnabled()) return;
		MinecraftClient client = MinecraftClient.getInstance();
		World clientWorld = MinecraftClient.getInstance().world;
		Entity cameraEntity = client.getCameraEntity();
		if (clientWorld == null || cameraEntity == null || client.options.debugEnabled) return;

		matrixStack.push();
		MutableText coordinateText = Text.translatable("picohud.hud.coordinates", (int) cameraEntity.getX(), (int) cameraEntity.getY(), (int) cameraEntity.getZ());
		MutableText facingText = DIRECTIONS.get((int) ((((cameraEntity.getYaw(tickDelta) * 2 + 45) % 720) + 720) % 720 / 90));
		MutableText timeText = Text.translatable("picohud.hud.time.default", 1 + (clientWorld.getTime()  / 24000), String.format("%d:%02d", 6 + (clientWorld.getTimeOfDay() / 1000), clientWorld.getTimeOfDay() % 1000 > 500 ? 30 : 0));

		client.textRenderer.draw(matrixStack, coordinateText, 5, 5, 0xFFFFFF);
		client.textRenderer.draw(matrixStack, facingText, 5, 17, 0xFFFFFF);
		client.textRenderer.draw(matrixStack, timeText, 5, 29, 0xFFFFFF);

		matrixStack.pop();
	}
}
