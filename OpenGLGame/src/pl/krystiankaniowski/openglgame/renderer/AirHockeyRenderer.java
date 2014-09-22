package pl.krystiankaniowski.openglgame.renderer;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.krystiankaniowski.openglgame.R;
import pl.krystiankaniowski.openglgame.objects.Mallet;
import pl.krystiankaniowski.openglgame.objects.Table;
import pl.krystiankaniowski.openglgame.programs.ColorShaderProgram;
import pl.krystiankaniowski.openglgame.programs.TextureShaderProgram;
import pl.krystiankaniowski.openglgame.utils.MatrixHelper;
import pl.krystiankaniowski.openglgame.utils.TextureHelper;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public class AirHockeyRenderer implements Renderer {

	// =========================================================================
	// ----- STA�E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = AirHockeyRenderer.class.getSimpleName();

	private final Context context;
	private final float[] projectionMatrix = new float[16];
	private final float[] modelMatrix = new float[16];

	// =========================================================================
	// ----- ZMIENNE -----------------------------------------------------------
	// =========================================================================

	private Table table;
	private Mallet mallet;

	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;

	private int texture;

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
		mallet = new Mallet();

		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);

	}

	@Override
	public void onDrawFrame(GL10 arg0) {

		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);

		// Draw the table.
		textureProgram.useProgram();
		textureProgram.setUniforms(projectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();

		// Draw the mallets.
		colorProgram.useProgram();
		colorProgram.setUniforms(projectionMatrix);
		mallet.bindData(colorProgram);
		mallet.draw();

	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {

		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);

		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

		// to odsuwa macierz o 2 od punktu widzenia, dzi�ki czemu mo�na zobaczy�
		// obraz
		setIdentityM(modelMatrix, 0);
		// translateM(modelMatrix, 0, 0f, 0f, -2f);
		translateM(modelMatrix, 0, 0f, 0f, -2.5f);
		rotateM(modelMatrix, 0, -60f, 1f, 1f, 0f);

		final float[] temp = new float[16];
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);

	}

}
