package pong.main.util;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyHandler extends GLFWKeyCallback {

	public boolean[] keys = new boolean[65536];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW.GLFW_RELEASE;
	}

	public boolean isKeyDown(int key) {
		return keys[key];
	}

	public Integer[] getAllPressedKeys() {
		int counter = 0;
		ArrayList<Integer> pressed = new ArrayList<Integer>();
		for (boolean flag : keys) {
			if (flag)
				pressed.add(counter);
			counter++;
		}
		return Arrays.copyOf(pressed.toArray(new Integer[pressed.size()]), pressed.size(), Integer[].class);
	}

	public boolean isAnyKeyDown() {
		for (boolean flag : keys) {
			if (flag)
				return true;
		}
		return false;
	}

}
