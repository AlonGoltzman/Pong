package pong.main.util;

public enum NumberCoordGen {

	ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

	private enum Quads {
		LOWER, TOP_FULL, TOP_MIDDLE_LEFT, RIGHT_FULL, RIGHT_MIDDLE_UPPER, RIGHT_MIDDLE_LOWER, LEFT_FULL, LEFT_MIDDLE_UPPER, LEFT_MIDDLE_LOWER, MIDDLE, MIDDLE_VERTICAL
	}

	// 100 pixel width, 200 pixel height, "0,0" -> "150,200", "550,200"
	public static MultiMap<Double, Double> getCoord(NumberCoordGen num, byte width, byte side) {
		MultiMap<Double, Double> coords = new MultiMap<Double, Double>(20);
		switch (num) {
		case ZERO:
			addQuad(coords, width, side, Quads.LOWER);
			addQuad(coords, width, side, Quads.RIGHT_FULL);
			addQuad(coords, width, side, Quads.LEFT_FULL);
			addQuad(coords, width, side, Quads.TOP_FULL);

			break;
		case ONE:
			addQuad(coords, width, side, Quads.TOP_MIDDLE_LEFT);
			addQuad(coords, width, side, Quads.MIDDLE);
			addQuad(coords, width, side, Quads.LOWER);

			break;
		case TWO:
			addQuad(coords, width, side, Quads.LOWER);
			addQuad(coords, width, side, Quads.RIGHT_MIDDLE_UPPER);
			addQuad(coords, width, side, Quads.MIDDLE_VERTICAL);
			addQuad(coords, width, side, Quads.LEFT_MIDDLE_LOWER);
			addQuad(coords, width, side, Quads.TOP_FULL);

			break;
		case THREE:
			addQuad(coords, width, side, Quads.TOP_FULL);
			addQuad(coords, width, side, Quads.RIGHT_FULL);
			addQuad(coords, width, side, Quads.MIDDLE_VERTICAL);
			addQuad(coords, width, side, Quads.LOWER);

			break;
		case FOUR:
			addQuad(coords, width, side, Quads.RIGHT_FULL);
			addQuad(coords, width, side, Quads.MIDDLE_VERTICAL);
			addQuad(coords, width, side, Quads.LEFT_MIDDLE_UPPER);

			break;
		case FIVE:
			addQuad(coords, width, side, Quads.TOP_FULL);
			addQuad(coords, width, side, Quads.LEFT_MIDDLE_UPPER);
			addQuad(coords, width, side, Quads.MIDDLE_VERTICAL);
			addQuad(coords, width, side, Quads.RIGHT_MIDDLE_LOWER);
			addQuad(coords, width, side, Quads.LOWER);

			break;
		case SIX:
			addQuad(coords, width, side, Quads.LOWER);
			addQuad(coords, width, side, Quads.LEFT_FULL);
			addQuad(coords, width, side, Quads.MIDDLE_VERTICAL);
			addQuad(coords, width, side, Quads.RIGHT_MIDDLE_LOWER);
			addQuad(coords, width, side, Quads.TOP_FULL);

			break;
		case SEVEN:
			addQuad(coords, width, side, Quads.RIGHT_FULL);
			addQuad(coords, width, side, Quads.TOP_FULL);

			break;
		case EIGHT:
			addQuad(coords, width, side, Quads.TOP_FULL);
			addQuad(coords, width, side, Quads.RIGHT_FULL);
			addQuad(coords, width, side, Quads.LEFT_FULL);
			addQuad(coords, width, side, Quads.MIDDLE_VERTICAL);
			addQuad(coords, width, side, Quads.LOWER);

			break;
		case NINE:
			addQuad(coords, width, side, Quads.LOWER);
			addQuad(coords, width, side, Quads.RIGHT_FULL);
			addQuad(coords, width, side, Quads.MIDDLE_VERTICAL);
			addQuad(coords, width, side, Quads.LEFT_MIDDLE_UPPER);
			addQuad(coords, width, side, Quads.TOP_FULL);

			break;
		}
		return coords;

	}

	private static void addQuad(MultiMap<Double, Double> coords, double width, byte side, Quads quad) {
		final double xLower = (side == Util.LEFT ? 200.0 : 500.0);
		final double xHigher = (side == Util.LEFT ? 300.0 : 600.0);
		final double yLower = 200.0;
		final double yHigher = 400.0;

		final double yMiddle = 300.0;
		final double xMiddle = xLower + ((xHigher - xLower) / 2.0);

		final double yLowerWithWidth = yLower + width;
		final double xLowerWithWidth = xLower + width;
		final double yHigherNoWidth = yHigher - width;
		final double xHigherNoWidth = xHigher - width;

		final double yMiddleNoWidth = yMiddle - (width / 2.0);
		final double yMiddleWithWidth = yMiddle + (width / 2.0);

		switch (quad) {
		case LOWER:
			coords.put(xLower, yLower);
			coords.put(xLower, yLowerWithWidth);
			coords.put(xHigher, yLowerWithWidth);
			coords.put(xHigher, yLower);
			break;
		case TOP_FULL:
			coords.put(xLower, yHigherNoWidth);
			coords.put(xLower, yHigher);
			coords.put(xHigher, yHigher);
			coords.put(xHigher, yHigherNoWidth);
			break;
		case TOP_MIDDLE_LEFT:
			coords.put(xLower, yHigherNoWidth);
			coords.put(xLower, yHigher);
			coords.put(xMiddle + (width / 2), yHigher);
			coords.put(xMiddle + (width / 2), yHigherNoWidth);
			break;
		case RIGHT_FULL:
			coords.put(xHigherNoWidth, yLowerWithWidth);
			coords.put(xHigherNoWidth, yHigherNoWidth);
			coords.put(xHigher, yHigherNoWidth);
			coords.put(xHigher, yLowerWithWidth);
			break;
		case RIGHT_MIDDLE_UPPER:
			coords.put(xHigherNoWidth, yMiddleNoWidth);
			coords.put(xHigherNoWidth, yHigherNoWidth);
			coords.put(xHigher, yHigherNoWidth);
			coords.put(xHigher, yMiddleNoWidth);
			break;
		case RIGHT_MIDDLE_LOWER:
			coords.put(xHigherNoWidth, yLowerWithWidth);
			coords.put(xHigherNoWidth, yMiddleWithWidth);
			coords.put(xHigher, yMiddleWithWidth);
			coords.put(xHigher, yLowerWithWidth);
			break;
		case LEFT_FULL:
			coords.put(xLower, yLowerWithWidth);
			coords.put(xLower, yHigherNoWidth);
			coords.put(xLowerWithWidth, yHigherNoWidth);
			coords.put(xLowerWithWidth, yLowerWithWidth);
			break;
		case LEFT_MIDDLE_UPPER:
			coords.put(xLower, yMiddleNoWidth);
			coords.put(xLower, yHigherNoWidth);
			coords.put(xLowerWithWidth, yHigherNoWidth);
			coords.put(xLowerWithWidth, yMiddleNoWidth);
			break;
		case LEFT_MIDDLE_LOWER:
			coords.put(xLower, yLowerWithWidth);
			coords.put(xLower, yMiddleWithWidth);
			coords.put(xLowerWithWidth, yMiddleWithWidth);
			coords.put(xLowerWithWidth, yLowerWithWidth);
			break;
		case MIDDLE:
			coords.put(xMiddle - (width / 2), yLowerWithWidth);
			coords.put(xMiddle - (width / 2), yHigherNoWidth);
			coords.put(xMiddle + (width / 2), yHigherNoWidth);
			coords.put(xMiddle + (width / 2), yLowerWithWidth);
			break;

		case MIDDLE_VERTICAL:
			coords.put(xLowerWithWidth, yMiddleNoWidth);
			coords.put(xLowerWithWidth, yMiddleWithWidth);
			coords.put(xHigherNoWidth, yMiddleWithWidth);
			coords.put(xHigherNoWidth, yMiddleNoWidth);
			break;
		}
	}
}
