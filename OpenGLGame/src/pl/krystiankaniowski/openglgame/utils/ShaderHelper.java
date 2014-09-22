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
	// ----- STA£E -------------------------------------------------------------
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

		// S¹ dwa typy shaderów - GL_VERTEX_SHADER i GL_FRAGMENT_SHADER
		// Je¿eli chcemy utworzyæ shader to wo³amy glCreateShader(type) i
		// dostajemy id obiektu openGl który bêdzie tym shaderem

		// Je¿eli id == 0 to oznacza to b³¹d
		// OpenGl nie rzuca wyj¹tków natychmiast, dlatego jak dostaniemy 0 to
		// mo¿emy sprawdziæ jaka czêœæ naszego API nie wysz³a

		final int shaderObjectId = glCreateShader(type);
		if (shaderObjectId == 0) {
			Debug.openglWarning(TAG, "Could not create new shader.");
			return 0;
		}

		// Nastêpnie nale¿y przekazaæ kod Ÿród³owy shadera do w³aœciwego obiektu
		// i skompilowaæ

		glShaderSource(shaderObjectId, shaderCode);
		glCompileShader(shaderObjectId);

		// W kolejnym kroku mo¿emy pobraæ status kompilacji, czy wszystko siê
		// uda³o. Przekazujemy do funkcji tablicê jednoelementow¹ do której
		// funkcja wpisze status - tak/nie

		final int[] compileStatus = new int[1];
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

		// Je¿eli chce siê dowiedzieæ wiêcej informacji, to nale¿y zapytaæ
		// funkcj¹ obiekt o wskazanym id, który przedstawi status w formie
		// zrozumia³ej dla cz³owieka. Je¿eli shader bêdzie mia³ coœ ciekawego do
		// powiedzenia to zapisze do logu

		Debug.openglStatus(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:" + glGetShaderInfoLog(shaderObjectId));

		// 0 oznacza, ¿e siê nie uda³o, wtedy nale¿y usun¹æ obiekt

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
