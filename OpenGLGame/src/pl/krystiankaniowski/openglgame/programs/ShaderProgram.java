package pl.krystiankaniowski.openglgame.programs;

import static android.opengl.GLES20.glUseProgram;
import pl.krystiankaniowski.openglgame.utils.ShaderHelper;
import pl.krystiankaniowski.openglgame.utils.TextResourceReader;
import android.content.Context;

public class ShaderProgram {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = ShaderProgram.class.getSimpleName();

	// Uniform constants
	protected static final String U_MATRIX = "u_Matrix";
	protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

	// Attribute constants
	protected static final String A_POSITION = "a_Position";
	protected static final String A_COLOR = "a_Color";
	protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

	// Shader program
	protected final int program;

	protected static final String U_COLOR = "u_Color" ;
	
	// =========================================================================
	// ----- KONSTRUKTOR -------------------------------------------------------
	// =========================================================================

	protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {

		// Compile the shaders and link the program.
		program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
				TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));

	}

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	public void useProgram() {

		// Set the current OpenGL shader program to this program.
		glUseProgram(program);

	}

}
