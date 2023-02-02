package maze;

public class pathNode {
	public int[] coordinate;
	public pathNode parent;
	public pathNode a;
	public pathNode b;
	public pathNode c;
	public int status;
	
	public pathNode(int x, int y) {
		int[] coord = {x, y};
		coordinate = coord;
	}
}