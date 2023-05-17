package folk.sisby.picohud;

import org.quiltmc.config.api.WrappedConfig;
import org.quiltmc.config.api.annotations.Comment;

public class PicoHudConfig extends WrappedConfig {
	@Comment("Whether the show overlay key should act as a toggle, instead of a hold")
	public final Boolean toggleShowOverlay = false;
}
