package pl.krystiankaniowski.openglgame.utils;

public class StringUtils {

	public static String normalizeStringLenght(String string, int lenght) {

		if (string.length() > lenght) {
			// TODO: przetestować
			return string.substring(0, lenght);
		} else if (string.length() == lenght) {
			// TODO: przetestować
			return string;
		} else {
			// TODO: dodać dopełnianie stringu
			return string;
		}

	}

}
