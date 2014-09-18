package pl.krystiankaniowski.openglgame.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Debug {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	public static final int LOG_TAG_LENGHT = 16;

	public static class ClassType {
		public static final String ACTIVITY = "A";
		public static final String CLASS = "C";
	}

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	public static void toast(Context context, String tag, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		log(tag, "Toast: " + message);
	}

	public static void log(String tag, String message) {
		Log.v(tag, message);
	}

	public static void openglWarning(String tag, String message) {
		Log.w(tag, message);
	}

	public static void openglStatus(String tag, String message) {
		Log.v(tag, message);
	}

	public static String normalizeTag(String tag, String classType) {
		return StringUtils.normalizeStringLenght(tag, LOG_TAG_LENGHT) + " " + classType;
	}

}
