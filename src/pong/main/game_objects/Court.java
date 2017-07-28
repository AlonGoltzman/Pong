package pong.main.game_objects;

import org.lwjgl.opengl.GL11;

import pong.main.util.Rectangle;

public class Court extends BaseGameObject {

	private double x = 395;
	private double y = 0;
	private final double width = 10;
	private final double height = 58;

	private String name = "Court";
	private final int nO_Lines = 10;

	public final byte TOP_BOT = 0;
	public final byte RIGHT_LEFT = 1;

	public Court() {
		super(new Rectangle(0, 0, 800, 600));
	}

	@Override
	public void render() {
		for (int i = 0; i < nO_Lines; i++) {
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glColor3f(.5f, .5f, .5f);
				GL11.glVertex3d(x, y + (height * i + 20), 0);
				GL11.glVertex3d(x, y + (height * i + height), 0);
				GL11.glVertex3d(x + width, y + (height * i + height), 0);
				GL11.glVertex3d(x + width, y + (height * i + 20), 0);
			}
			GL11.glEnd();
		}
		//
	}

	@Override
	public void update() {
	}

	@Override
	public void destroy() {
		x = 395;
		y = 0;

	}

	@Override
	public String getName() {
		return name;
	}

	public boolean outOfCourtPartial(BaseGameObject obj, byte dir) {
		Rectangle rect = obj.hBox;
		if (dir != TOP_BOT && dir != RIGHT_LEFT)
			throw new IllegalArgumentException("Must be 0 [Top or Bottom], or 1 [Right or Left].");
		// hBox.
		double objHigh = (dir == TOP_BOT ? rect.getHighestY() : rect.getFurthestX()),
				objLow = (dir == TOP_BOT ? rect.getLowestY() : rect.getClosestX());
		return (objHigh > (dir == TOP_BOT ? hBox.getHighestY() : hBox.getFurthestX())
				|| objLow < (dir == TOP_BOT ? hBox.getLowestY() : hBox.getClosestX()));
	}
}
