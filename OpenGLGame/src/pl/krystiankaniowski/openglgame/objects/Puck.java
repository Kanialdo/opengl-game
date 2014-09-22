package pl.krystiankaniowski.openglgame.objects;

import java.util.List;

import pl.krystiankaniowski.openglgame.data.VertexArray;
import pl.krystiankaniowski.openglgame.objects.ObjectBuilder.DrawCommand;
import pl.krystiankaniowski.openglgame.objects.ObjectBuilder.GeneratedData;
import pl.krystiankaniowski.openglgame.programs.ColorShaderProgram;
import pl.krystiankaniowski.openglgame.utils.Geometry.Cylinder;
import pl.krystiankaniowski.openglgame.utils.Geometry.Point;

public class Puck {

	// =========================================================================
	// ----- STA£E -------------------------------------------------------------
	// =========================================================================

	public static final String TAG = Puck.class.getSimpleName();

	private static final int POSITION_COMPONENT_COUNT = 3;
	public final float radius, height;
	private final VertexArray vertexArray;
	private final List<DrawCommand> drawList;

	// =========================================================================
	// ----- KONSTRUKTOR -------------------------------------------------------
	// =========================================================================

	public Puck(float radius, float height, int numPointsAroundPuck) {
		GeneratedData generatedData = ObjectBuilder.createPuck(new Cylinder(new Point(0f, 0f, 0f), radius, height), numPointsAroundPuck);
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
