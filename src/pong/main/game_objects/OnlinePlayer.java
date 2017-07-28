package pong.main.game_objects;

import org.lwjgl.opengl.GL11;

import pong.main.util.Rectangle;

public class OnlinePlayer extends PhysicsGameObject {
	private static double x = 40;
	private static double y = 300;
	private static final double width = 20;
	private static final double height = 200;

	private byte side; // >> 0 for left 1 for right.

	private final String name = "OnlinePlayer";

	public OnlinePlayer(byte side) {
		super(new Rectangle(x, y, width, height));
		this.side = side;
		x = (side == 0 ? 40 : 740);
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(1f, .25f, .25f);
			GL11.glVertex3d(x, y, 0);
			GL11.glVertex3d(x, y + height, 0);
			GL11.glVertex3d(x + width, y + height, 0);
			GL11.glVertex3d(x + width, y, 0);
		}
		GL11.glEnd();
	}

	@Override
	public void update() {
		hBox.updateCoords(x, y);
	}

	@Override
	public void destroy() {
		x = (side == 0 ? 40 : 740);
		y = 300;
	}

	@Override
	public String getName() {
		return name;
	}

	public byte getSide() {
		return side;
	}

	public void setPosition(double posX, double posY) {
		x = posX;
		y = posY;
	}

}
