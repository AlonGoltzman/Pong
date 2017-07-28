package pong.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import pong.main.ObjectInstantiator.GameObjects;
import pong.main.comms.InputData;
import pong.main.comms.OutputData;
import pong.main.game_objects.Ball;
import pong.main.game_objects.BaseGameObject;
import pong.main.game_objects.BaseScreenObject;
import pong.main.game_objects.OnlinePlayer;
import pong.main.game_objects.Player;
import pong.main.game_objects.ScoreItem;
import pong.main.game_objects.ScoreKeeper;
import pong.main.util.Couple;
import pong.main.util.KeyHandler;

public class WorldManager extends BaseScreenObject {

	private static WorldManager $instance = null;

	private ArrayList<BaseGameObject> wObjects = new ArrayList<BaseGameObject>();
	private ArrayList<BaseScreenObject> sObjects = new ArrayList<BaseScreenObject>();
	private KeyHandler keyHandler;

	private GameObjects[] currentScreenObjects;

	private byte playerSide;
	private byte secPlayerSide;

	private InputData inData;
	private OutputData outData;
	private ServerSocket hostToSRV_Socket;
	private Socket fromClientToSRV_Socket;
	private Socket incomingClientToSRV_Socket;

	// ================== SingleTon =======================
	private WorldManager(KeyHandler kHandler) {
		keyHandler = kHandler;
	}

	public static WorldManager getInstance(KeyHandler kHandler) {
		return ($instance == null ? $instance = new WorldManager(kHandler) : $instance);
	}

	// ========== Overridden Methods ========================
	@Override
	public void render() {
		if (wObjects.size() != 0)
			for (BaseGameObject bgO : wObjects)
				bgO.render();
		if (sObjects.size() != 0)
			for (BaseScreenObject bsO : sObjects) {
				bsO.render();
			}
	}

	@Override
	public void update() {
		if (wObjects.size() != 0)
			for (BaseGameObject bgO : wObjects) {
				if (bgO instanceof Player)
					((Player) bgO).input(keyHandler.getAllPressedKeys());
				bgO.update();
			}
	}

	@Override
	public void destroy() {
		for (BaseGameObject object : wObjects)
			object.destroy();
		for (BaseScreenObject object : sObjects)
			object.destroy();
		wObjects.clear();
		sObjects.clear();
	}

	// ================ BaseGameObjects Functions ===================
	public void addObject(BaseGameObject obj) {
		wObjects.add(obj);
	}

	public BaseGameObject getObject(String name) {
		for (BaseGameObject bgo : wObjects) {
			if (bgo.getName().equalsIgnoreCase(name))
				return bgo;
		}
		return null;
	}

	public ArrayList<BaseGameObject> getWorldObjects(ArrayList<BaseGameObject> bgO) {
		return (bgO.containsAll(wObjects) ? bgO : wObjects);
	}

	// ====================== Collision Handling =========================
	public void createCollision(BaseGameObject firstObj, BaseGameObject secObj,
			Couple<Couple<ArrayList<Object[]>>> data) {
		// >> The data we receive is divided to two and two: the first couple is
		// object one and two. and the second couple is functions and
		// variables.
		// TODO: Optimized
		Collision firstCol = new Collision(firstObj, secObj);
		Collision secCol = new Collision(firstObj, secObj);
		Couple<ArrayList<Object[]>> firstColInstructSet = data.get(0);
		Couple<ArrayList<Object[]>> secColInstructSet = data.get(1);
		ArrayList<Object[]> firstCol_FUNC_InstructData = firstColInstructSet.get(0);
		ArrayList<Object[]> firstCol_VAR_InstructData = firstColInstructSet.get(1);
		ArrayList<Object[]> secCol_FUNC_InstructData = secColInstructSet.get(0);
		ArrayList<Object[]> secCol_VAR_InstructData = secColInstructSet.get(1);
		for (Object[] objArr : firstCol_FUNC_InstructData)
			firstCol.addInstruction((byte) 0, Collision.FUNCTION, objArr);
		for (Object[] objArr : firstCol_VAR_InstructData)
			firstCol.addInstruction((byte) 0, Collision.VARIABLE, objArr);
		for (Object[] objArr : secCol_FUNC_InstructData)
			secCol.addInstruction((byte) 1, Collision.FUNCTION, objArr);
		for (Object[] objArr : secCol_VAR_InstructData)
			secCol.addInstruction((byte) 1, Collision.VARIABLE, objArr);
		firstObj.collision(firstCol);
		secObj.collision(secCol);
	}

	// ==================== Input Handler ==================================
	public void setKeyHandler(KeyHandler newKHandler) {
		if (keyHandler == null)
			keyHandler = newKHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	// ===================================Online============================

	public void updateObjectLocation(String name, double posX, double posY) {
		BaseGameObject bgo = getObject(name);
		if (bgo instanceof Ball)
			((Ball) bgo).setPosition(posX, posY);
		else if (bgo instanceof OnlinePlayer)
			((OnlinePlayer) bgo).setPosition(posX, posY);
		else
			throw new RuntimeException("No object was found with the name: " + name);
	}

	// =============================Create Game=============================

	public void createOnlineGame(boolean isHost, InetAddress addr, GameObjects... objects) {
		byte side = 0;
		DataOutputStream out = null;
		DataInputStream in = null;
		try {

			if (isHost) {
				hostToSRV_Socket = new ServerSocket(4242);
				incomingClientToSRV_Socket = hostToSRV_Socket.accept();
				out = new DataOutputStream(incomingClientToSRV_Socket.getOutputStream());
				in = new DataInputStream(incomingClientToSRV_Socket.getInputStream());
				out.writeByte(new Random().nextInt(100) % 2 == 0 ? ++side : side);
				out.flush();
				side = side == 1 ? (byte) 0 : (byte) 1;
			} else {
				fromClientToSRV_Socket = new Socket(addr, 4242);
				out = new DataOutputStream(fromClientToSRV_Socket.getOutputStream());
				in = new DataInputStream(fromClientToSRV_Socket.getInputStream());
				side = in.readByte();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		wObjects.clear();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		for (GameObjects object : objects) {
			BaseScreenObject obj = ObjectInstantiator.CreateNewObject(object, (object == GameObjects.PLAYER ? side
					: (object == GameObjects.ONLINE_PLAYER ? (side == 0 ? (byte) 1 : (byte) 0) : (byte) 0)));
			if (obj instanceof BaseGameObject)
				wObjects.add((BaseGameObject) obj);
		}
		sObjects.add(new ScoreItem(0, (byte) 10, side));
		sObjects.add(new ScoreItem(0, (byte) 10, (side == 0 ? (byte) 1 : (byte) 0)));
		ScoreKeeper.getInstance((ScoreItem) sObjects.get((side == 0 ? (isHost ? 0 : 1) : (isHost ? 1 : 0))),
				(ScoreItem) sObjects.get((side == 0 ? (isHost ? 1 : 0) : (isHost ? 0 : 1))));
		if (isHost)
			outData = new OutputData(out, (Player) getObject("Player"), (Ball) getObject("Ball"),
					ScoreKeeper.getInstance((ScoreItem[]) null));
		else
			outData = new OutputData(out, (Player) getObject("Player"), null, null);
		inData = new InputData(in, isHost);

	}

	public void createOfflineGame(boolean newScreen, GameObjects... objects) {
		if (objects != null) {
			currentScreenObjects = objects;
			playerSide = 0;
			secPlayerSide = 1;
		}
		for (int i = 0; i < wObjects.size(); i++) {
			BaseGameObject object = wObjects.get(i);
			object.destroy();
			wObjects.set(i, null);
		}
		wObjects.clear();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		byte side = (byte) new Random().nextInt(2);
		for (GameObjects object : (objects == null ? currentScreenObjects : objects)) {
			if (objects != null)
				if (object == GameObjects.PLAYER)
					playerSide = side;
				else if (object == GameObjects.AI)
					secPlayerSide = side;
			BaseScreenObject obj = ObjectInstantiator.CreateNewObject(object, (objects == null
					? (object == GameObjects.PLAYER ? playerSide : secPlayerSide) : (side == 0 ? side++ : side--)));
			if (obj instanceof BaseGameObject)
				wObjects.add((BaseGameObject) obj);
		}
		if (newScreen) {
			sObjects.add(new ScoreItem(0, (byte) 10, playerSide));
			sObjects.add(new ScoreItem(0, (byte) 10, secPlayerSide));

			ScoreKeeper.getInstance((ScoreItem) sObjects.get((playerSide == 0 ? 0 : 1)),
					(ScoreItem) sObjects.get((playerSide == 0 ? 1 : 0)));
		}
	}

	public void restartCurrentScreen() {
		if (!Main.isOnline)
			createOfflineGame(false, (GameObjects[]) null);
		else if (Main.isHost && Main.isOnline) {
			getObject("Ball").destroy();
			getObject("Player").destroy();
		} else if (Main.isOnline && !Main.isHost) {
			getObject("Player").destroy();
		}
	}

	public InputData getInputThread() {
		return inData;
	}

	public OutputData getOutputThread() {
		return outData;
	}

}
