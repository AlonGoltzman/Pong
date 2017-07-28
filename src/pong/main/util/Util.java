package pong.main.util;

import java.util.Map;

public final class Util {

	public static final byte X = 0;
	public static final byte Y = 1;
	public static final byte LEFT = 0;
	public static final byte RIGHT = 1;
	public static final byte DRAW = -1;

	public static int b2bit(boolean flag) {
		return (byte) (flag ? 1 : 0);
	}

	public static float i2float(int i) {
		return (float) i;
	}

	public static int f2int(float f) {
		return (int) f;
	}

	public static double i2double(int i) {
		return (double) i;
	}

	public static double getAngle(double distance) {
		if (distance == 0)
			return 0;
		else
			return (double) /* Max distance from center. */(80.0 / 119.0) * distance;
	}

	public static boolean nullCheck(Object... item) {
		boolean flag = false;
		for (Object object : item) {
			if (object == null)
				flag = true;
		}
		return flag;
	}

	public static boolean emptyCheck(Map<?, ?>... item) {
		boolean flag = true;
		for (Map<?, ?> map : item) {
			if (!map.isEmpty())
				flag = false;
		}
		return flag;
	}

	public static NumberCoordGen convertScoreToCoord(int score) {
		if (score < 10) {
			NumberCoordGen num = null;
			switch (score) {
			case 0:
				num = NumberCoordGen.ZERO;
				break;
			case 1:
				num = NumberCoordGen.ONE;
				break;
			case 2:
				num = NumberCoordGen.TWO;
				break;
			case 3:
				num = NumberCoordGen.THREE;
				break;
			case 4:
				num = NumberCoordGen.FOUR;
				break;
			case 5:
				num = NumberCoordGen.FIVE;
				break;
			case 6:
				num = NumberCoordGen.SIX;
				break;
			case 7:
				num = NumberCoordGen.SEVEN;
				break;
			case 8:
				num = NumberCoordGen.EIGHT;
				break;
			case 9:
				num = NumberCoordGen.NINE;
				break;
			}
			return num;
		}
		throw new IllegalArgumentException("Score can be up to 9");
	}
}
