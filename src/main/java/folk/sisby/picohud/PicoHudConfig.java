package folk.sisby.picohud;

import org.quiltmc.config.api.WrappedConfig;
import org.quiltmc.config.api.annotations.Comment;

public class PicoHudConfig extends WrappedConfig {
	@Comment("Whether the show overlay key should act as a toggle, instead of a hold")
	public final Boolean useKeyToggle = false;
	@Comment("Whether to show rounded coordinates")
	public final Boolean showCoordinates = true;
	@Comment("Whether to show cardinal facing direction")
	public final Boolean showDirectionCardinal = true;
	@Comment("Whether to show an indicator (e.g. [=+]) that indicates how travelling forward will affect your X and Z coordinates")
	public final Boolean showDirectionAxes = false;
	@Comment("Whether to the current day and time, formatted using seasons if it's installed")
	public final Boolean showDayTime = true;
}
