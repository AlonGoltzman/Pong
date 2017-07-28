package pong.main.game_objects;

import org.lwjgl.opengl.GL11;

import pong.main.Collision;
import pong.main.WorldManager;
import pong.main.util.Rectangle;

public class AI extends PhysicsGameObject {

	private static double x = 740;
	private static double y = 200;
	private static final double width = 20;
	private static final double height = 200;

	private boolean movAllowedUp = true;
	private boolean movAllowedDown = true;
	private boolean movUp = false;
	private boolean movDown = false;

	private double movSpeed = 0;

	private final String name = "AI";

	public AI(byte side) {
		super(new Rectangle(x, y, width, height));
		x = side == 0 ? 40 : 740;
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(.75f, .5f, .5f);
			GL11.glVertex3d(x, y, 0);
			GL11.glVertex3d(x, y + height, 0);
			GL11.glVertex3d(x + width, y + height, 0);
			GL11.glVertex3d(x + width, y, 0);
		}
		GL11.glEnd();
	}

	@Override
	public void destroy() {
		x = 390;
		y = 290;
		movAllowedUp = true;
		movAllowedDown = true;
		movUp = false;
		movDown = false;
		movSpeed = 0;
	}

	@Override
	public void update() {
		// =========== Logic ================

		double ballY = ((Ball) WorldManager.getInstance(null).getObject("Ball")).getCenterPoint().y;
		if (ballY > y + (height / 2) + 3) {
			movUp = true;
			movDown = false;
		} else if (ballY < y + (height / 2) - 3) {
			movDown = true;
			movUp = false;
		} else {
			movUp = false;
			movDown = false;
		}
		// =========== Execution ===============
		if (movUp && movAllowedUp) {
			movSpeed = 3;
			movAllowedDown = true;
		} else if (movDown && movAllowedDown) {
			movSpeed = -3;
			movAllowedUp = true;
		} else
			movSpeed = 0;

		y += movSpeed;
		hBox.updateCoords(x, y);
	}

	@Override
	public void executeUse(byte type, String name, Object[] data) {
		if (type == Collision.FUNCTION) {

		} else if (type == Collision.VARIABLE) {
			if (name.equalsIgnoreCase("x"))
				x = (double) data[0];
			else if (name.equalsIgnoreCase("y"))
				y = (double) data[0];
			else if (name.equalsIgnoreCase("movAllowedUp"))
				movAllowedUp = (boolean) data[0];
			else if (name.equalsIgnoreCase("movAllowedDown"))
				movAllowedDown = (boolean) data[0];
			else if (name.equalsIgnoreCase("movSpeed"))
				movSpeed = (double) data[0];
			else
				throw new IllegalArgumentException("The name given does not match any variable available here.");

		}
	}

	@Override
	public String getName() {
		return name;
	}

}
