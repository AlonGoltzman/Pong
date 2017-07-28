package pong.main.game_objects;

import java.util.ArrayList;
import java.util.Arrays;

import pong.main.Collision;
import pong.main.util.Rectangle;

@SuppressWarnings("unused")
public class PhysicsGameObject extends BaseGameObject {

	private int x;
	private int y;
	private int width;
	private int height;

	private Rectangle hitBox;

	public PhysicsGameObject() {
		super();
	}

	public PhysicsGameObject(Rectangle hBox) {
		super(hBox);
	}

	@Override
	public void render() {
	}

	@Override
	public void update() {

	}

	@Override
	public void collision(Collision col) {
		byte num = col.getObjNum(this);
		if (num == -1)
			throw new RuntimeException(
					"A collision has reached this object, whilst it wasn't registered to the collision.");
		ArrayList<Object[]> funcInstructs = col.getAllInstructs_Functions(num);
		ArrayList<Object[]> varInstructs = col.getAllInstructs_Variables(num);
		// TODO:Optomize
		if (!funcInstructs.isEmpty()) {
			for (Object[] objArr : funcInstructs) {
				String functionName = (String) objArr[0];
				Object[] newArr = Arrays.copyOfRange(objArr, 1, objArr.length);
				executeUse(Collision.FUNCTION, functionName, newArr);
			}
		}
		if (!varInstructs.isEmpty()) {
			for (Object[] objArr : varInstructs) {
				String varName = (String) objArr[0];
				Object[] newArr = Arrays.copyOfRange(objArr, 1, objArr.length);
				executeUse(Collision.VARIABLE, varName, newArr);
			}
		}
	}

	public void executeUse(byte type, String name, Object[] data) {
	}
}
