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

	public static final List<Text> DIRECTIONS = List.of(
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

		LOGGER.info("[PicoHUD] Initialized.");
	}

	@Override
	public void onHudRender(MatrixStack matrixStack, float tickDelta) {
		SHOW_OVERLAY = PicoHudKeybindings.showOverlayKeybinding.isPressed();

		if (!SHOW_OVERLAY) return;
		MinecraftClient client = MinecraftClient.getInstance();
		Entity cameraEntity = client.getCameraEntity();
		if (cameraEntity == null) {
			return;
		}

		World clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) return;

		matrixStack.push();
		MutableText hudText = Text.literal(String.format("%4.0f, %4.0f, %4.0f", cameraEntity.getX(), cameraEntity.getY(), cameraEntity.getZ()))
			.append(Text.literal("\n"))
			.append(DIRECTIONS.get((int) (((cameraEntity.getYaw(tickDelta) % 360) + 360) % 360 / 45)))
			.append(Text.literal("\n"))
			.append(Text.translatable("picohud.hud.time.default", 1 + (clientWorld.getTime()  / 24000), String.format("%d:%d", 6 + (clientWorld.getTimeOfDay() / 1000), clientWorld.getTimeOfDay() % 1000 > 500 ? 30 : 0)));
		client.textRenderer.draw(matrixStack, hudText, 5, 5, 0xFFFFFF);

		matrixStack.pop();
	}
}
