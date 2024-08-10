package folk.sisby.picohud;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;

public class PicoHudConfig extends WrappedConfig {
	@Comment("Whether the 'show overlay' key should act as a toggle, instead of a hold")
	public boolean useKeyToggle = false;
	@Comment("Whether to show rounded coordinates")
	public boolean showCoordinates = true;
	@Comment("Whether to show cardinal facing direction")
	public boolean showDirectionCardinal = true;
	@Comment("Whether to show an indicator (e.g. [=+]) that indicates how travelling forward will affect your X and Z coordinates")
	public boolean showDirectionAxes = false;
	@Comment("Whether to show the current day and time, formatted using seasons if it's installed")
	public boolean showDayTime = true;
	@Comment("Whether to show the name of the biome you're standing in")
	public boolean showBiome = true;
}
