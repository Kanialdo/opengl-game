package pl.krystiankaniowski.openglgame.utils;

public class StringUtils {

	public static String normalizeStringLenght(String string, int lenght) {

		if (string.length() > lenght) {
			// TODO: przetestowa�
			return string.substring(0, lenght);
		} else if (string.length() == lenght) {
			// TODO: przetestowa�
			return string;
		} else {
			// TODO: doda� dope�nianie stringu
			return string;
		}

	}

}
