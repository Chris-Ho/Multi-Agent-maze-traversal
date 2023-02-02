package myLib;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.asSyntax.NumberTerm;
//import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.PriorityQueue;
//import java.util.Queue;

@SuppressWarnings("serial")
public class agentmap extends DefaultInternalAction{
		private static int[][] agentMap;
		protected static int height;
		protected static int width;
		private static int x;
		private static int y;
		private static boolean initialised = false;
		private static LinkedList<int[]> frontier = new LinkedList<int[]>();
		private static LinkedList<int[]> archives = new LinkedList<int[]>();
		
		private static int verticalVisionLimitA; //must be greater than A
		private static int verticalVisionLimitB; //less than B
		private static int horizontalVisionLimitA;
		private static int horizontalVisionLimitB;
		
		private static boolean specialVerticalA;
		private static boolean specialVerticalB;
		private static boolean specialHorizontalA;
		private static boolean specialHorizontalB;
		
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
		
//		public static void setupSearch(int scanrange) {
//			int sections = (width / (2*scanrange + 1));
//			int x = 0;
//			int y = 0;
//			for(int j = 0; j<height/(scanrange); j++) {
//				for(int i = 0;i<sections;i++) {
//					Integer[] coord = {x,y};
//					x+=14;
//					if(x>=width) {
//						x=x-width;
//					}
//					frontier.add(coord);
//				}
//				x=x-(scanrange+1);
//				y=y+scanrange;
//				if(x<0) {
//					x = width + x;
//				}
//				if(y>height) {
//					y = y-height;
//				}
//			}
//			currentHotspot = frontier.get(0);
//			frontier.remove(0);
//		}
		
		public static int[][] getMap() {
			return agentMap;
		}
		
		public synchronized static void updateMap(int[][] latest) {
			agentMap = latest;
		}
		
		
		public static void addScan(int x, int y, int scanrange) {
			
			archives.addAll(frontier);
			frontier.clear();
			
			updateVision(x,y,scanrange);
			
			for(int i=-scanrange;i<scanrange+1;i++) {
				for(int j = -(scanrange-Math.abs(i));j<scanrange-Math.abs(i)+1;j++) {
					int absX = adjustX(x+i);
					int absY = adjustY(y+j);
					if(agentMap[absX][absY] == 0 || agentMap[absX][absY] == 1 || agentMap[absX][absY] == 2) { 
						if(isVisible(absX, absY) || agentMap[absX][absY] == 2) {
							int[] temp = {absX, absY};
							agentMap[absX][absY] = 2;
							System.out.println("FRONTIER:" + absX + " " + absY);
							frontier.add(temp);
						}
						else if(agentMap[absX][absY] !=2){
							agentMap[absX][absY] = 1;
							System.out.println("not visible");
						}
					}
				}
			}
			System.out.println(frontier.size());
			propogateVision();
			System.out.println(frontier.size());

		}
		
		public static void propogateVision() {
			for(int i =0;i<frontier.size();i++) {
				int[] current = frontier.get(i);
				recursivePropogation(current[0], current[1]);
			}
		}
		
		public static void recursivePropogation(int x, int y) {
			int surroundingWalls = 0;
			int yplus = adjustY(y+1);
			int yminus = adjustY(y-1);
			int xplus = adjustX(x+1);
			int xminus = adjustX(x-1);
			if(agentMap[x][yplus] == 1) {
				agentMap[x][yplus] = 2;
				int[] t = {x, yplus};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);
					
				recursivePropogation(x, yplus);
			}
			else if(agentMap[x][yplus] == -1) {
				surroundingWalls+=1;
			}
			if(agentMap[x][yminus] == 1) {
				agentMap[x][yminus] = 2;
				int[] t = {x, yminus};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);

				recursivePropogation(x, yminus);
			}
			else if(agentMap[x][yminus] == -1) {
				surroundingWalls+=1;
			}
			if(agentMap[xplus][y] == 1) {
				agentMap[xplus][y] = 2;
				int[] t = {xplus, y};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);

				recursivePropogation(xplus, y);
			}
			else if(agentMap[xplus][y] == -1) {
				surroundingWalls+=1;
			}
			if(agentMap[xminus][y] == 1) {
				agentMap[xminus][y] = 2;
				int[] t = {xminus, y};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);

				recursivePropogation(xminus, y);
			}
			else if(agentMap[xminus][y] == -1) {
				surroundingWalls+=1;
			}
			//DIAGONALS
			if(agentMap[xminus][yminus] == 1) {
				agentMap[xminus][yminus] = 2;
				int[] t = {xminus, yminus};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);

				recursivePropogation(xminus, yminus);
			}
			else if(agentMap[xminus][yminus] == -1) {
				surroundingWalls+=1;
			}
			
			if(agentMap[xminus][yplus] == 1) {
				agentMap[xminus][yplus] = 2;
				int[] t = {xminus, yplus};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);

				recursivePropogation(xminus, yplus);
			}
			else if(agentMap[xminus][yplus] == -1) {
				surroundingWalls+=1;
			}
			
			if(agentMap[xplus][yminus] == 1) {
				agentMap[xplus][yminus] = 2;
				int[] t = {xplus, yminus};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);

				recursivePropogation(xplus, yminus);
			}
			
			if(agentMap[xplus][yplus] == 1) {
				agentMap[xplus][yplus] = 2;
				int[] t = {xplus, yplus};
				frontier.add(t);
				System.out.println("FRONTIER:" + t[0] + " " + t[1]);

				recursivePropogation(xplus, yplus);
			}
			else if(agentMap[xplus][yplus] == -1) {
				surroundingWalls+=1;
			}
			
			if(surroundingWalls>=5) {
				agentMap[x][y] = 3;
			}
		}
		
		public static void updateVision(int x, int y, int scanrange) {
			int previousHLA = x-scanrange;
			int previousHLB = x+scanrange;
			int previousVLA = y-scanrange;
			int previousVLB = y+scanrange;

			verticalVisionLimitA = -10000;
			verticalVisionLimitB = 10000;
			horizontalVisionLimitA = -10000;
			horizontalVisionLimitB = 10000;
			
			specialVerticalA = false;
			specialVerticalB = false;
			specialHorizontalA = false;
			specialHorizontalB = false;
			
			for(int i=-scanrange;i<scanrange+1;i++) {
				for(int j = -(scanrange-Math.abs(i));j<scanrange-Math.abs(i)+1;j++) {
					int absX = adjustX(x+i);
					int absY = adjustY(y+j);
					if(agentMap[absX][absY] == -1) {
						
						if(absX==x) {
							if(absY>=y) {
								if(absY-y <= scanrange) {
									if(absY<verticalVisionLimitB) {
										previousVLB = verticalVisionLimitB;
										verticalVisionLimitB = absY;
									}
								}
								else {
									specialVerticalA = true;
									previousVLA = -100000;
									verticalVisionLimitA = absY;
								}
							}
							else if(absY<=y) {
								if(y-absY <= scanrange) {
									if(absY>verticalVisionLimitA) {
										previousVLA = verticalVisionLimitA;
										verticalVisionLimitA = absY;
									}
									else if(specialVerticalA) {
										specialVerticalA = false;
										previousVLA = verticalVisionLimitA;
										verticalVisionLimitA = absY;
									}
								}
								else {
									specialVerticalB = true;
									previousVLB = 100000;
									verticalVisionLimitB = absY;
								}
							}
						}
						
						if(absY==y) {
							if(absX>=x) {
								if(absX-x <= scanrange) {
									if(absX<horizontalVisionLimitB) {
										previousHLB = horizontalVisionLimitB;
										horizontalVisionLimitB = absX;
									}
								}
								else {
									specialHorizontalA = true;
									previousHLA = -100000;
									horizontalVisionLimitA = absX;
									
								}
							}
							else if(absX<=x) {
								if(x-absX <= scanrange) {
									if(absX>horizontalVisionLimitA) {
										previousHLA = horizontalVisionLimitA;
										horizontalVisionLimitA = absX;
									}
									else if(specialHorizontalA) {
										specialHorizontalA = false;
										previousHLA = horizontalVisionLimitA;
										horizontalVisionLimitA = absX;
									}
								}
								else {
									//ADD UPDATED HORIZONTAL AND VERTICAL LIMITS A/B FOR NEW VERSION OF ISVISIBLE
									specialHorizontalB = true; 
									previousHLB = 100000;
									horizontalVisionLimitB = absX;
								}
							}
						}
						
					}
//					else if(i == -j || j==i) {
//						if(i<0 && j>0) { //bottom left corner open
//							if(horizontalVisionLimitA == absX && verticalVisionLimitB == absY) {
//								horizontalVisionLimitA = previousHLA;
//								verticalVisionLimitB = previousVLB;
//							}
//						}
//						
//						if(i<0 && j<0) { //top left corner open
//							if(horizontalVisionLimitA == absX && verticalVisionLimitA == absY) {
//								horizontalVisionLimitA = previousHLA;
//								verticalVisionLimitA = previousVLA;
//							}
//						}
//						
//						if(i>0 && j<0) { //top right corner open
//							if(horizontalVisionLimitB == absX && verticalVisionLimitB == absY) {
//								horizontalVisionLimitB = previousHLB;
//								verticalVisionLimitB = previousVLB;
//							}
//						}
//						
//						if(i>0 && j>0) { //bottom right corner open
//							if(horizontalVisionLimitA == absX && verticalVisionLimitB == absY) {
//								horizontalVisionLimitB = previousHLB;
//								verticalVisionLimitB = previousVLB;
//							}
//						}
//					}
				}
			}
			System.out.println("Horizontal A: Horizontal B: SpecialA? : SpecialB? " + horizontalVisionLimitA + " " + horizontalVisionLimitB + " " + specialHorizontalA + " " + specialHorizontalB);
			System.out.println("Vertical A: Vertical B: SpecialA? : SpecialB? " + verticalVisionLimitA + " " + verticalVisionLimitB + " " + specialVerticalA + " " + specialVerticalB);

		}
		
		public static boolean isVisible(int absX, int absY) {
			if(specialVerticalA && (absY<=verticalVisionLimitB || (absY>=verticalVisionLimitA && absY<height))) {
				if(specialHorizontalA && (absX<horizontalVisionLimitB || absX>horizontalVisionLimitA && absX<width)) {
					return true;
				}
				else if(absX>=horizontalVisionLimitA && absX<=horizontalVisionLimitB) {
					return true;
				}
				else if(specialHorizontalB && (absX>=horizontalVisionLimitA || absX<=horizontalVisionLimitB && absX>=0)) {
					return true;
				}
			}
			else if(specialVerticalB && (absY>=verticalVisionLimitA || absY<=verticalVisionLimitB && absY>=0)) {
				if(specialHorizontalA && (absX<=horizontalVisionLimitB || absX>=horizontalVisionLimitA && absX<width)) {
					return true;
				}
				else if(absX>horizontalVisionLimitA && absX<horizontalVisionLimitB) {
					return true;
				}
				else if(specialHorizontalB && (absX>=horizontalVisionLimitA || absX<=horizontalVisionLimitB && absX>=0)) {
					return true;
				}
			}
			else if (absY>= verticalVisionLimitA && absY<= verticalVisionLimitB) {
				if(specialHorizontalA && (absX<=horizontalVisionLimitB || absX>=horizontalVisionLimitA && absX<width)) {
					return true;
				}
				else if(absX>horizontalVisionLimitA && absX<horizontalVisionLimitB) {
					return true;
				}
				else if(specialHorizontalB && (absX>=horizontalVisionLimitA || absX<=horizontalVisionLimitB && absX>=0)) {
					return true;
				}
			}
			return false;
		}
		
		public static int[] getFrontier(){
			if(frontier.size() == 0) {
				System.out.println("FRONTIER EMPTY");
				int[] fail = {-1};
				return fail;
			}
			double max = -10000;
			int[] next = null;
			int index = 0;

			for(int i =0;i<frontier.size();i++) {
				double h = navigatemap.heuristic(x, y, frontier.get(i)[0], frontier.get(i)[1]);
				if (h > max){
					max = h;
					next = frontier.get(i);
					index = i;
				}
			}
			frontier.remove(index);

			return next;
		}
		
		public static int[] backtrack() {
			int[] next = {};
			for(int i =0;i<archives.size();i++) {
				System.out.println("Archive entry: " + archives.get(i)[0] + " " + archives.get(i)[1]);
			}
			double min = 1000;
			int index = 0;
			for(int i =0;i<archives.size();i++) {
				if(agentMap[archives.get(i)[0]][archives.get(i)[1]] == 2) {
					double h = navigatemap.heuristic(x, y, frontier.get(i)[0], frontier.get(i)[1]);
					if (h < min){
						min = h;
						next = frontier.get(i);
						index = i;
					}
					next = archives.get(i);
				}
				else {
					System.out.println("Removing: " + archives.get(i)[0] + " " + archives.get(i)[1] + " val: " + agentMap[archives.get(i)[0]][archives.get(i)[1]]);
					archives.remove(i);
				}
			}
			
			frontier.remove(index);
			return next;
		}
		
		public static void removeFrontier(int x, int y) {
			int [] visited = {x,y};
			agentMap[x][y] = 3;
			try {
				frontier.remove(visited);
			}
			catch (Exception e) {
				//Not in frontier.
			}
		}
		
		public static void markVisited(int x, int y) {
			agentMap[x][y] = 3;
		}
		
//		public static int[] addHotspot(int x, int y) {
//			
//			if(agentMap[currentHotspot[0]][currentHotspot[1]] == -1) {
//				int countX = 0;
//				int countY = 0;
//				int nx = currentHotspot[0];
//				int ny = currentHotspot[1];
//				while(agentMap[nx][ny] == -1) {
//					if(countX > countY) {
//						ny+=1;
//						countY++;
//					}
//					else {
//						nx+=1;
//						countX++;
//					}
//				}
//				
//				Integer[] coord = {nx, ny};
//				frontier.add(coord);
//				currentHotspot = frontier.get(0);
//				frontier.remove(0);
//			}
//			
//			int[] coords = {(int)currentHotspot[0], (int)currentHotspot[1]};
//			return coords;
//			
//		}
		
		
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