package pong.main;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import pong.main.ObjectInstantiator.GameObjects;
import pong.main.game_objects.ScoreItem;
import pong.main.game_objects.ScoreKeeper;
import pong.main.util.KeyHandler;

public class Main implements Runnable {

	private static boolean $running = true;
	private long $wnd;

	private final int width = 800;
	private final int height = 600;
	private final String title = "Pong";

	private WorldManager wManager;
	private CollisionsManager cManager;

	public static KeyHandler $keyHandler;

	private final int fps = 120;
	private long startTime;
	private long initTime = System.currentTimeMillis();
	private int logicStep = 0;
	private int renderStep = 0;

	private int maxScore;
	private int maxSet;

	// =================== Online ================
	public static boolean isHost = false;
	public static boolean isOnline = false;
	private InetAddress ip;

	// ============= Settings Frame ==============

	private static SettingsFrame frame;

	public static void main(String[] args) {
		frame = new SettingsFrame();
	}

	public Main() {
	}

	public Main(boolean isOnline, boolean isHost, InetAddress ip) {
		if (isOnline) {
			Main.isOnline = isOnline;
			Main.isHost = isHost;
			if (!isHost)
				this.ip = ip;
		}
	}

	public void init_Thread(int scoreLimit, int setLimit) {
		// >> Setting up the game thread and starting it.
		maxScore = scoreLimit;
		maxSet = setLimit;
		Thread gameThread = new Thread(this, title);
		gameThread.start();
	}

	public void init() {
		if (!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW Library!");

		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		$wnd = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);

		if ($wnd == NULL)
			throw new RuntimeException("Failed to create window!");

		GLFWVidMode monitor = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

		GLFW.glfwSetWindowPos($wnd, (monitor.width() - 800) / 2, (monitor.height() - 600) / 2);

		GLFW.glfwMakeContextCurrent($wnd);

		GLFW.glfwShowWindow($wnd);

		// >> OpenGL items:

		GL.createCapabilities();

		/**
		 * >> Setting the screen in orthogonal mode so that coordinates in GL
		 * can be written in pixels.
		 */
		GL11.glOrtho(0.f, (float) width, 0.f, (float) height, 0.f, 1.f);

		// >> Input handling
		GLFW.glfwSetKeyCallback($wnd, $keyHandler = new KeyHandler());

		// >> Setting up the world.
		wManager = WorldManager.getInstance($keyHandler);
		if (wManager.getKeyHandler() == null)
			wManager.setKeyHandler($keyHandler);

		// >> Setting up the collisions manager.
		cManager = CollisionsManager.getInstance(wManager);

		if (!isOnline)
			wManager.createOfflineGame(true, GameObjects.PLAYER, GameObjects.AI, GameObjects.BALL, GameObjects.COURT);
		else
			wManager.createOnlineGame(isHost, ip, GameObjects.PLAYER, GameObjects.ONLINE_PLAYER, GameObjects.BALL,
					GameObjects.COURT);

		ScoreKeeper.getInstance((ScoreItem[]) null).setGameLimits(maxScore, maxSet);
	}

	public void render() {
		GLFW.glfwSwapBuffers($wnd);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		wManager.render();
	}

	public void update() {
		GLFW.glfwPollEvents();
		cManager.update();
		wManager.update();
	}

	private long getTimeSinceLastLogicStep() {
		long currentTime = System.currentTimeMillis();
		return currentTime - startTime;
	}

	@Override
	public void run() {
		init();
		while ($running) {
			// =========== Update Loop ==============
			if ((double) getTimeSinceLastLogicStep() / (double) 1000 >= (double) 1 / (double) fps) {
				update();
				startTime = System.currentTimeMillis();
				logicStep++;
				if (System.currentTimeMillis() - initTime >= 1000) {
					initTime = System.currentTimeMillis();
					System.out.println("Logic Steps in 1 Second: " + logicStep + " , Render Steps: " + renderStep);
					logicStep = 0;
					renderStep = 0;
				}
			}
			// ========== Render Loop ================
			render();
			renderStep++;

			// ========== Close Game ===============
			if (GLFW.glfwWindowShouldClose($wnd)) {
				$running = false;
				if (isOnline) {
					wManager.getInputThread().interrupt();
					wManager.getOutputThread().interrupt();
				}
				frame.setVisible(true);
			}

		}

		/*
		 * if (isOnline) { System.out.println("Playing in online mode...");
		 * System.out.println("We are " + (isHost ? "" : "not ") + "the host.");
		 * try { DataOutputStream out; DataInputStream in; ball = new Ball(); if
		 * (isHost) { hostToSRV_Socket = new ServerSocket(port);
		 * System.out.println("Server socket connected, awaiting client...");
		 * incomingClientToSRV_Socket = hostToSRV_Socket.accept();
		 * System.out.println("Client Connected..."); out = new
		 * DataOutputStream(incomingClientToSRV_Socket.getOutputStream()); in =
		 * new DataInputStream(incomingClientToSRV_Socket.getInputStream());
		 * byte side = (byte) new Random().nextInt(2); if (side > 1) side = 1;
		 * player = new Player(side); onlinePlayer = new OnlinePlayer((byte)
		 * (side == 1 ? 0 : 1)); out.writeByte((byte) (side == 1 ? 0 : 1));
		 * out.flush(); outData = new OutputData(out, player, ball); } else {
		 * fromClientToSRV_Socket = new Socket(getConnectionIP(), port);
		 * System.out.println("Connected to server..."); out = new
		 * DataOutputStream(fromClientToSRV_Socket.getOutputStream()); in = new
		 * DataInputStream(fromClientToSRV_Socket.getInputStream()); byte side =
		 * in.readByte(); player = new Player(side); onlinePlayer = new
		 * OnlinePlayer((byte) (side == 1 ? 0 : 1)); } if (outData == null)
		 * outData = new OutputData(out, player); if (inData == null) inData =
		 * new InputData(in, isHost); wManager.addObject(player);
		 * wManager.addObject(onlinePlayer); wManager.addObject(ball); } catch
		 * (IOException ex) { ex.printStackTrace(); } } else { player = new
		 * Player((byte) 0); ball = new Ball(); ai = new AI((byte) 1);
		 * wManager.addObject(player); wManager.addObject(ball);
		 * wManager.addObject(ai); } startTime = System.currentTimeMillis(); try
		 * { while ($running) { // =========== Update Loop ============== if
		 * ((double) getTimeSinceLastLogicStep() / (double) 1000 >= (double) 1 /
		 * (double) fps) { update(); startTime = System.currentTimeMillis();
		 * logicStep++; if (System.currentTimeMillis() - initTime >= 1000) {
		 * initTime = System.currentTimeMillis();
		 * System.out.println("Logic Steps in 1 Second: " + logicStep +
		 * " , Render Steps: " + renderStep); logicStep = 0; renderStep = 0; } }
		 * // ========== Render Loop ================ render(); renderStep++;
		 * 
		 * // ========== Close Game =============== if
		 * (GLFW.glfwWindowShouldClose($wnd)) { $running = false;
		 * inData.interrupt(); outData.interrupt(); } } } catch (Exception ex) {
		 * inData.interrupt(); outData.interrupt(); } finally { try {
		 * inData.interrupt(); outData.interrupt(); } catch
		 * (NullPointerException ex) { } }
		 */
	}

	public static void stop() {
		$running = false;
	}

	public static void stop(byte winningSide) {

	}

}

// =================== Settings Frame =====================

class SettingsFrame implements ActionListener {

	// =========== Constants ===========
	private final String FRAME_NAME = "Pong - Settings";
	private final int WIDTH = 500;
	private final int HEIGHT = 250;
	private final Border BORDER = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

	// =========== UI Elements ===========

	// Containers:
	private JFrame window;
	private JPanel playPanel;

	private JPanel onlineSettings;
	private JPanel offlineSettings;
	private JPanel settings;

	private JPanel gameSettings;
	private JPanel scores;
	private JPanel sets;

	// Elements:
	private JButton play;
	private JToggleButton isOnline;
	private JToggleButton isHost;
	private JTextField ipAddr;
	private JRadioButtonController aiDifficulty;
	private JSpinner score_limit;
	private JLabel score_label;
	private JSpinner set_limit;
	private JLabel set_label;

	// =========== UI Data ===========
	private int x;
	private int y;

	public SettingsFrame() {
		setBounds();

		window = new JFrame(FRAME_NAME);

		initUI();

		setupInfo();

		addUI();

		setListeners();

		setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(x, y, WIDTH, HEIGHT);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == isOnline) {
			boolean selected = isOnline.isSelected();
			isHost.setEnabled(selected);
			ipAddr.setEnabled(selected);
			offlineSettings.setEnabled(!selected);
			aiDifficulty.setEnabled(!selected);
			score_limit.setEnabled(!selected);
			set_limit.setEnabled(!selected);
			score_label.setEnabled(!selected);
			set_label.setEnabled(!selected);
			gameSettings.setEnabled(!selected);
		} else if (source == isHost) {
			boolean selected = isHost.isSelected();
			ipAddr.setEnabled(!selected);
			gameSettings.setEnabled(selected);
			score_limit.setEnabled(selected);
			set_limit.setEnabled(selected);
			score_label.setEnabled(selected);
			set_label.setEnabled(selected);
		} else if (source == aiDifficulty.get(0)) {
			boolean selected = aiDifficulty.get(0).isSelected();
			if (selected)
				aiDifficulty.setSelected(0);
		} else if (source == aiDifficulty.get(1)) {
			boolean selected = aiDifficulty.get(1).isSelected();
			if (selected)
				aiDifficulty.setSelected(1);
		} else if (source == aiDifficulty.get(2)) {
			boolean selected = aiDifficulty.get(2).isSelected();
			if (selected)
				aiDifficulty.setSelected(2);
		} else if (source == play) {
			setVisible(false);
			Main main = null;
			if (!isOnline.isSelected()) {
				main = new Main(false, false, null);
			} else {
				try {
					main = new Main(true, isHost.isSelected(), InetAddress.getByName(ipAddr.getText()));
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
			}
			main.init_Thread((int) score_limit.getModel().getValue(), (int) set_limit.getModel().getValue());
		}
	}

	public void setVisible(boolean flag) {
		window.setVisible(flag);
	}

	private void initUI() {
		play = new JButton();
		isOnline = new JToggleButton();
		isHost = new JToggleButton();
		ipAddr = new JTextField();
		aiDifficulty = new JRadioButtonController(3);

		score_limit = new JSpinner(new SpinnerNumberModel(9, 1, 9, 1));
		set_limit = new JSpinner(new SpinnerNumberModel(1, 1, 9, 1));

		score_label = new JLabel();
		set_label = new JLabel();

		onlineSettings = new JPanel();
		offlineSettings = new JPanel();
		settings = new JPanel();

		playPanel = new JPanel();

		scores = new JPanel();
		sets = new JPanel();

		gameSettings = new JPanel();
	}

	private void setupInfo() {
		isHost.setEnabled(false);
		ipAddr.setEnabled(false);

		isOnline.setText("Playing Online?");
		isHost.setText("You're the host?");
		ipAddr.setPreferredSize(new Dimension(10, 50));

		aiDifficulty.get(0).setText("AI Level - Easy");
		aiDifficulty.get(1).setText("AI Level - Medium");
		aiDifficulty.get(2).setText("AI Level - Hard");

		score_label.setText("How many points per set: ");
		set_label.setText("How many sets per this single game: ");

		play.setText("Start Pong");

		aiDifficulty.get(0).setHorizontalAlignment(JRadioButton.CENTER);
		aiDifficulty.get(1).setHorizontalAlignment(JRadioButton.CENTER);
		aiDifficulty.get(2).setHorizontalAlignment(JRadioButton.CENTER);

		onlineSettings.setLayout(new GridLayout(3, 1, 5, 5));
		onlineSettings.setPreferredSize(new Dimension(250, 50));
		offlineSettings.setLayout(new GridLayout(3, 1, 5, 5));
		offlineSettings.setPreferredSize(new Dimension(250, 50));

		scores.setLayout(new GridLayout(1, 2, 5, 5));
		scores.setPreferredSize(new Dimension(500, 25));
		sets.setLayout(new GridLayout(1, 2, 5, 5));
		sets.setPreferredSize(new Dimension(500, 25));

		TitledBorder on_border = new TitledBorder("Online Settings");
		on_border.setTitleJustification(TitledBorder.LEFT);
		on_border.setTitlePosition(TitledBorder.TOP);
		TitledBorder off_border = new TitledBorder("Offline Settings");
		off_border.setTitleJustification(TitledBorder.LEFT);
		off_border.setTitlePosition(TitledBorder.TOP);
		TitledBorder score_info = new TitledBorder("Game Settings");
		score_info.setTitleJustification(TitledBorder.LEFT);
		score_info.setTitlePosition(TitledBorder.TOP);

		onlineSettings.setBorder(on_border);
		offlineSettings.setBorder(off_border);
		gameSettings.setBorder(score_info);
		gameSettings.setLayout(new GridLayout(2, 1, 5, 5));
		settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));
		settings.setPreferredSize(new Dimension(500, 100));
		settings.setBorder(BORDER);
		playPanel.setLayout(new BorderLayout());
		window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.PAGE_AXIS));
	}

	private void addUI() {
		onlineSettings.add(isOnline);
		onlineSettings.add(isHost);
		onlineSettings.add(ipAddr);

		for (int i = 0; i < aiDifficulty.size(); i++)
			offlineSettings.add(aiDifficulty.get(i));

		settings.add(onlineSettings);
		settings.add(offlineSettings);

		scores.add(score_label);
		scores.add(score_limit);
		sets.add(set_label);
		sets.add(set_limit);

		gameSettings.add(scores);
		gameSettings.add(sets);

		playPanel.add(play, BorderLayout.CENTER);

		window.add(settings);
		window.add(gameSettings);
		window.add(playPanel);

		window.setResizable(false);

		window.pack();
	}

	private void setListeners() {
		isOnline.addActionListener(this);
		isHost.addActionListener(this);
		aiDifficulty.setActionListener(this);
		play.addActionListener(this);
	}

	private void setBounds() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		x = (dim.width - WIDTH) / 2;
		y = (dim.height - HEIGHT) / 2;
	}

	private final class JRadioButtonController {

		private ArrayList<JRadioButton> buttons = new ArrayList<>();

		public JRadioButtonController(int amount) {
			for (int i = 0; i < amount; i++)
				buttons.add(new JRadioButton());
			buttons.get(0).setSelected(true);
		}

		public void setActionListener(ActionListener listener) {
			for (JRadioButton btn : buttons)
				btn.addActionListener(listener);
		}

		public void setEnabled(boolean flag) {
			for (JRadioButton btn : buttons)
				btn.setEnabled(flag);
		}

		public void setSelected(int index) {
			if (index < 0 || index > buttons.size())
				throw new IndexOutOfBoundsException();
			for (int i = 0; i < buttons.size(); i++)
				buttons.get(i).setSelected(i == index);
		}

		public int size() {
			return buttons.size();
		}

		public JRadioButton get(int index) {
			return buttons.get(index);
		}

	}
}