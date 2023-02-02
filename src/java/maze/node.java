package maze;

public class node {
	public int[] coordinate;
	public node parent;
	public double f;
	public double g;
	public double h;
	public int[] move;
	public int val;
	
	public node(int[] coord, int[] path) {
		coordinate = coord;
		parent = null;
		g = 0;
		h = 0;
		f = 0;
		move = path;
	}
}