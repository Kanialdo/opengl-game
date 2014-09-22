package pl.krystiankaniowski.openglgame.objects;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import pl.krystiankaniowski.openglgame.Constansts;
import pl.krystiankaniowski.openglgame.data.VertexArray;
import pl.krystiankaniowski.openglgame.programs.ColorShaderProgram;

public class Mallet {

	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int COLOR_COMPONENT_COUNT = 3;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constansts.BYTES_PER_FLOAT;

	// @formatter:off
	
	/**
	 * Order of coordinates: X, Y, R, G, B
	 * X i Y to wspó³rzêdne wierzcho³ków
	 * R G i B to kolory
	 */
	
	private static final float[] VERTEX_DATA = {
		0f, -0.4f,  0f,  0f,  1f,
		0f,  0.4f,  1f,  0f,  0f
	};

	// @formatter:on

	private final VertexArray vertexArray;

	public Mallet() {
		vertexArray = new VertexArray(VERTEX_DATA);
	}

	public void bindData(ColorShaderProgram colorProgram) {
		vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, colorProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
	}

	public void draw() {
		glDrawArrays(GL_POINTS, 0, 2);
	}

}
