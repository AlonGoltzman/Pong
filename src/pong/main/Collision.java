package pong.main;

import java.util.ArrayList;

import pong.main.game_objects.BaseGameObject;
import pong.main.util.Couple;

public class Collision {

	public BaseGameObject firstObj, secObj;
	// >> TODO: Optimize
	// >> Containing both obj's instructions > Containing an obj's instructions
	// >> > Containing both function and variable instructions > the instructs.
	// private Quad<Couple<Couple<ArrayList<Object[]>>>> instructs = new
	// Quad<Couple<Couple<ArrayList<Object[]>>>>();
	private Couple<ArrayList<Object[]>> functionInstructs = new Couple<ArrayList<Object[]>>(new ArrayList<Object[]>(),
			new ArrayList<Object[]>());
	private Couple<ArrayList<Object[]>> varInstructs = new Couple<ArrayList<Object[]>>(new ArrayList<Object[]>(),
			new ArrayList<Object[]>());

	public static final byte VARIABLE = 1;
	public static final byte FUNCTION = 2;

	public Collision(BaseGameObject firstObj, BaseGameObject secObj) {
		this.firstObj = firstObj;
		this.secObj = secObj;
	}

	public void addInstruction(byte objNum, byte attrNum, Object... data) {
		if (attrNum == VARIABLE)
			varInstructs.get(objNum).add(data);
		else if (attrNum == FUNCTION)
			functionInstructs.get(objNum).add(data);
		else
			throw new IllegalArgumentException("Object Num must be either 1 or 2.");
	}

	public ArrayList<Object[]> getAllInstructs_Functions(byte objNum) {
		return functionInstructs.get(objNum);
	}

	public ArrayList<Object[]> getAllInstructs_Variables(byte objNum) {
		return varInstructs.get(objNum);
	}

	public byte getObjNum(BaseGameObject obj) {
		return (firstObj.equals(obj) ? (byte) 0 : (secObj.equals(obj) ? (byte) 1 : (byte) -1));
	}

	public Couple<Couple<ArrayList<Object[]>>> getCompleteObject() {
		return new Couple<Couple<ArrayList<Object[]>>>(
				new Couple<ArrayList<Object[]>>(functionInstructs.get(0), varInstructs.get(0)),
				new Couple<ArrayList<Object[]>>(functionInstructs.get(1), varInstructs.get(1)));
	}

	@Override
	public String toString() {
		return "First Object: " + firstObj.getName() + ", Second Object: " + secObj.getName();
	}

}
