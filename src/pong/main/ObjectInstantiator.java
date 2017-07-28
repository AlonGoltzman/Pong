package pong.main;

import pong.main.game_objects.AI;
import pong.main.game_objects.Ball;
import pong.main.game_objects.BaseScreenObject;
import pong.main.game_objects.Court;
import pong.main.game_objects.OnlinePlayer;
import pong.main.game_objects.Player;
import pong.main.game_objects.ScoreItem;

public final class ObjectInstantiator {

	public enum GameObjects {
		PLAYER, BALL, AI, ONLINE_PLAYER, COURT, SCORE_ITEM;

		static boolean contains(GameObjects obj) {
			return obj == PLAYER || obj == BALL || obj == AI || obj == ONLINE_PLAYER || obj == COURT;
		}
	}

	public static BaseScreenObject CreateNewObject(GameObjects object, Object sideIfValid) {
		if (GameObjects.contains(object)) {
			if (object == GameObjects.PLAYER)
				return new Player((byte) sideIfValid);
			else if (object == GameObjects.AI)
				return new AI((byte) sideIfValid);
			else if (object == GameObjects.COURT)
				return new Court();
			else if (object == GameObjects.ONLINE_PLAYER)
				return new OnlinePlayer((byte) sideIfValid);
			else if (object == GameObjects.BALL)
				return new Ball();
			else if (object == GameObjects.SCORE_ITEM)
				return new ScoreItem(0, (byte) 10, (byte) sideIfValid);
		}
		throw new IllegalArgumentException(
				"The object you requested to instanciate does not exist in gameobjects enum.");
	}

}
