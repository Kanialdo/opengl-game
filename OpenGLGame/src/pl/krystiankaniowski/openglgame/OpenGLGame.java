package pl.krystiankaniowski.openglgame;

import pl.krystiankaniowski.openglgame.renderer.AirHockeyRenderer;
import pl.krystiankaniowski.openglgame.utils.Debug;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLGame extends Activity {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = Debug.normalizeTag(OpenGLGame.class.getSimpleName(), Debug.ClassType.ACTIVITY);

	// =========================================================================
	// ----- ZMIENNE -----------------------------------------------------------
	// =========================================================================

	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;

	// =========================================================================
	// ----- LIFE - CYCLE ------------------------------------------------------
	// =========================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		glSurfaceView = new GLSurfaceView(this);
		setContentView(glSurfaceView);

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		// Sprawdzenie czy telefon obs³uguje OpenGL ES 2.0
		
		if (supportsEs2) {
			Debug.toast(this, TAG, "OpenGL ES 2.0. jest obs³ugiwany");
			// Request an OpenGL ES 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);
			// Assign our renderer.
			glSurfaceView.setRenderer(new AirHockeyRenderer(this));
			rendererSet = true;
		} else {
			Debug.toast(this, TAG, "OpenGL ES 2.0. nie jest obs³ugiwany");
			return;
		}

	}

	// Ze wzglêdu na cykl ¿ycia SurfaceView nale¿y wywo³aæ na nim onPause i
	// onResume w chwili wy³¹czenia ekranu, aby system nie ubi³ aplikacji

	@Override
	protected void onPause() {
		super.onPause();
		if (rendererSet) {
			glSurfaceView.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (rendererSet) {
			glSurfaceView.onResume();
		}
	}

}
