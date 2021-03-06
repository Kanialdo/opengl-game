package pl.krystiankaniowski.openglgame.utils;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class TextureHelper {

	// =========================================================================
	// ----- STA�E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = TextureHelper.class.getSimpleName();

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	public static int loadTexture(Context context, int resourceId) {

		final int[] textureObjectIds = new int[1];

		glGenTextures(1, textureObjectIds, 0);

		if (textureObjectIds[0] == 0) {
			Log.w(TAG, "Could not generate a new OpenGL texture object.");
			return 0;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
		if (bitmap == null) {
			Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}

		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

		// ustalaniae spos�b skalowania przez OpenGL
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

		// Zwolnienie pami�ci
		bitmap.recycle();

		glGenerateMipmap(GL_TEXTURE_2D);

		// Zwolnienie - Passing 0 to glBindTexture unbinds from the current
		// texture.
		glBindTexture(GL_TEXTURE_2D, 0);

		return textureObjectIds[0];

	}

}
