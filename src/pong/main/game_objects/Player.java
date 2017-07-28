package pong.main.game_objects;

import static pong.main.util.Util.nullCheck;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import pong.main.Collision;
import pong.main.util.Rectangle;
import pong.main.util.Util;

public class Player extends PhysicsGameObject {

	private static double x = 40;
	private static double y = 200;
	private static final double width = 20;
	private static final double height = 200;

	private byte side; // >> 0 for left 1 for right.

	private ArrayList<Integer> keysPressed;

	private boolean movAllowedUp = true;
	private boolean movAllowedDown = true;

	private int movSpeed = 0;

	private final String name = "Player";

	public Player(byte side) {
		super(new Rectangle(x, y, width, height));
		this.side = side;
		x = (side == 0 ? 40 : 740);
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(.5f, .5f, .5f);
			GL11.glVertex3d(x, y, 0);
			GL11.glVertex3d(x, y + height, 0);
			GL11.glVertex3d(x + width, y + height, 0);
			GL11.glVertex3d(x + width, y, 0);
		}
		GL11.glEnd();
	}

	@Override
	public void update() {
		if (!nullCheck(keysPressed) && !keysPressed.isEmpty()) {
			boolean upPressed = (side == 0 ? keysPressed.contains(GLFW.GLFW_KEY_W)
					: keysPressed.contains(GLFW.GLFW_KEY_UP));
			boolean downPressed = (side == 0 ? keysPressed.contains(GLFW.GLFW_KEY_S)
					: keysPressed.contains(GLFW.GLFW_KEY_DOWN));
			if (downPressed && !movAllowedUp)
				movAllowedUp = true;
			if (upPressed && !movAllowedDown)
				movAllowedDown = true;
			movSpeed = (upPressed && movAllowedUp ? 3 : (downPressed && movAllowedDown ? -3 : 0));
		} else
			movSpeed = 0;
		y += movSpeed;

		hBox.updateCoords(x, y);
	}

	@Override
	public void destroy() {
		x = (side == 0 ? 40 : 740);
		y = 200;
		keysPressed.clear();
		movAllowedUp = true;
		movAllowedDown = true;
		movSpeed = 0;
	}

	public void input(Integer[] pressedKeys) {
		keysPressed = new ArrayList<Integer>(Arrays.asList(pressedKeys));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeUse(byte type, String name, Object[] data) {
		if (type == Collision.FUNCTION) {

		} else if (type == Collision.VARIABLE) {
			if (name.equalsIgnoreCase("x"))
				x = (int) data[0];
			else if (name.equalsIgnoreCase("y"))
				y = (int) data[0];
			else if (name.equalsIgnoreCase("side"))
				side = (byte) data[0];
			else if (name.equalsIgnoreCase("movAllowedUp"))
				movAllowedUp = (boolean) data[0];
			else if (name.equalsIgnoreCase("movAllowedDown"))
				movAllowedDown = (boolean) data[0];
			else if (name.equalsIgnoreCase("movSpeed"))
				movSpeed = (int) data[0];
			else if (name.equalsIgnoreCase("keysPressed"))
				keysPressed = (ArrayList<Integer>) data[0];
			else
				throw new IllegalArgumentException("The name given does not match any variable available here.");

		}
	}

	@Override
	public String getName() {
		return name;
	}

	public byte getSide() {
		return side;
	}

	public double getPositionY() {
		return y;
	}

	public double getPosition(byte type) {
		if (type == Util.X)
			return x;
		else if (type == Util.Y)
			return y;
		throw new IllegalArgumentException("type must be 0 or 1.");
	}

}
