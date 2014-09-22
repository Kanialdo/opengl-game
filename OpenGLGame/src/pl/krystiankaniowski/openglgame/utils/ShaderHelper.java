package pl.krystiankaniowski.openglgame.utils;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;
import android.util.Log;

public class ShaderHelper {

	// =========================================================================
	// ----- STA�E -------------------------------------------------------------
	// =========================================================================

	private static final String TAG = ShaderHelper.class.getSimpleName();

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	public static int compileVertexShader(String shaderCode) {
		return compileShader(GL_VERTEX_SHADER, shaderCode);
	}

	public static int compileFragmentShader(String shaderCode) {
		return compileShader(GL_FRAGMENT_SHADER, shaderCode);
	}

	private static int compileShader(int type, String shaderCode) {

		// S� dwa typy shader�w - GL_VERTEX_SHADER i GL_FRAGMENT_SHADER
		// Je�eli chcemy utworzy� shader to wo�amy glCreateShader(type) i
		// dostajemy id obiektu openGl kt�ry b�dzie tym shaderem

		// Je�eli id == 0 to oznacza to b��d
		// OpenGl nie rzuca wyj�tk�w natychmiast, dlatego jak dostaniemy 0 to
		// mo�emy sprawdzi� jaka cz�� naszego API nie wysz�a

		final int shaderObjectId = glCreateShader(type);
		if (shaderObjectId == 0) {
			Debug.openglWarning(TAG, "Could not create new shader.");
			return 0;
		}

		// Nast�pnie nale�y przekaza� kod �r�d�owy shadera do w�a�ciwego obiektu
		// i skompilowa�

		glShaderSource(shaderObjectId, shaderCode);
		glCompileShader(shaderObjectId);

		// W kolejnym kroku mo�emy pobra� status kompilacji, czy wszystko si�
		// uda�o. Przekazujemy do funkcji tablic� jednoelementow� do kt�rej
		// funkcja wpisze status - tak/nie

		final int[] compileStatus = new int[1];
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

		// Je�eli chce si� dowiedzie� wi�cej informacji, to nale�y zapyta�
		// funkcj� obiekt o wskazanym id, kt�ry przedstawi status w formie
		// zrozumia�ej dla cz�owieka. Je�eli shader b�dzie mia� co� ciekawego do
		// powiedzenia to zapisze do logu

		Debug.openglStatus(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:" + glGetShaderInfoLog(shaderObjectId));

		// 0 oznacza, �e si� nie uda�o, wtedy nale�y usun�� obiekt

		if (compileStatus[0] == 0) {
			glDeleteShader(shaderObjectId);
			Debug.openglWarning(TAG, "Compilation of shader failed.");
			return 0;
		}

		return shaderObjectId;

	}

	public static int linkProgram(int vertexShaderId, int fragmentShaderId) {

		final int programObjectId = glCreateProgram();
		if (programObjectId == 0) {
			Debug.openglWarning(TAG, "Could not create new program");
			return 0;
		}

		glAttachShader(programObjectId, vertexShaderId);
		glAttachShader(programObjectId, fragmentShaderId);

		glLinkProgram(programObjectId);

		final int[] linkStatus = new int[1];
		glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

		// Print the program info log to the Android log output.
		Log.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId));

		if (linkStatus[0] == 0) {
			// If it failed, delete the program object.
			glDeleteProgram(programObjectId);
			Log.w(TAG, "Linking of program failed.");
			return 0;
		}

		return programObjectId;

	}

	public static boolean validateProgram(int programObjectId) {
		glValidateProgram(programObjectId);
		final int[] validateStatus = new int[1];
		glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
		Debug.openglStatus(TAG, "Results of validating program: " + validateStatus[0] + "\nLog:" + glGetProgramInfoLog(programObjectId));
		return validateStatus[0] != 0;
	}

	public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
		
		int program;
		
		// Compile the shaders.
		int vertexShader = compileVertexShader(vertexShaderSource);
		int fragmentShader = compileFragmentShader(fragmentShaderSource);
		
		// Link them into a shader program.
		program = linkProgram(vertexShader, fragmentShader);
		validateProgram(program);
	
		return program;
	
	}

}
