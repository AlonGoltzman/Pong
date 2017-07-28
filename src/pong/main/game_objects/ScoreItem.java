package pong.main.game_objects;

import static pong.main.util.Util.convertScoreToCoord;
import static pong.main.util.Util.nullCheck;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import pong.main.util.DualSingularSet;
import pong.main.util.MultiMap;
import pong.main.util.NumberCoordGen;

public class ScoreItem extends BaseScreenObject {

	private MultiMap<Double, Double> coords;
	private ArrayList<MultiMap<Double, Double>> quads = new ArrayList<MultiMap<Double, Double>>() {

		private static final long serialVersionUID = 7938642190700073762L;

		@Override
		public boolean add(MultiMap<Double, Double> e) {
			if (size() <= 5)
				return super.add(e);
			else
				throw new IllegalArgumentException("Can't add more than 5 elements");
		}
	};

	private int score;
	private byte width;
	private byte side;

	public ScoreItem(byte side) {
		this((int) 0, side);
	}

	public ScoreItem(int score, byte side) {
		this(score, (byte) 10, side);
	}

	public ScoreItem(int score, byte width, byte side) {
		this(NumberCoordGen.getCoord(convertScoreToCoord(score), width, side), score, width, side);
	}

	public ScoreItem(final MultiMap<Double, Double> coords, int score, byte width, byte side) {
		this.score = score;
		this.width = width;
		this.side = side;
		if (!nullCheck(coords) && !coords.isEmpty()) {
			this.coords = coords;
			genCoordParts();
		} else
			throw new IllegalArgumentException("Coords that were given are either empty or null");
	}

	@Override
	public void render() {
		for (MultiMap<Double, Double> quad : quads) {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1f, 1f, 1f);
			{
				for (int i = 0; i < quad.size(); i++) {
					DualSingularSet<Double, Double> set = quad.get(i);
					GL11.glVertex3d(set.getKey(), set.getValue(), 0);
				}
			}
			GL11.glEnd();
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void destroy() {
		coords = null;
		quads = new ArrayList<MultiMap<Double, Double>>() {

			private static final long serialVersionUID = 7938642190700073762L;

			@Override
			public boolean add(MultiMap<Double, Double> e) {
				if (size() <= 5)
					return super.add(e);
				else
					throw new IllegalArgumentException("Can't add more than 5 elements");
			}
		};
	}

	public void point(int i) {
		score = i;
		coords = NumberCoordGen.getCoord(convertScoreToCoord(score), width, side);
		genCoordParts();
	}

	private void genCoordParts() {
		quads = coords.splitFor(4);
	}

}
