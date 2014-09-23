package pl.krystiankaniowski.openglgame.renderer;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.krystiankaniowski.openglgame.R;
import pl.krystiankaniowski.openglgame.objects.Mallet;
import pl.krystiankaniowski.openglgame.objects.Puck;
import pl.krystiankaniowski.openglgame.objects.Table;
import pl.krystiankaniowski.openglgame.programs.ColorShaderProgram;
import pl.krystiankaniowski.openglgame.programs.TextureShaderProgram;
import pl.krystiankaniowski.openglgame.utils.Geometry;
import pl.krystiankaniowski.openglgame.utils.Geometry.Point;
import pl.krystiankaniowski.openglgame.utils.Geometry.Ray;
import pl.krystiankaniowski.openglgame.utils.Geometry.Sphere;
import pl.krystiankaniowski.openglgame.utils.MatrixHelper;
import pl.krystiankaniowski.openglgame.utils.TextureHelper;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class AirHockeyRenderer implements Renderer {

	// =========================================================================
	// ----- STA�E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = AirHockeyRenderer.class.getSimpleName();

	private final Context context;

	private final float[] viewMatrix = new float[16];
	private final float[] viewProjectionMatrix = new float[16];
	private final float[] modelViewProjectionMatrix = new float[16];
	private final float[] invertedViewProjectionMatrix = new float[16];

	private final float[] projectionMatrix = new float[16];
	private final float[] modelMatrix = new float[16];

	// =========================================================================
	// ----- ZMIENNE -----------------------------------------------------------
	// =========================================================================

	private Table table;
	private Mallet mallet;
	private Puck puck;

	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;

	private int texture;

	private boolean malletPressed = false;
	private Point blueMalletPosition;

	// =========================================================================
	// ----- KONSTRUKTOR -------------------------------------------------------
	// =========================================================================

	public AirHockeyRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		table = new Table();
		mallet = new Mallet(0.08f, 0.15f, 32);
		puck = new Puck(0.06f, 0.02f, 32);

		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);

		blueMalletPosition = new Point(0f, mallet.height / 2f, 0.4f);

	}

	@Override
	public void onDrawFrame(GL10 arg0) {

		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);

		// rotateM(viewMatrix, 0, -0.1f, 0f, 1f, 0f);

		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);

		positionTableInScene();
		textureProgram.useProgram();
		textureProgram.setUniforms(modelViewProjectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();

		// Draw the mallets.
		positionObjectInScene(0f, mallet.height / 2f, -0.4f);
		colorProgram.useProgram();
		colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
		mallet.bindData(colorProgram);
		mallet.draw();

		positionObjectInScene(0f, mallet.height / 2f, 0.4f);
		colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
		// Note that we don't have to define the object data twice -- we just
		// draw the same mallet again but in a different position and with a
		// different color.
		mallet.draw();

		// Draw the puck.
		positionObjectInScene(0f, puck.height / 2f, 0f);
		colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
		puck.bindData(colorProgram);
		puck.draw();

	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {

		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);
		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

		// setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float
		// eyeZ, float centerX, float centerY, float centerZ, float
		// upX, float upY, float upZ)

		setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);

	}

	private void positionTableInScene() {
		// The table is defined in terms of X & Y coordinates, so we rotate it
		// 90 degrees to lie flat on the XZ plane.
		setIdentityM(modelMatrix, 0);
		rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
		multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
	}

	private void positionObjectInScene(float x, float y, float z) {
		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, x, y, z);
		multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
	}

	public void handleTouchPress(float normalizedX, float normalizedY) {

		Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);

		// Now test if this ray intersects with the mallet by creating a
		// bounding sphere that wraps the mallet.
		Sphere malletBoundingSphere = new Sphere(new Point(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z), mallet.height / 2f);

		// If the ray intersects (if the user touched a part of the screen that
		// intersects the mallet's bounding sphere), then set malletPressed =
		// true.
		malletPressed = Geometry.intersects(malletBoundingSphere, ray);

		Log.v(TAG, "Czy dotykam m�otka: " + malletPressed);

	}

	public void handleTouchDrag(float normalizedX, float normalizedY) {
	}

	private Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY) {

		// We'll convert these normalized device coordinates into world-space
		// coordinates. We'll pick a point on the near and far planes, and draw
		// a
		// line between them. To do this transform, we need to first multiply by
		// the inverse matrix, and then we need to undo the perspective divide.
		final float[] nearPointNdc = { normalizedX, normalizedY, -1, 1 };
		final float[] farPointNdc = { normalizedX, normalizedY, 1, 1 };
		final float[] nearPointWorld = new float[4];
		final float[] farPointWorld = new float[4];
		multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
		multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);
		divideByW(nearPointWorld);
		divideByW(farPointWorld);

		Point nearPointRay = new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
		Point farPointRay = new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

		return new Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));

	}

	private void divideByW(float[] vector) {
		vector[0] /= vector[3];
		vector[1] /= vector[3];
		vector[2] /= vector[3];
	}
}
