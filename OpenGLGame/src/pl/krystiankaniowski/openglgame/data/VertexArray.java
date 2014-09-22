package pl.krystiankaniowski.openglgame.data;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import pl.krystiankaniowski.openglgame.Constansts;
import pl.krystiankaniowski.openglgame.utils.MatrixHelper;

public class VertexArray {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = MatrixHelper.class.getSimpleName();

	private final FloatBuffer floatBuffer;

	public VertexArray(float[] vertexData) {
		floatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constansts.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
	}

	public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
		floatBuffer.position(dataOffset);
		glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
		glEnableVertexAttribArray(attributeLocation);
		floatBuffer.position(0);
	}

}
