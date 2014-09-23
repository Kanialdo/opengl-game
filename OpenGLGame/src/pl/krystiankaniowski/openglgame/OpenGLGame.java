package pl.krystiankaniowski.openglgame;

import pl.krystiankaniowski.openglgame.renderer.AirHockeyRenderer;
import pl.krystiankaniowski.openglgame.utils.Debug;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OpenGLGame extends Activity {

	// =========================================================================
	// ----- STA�E -------------------------------------------------------------
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
		final AirHockeyRenderer airHockeyRenderer = new AirHockeyRenderer(this);

		// Sprawdzenie czy telefon obs�uguje OpenGL ES 2.0

		if (supportsEs2) {
			Debug.toast(this, TAG, "OpenGL ES 2.0. jest obs�ugiwany");
			// Request an OpenGL ES 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);
			// Assign our renderer.
			glSurfaceView.setRenderer(airHockeyRenderer);
			rendererSet = true;

			glSurfaceView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event != null) {
						// Convert touch coordinates into normalized device
						// coordinates, keeping in mind that Android's Y
						// coordinates are inverted.
						final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
						final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							glSurfaceView.queueEvent(new Runnable() {
								@Override
								public void run() {
									airHockeyRenderer.handleTouchPress(normalizedX, normalizedY);
								}
							});
						} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
							glSurfaceView.queueEvent(new Runnable() {
								@Override
								public void run() {
									airHockeyRenderer.handleTouchDrag(normalizedX, normalizedY);
								}
							});
						}
						return true;
					} else {
						return false;
					}
				}
			});

		} else {
			Debug.toast(this, TAG, "OpenGL ES 2.0. nie jest obs�ugiwany");
			return;
		}

	}

	// Ze wzgl�du na cykl �ycia SurfaceView nale�y wywo�a� na nim onPause i
	// onResume w chwili wy��czenia ekranu, aby system nie ubi� aplikacji

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
