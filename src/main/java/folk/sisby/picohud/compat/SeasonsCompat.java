package folk.sisby.picohud.compat;

import io.github.lucaargolo.seasons.FabricSeasons;
import io.github.lucaargolo.seasons.utils.Season;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Map;

import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;

@SuppressWarnings("deprecation")
public class SeasonsCompat {
	public static final Map<Season, Style> SEASON_STYLES = Map.of(
		Season.WINTER, Style.EMPTY.withColor(0x70ffff),
		Season.FALL, Style.EMPTY.withColor(0xd4361e),
		Season.SUMMER, Style.EMPTY.withColor(0xffc200),
		Season.SPRING, Style.EMPTY.withColor(0x53ff57)
	);

	public static long getTimeToNextSeason(World world) {
		long springTime = world.getTimeOfDay() % CONFIG.getSeasonLength();
		long summerTime = springTime - CONFIG.getSeasonLength();
		long fallTime = summerTime - CONFIG.getSeasonLength();
		long winterTime = fallTime - CONFIG.getSeasonLength();

		long seasonTime = switch (FabricSeasons.getCurrentSeason(world)) {
			case SPRING -> springTime;
			case SUMMER -> summerTime;
			case FALL -> fallTime;
			case WINTER -> winterTime;
		};
		return CONFIG.getSeasonLength() - seasonTime;
	}

	public static int getDayOfSeason(World world) {
		return CONFIG.isValidInDimension(world.getRegistryKey()) ? 1 + (int) ((CONFIG.getSeasonLength() - (getTimeToNextSeason(world))) / 24000) : -1;
	}

	public static int getYear(World world) {
		return 1 + (int) (world.getTimeOfDay() / (CONFIG.getSeasonLength() * 4));
	}

	public static Text getSeasonText(World world) {
		Season season = FabricSeasons.getCurrentSeason(world);
		return new LiteralText(WordUtils.capitalize(season.asString())).setStyle(SEASON_STYLES.getOrDefault(season, Style.EMPTY));
	}
}
