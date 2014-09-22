package pl.krystiankaniowski.openglgame.objects;

import java.util.ArrayList;
import java.util.List;

import pl.krystiankaniowski.openglgame.data.VertexArray;
import pl.krystiankaniowski.openglgame.objects.ObjectBuilder.DrawCommand;
import pl.krystiankaniowski.openglgame.objects.ObjectBuilder.GeneratedData;
import pl.krystiankaniowski.openglgame.programs.ColorShaderProgram;
import pl.krystiankaniowski.openglgame.utils.Geometry.Point;

public class Mallet {

	// =========================================================================
	// ----- STA�E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = Mallet.class.getSimpleName();

	private static final int POSITION_COMPONENT_COUNT = 3;
	public final float radius;
	public final float height;
	private final VertexArray vertexArray;
	private final List<DrawCommand> drawList;

	// =========================================================================
	// ----- KONSTRUKTOR -------------------------------------------------------
	// =========================================================================

	public Mallet(float radius, float height, int numPointsAroundMallet) {
		GeneratedData generatedData = ObjectBuilder.createMallet(new Point(0f, 0f, 0f), radius, height, numPointsAroundMallet);
		this.radius = radius;
		this.height = height;
		vertexArray = new VertexArray(generatedData.vertexData);
		drawList = generatedData.drawList;
	}

	// =========================================================================
	// ----- LOGIKA ------------------------------------------------------------
	// =========================================================================

	public void bindData(ColorShaderProgram colorProgram) {
		vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
	}

	public void draw() {
		for (DrawCommand drawCommand : drawList) {
			drawCommand.draw();
		}
	}

}
