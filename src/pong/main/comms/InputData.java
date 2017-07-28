package pong.main.comms;

import java.io.DataInputStream;
import java.io.IOException;

import pong.main.Main;
import pong.main.WorldManager;
import pong.main.game_objects.ScoreKeeper;
import pong.main.util.Util;

public class InputData extends Thread {

	private DataInputStream in;
	private boolean isHost;

	public InputData(DataInputStream in, boolean isHost) {
		this.in = in;
		this.isHost = isHost;
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				double otherPlayerPosX = in.readDouble();
				double otherPlayerPosY = in.readDouble();
				double ballX = in.readDouble();
				double ballY = in.readDouble();
				WorldManager.getInstance(null).updateObjectLocation("OnlinePlayer", otherPlayerPosX, otherPlayerPosY);
				if (!isHost) {
					int scoreRight = in.readInt();
					int scoreLeft = in.readInt();
					WorldManager.getInstance(null).updateObjectLocation("Ball", ballX, ballY);
					ScoreKeeper.setPoint(Util.LEFT, scoreLeft);
					ScoreKeeper.setPoint(Util.RIGHT, scoreRight);
				}
			} catch (IOException ex) {
				System.out.println("Stopping comms...");
				Main.stop();
				break;
			}
		}
	}
}
