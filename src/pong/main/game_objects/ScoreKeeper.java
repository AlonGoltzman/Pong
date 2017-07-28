package pong.main.game_objects;

import pong.main.Main;
import pong.main.util.Util;

public final class ScoreKeeper {

	private static int score_left = 0;
	private static int score_right = 0;

	private static int scoreLimit = 9;
	private static int setLimit = 1;

	private static int setNum = 1;

	private static ScoreItem[] scores;
	private static ScoreKeeper keeper;

	public static ScoreKeeper getInstance(ScoreItem... displays) {
		return keeper == null ? new ScoreKeeper(displays) : keeper;
	}

	private ScoreKeeper(ScoreItem... displays) {
		if (displays != null)
			scores = displays;
	}

	public static void pointTo(byte side) {
		if (side == Util.LEFT) {
			score_left++;
		} else if (side == Util.RIGHT) {
			score_right++;
		}
		scores[side].point(side == Util.LEFT ? score_left : score_right);
		checkLimit();
	}

	public static byte end() {
		return (score_left > score_right ? Util.LEFT : (score_left == score_right ? Util.DRAW : Util.RIGHT));
	}

	public static void reset(boolean newSet) {
		if (newSet && setNum < setLimit)
			setNum++;
		else if (setNum == setLimit && newSet)
			Main.stop();
		score_left = 0;
		score_right = 0;
		scores[Util.LEFT].point(0);
		scores[Util.RIGHT].point(0);
	}

	public static void setPoint(byte side, int score) {
		if (side == Util.LEFT) {
			score_left = score;
		} else if (side == Util.RIGHT) {
			score_right = score;
		}
		scores[side].point(side == Util.LEFT ? score_left : score_right);
		checkLimit();
	}

	public int getScore(byte side) {
		return (side == Util.LEFT ? score_left : score_right);
	}

	public void setGameLimits(int maxScore, int maxSet) {
		scoreLimit = maxScore;
		setLimit = maxSet;
	}

	private static void checkLimit() {
		if (score_left == scoreLimit)
			Main.stop(Util.LEFT);
		if (score_right == scoreLimit)
			Main.stop(Util.RIGHT);

	}
}
