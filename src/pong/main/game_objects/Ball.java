package pong.main.game_objects;

import static pong.main.util.Util.getAngle;

import java.awt.Point;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import pong.main.Collision;
import pong.main.Main;
import pong.main.util.Rectangle;
import pong.main.util.Util;

public class Ball extends PhysicsGameObject {

	private static double x = 390;
	private static double y = 290;
	private static final double width = 20;
	private static final double height = 20;

	private double maxSpeed = 3;
	private double movSpeedx = -3;
	private double movSpeedy = 0;
	// >> TODO: add max speed + acceleration.

	private final String name = "Ball";
	private long timer = 0;

	public Ball() {
		this(new Random().nextInt(181));
	}

	public Ball(double deg) {
		super(new Rectangle(x, y, (int) width, (int) height));
		timer = System.currentTimeMillis();
		movSpeedy = (double) (Math.sin(Math.toRadians(deg)) * maxSpeed);
		movSpeedx = movSpeedx * (new Random().nextInt(2) == 0 ? 1 : -1);
	}

	@Override
	public void update() {
		if (Main.isHost || !Main.isOnline) {
			x += movSpeedx;
			y += movSpeedy;
		}
		hBox.updateCoords(x, y);
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(.75f, .75f, .75f);
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
		maxSpeed = 3;
		movSpeedx = -3;
		movSpeedy = (double) (Math.sin(Math.toRadians(new Random().nextInt(181))) * maxSpeed);
		movSpeedx = movSpeedx * (new Random().nextInt(2) == 0 ? 1 : -1);
	}

	@Override
	public void collision(Collision col) {
		super.collision(col);
		maxSpeed += 0.05;
	}

	public void hitObject(boolean notTheBoard, double disFromCenter) {
		if (System.currentTimeMillis() - timer >= 100) {
			if (notTheBoard) {
				if (disFromCenter == 0)
					movSpeedy = 0;
				else
					movSpeedy = (double) (Math.sin(Math.toRadians(getAngle(disFromCenter))) * maxSpeed);
				movSpeedx = -movSpeedx;
				movSpeedx = (movSpeedx < 0 ? -maxSpeed : maxSpeed);
			} else {
				movSpeedy = -movSpeedy;
				movSpeedy = (movSpeedy < 0 ? -maxSpeed : maxSpeed);
			}
			timer = System.currentTimeMillis();
		}
	}

	public void hit_PlayerOrAI_TopOrBottom(byte topOrBottom) {
		if (System.currentTimeMillis() - timer >= 100) {
			if (topOrBottom != Rectangle.UP && topOrBottom != Rectangle.DOWN)
				throw new RuntimeException(
						"This function can only run with collision on edges 1 (Top \\ Up) and 3 (Bottom \\ Down).");
			if (movSpeedy == 0)
				movSpeedy = (double) (Math.sin(Math.toRadians((topOrBottom == Rectangle.UP ? 45 : -45))) * maxSpeed);
			else {
				double angle = Math.toDegrees(Math.asin(movSpeedy / maxSpeed));
				if (topOrBottom == Rectangle.UP && angle < 0 || topOrBottom == Rectangle.DOWN && angle > 0) {
					// TODO: Add speed.
				} else if (topOrBottom == Rectangle.UP && angle > 0 || topOrBottom == Rectangle.DOWN && angle < 0) {
					angle = -angle;
					movSpeedy = Math.sin(Math.toRadians(angle)) * maxSpeed;
				}
			}
			timer = System.currentTimeMillis();
		}
	}

	@Override
	public void executeUse(byte type, String name, Object[] data) {
		if (type == Collision.FUNCTION) {
			if (name.equalsIgnoreCase("hitObject"))
				hitObject((boolean) data[0], (double) data[1]);
			else if (name.equalsIgnoreCase("hit_PlayerOrAI_TopOrBottom"))
				hit_PlayerOrAI_TopOrBottom((byte) data[0]);
		} else if (type == Collision.VARIABLE) {
			if (name.equalsIgnoreCase("movSpeedx"))
				movSpeedx = (int) data[0];
			else if (name.equalsIgnoreCase("movSpeedy"))
				movSpeedy = (int) data[0];
			else
				throw new IllegalArgumentException("The name given does not match any variable available here.");

		}
	}

	@Override
	public String getName() {
		return name;
	}

	public Point getCenterPoint() {
		return new Point((int) x + (int) width / 2, (int) y + (int) height / 2);
	}

	public void setPosition(double posX, double posY) {
		x = posX;
		y = posY;
	}

	public double getPosition(byte type) {
		if (type == Util.X)
			return x;
		else if (type == Util.Y)
			return y;
		throw new IllegalArgumentException("type must be 0 or 1.");
	}
}
