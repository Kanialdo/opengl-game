package pl.krystiankaniowski.openglgame.utils;

public class MatrixHelper {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = MatrixHelper.class.getSimpleName();

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {

		// Calculating the Focal Length

		final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
		final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

		// Writing Out the Matrix

		// @formatter:off 
		
		// MACIERZ
		// [  a/aspect  0  0                    0  ]
		// [  0         a  0                    0  ]
		// [  0         0  -((f+n)/(f-n))      -1  ]
		// [  0         0  -((2f *f*n)/(f-n))   0  ]
		
		// @formatter:on 

		m[0] = a / aspect;
		m[1] = 0f;
		m[2] = 0f;
		m[3] = 0f;
		m[4] = 0f;
		m[5] = a;
		m[6] = 0f;
		m[7] = 0f;
		m[8] = 0f;
		m[9] = 0f;
		m[10] = -((f + n) / (f - n));
		m[11] = -1f;
		m[12] = 0f;
		m[13] = 0f;
		m[14] = -((2f * f * n) / (f - n));
		m[15] = 0f;

	}
}
