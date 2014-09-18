package pl.krystiankaniowski.openglgame.utils;

public class StringUtils {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	private static final String TAG = StringUtils.class.getSimpleName();

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	public static String normalizeStringLenght(String string, int lenght) {

		if (string.length() > lenght) {
			// TODO: przetestowaæ
			return string.substring(0, lenght);
		} else if (string.length() == lenght) {
			// TODO: przetestowaæ
			return string;
		} else {
			// TODO: dodaæ dope³nianie stringu
			return string;
		}

	}

}
