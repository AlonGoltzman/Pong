package pong.main.comms;

import static pong.main.util.Util.nullCheck;

import java.io.DataOutputStream;
import java.io.IOException;

import pong.main.Main;
import pong.main.game_objects.Ball;
import pong.main.game_objects.Player;
import pong.main.game_objects.ScoreKeeper;
import pong.main.util.Util;

public class OutputData extends Thread {

	private DataOutputStream out;
	private Player player;
	private Ball ball;
	private ScoreKeeper keeper;

	public OutputData(DataOutputStream out, Player player) {
		this(out, player, null, null);
	}

	public OutputData(DataOutputStream out, Player player, Ball ball) {
		this(out, player, ball, null);
	}

	public OutputData(DataOutputStream out, Player player, ScoreKeeper keeper) {
		this(out, player, null, keeper);
	}

	public OutputData(DataOutputStream out, Player player, Ball ball, ScoreKeeper keeper) {
		this.out = out;
		this.ball = ball;
		this.keeper = keeper;
		this.player = player;
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				out.writeDouble(player.getPosition(Util.X));
				out.writeDouble(player.getPosition(Util.Y));
				if (!nullCheck(ball)) {
					out.writeDouble(ball.getPosition(Util.X));
					out.writeDouble(ball.getPosition(Util.Y));
				}
				if (!nullCheck(keeper)) {
					out.writeInt(keeper.getScore(Util.LEFT));
					out.writeInt(keeper.getScore(Util.RIGHT));
				}
			} catch (IOException ex) {
				System.out.println("Stopping comms...");
				Main.stop();
				break;
			}
		}
	}
}
