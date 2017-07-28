package pong.main.util;

import java.util.ArrayList;
import java.util.HashMap;

public final class CoordGenerator {

	private static double X;
	private static double Y;
	private static int W;
	private static int H;
	private static byte lW;
	private static CoordGenerator instance;

	private final HashMap<Quads, MultiMap<Double, Double>> quadMap = new HashMap<>();
	private final HashMap<Character, ArrayList<MultiMap<Double, Double>>> charMap = new HashMap<>();

	private final QuadPointData pointData;

	static {
		X = 0;
		Y = 0;
		W = 100;
		H = 100;
		lW = 10;
		instance = null;
	}

	private enum Quads {
		BOTTOM_L, BOTTOM_M, BOTTOM_S, BOTTOM_M_LEFT, BOTTOM_M_MIDDLE, BOTTOM_M_RIGHT, BOTTOM_S_LEFT, BOTTOM_S_MIDDLE, BOTTOM_S_RIGHT, //
		TOP_L, TOP_M, TOP_S, TOP_M_LEFT, TOP_M_MIDDLE, TOP_M_RIGHT, TOP_S_LEFT, TOP_S_MIDDLE, TOP_S_RIGHT, //
		LEFT_L, LEFT_M, LEFT_S, LEFT_M_TOP, LEFT_M_MIDDLE, LEFT_M_BOTTOM, LEFT_S_TOP, LEFT_S_MIDDLE, LEFT_S_BOTTOM, //
		RIGHT_L, RIGHT_M, RIGHT_S, RIGHT_M_TOP, RIGHT_M_MIDDLE, RIGHT_M_BOTTOM, RIGHT_S_TOP, RIGHT_S_MIDDLE, RIGHT_S_BOTTOM, ///
		CENTER_V_L, CENTER_V_M, CENTER_V_S, CENTER_V_M_TOP, CENTER_V_M_MIDDLE, CENTER_V_M_BOTTOM, CENTER_V_S_TOP, CENTER_V_S_MIDDLE, CENTER_V_S_BOTTOM, //
		CENTER_H_L, CENTER_H_M, CENTER_H_S, CENTER_H_M_LEFT, CENTER_H_M_MIDDLE, CENTER_H_M_RIGHT, CENTER_H_S_LEFT, CENTER_H_S_MIDDLE, CENTER_H_S_RIGHT, //
		SLASH_T2B_L, SLASH_T2B_M, SLASH_T2B_S, SLASH_T2B_M_TOP, SLASH_T2B_M_MIDDLE, SLASH_T2B_M_BOTTOM, SLASH_T2B_S_TOP, SLASH_T2B_S_MIDDLE, SLASH_T2B_S_BOTTOM, //
		SLASH_B2T_L, SLASH_B2T_M, SLASH_B2T_S, SLASH_B2T_M_TOP, SLASH_B2T_M_MIDDLE, SLASH_B2T_M_BOTTOM, SLASH_B2T_S_TOP, SLASH_B2T_S_MIDDLE, SLASH_B2T_S_BOTTOM, //
		V_CUSTOM_QUAD_LEFT, V_CUSTOM_QUAD_RIGHT;
	}

	// Singleton ===============================================

	public static CoordGenerator getInstance() {
		return instance == null ? new CoordGenerator() : instance;
	}

	private CoordGenerator() {
		instance = this;
		generateData();
	}

	// Generation ==============================================

	private void generateData() {
		Quads[] quads = Quads.values();
		for (Quads quad : quads)
			quadMap.put(quad, getCoordsForQuad(quad));

		final String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		ArrayList<Quads> quadsForChar = new ArrayList<Quads>();
		for (char possibleChar : allChars.toCharArray()) {
			quadsForChar = getQuadsForChar(possibleChar);
			ArrayList<MultiMap<Double, Double>> currentCharList = new ArrayList<>();
			for (Quads quad : quadsForChar)
				currentCharList.add(quadMap.get(quad));
			charMap.put(possibleChar, currentCharList);
			quadsForChar.clear();
		}

	}

	// Fetchers ================================================

	public Coords genCoords(Object input) {
		return genCoords(input, lW);
	}

	public Coords genCoords(Object input, byte line_width) {
		return genCoords(input, line_width, X, Y);
	}

	public Coords genCoords(Object input, byte line_width, double xPos, double yPos) {
		return genCoords(input, line_width, xPos, yPos, W, H);
	}

	public Coords genCoords(Object input, byte line_width, double xPos, double yPos, int width, int height) {
		Coords item = null;
		lW = line_width;
		X = xPos;
		Y = yPos;
		W = width;
		H = height;
		if (input instanceof Integer)
			item = getCoordsForInt((int) input);
		else if (input instanceof String)
			item = getCoordsForString(((String) input).toUpperCase());
		else
			throw new IllegalArgumentException("Object type is not supported.");
		return item;
	}

	// Generation Methods ======================================

	private Coords getCoordsForInt(int input) {
		return getCoordsForString(String.valueOf(input));
	}

	private Coords getCoordsForString(String input) {
		ArrayList<ArrayList<MultiMap<Double, Double>>> charsInfo = new ArrayList<>();
		for (char letter : input.toCharArray())
			charsInfo.add(charMap.get(letter));
		return new Coords(charsInfo);
	}

	// Data Generation ==========================================

	private ArrayList<Quads> getQuadsForChar(char c) {
		ArrayList<Quads> quads = new ArrayList<>();
		switch (c) {
		case '0':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.SLASH_B2T_L);
			break;
		case '1':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.CENTER_V_L);
			quads.add(Quads.TOP_M_LEFT);
			break;
		case '2':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_M_BOTTOM);
			quads.add(Quads.CENTER_H_L);
			quads.add(Quads.RIGHT_M_TOP);
			break;
		case '3':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.CENTER_H_L);
			break;
		case '4':
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_M_TOP);
			quads.add(Quads.CENTER_H_L);
			break;
		case '5':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_M_BOTTOM);
			quads.add(Quads.CENTER_H_L);
			quads.add(Quads.LEFT_M_TOP);
			break;
		case '6':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.CENTER_H_L);
			quads.add(Quads.RIGHT_M_BOTTOM);
			break;
		case '7':
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_L);
			break;
		case '8':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.CENTER_H_L);
			break;
		case '9':
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_M_TOP);
			quads.add(Quads.CENTER_H_L);
			break;
		case 'A':
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.CENTER_H_L);
			break;
		case 'B':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_M_LEFT);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.CENTER_H_L);
			quads.add(Quads.RIGHT_M_BOTTOM);
			quads.add(Quads.CENTER_V_M_TOP);
			break;
		case 'C':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_L);
			break;
		case 'D':
			quads.add(Quads.LEFT_L);
			quads.add(Quads.SLASH_T2B_M_TOP);
			quads.add(Quads.SLASH_B2T_M_BOTTOM);
			break;
		case 'E':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.CENTER_H_L);
			break;
		case 'F':
			quads.add(Quads.LEFT_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.CENTER_H_L);
			break;
		case 'G':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.RIGHT_M_BOTTOM);
			quads.add(Quads.CENTER_H_M_RIGHT);
			break;
		case 'H':
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.CENTER_H_L);
			break;
		case 'I':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.CENTER_V_L);
			break;
		case 'J':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_M_BOTTOM);
			break;
		case 'K':
			quads.add(Quads.CENTER_V_L);
			quads.add(Quads.SLASH_T2B_M_BOTTOM);
			quads.add(Quads.SLASH_B2T_M_TOP);
			break;
		case 'L':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.LEFT_L);
			break;
		case 'M':
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.SLASH_T2B_M_TOP);
			quads.add(Quads.SLASH_B2T_M_TOP);
			break;
		case 'N':
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.SLASH_T2B_L);
			break;
		case 'O':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.RIGHT_L);
			break;
		case 'P':
			quads.add(Quads.TOP_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.RIGHT_M_TOP);
			quads.add(Quads.CENTER_H_L);
			break;
		case 'Q':
			quads.add(Quads.TOP_L);
			quads.add(Quads.CENTER_H_L);
			quads.add(Quads.LEFT_M_TOP);
			quads.add(Quads.RIGHT_M_TOP);
			quads.add(Quads.SLASH_T2B_M_BOTTOM);
			break;
		case 'R':
			quads.add(Quads.LEFT_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.RIGHT_M_TOP);
			quads.add(Quads.CENTER_H_L);
			quads.add(Quads.SLASH_T2B_M_BOTTOM);
			break;
		case 'S':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.CENTER_H_L);
			quads.add(Quads.RIGHT_M_BOTTOM);
			quads.add(Quads.LEFT_M_TOP);
			break;
		case 'T':
			quads.add(Quads.TOP_L);
			quads.add(Quads.CENTER_V_L);
			break;
		case 'U':
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.BOTTOM_L);
			break;
		case 'V':
			quads.add(Quads.V_CUSTOM_QUAD_LEFT);
			quads.add(Quads.V_CUSTOM_QUAD_RIGHT);
			break;
		case 'W':
			quads.add(Quads.RIGHT_L);
			quads.add(Quads.LEFT_L);
			quads.add(Quads.SLASH_T2B_M_BOTTOM);
			quads.add(Quads.SLASH_B2T_M_BOTTOM);
			break;
		case 'X':
			quads.add(Quads.SLASH_T2B_L);
			quads.add(Quads.SLASH_B2T_L);
			break;
		case 'Y':
			quads.add(Quads.CENTER_V_M_BOTTOM);
			quads.add(Quads.SLASH_B2T_M_TOP);
			quads.add(Quads.SLASH_T2B_M_TOP);
			break;
		case 'Z':
			quads.add(Quads.BOTTOM_L);
			quads.add(Quads.TOP_L);
			quads.add(Quads.SLASH_B2T_L);
			break;
		}
		return quads;
	}

	private MultiMap<Double, Double> getCoordsForQuad(Quads quad, QuadPointData.PointData pointData) {
		MultiMap<Double, Double> quadCoords = new MultiMap<Double, Double>(4);

		Double[] xCoords = new Double[4];
		Double[] yCoords = new Double[4];

		switch (quad) {
		case BOTTOM_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL, QuadPointData.PointData.YH, QuadPointData.PointData.YH);
			break;
		case BOTTOM_M_MIDDLE:
		case BOTTOM_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$4, QuadPointData.PointData.XL_1$4,
					QuadPointData.PointData.XH_1$4, QuadPointData.PointData.XH_1$4, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL);
			break;
		case BOTTOM_S_MIDDLE:
		case BOTTOM_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XL_3$8,
					QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL);
			break;
		case BOTTOM_M_LEFT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL);
			break;
		case BOTTOM_M_RIGHT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL);
			break;
		case BOTTOM_S_LEFT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$8, QuadPointData.PointData.XL_1$8,
					QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL);
			break;
		case BOTTOM_S_RIGHT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_1$8, QuadPointData.PointData.XH_1$8,
					QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL_WW, QuadPointData.PointData.YL);
			break;
		case TOP_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YH,
					QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH);
			break;
		case TOP_M_MIDDLE:
		case TOP_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$4, QuadPointData.PointData.XL_1$4,
					QuadPointData.PointData.XH_1$4, QuadPointData.PointData.XH_1$4, QuadPointData.PointData.YH,
					QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH);
			break;
		case TOP_S_MIDDLE:
		case TOP_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XL_3$8,
					QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.YH,
					QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH);
			break;
		case TOP_M_LEFT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YH,
					QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH);
			break;
		case TOP_M_RIGHT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YH,
					QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH);
			break;
		case TOP_S_LEFT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$8, QuadPointData.PointData.XL_1$8,
					QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.YH,
					QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH);
			break;
		case TOP_S_RIGHT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_1$8, QuadPointData.PointData.XH_1$8,
					QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.YH,
					QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH_NW, QuadPointData.PointData.YH);
			break;
		case LEFT_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XL_WW, QuadPointData.PointData.XL_WW, QuadPointData.PointData.YL,
					QuadPointData.PointData.YH, QuadPointData.PointData.YH, QuadPointData.PointData.YL);
			break;
		case LEFT_M_MIDDLE:
		case LEFT_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XL_WW, QuadPointData.PointData.XL_WW, QuadPointData.PointData.YL_1$4,
					QuadPointData.PointData.YH_1$4, QuadPointData.PointData.YH_1$4, QuadPointData.PointData.YL_1$4);
			break;
		case LEFT_S_MIDDLE:
		case LEFT_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XL_WW, QuadPointData.PointData.XL_WW, QuadPointData.PointData.YL_3$8,
					QuadPointData.PointData.YH_3$8, QuadPointData.PointData.YH_3$8, QuadPointData.PointData.YL_3$8);

			break;
		case LEFT_M_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XL_WW, QuadPointData.PointData.XL_WW, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YH, QuadPointData.PointData.YH, QuadPointData.PointData.YM_NW);
			break;
		case LEFT_M_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XL_WW, QuadPointData.PointData.XL_WW, QuadPointData.PointData.YL,
					QuadPointData.PointData.YM_NW, QuadPointData.PointData.YM_NW, QuadPointData.PointData.YL);
			break;
		case LEFT_S_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XL_WW, QuadPointData.PointData.XL_WW, QuadPointData.PointData.YH_3$8,
					QuadPointData.PointData.YH_1$8, QuadPointData.PointData.YH_1$8, QuadPointData.PointData.YH_3$8);
			break;
		case LEFT_S_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XL_WW, QuadPointData.PointData.XL_WW, QuadPointData.PointData.YL_1$8,
					QuadPointData.PointData.YL_3$8, QuadPointData.PointData.YL_3$8, QuadPointData.PointData.YL_1$8);
			break;
		case RIGHT_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_NW, QuadPointData.PointData.XH_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YL,
					QuadPointData.PointData.YH, QuadPointData.PointData.YH, QuadPointData.PointData.YL);
			break;
		case RIGHT_M_MIDDLE:
		case RIGHT_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_NW, QuadPointData.PointData.XH_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YL_1$4,
					QuadPointData.PointData.YH_1$4, QuadPointData.PointData.YH_1$4, QuadPointData.PointData.YL_1$4);
			break;
		case RIGHT_S_MIDDLE:
		case RIGHT_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_NW, QuadPointData.PointData.XH_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YL_3$8,
					QuadPointData.PointData.YH_3$8, QuadPointData.PointData.YH_3$8, QuadPointData.PointData.YL_3$8);
			break;
		case RIGHT_M_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_NW, QuadPointData.PointData.XH_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YH, QuadPointData.PointData.YH, QuadPointData.PointData.YM_NW);
			break;
		case RIGHT_M_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_NW, QuadPointData.PointData.XH_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YL,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YL);
			break;
		case RIGHT_S_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_NW, QuadPointData.PointData.XH_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YH_3$8,
					QuadPointData.PointData.YH_1$8, QuadPointData.PointData.YH_1$8, QuadPointData.PointData.YH_3$8);
			break;
		case RIGHT_S_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_NW, QuadPointData.PointData.XH_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YL_1$8,
					QuadPointData.PointData.YL_3$8, QuadPointData.PointData.YL_3$8, QuadPointData.PointData.YL_1$8);
			break;
		case CENTER_V_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YL,
					QuadPointData.PointData.YH, QuadPointData.PointData.YH, QuadPointData.PointData.YL);
			break;
		case CENTER_V_M_MIDDLE:
		case CENTER_V_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YL_1$4,
					QuadPointData.PointData.YH_1$4, QuadPointData.PointData.YH_1$4, QuadPointData.PointData.YL_1$4);
			break;
		case CENTER_V_S_MIDDLE:
		case CENTER_V_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YL_3$8,
					QuadPointData.PointData.YH_3$8, QuadPointData.PointData.YH_3$8, QuadPointData.PointData.YL_3$8);
			break;
		case CENTER_V_M_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YH, QuadPointData.PointData.YH, QuadPointData.PointData.YM_NW);
			break;
		case CENTER_V_M_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YL,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YL);
			break;
		case CENTER_V_S_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YH_3$8,
					QuadPointData.PointData.YH_1$8, QuadPointData.PointData.YH_1$8, QuadPointData.PointData.YH_3$8);
			break;
		case CENTER_V_S_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YL_3$8,
					QuadPointData.PointData.YL_1$8, QuadPointData.PointData.YL_1$8, QuadPointData.PointData.YL_3$8);
			break;
		case CENTER_H_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_NW);
			break;
		case CENTER_H_M_MIDDLE:
		case CENTER_H_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$4, QuadPointData.PointData.XL_1$4,
					QuadPointData.PointData.XH_1$4, QuadPointData.PointData.XH_1$4, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_NW);
			break;
		case CENTER_H_S_MIDDLE:
		case CENTER_H_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XL_3$8,
					QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_NW);
			break;
		case CENTER_H_M_LEFT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XL,
					QuadPointData.PointData.XM_WW, QuadPointData.PointData.XM_WW, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_NW);
			break;
		case CENTER_H_M_RIGHT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XM_NW, QuadPointData.PointData.XM_NW,
					QuadPointData.PointData.XH, QuadPointData.PointData.XH, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_NW);
			break;
		case CENTER_H_S_LEFT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$8, QuadPointData.PointData.XL_1$8,
					QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_NW);
			break;
		case CENTER_H_S_RIGHT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XH_3$8,
					QuadPointData.PointData.XH_1$8, QuadPointData.PointData.XH_1$8, QuadPointData.PointData.YM_NW,
					QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_WW, QuadPointData.PointData.YM_NW);
			break;
		case SLASH_T2B_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XLS_T2B,
					QuadPointData.PointData.XH, QuadPointData.PointData.XHS_T2B, QuadPointData.PointData.YHS_T2B,
					QuadPointData.PointData.YH, QuadPointData.PointData.YLS_T2B, QuadPointData.PointData.YL);
			break;
		case SLASH_T2B_M_MIDDLE:
		case SLASH_T2B_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$4, QuadPointData.PointData.XLS_T2B_1$4,
					QuadPointData.PointData.XHS_T2B_1$4, QuadPointData.PointData.XH_1$4, QuadPointData.PointData.YH_1$4,
					QuadPointData.PointData.YHS_T2B_1$4, QuadPointData.PointData.YLS_T2B_1$4,
					QuadPointData.PointData.YL_1$4);
			break;
		case SLASH_T2B_S_MIDDLE:
		case SLASH_T2B_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XLS_T2B_3$8,
					QuadPointData.PointData.XHS_T2B_3$8, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.YH_3$8,
					QuadPointData.PointData.YHS_T2B_3$8, QuadPointData.PointData.YLS_T2B_3$8,
					QuadPointData.PointData.YL_3$8);
			break;
		case SLASH_T2B_M_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XLS_T2B,
					QuadPointData.PointData.CPX_SS_2, QuadPointData.PointData.CPX_SS_2_M,
					QuadPointData.PointData.YHS_T2B, QuadPointData.PointData.YH, QuadPointData.PointData.CPY_SS_2,
					QuadPointData.PointData.CPY_SS_2_M);
			break;
		case SLASH_T2B_M_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.CPX_SS_2_M, QuadPointData.PointData.CPX_SS_2,
					QuadPointData.PointData.XH, QuadPointData.PointData.XHS_T2B, QuadPointData.PointData.CPY_SS_2_M,
					QuadPointData.PointData.CPY_SS_2, QuadPointData.PointData.YLS_T2B, QuadPointData.PointData.YL);
			break;
		case SLASH_T2B_S_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$8, QuadPointData.PointData.XLS_T2B_1$8,
					QuadPointData.PointData.XLS_T2B_3$8, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.YH_1$8,
					QuadPointData.PointData.YHS_T2B_1$8, QuadPointData.PointData.YHS_T2B_3$8,
					QuadPointData.PointData.YH_3$8);
			break;
		case SLASH_T2B_S_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XHS_T2B_3$8,
					QuadPointData.PointData.XHS_T2B_1$8, QuadPointData.PointData.XH_1$8, QuadPointData.PointData.YL_3$8,
					QuadPointData.PointData.YLS_T2B_3$8, QuadPointData.PointData.YLS_T2B_1$8,
					QuadPointData.PointData.YL_1$8);
			break;
		case SLASH_B2T_L:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XLS_B2T, QuadPointData.PointData.XL,
					QuadPointData.PointData.XHS_B2T, QuadPointData.PointData.XH, QuadPointData.PointData.YL,
					QuadPointData.PointData.YLS_B2T, QuadPointData.PointData.YH, QuadPointData.PointData.YHS_B2T);
			break;
		case SLASH_B2T_M_MIDDLE:
		case SLASH_B2T_M:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$4, QuadPointData.PointData.XLS_B2T_1$4,
					QuadPointData.PointData.XHS_B2T_1$4, QuadPointData.PointData.XH_1$4, QuadPointData.PointData.YL_1$4,
					QuadPointData.PointData.YLS_B2T_1$4, QuadPointData.PointData.YHS_B2T_1$4,
					QuadPointData.PointData.YH_1$4);
			break;
		case SLASH_B2T_S_MIDDLE:
		case SLASH_B2T_S:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.XLS_B2T_3$8,
					QuadPointData.PointData.XHS_B2T_3$8, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.YL_3$8,
					QuadPointData.PointData.YLS_B2T_3$8, QuadPointData.PointData.YHS_B2T_3$8,
					QuadPointData.PointData.YL_3$8);
			break;
		case SLASH_B2T_M_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.CPX_SS_2, QuadPointData.PointData.CPX_SS_2_M,
					QuadPointData.PointData.XHS_B2T, QuadPointData.PointData.XH, QuadPointData.PointData.CPY_SS_2_M,
					QuadPointData.PointData.CPY_SS_2, QuadPointData.PointData.YH, QuadPointData.PointData.YHS_B2T);
			break;
		case SLASH_B2T_M_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XLS_B2T, QuadPointData.PointData.XL,
					QuadPointData.PointData.CPX_SS_2_M, QuadPointData.PointData.CPX_SS_2, QuadPointData.PointData.YL,
					QuadPointData.PointData.YLS_B2T, QuadPointData.PointData.CPY_SS_2,
					QuadPointData.PointData.CPY_SS_2_M);
			break;
		case SLASH_B2T_S_TOP:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XH_3$8, QuadPointData.PointData.XHS_B2T_3$8,
					QuadPointData.PointData.XHS_B2T_1$8, QuadPointData.PointData.XH_1$8, QuadPointData.PointData.YH_3$8,
					QuadPointData.PointData.YHS_B2T_3$8, QuadPointData.PointData.YHS_B2T_1$8,
					QuadPointData.PointData.YH_1$8);
			break;
		case SLASH_B2T_S_BOTTOM:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL_1$8, QuadPointData.PointData.XLS_B2T_1$8,
					QuadPointData.PointData.XLS_B2T_3$8, QuadPointData.PointData.XL_3$8, QuadPointData.PointData.YL_1$8,
					QuadPointData.PointData.YLS_B2T_1$8, QuadPointData.PointData.YLS_B2T_3$8,
					QuadPointData.PointData.YL_3$8);
			break;
		case V_CUSTOM_QUAD_LEFT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.XL, QuadPointData.PointData.XLS_T2B,
					QuadPointData.PointData.CPX_SS, QuadPointData.PointData.CPX_SS_M, QuadPointData.PointData.YHS_T2B,
					QuadPointData.PointData.YH, QuadPointData.PointData.YL_SS, QuadPointData.PointData.YL);
			break;
		case V_CUSTOM_QUAD_RIGHT:
			pointData.setInto(xCoords, yCoords, QuadPointData.PointData.CPX_SS, QuadPointData.PointData.CPX_SS_M,
					QuadPointData.PointData.XHS_T2B, QuadPointData.PointData.XH, QuadPointData.PointData.YL,
					QuadPointData.PointData.YL_SS, QuadPointData.PointData.YH, QuadPointData.PointData.YHS_T2B);
			break;
		}
		for (int i = 0; i < 4; i++)
			quadCoords.put(xCoords[i], yCoords[i]);
		return quadCoords;
	}

	public class Coords {

		public ArrayList<ArrayList<MultiMap<Double, Double>>> coords;

		public Coords(ArrayList<ArrayList<MultiMap<Double, Double>>> lists) {
			coords = lists;
		}

		public ArrayList<MultiMap<Double, Double>> getListPerX(int x) {
			return coords.get(x);
		}

	}

	private static final class QuadPointData {

		private static int wSize = W;
		private static int hSize = H;
		private static byte lineSize = lW;

		private static double xL = X;
		private static double yL = Y;

		private static double xH = xL + wSize;
		private static double yH = yL + hSize;

		private static double xM = xL + ((double) wSize / 2.0);
		private static double yM = yL + ((double) hSize / 2.0);

		private static double xH_NoWidth = xH - lineSize;
		private static double yH_NoWidth = yH - lineSize;

		private static double xL_WithWidth = xL + lineSize;
		private static double yL_WithWidth = yL + lineSize;

		private static double xM_NoWidth = xM - (lineSize / (byte) 2);
		private static double xM_WithWidth = xM + (lineSize / (byte) 2);

		private static double yM_NoWidth = yM - (lineSize / (byte) 2);
		private static double yM_WithWidth = yM + (lineSize / (byte) 2);

		private static double xL_P_1$4 = xL + ((double) wSize / 4.0);
		private static double yL_P_1$4 = yL + ((double) hSize / 4.0);

		private static double xH_M_1$4 = xH - ((double) wSize / 4.0);
		private static double yH_M_1$4 = yH - ((double) hSize / 4.0);

		private static double xL_P_1$8 = xL + ((double) wSize / 8.0);
		private static double yL_P_1$8 = yL + ((double) hSize / 8.0);

		private static double xH_M_1$8 = xH - ((double) wSize / 8.0);
		private static double yH_M_1$8 = yH - ((double) hSize / 8.0);

		private static double xL_P_3$8 = xL + (3 * ((double) wSize / 8.0));
		private static double yL_P_3$8 = yL + (3 * ((double) hSize / 8.0));

		private static double xH_M_3$8 = xH - (3 * ((double) wSize / 8.0));
		private static double yH_M_3$8 = yH - (3 * ((double) hSize / 8.0));

		private static double slashSize = Math.sqrt(Math.pow(lineSize, 2) / 2);

		private static double xL_P_S_T2B = xL + slashSize;
		private static double yL_P_S_T2B = yL + slashSize;

		private static double xH_M_S_T2B = xH - slashSize;
		private static double yH_M_S_T2B = yH - slashSize;

		private static double xL_S_T2B_1$4 = xL_P_1$4 + slashSize;
		private static double yL_S_T2B_1$4 = yL_P_1$4 + slashSize;

		private static double xL_S_T2B_1$8 = xL_P_1$8 + slashSize;
		private static double yL_S_T2B_1$8 = yL_P_1$8 + slashSize;

		private static double xL_S_T2B_3$8 = xL_P_3$8 + slashSize;
		private static double yL_S_T2B_3$8 = yL_P_3$8 + slashSize;

		private static double xH_S_T2B_1$4 = xH_M_1$4 + slashSize;
		private static double yH_S_T2B_1$4 = yH_M_1$4 + slashSize;

		private static double xH_S_T2B_1$8 = xH_M_1$8 + slashSize;
		private static double yH_S_T2B_1$8 = yH_M_1$8 + slashSize;

		private static double xH_S_T2B_3$8 = xH_M_3$8 + slashSize;
		private static double yH_S_T2B_3$8 = yH_M_3$8 + slashSize;

		private static double xL_P_S_B2T = xL + slashSize;
		private static double yL_P_S_B2T = yL + slashSize;

		private static double xH_M_S_B2T = xH - slashSize;
		private static double yH_M_S_B2T = yH - slashSize;

		private static double xL_S_B2T_1$4 = xL_P_1$4 - slashSize;
		private static double yL_S_B2T_1$4 = yL_P_1$4 + slashSize;

		private static double xL_S_B2T_1$8 = xL_P_1$8 - slashSize;
		private static double yL_S_B2T_1$8 = yL_P_1$8 + slashSize;

		private static double xL_S_B2T_3$8 = xL_P_3$8 - slashSize;
		private static double yL_S_B2T_3$8 = yL_P_3$8 + slashSize;

		private static double xH_S_B2T_1$4 = xH_M_1$4 - slashSize;
		private static double yH_S_B2T_1$4 = yH_M_1$4 + slashSize;

		private static double xH_S_B2T_1$8 = xH_M_1$8 - slashSize;
		private static double yH_S_B2T_1$8 = yH_M_1$8 + slashSize;

		private static double xH_S_B2T_3$8 = xH_M_3$8 - slashSize;
		private static double yH_S_B2T_3$8 = yH_M_3$8 + slashSize;

		private static double centerPointX = xL + ((xH - xL) / 2);
		private static double centerPointY = yL + ((yH - yL) / 2);

		public enum PointData {
			WIDTH(wSize), HEIGHT(hSize), LINE_WIDTH(lineSize), XL(xL), YL(yL), XH(xH), YH(yH), XM(xM), YM(yM), //
			XH_NW(xH_NoWidth), YH_NW(yH_NoWidth), XL_WW(xL_WithWidth), YL_WW(yL_WithWidth), XM_NW(xM_NoWidth), //
			YM_NW(yM_NoWidth), XM_WW(xM_WithWidth), YM_WW(yM_WithWidth), XL_1$4(xL_P_1$4), YL_1$4(yL_P_1$4), //
			XL_1$8(xL_P_1$4), YL_1$8(yL_P_1$8), XL_3$8(xL_P_3$8), YL_3$8(yL_P_3$8), XH_1$4(xH_M_1$4), //
			YH_1$4(yH_M_1$4), XH_1$8(xH_M_1$4), YH_1$8(yH_M_1$8), XH_3$8(xH_M_3$8), YH_3$8(yH_M_3$8), //
			SS(slashSize), XLS_T2B(xL_P_S_T2B), YLS_T2B(yL_P_S_T2B), XHS_T2B(xH_M_S_T2B), //
			YHS_T2B(yH_M_S_T2B), XLS_T2B_1$4(xL_S_T2B_1$4), YLS_T2B_1$4(yL_S_T2B_1$4), //
			XHS_T2B_1$4(xH_S_T2B_1$4), YHS_T2B_1$4(yH_S_T2B_1$4), XLS_T2B_1$8(xL_S_T2B_1$8), //
			YLS_T2B_1$8(yL_S_T2B_1$8), XHS_T2B_1$8(xH_S_T2B_1$8), YHS_T2B_1$8(yH_S_T2B_1$8), //
			XLS_T2B_3$8(xL_S_T2B_3$8), YLS_T2B_3$8(yL_S_T2B_3$8), XHS_T2B_3$8(xH_S_T2B_3$8), //
			YHS_T2B_3$8(yH_S_T2B_3$8), XLS_B2T(xL_P_S_B2T), YLS_B2T(yL_P_S_B2T), XHS_B2T(xH_M_S_B2T), //
			YHS_B2T(yH_M_S_B2T), XLS_B2T_1$4(xL_S_B2T_1$4), YLS_B2T_1$4(yL_S_B2T_1$4), //
			XHS_B2T_1$4(xH_S_B2T_1$4), YHS_B2T_1$4(yH_S_B2T_1$4), XLS_B2T_1$8(xL_S_B2T_1$8), //
			YLS_B2T_1$8(yL_S_B2T_1$8), XHS_B2T_1$8(xH_S_B2T_1$8), YHS_B2T_1$8(yH_S_B2T_1$8), //
			XLS_B2T_3$8(xL_S_B2T_3$8), YLS_B2T_3$8(yL_S_B2T_3$8), XHS_B2T_3$8(xH_S_B2T_3$8), //
			YHS_B2T_3$8(yH_S_B2T_3$8), CPX(centerPointX), CPY(centerPointY), CPX_SS(centerPointX + slashSize), //
			CPX_SS_M(centerPointX - slashSize), CPY_SS(centerPointY + slashSize), //
			CPY_SS_M(centerPointY - slashSize), CPX_SS_2_M(centerPointX - (slashSize / 2.0)), //
			CPX_SS_2(centerPointX + (slashSize / 2.0)), CPY_SS_2_M(centerPointY - (slashSize / 2.0)), //
			CPY_SS_2(centerPointY + (slashSize / 2.0)), YL_SS(yL + slashSize);

			private final double value;

			PointData(final double val) {
				value = val;
			}

			public void setInto(Double[] xCoords, Double[] yCoords, PointData... points) {
				for (int i = 0; i < 4; i++) {
					xCoords[i] = points[i].getValue();
					yCoords[i] = points[i + 4].getValue();
				}

			}

			@SuppressWarnings("unused")
			public double getValue() {
				return value;
			}
		}

	}

}
