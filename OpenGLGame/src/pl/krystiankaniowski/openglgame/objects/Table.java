package pl.krystiankaniowski.openglgame.objects;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import pl.krystiankaniowski.openglgame.Constansts;
import pl.krystiankaniowski.openglgame.data.VertexArray;

public class Table {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	/** TODO: */
	private static final int POSITION_COMPONENT_COUNT = 2;

	/** TODO: */
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

	/** TODO: */
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constansts.BYTES_PER_FLOAT;

	// @formatter:off
	
	/**
	 * Order of coordinates: X, Y, S, T | Triangle Fan
	 * X i Y to wspó³rzêdne wierzcho³ków
	 * S i T to wspó³rzêdne tekstury
	 */
	
	private static final float[] VERTEX_DATA = {
		 0f,    0f,    0.5f,  0.5f,
		-0.5f, -0.8f,  0f,    0.9f,
		 0.5f, -0.8f,  1f,    0.9f,
		 0.5f,  0.8f,  1f,    0.1f,
		-0.5f,  0.8f,  0f,    0.1f,
		-0.5f, -0.8f,  0f,    0.9f
	};

	// @formatter:on

	private final VertexArray vertexArray;

	// =========================================================================
	// ----- KONSTRUKTOR -------------------------------------------------------
	// =========================================================================

	public Table() {
		vertexArray = new VertexArray(VERTEX_DATA);
	}

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	// public void bindData(TextureShaderProgram textureProgram) {
	// vertexArray.setVertexAttribPointer(0,
	// textureProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT,
	// STRIDE);
	// vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
	// textureProgram.getTextureCoordinatesAttributeLocation(),
	// TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	// }

	public void draw() {
		glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
	}

}
