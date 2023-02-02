package maze;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.asSyntax.NumberTerm;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.PriorityQueue;
//import java.util.Queue;
import java.util.regex.PatternSyntaxException;

@SuppressWarnings("serial")
public class agentmap extends DefaultInternalAction{
		private static int[][] agentMap;
		protected static int height;
		protected static int width;
		private static int x;
		private static int y;
		private static boolean initialised = false;
		private static LinkedList<int[]> paths = new LinkedList<int[]>();

		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {
			
			height = (int)((NumberTerm)args[0]).solve();
			width = (int)((NumberTerm)args[1]).solve();
			
			if(!initialised) {
				initialised = true;
				x = 0;
				y = 0;
				agentMap = new int[width][height];
				for(int j=0; j<width;j++) {
					for(int i =0; i<height; i++) {
						agentMap[i][j] = 0;
					}
				}
				agentMap[0][0] = 3;
				}
			return true;
		}
		
		public synchronized static void addScan(int x, int y) {
			int scanrange = 1;
			agentMap[x][y] = 3;
			for(int i=-scanrange;i<scanrange+1;i++) {
				for(int j = -(scanrange-Math.abs(i));j<scanrange-Math.abs(i)+1;j++) {
					int absX = adjustX(x+i);
					int absY = adjustY(y+j);
					if(agentMap[absX][absY] !=-1 && agentMap[absX][absY] !=3){
						agentMap[absX][absY] = 1;
					}
				}
			}
			
		}
		
		public synchronized static void removePath(int x, int y) {
			int[] coord = {x,y};
			agentMap[x][y] = 3;
			for(int i =0;i<paths.size();i++) {
				int[] test = paths.get(i);
				if(test[0] == x && test[1] == y) {
					paths.remove(i);
				}
			}
		}
		
		
		public static void addOccupant(int x, int y) {
			agentMap[x][y] = 5;
		}
		
		public static void removeOccupant(int x, int y) {
			agentMap[x][y] = 3;
		}
		
		public static int[][] getMap() {
			return agentMap;
		}
		
		public static LinkedList<int[]> getPaths(){
			return paths;
		}
		
		public static boolean contains(int [] coord) {
			int x = coord[0];
			int y = coord[1];
			for(int i =0;i<paths.size();i++) {
				int[] test = paths.get(i);
				if(test[0] == x && test[1] == y) {
					return true;
				}
			}
			return false;
		}
		
		public synchronized static ArrayList<int[]> checkPaths(int x, int y) {
			ArrayList<int[]> newPaths = new ArrayList<int[]>();
			if(agentMap[adjustX(x)][adjustY(y+1)] == 1) {
				int[] temp = {adjustX(x), adjustY(y+1)};
				if(!contains(temp)) {
					paths.add(temp);
					newPaths.add(temp);
				}
			}
			if(agentMap[adjustX(x)][adjustY(y-1)] == 1) {
				int[] temp = {adjustX(x), adjustY(y-1)};
				if(!contains(temp)) {
					paths.add(temp);
					newPaths.add(temp);
				}
			}
			if(agentMap[adjustX(x+1)][adjustY(y)] == 1) {
				int[] temp = {adjustX(x+1), adjustY(y)};
				if(!contains(temp)) {
					paths.add(temp);
					newPaths.add(temp);
				}
			}
			if(agentMap[adjustX(x-1)][adjustY(y)] == 1) {
				int[] temp = {adjustX(x-1), adjustY(y)};
				if(!contains(temp)) {
					paths.add(temp);
					newPaths.add(temp);
				}
			}
			
			return newPaths;
		}
	
		
		public static void markVisited(int x, int y) {
			agentMap[x][y] = 3;
		}
		
		public static boolean checkIfUnscanned(int x, int y) {
			if(agentMap[x][y] == 0 || agentMap[x][y] == 1) {
				return true;
			}
			return false;
		}

		public static void addObstacle(int x, int y) {
			agentMap[x][y] = -1;
		}
		
		public static boolean checkObstacle(int x, int y) {
			if(agentMap[x][y] == -1) {
				return true;
			}
			return false;
		}
		
		public synchronized static void updateCoords(int i, int j) {
			x +=i;
			y +=j;
			
			x = adjustX(x);
			y = adjustY(y);			
		}
		
		public static int adjustX(int x) {
			if(x>=width) {
				x=x-width;
			}
			
			if(x<0) {
				x=x+width;
			}
			return x;
		}
		
		public static int adjustY(int y) {
			if(y>=height) {
				y=y-height;
			}
			
			if(y<0) {
				y=y+height;
			}
			return y;
		}
		
		public static int[] getCoords() {
			int coords[] = {x,y};
			return coords;
		}
		
		public static int getWidth() {
			return width;
		}
		
		public static int getHeight() {
			return height;
		}
		
		
}