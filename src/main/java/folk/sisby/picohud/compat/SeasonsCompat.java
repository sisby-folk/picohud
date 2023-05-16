package folk.sisby.picohud.compat;

import io.github.lucaargolo.seasons.utils.Season;
import net.minecraft.text.Text;

import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;

public class SeasonsCompat {
	public static int getDayOfSeason(long time) {
		long springTime = time % CONFIG.getYearLength();
		long summerTime = springTime - CONFIG.getSpringLength();
		long fallTime = summerTime - CONFIG.getSummerLength();
		long winterTime = fallTime - CONFIG.getFallLength();

		if(winterTime >= 0 && CONFIG.getWinterLength() > 0) {
			return 1 + (int) winterTime / 24000;
		}else if(fallTime >= 0 && CONFIG.getFallLength() > 0) {
			return 1 + (int) fallTime / 24000;
		}else if(summerTime >= 0 && CONFIG.getSummerLength() > 0) {
			return 1 + (int) summerTime / 24000;
		}else if(springTime >= 0 && CONFIG.getSpringLength() > 0) {
			return 1 + (int) springTime / 24000;
		}

		return 0;
	}

	public static int getYear(long time) {
		return (int) (time / CONFIG.getYearLength());
	}

	public static Text getSeasonText(long time) {
		long springTime = time % CONFIG.getYearLength();
		long summerTime = springTime - CONFIG.getSpringLength();
		long fallTime = summerTime - CONFIG.getSummerLength();
		long winterTime = fallTime - CONFIG.getFallLength();

		if(winterTime >= 0 && CONFIG.getWinterLength() > 0) {
			return Text.translatable(Season.WINTER.getTranslationKey());
		}else if(fallTime >= 0 && CONFIG.getFallLength() > 0) {
			return Text.translatable(Season.FALL.getTranslationKey());
		}else if(summerTime >= 0 && CONFIG.getSummerLength() > 0) {
			return Text.translatable(Season.SUMMER.getTranslationKey());
		}else if(springTime >= 0 && CONFIG.getSpringLength() > 0) {
			return Text.translatable(Season.SPRING.getTranslationKey());
		}

		return Text.literal("");
	}
}
