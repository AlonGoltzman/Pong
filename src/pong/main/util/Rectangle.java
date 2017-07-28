package pong.main.util;

import static pong.main.util.Util.b2bit;

import java.util.ArrayList;

public class Rectangle {

	private double x;
	private double y;
	private double w;
	private double h;

	public static final byte UP = 1;
	public static final byte DOWN = 3;
	public static final byte LEFT = 0;
	public static final byte RIGHT = 2;
	/**
	 * Edges of the rectangle are defined as follows:
	 * 
	 */
	private ArrayList<Double[][]> edges = new ArrayList<Double[][]>() {
		private static final long serialVersionUID = 8258405189700517142L;

		@Override
		public boolean add(Double[][] e) {
			// [0]-->[0,0][0,1], [1]-->[1,0][1,1]
			if (e.length > 2)
				throw new RuntimeException("Tried to add an edge with more then two coords.");
			if (e[0].length > 2 || e[1].length > 2)
				throw new RuntimeException("Tried to add an edge coord with more then two points.");
			if (this.size() == 4)
				throw new RuntimeException("Tried to add more than four edges");
			return super.add(e);
		}
	};

	/**
	 * Create a rectangle with pixel coordinates
	 * 
	 * @param x
	 *            - left bottom.
	 * @param y
	 *            - left bottom.
	 * @param w
	 *            - right top.
	 * @param h
	 *            - right top.
	 */
	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		defineEdges();
	}

	/**
	 * Create a rectangle with screen-relative coordinates, meaning if a
	 * variable is 1 it will receive the full size of the relative screen.
	 * 
	 * @param x
	 *            - left bottom.
	 * @param y
	 *            - left bottom.
	 * @param w
	 *            - right top.
	 * @param h
	 *            - right top.
	 */
	public Rectangle(float x, float y, float w, float h) {
		this.x = (int) (x * 800);
		this.y = (int) (y * 600);
		this.w = (int) (w * 800);
		this.h = (int) (h * 600);
		defineEdges();
	}

	/**
	 * Create a rectangle with pixel coordinates
	 * 
	 * @param x
	 *            - left bottom.
	 * @param y
	 *            - left bottom.
	 * @param w
	 *            - right top.
	 * @param h
	 *            - right top.
	 */
	public Rectangle(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		defineEdges();
	}

	public ArrayList<Double[][]> getEdges() {
		return edges;
	}

	/**
	 * * We are checking to see if another rectangle is inside our edges.<br>
	 * The way we do this is as follows:<br>
	 * <ul>
	 * <li>If the other one is bigger, we check the their edge#1 is above our
	 * edge#3 and their edge#3 is lower than our edge#1.<br>
	 * </li>
	 * <li>If the previous condition is met, then we check if our edge#2 is
	 * further than their edge#0 and, their edge#2 is further than our
	 * edge#2<br>
	 * </li>
	 * <li>If the above isn't met, we can say that we are bigger (thanks to the
	 * equal size parameter).<br>
	 * </li>
	 * <li>We check if our edge#1 is above their edge#3, and their edge#1 is
	 * above our edge#3.<br>
	 * </li>
	 * <li>If the above condition is met, we check if their edge#2 is further
	 * than our edge#0 and their our edge#2 is further than their edge#2.<br>
	 * </li>
	 * <li>If some of the conditions aren't met, we can assume we are not inside
	 * the edges.<br>
	 * </li>
	 * 
	 * @param rect
	 *            - the rectangle that we want to see if he's inside us.
	 * @return - true if he is, false otherwise.
	 */
	public boolean inEdges(Rectangle rect) {
		ArrayList<Double[][]> edges = rect.getEdges();
		Double[][] f_edge0 = edges.get(0);
		Double[][] f_edge1 = edges.get(1);
		Double[][] f_edge2 = edges.get(2);
		Double[][] f_edge3 = edges.get(3);
		Double[][] o_edge0 = this.edges.get(0);
		Double[][] o_edge1 = this.edges.get(1);
		Double[][] o_edge2 = this.edges.get(2);
		Double[][] o_edge3 = this.edges.get(3);

		boolean thisBigger = rect.rectangleBigger(this), equalSize = rectangleSameSize(rect),
				otherBigger = this.rectangleBigger(rect);
		if (otherBigger && !equalSize && thisBigger)
			throw new RuntimeException("Two rectangles can not be bigger than each other!");
		else if (equalSize)
			throw new RuntimeException("Rectangles of the same size shouldn't collide, or be inside one anohter!");
		if (otherBigger ? (edgeCheck(f_edge1, o_edge3, (byte) 1) && edgeCheck(o_edge1, f_edge3, (byte) 1))
				: (edgeCheck(o_edge1, f_edge3, (byte) 1) && edgeCheck(f_edge1, o_edge3, (byte) 1))) {
			if (otherBigger ? (edgeCheck(o_edge2, f_edge0, (byte) 0) && edgeCheck(f_edge2, o_edge2, (byte) 0))
					: (edgeCheck(f_edge2, o_edge0, (byte) 0) && edgeCheck(o_edge2, f_edge0, (byte) 0))) {
				return true;
			}
		}
		return false;

	}

	public boolean touchingEdges(Rectangle rect) {
		ArrayList<Double[][]> edges = rect.getEdges();
		Double[][] f_edge0 = edges.get(0);
		Double[][] o_edge2 = this.edges.get(2);
		Double[][] f_edge2 = edges.get(2);
		Double[][] o_edge0 = this.edges.get(0);
		if (!inEdges(rect)) {
			boolean xMatches = (f_edge0[0][0] == o_edge2[0][0] || o_edge0[0][0] == f_edge2[0][0]);
			if (f_edge0[0][0] == o_edge2[0][0] && o_edge0[0][0] == f_edge2[0][0])
				throw new RuntimeException(
						"Two parallel edges can not touch with two other parallel edges at the same time.");
			boolean yMatches = ((edgeCheck(f_edge0, o_edge2, (byte) 1)) && edgeCheck(o_edge0, f_edge2, (byte) 1));
			return xMatches && yMatches;
		}
		return false;
	}

	public boolean touchingEdges_Num(Rectangle rect, byte edge, boolean invert) {
		if (edge > 3 || edge < 0)
			throw new IllegalArgumentException(
					"Edge number must be from 0 to 3 [Inclusive].\n Use the predefined variables UP\\DOWN\\LEFT\\RIGHT in the Rectangle class.");
		Double[] oEdgeX = new Double[] { edges.get(edge)[0][0], edges.get(edge)[1][0] };
		Double[] oEdgeY = new Double[] { edges.get(edge)[0][1], edges.get(edge)[1][1] };

		// >> Invert the edge for the other rect.
		if (invert)
			if (edge == UP)
				edge = DOWN;
			else if (edge == LEFT)
				edge = RIGHT;
			else if (edge == DOWN)
				edge = UP;
			else
				edge = LEFT;

		Double[] fEdgeX = new Double[] { rect.edges.get(edge)[0][0], rect.edges.get(edge)[1][0] };
		Double[] fEdgeY = new Double[] { rect.edges.get(edge)[0][1], rect.edges.get(edge)[1][1] };
		sort(oEdgeX, oEdgeY, fEdgeX, fEdgeY);
		boolean xTouching = false;
		boolean yTouching = false; // >> Can't be both at the same time.
		if (edge == UP || edge == DOWN) // 1 or 3.
			// >> According to the rect logic, the Y's should be the same.
			yTouching = Math.abs(oEdgeY[0] - fEdgeY[0]) <= 1;
		else if (edge == RIGHT || edge == LEFT) // 0 or 2.
			// >> According to the rect logic, the Y's should be the same.
			xTouching = Math.abs(oEdgeX[0] - fEdgeX[0]) <= 1;
		boolean thisBigger = rect.rectangleBigger(this), equalSize = rectangleSameSize(rect);
		if (thisBigger && equalSize)
			throw new RuntimeException("Two quads of the same size can't touch edges.");
		// >> Done<TO-DO>: Optimize this code:
		/**
		 * if (thisBigger && xTouching) {<br>
		 * return oEdgeY[1] > fEdgeY[0] && fEdgeY[1] > oEdgeY[0];<br>
		 * } else if (thisBigger && yTouching) {<br>
		 * return oEdgeX[1] > fEdgeX[0] && fEdgeX[1] > oEdgeX[0];<br>
		 * } else if (!thisBigger && xTouching) {<br>
		 * return fEdgeY[1] > oEdgeY[0] && oEdgeY[1] > fEdgeY[0];<br>
		 * } else if (!thisBigger && yTouching) {<br>
		 * return fEdgeX[1] > oEdgeX[0] && oEdgeX[1] > fEdgeX[0];<br>
		 * }<br>
		 */
		// >> Edge 3 fucks this up.
		// >> Solution: sort the array.
		return (thisBigger
				? (xTouching ? oEdgeY[1] > fEdgeY[0] && fEdgeY[1] > oEdgeY[0]
						: (yTouching ? oEdgeX[1] > fEdgeX[0] && fEdgeX[1] > oEdgeX[0] : false))
				: (xTouching ? fEdgeY[1] > oEdgeY[0] && oEdgeY[1] > fEdgeY[0]
						: (yTouching ? fEdgeX[1] > oEdgeX[0] && oEdgeX[1] > fEdgeX[0] : false)));
	}

	/**
	 * A method to check if this rectangle contains another one.
	 * 
	 * @param rect
	 *            - the other rectangle.
	 * @return true if it does, false otherwise.
	 */
	public boolean containing(Rectangle rect) {
		Double[][] f_edge0 = rect.edges.get(0);
		Double[][] f_edge1 = rect.edges.get(1);
		Double[][] f_edge2 = rect.edges.get(2);
		Double[][] f_edge3 = rect.edges.get(3);
		Double[][] o_edge0 = this.edges.get(0);
		Double[][] o_edge1 = this.edges.get(1);
		Double[][] o_edge2 = this.edges.get(2);
		Double[][] o_edge3 = this.edges.get(3);
		return (edgeCheck(f_edge0, o_edge0, (byte) 0) && edgeCheck(o_edge2, f_edge2, (byte) 0)
				&& edgeCheck(o_edge1, f_edge1, (byte) 1) && edgeCheck(f_edge3, o_edge3, (byte) 1));
	}

	public boolean not_containing_x(Rectangle rect) {
		double oXClose = this.edges.get(0)[0][0];
		double oXFar = this.edges.get(2)[0][0];
		double fXClose = rect.edges.get(0)[0][0];
		double fXFar = rect.edges.get(2)[0][0];
		return oXClose - fXFar > 30 || fXClose - oXFar > 30;
	}

	public double getDistanceFromCenterHit(Rectangle rect) {
		Double[][] f_edge0 = rect.edges.get(0);
		Double[][] o_edge0 = this.edges.get(0);
		double o_CenterPointY = (o_edge0[1][1] - o_edge0[0][1]) / 2 + o_edge0[0][1];
		double f_CenterPointY = (f_edge0[1][1] - f_edge0[0][1]) / 2 + f_edge0[0][1];
		return f_CenterPointY - o_CenterPointY;
	}

	public void updateCoords(int x, int y) {
		this.x = x;
		this.y = y;
		edges.clear();
		defineEdges();
	}

	public void updateCoords(double x, double y) {
		this.x = x;
		this.y = y;
		edges.clear();
		defineEdges();
	}

	public void updateCoords(float x, float y) {
		this.x = (int) (x * 800);
		this.y = (int) (y * 600);
		edges.clear();
		defineEdges();
	}

	/**
	 * A method to check if one edge is ahead (in terms of coords) of another
	 * one.
	 * 
	 * @param firstEdge
	 *            - the edge that we're thinking is ahead.
	 * @param secEdge
	 *            - the edge we're thinking is behind.
	 * @param pos
	 *            - the type of check, 0 for width, 1 for height.
	 * @return
	 */
	private boolean edgeCheck(Double[][] firstEdge, Double[][] secEdge, byte pos) {
		if (pos > 1 || pos < 0)
			throw new IllegalArgumentException("In edgeCheck, the variable pos, must be either 0 or 1.");
		double edge1;
		double edge1Addition = 0;
		double edge2;
		double edge2Addition = 0;

		edge1 = Math.min(firstEdge[0][pos], firstEdge[1][pos]);
		if (firstEdge[0][pos] != firstEdge[1][pos])
			edge1Addition = Math.max(firstEdge[0][pos], firstEdge[1][pos]);
		edge2 = Math.min(secEdge[0][pos], secEdge[1][pos]);
		if (secEdge[0][pos] != secEdge[1][pos])
			edge2Addition = Math.max(secEdge[0][pos], secEdge[1][pos]);

		if (edge1Addition == 0 && edge2Addition == 0)
			return edge1 > edge2;
		else if (edge1Addition == 0)
			return edge1 > edge2Addition;
		else if (edge2Addition == 0)
			return edge1Addition > edge2;
		else
			return edge1Addition > edge2Addition;
	}

	/**
	 * A method to check if a rectangle is the same size as this one.<br>
	 * We can not take into account the space of the rectangle, since it might
	 * be the same space but different dimension.
	 * 
	 * @param rect
	 *            - the other rect.
	 * @return
	 */
	private boolean rectangleSameSize(Rectangle rect) {
		double o_Edge0Size = getEdgeSize(this.edges.get(0), (byte) 0);
		double o_Edge1Size = getEdgeSize(this.edges.get(1), (byte) 1);
		double o_Edge2Size = getEdgeSize(this.edges.get(2), (byte) 2);
		double o_Edge3Size = getEdgeSize(this.edges.get(3), (byte) 3);
		double f_Edge0Size = getEdgeSize(rect.edges.get(0), (byte) 0);
		double f_Edge1Size = getEdgeSize(rect.edges.get(1), (byte) 1);
		double f_Edge2Size = getEdgeSize(rect.edges.get(2), (byte) 2);
		double f_Edge3Size = getEdgeSize(rect.edges.get(3), (byte) 3);
		return (o_Edge0Size == f_Edge0Size) && (o_Edge1Size == f_Edge1Size) && (o_Edge2Size == f_Edge2Size)
				&& (o_Edge3Size == f_Edge3Size);

	}

	/**
	 * A method to check if a rectangle is bigger than this one.<br>
	 * In this method we can take into account the space that the rectangle
	 * takes in the game world, whilst in {@link #rectangleSameSize(Rectangle)}
	 * we cannot since, it might be the same space, but different dimension.
	 * 
	 * @param rect
	 *            - the other rect.
	 * @return - if the other rectangle is bigger than this one.
	 */
	private boolean rectangleBigger(Rectangle rect) {
		double o_RectSize = getSize();
		double f_RectSize = rect.getSize();
		return f_RectSize > o_RectSize;
	}

	private double getEdgeSize(byte edgeNum) {
		return getEdgeSize(edges.get(edgeNum), edgeNum);
	}

	/**
	 * * All the return statements can be divided into two parts: <br>
	 * (part1 - part2). <br>
	 * Both parts are similar, consisting of the same ternary statements but
	 * with different sub-rules (the rules of the nested ternary statements).
	 * The primary ternary statements are constructed as follows:<br>
	 * (edgeNumCheck ? (sub-ternary) : (sub-ternary)). <br>
	 * The sub ternary statements are constructed as follows:<br>
	 * (boolean ? 1 : 0)<br>
	 * If the boolean (one or two, stated above), is true, return 1, if false,
	 * return 0. <br>
	 * Each part is constructed so that it matches the edge:<br>
	 * <ul>
	 * <li>edge[1][1] - edge[0][1] - edge 0
	 * <li>edge[0][0] - edge[1][0] - edge 3
	 * <li>edge[1][0] - edge[0][0] - edge 1
	 * <li>edge[0][1] - edge[1][1] - edge 2
	 * </ul>
	 * <Br>
	 * Edge 0 \ 3 :<br>
	 * Part 1 : edge <br>
	 * [edgeNum == 0 ? b2bit(one) : b2bit(!one)]<br>
	 * [edgeNum == 0 ? b2bit(two) : b2bit(!two)]<br>
	 * Part 2 : edge <br>
	 * [edgeNum == 0 ? b2bit(!one) : b2bit(one)]<br>
	 * [edgeNum == 0 ? b2bit(two) : b2bit(!two)]<br>
	 * <br>
	 * Edge 1 \ 2 : <br>
	 * Part 1 : edge<br>
	 * [edgeNum == 1 ? b2bit(!one) : b2bit(one)]<br>
	 * [edgeNum == 1 ? b2bit(!two) : b2bit(two)]<br>
	 * Part 2 : edge<br>
	 * [edgeNum == 1 ? b2bit(one) : b2bit(!one)]<br>
	 * [edgeNum == 1 ? b2bit(!two) : b2bit(two)]<br>
	 * 
	 * The rules these sets follow are as follows: <br>
	 * Part 1 of edges 0 & 3 is the inverted Part 2 of edges 1 & 2.<br>
	 * And <br>
	 * Part 2 of edges 0 & 3 is the inverted Part 2 of edges 1 & 2.<br>
	 * 
	 * @param edge
	 *            - the edge in question.
	 * @param edgeNum
	 *            - the edge number.
	 * @return - the size of the edge in question.
	 */
	private double getEdgeSize(Double[][] edge, byte edgeNum) {
		final boolean one = true, two = true;
		if (edgeNum == 0 || edgeNum == 3) {
			return (edge[edgeNum == 0 ? b2bit(one) : b2bit(!one)][edgeNum == 0 ? b2bit(two) : b2bit(!two)]
					- edge[edgeNum == 0 ? b2bit(!one) : b2bit(one)][edgeNum == 0 ? b2bit(two) : b2bit(!two)]);
		} else if (edgeNum == 1 || edgeNum == 2) {
			return (edge[edgeNum == 1 ? b2bit(!one) : b2bit(one)][edgeNum == 1 ? b2bit(!two) : b2bit(two)]
					- edge[edgeNum == 1 ? b2bit(one) : b2bit(!one)][edgeNum == 1 ? b2bit(!two) : b2bit(two)]);
		} else {
			throw new IllegalArgumentException("Byte variable edgeNum must be from 0 - 3");
		}
	}

	/**
	 * Gets the size of this rectangle.
	 * 
	 * @return - the size.
	 */
	private double getSize() {
		double edge0Size = getEdgeSize((byte) 0);
		double edge3Size = getEdgeSize((byte) 3);
		return edge0Size * edge3Size;
	}

	private void sort(Double[]... arrs) {
		for (Double[] arr : arrs) {
			if (arr[0] > arr[1]) {
				double temp = arr[0];
				arr[0] = arr[1];
				arr[1] = temp;
			}
		}
	}

	private void defineEdges() {
		edges.add(new Double[][] { { x, y }, { x, y + h } }); // Edge 0
		edges.add(new Double[][] { { x, y + h }, { x + w, y + h } }); // Edge 1
		edges.add(new Double[][] { { x + w, y + h }, { x + w, y } }); // Edge 2
		edges.add(new Double[][] { { x + w, y }, { x, y } }); // Edge 3
	}

	public double getHighestY() {
		return y + h;
	}

	public double getLowestY() {
		return y;
	}

	public double getFurthestX() {
		return x + w;
	}

	public double getClosestX() {
		return x;
	}

}
