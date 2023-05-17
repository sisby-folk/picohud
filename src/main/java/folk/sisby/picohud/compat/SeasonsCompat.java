package folk.sisby.picohud.compat;

import io.github.lucaargolo.seasons.FabricSeasons;
import io.github.lucaargolo.seasons.utils.Season;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Map;

import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;

public class SeasonsCompat {
	public static final Map<Season, Style> SEASON_STYLES = Map.of(
		Season.WINTER, Style.EMPTY.withColor(0x70ffff),
		Season.FALL, Style.EMPTY.withColor(0xb03333),
		Season.SUMMER, Style.EMPTY.withColor(0xffc200),
		Season.SPRING, Style.EMPTY.withColor(0x53ff57)
	);

	public static int getDayOfSeason(World world) {
		return CONFIG.isValidInDimension(world.getRegistryKey()) ? 1 + (int) ((FabricSeasons.getCurrentSeason().getSeasonLength() - FabricSeasons.getTimeToNextSeason(world)) / 24000) : -1;
	}

	public static int getYear(World world) {
		return 1 + (int) (world.getTimeOfDay() / CONFIG.getYearLength());
	}

	public static Text getSeasonText(World world) {
		Season season = FabricSeasons.getCurrentSeason(world);
		return Text.translatable(season.getTranslationKey()).setStyle(SEASON_STYLES.getOrDefault(season, Style.EMPTY));
	}
}
