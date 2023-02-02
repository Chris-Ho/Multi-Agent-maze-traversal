package myLib;


import static jason.asSyntax.ASSyntax.createList;

import java.util.ArrayList;
import java.util.Collections;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class navigatemap extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			
			int startX = (int)((NumberTerm)args[0]).solve();
			int startY = (int)((NumberTerm)args[1]).solve();
			
			startX = agentmap.adjustX(startX);
			startY = agentmap.adjustY(startY);
			
			int destinationX = (int)((NumberTerm)args[2]).solve();
			int destinationY = (int)((NumberTerm)args[3]).solve();
			
			destinationX = agentmap.adjustX(destinationX);
			destinationY = agentmap.adjustY(destinationY);


			int[] destination = {destinationX, destinationY};
			int[] start = {startX, startY};

			
			int[][] map = agentmap.getMap();

			
			ArrayList<int[]> path = pathfind(destination, map, start);
			Collections.reverse(path);
			
		
			
			ListTerm moves = createList();
				
			for(int i =0;i<path.size();i++) {				
				NumberTermImpl tempx =  new NumberTermImpl(path.get(i)[0]);
				NumberTermImpl tempy =  new NumberTermImpl(path.get(i)[1]);
				ListTerm tempCoords = createList();
				tempCoords.append(tempx);
				tempCoords.append(tempy);
				moves.append(tempCoords);
			}
	    	return un.unifies(moves, args[4]);

		}
		
		//A* search algorithm here is inspired by A* search algorithm by @Nicholas Swift on medium.com
		public static ArrayList<int[]> pathfind(int[] destination, int[][] map, int[] startCoords) {
			ArrayList<node> openList = new ArrayList<node>();
			ArrayList<node> closedList = new ArrayList<node>();
			int width = agentmap.getWidth();
			int height = agentmap.getHeight();
			
			int[] ghoststart = {calcResourceXY.normalise(startCoords[0]), calcResourceY.normalise(startCoords[1])};
			int[] ghostdestination = {calcResourceXY.normalise(destination[0]), calcResourceY.normalise(destination[1])};
						
			node start = new node(ghoststart, null);
			
			openList.add(start);
			
			while(openList.size() > 0) {
				node curNode = openList.get(0);
//				System.out.println(curNode.coordinate[0] + " " + curNode.coordinate[1]);
				int curIndex = 0;
				for(int i = 0;i<openList.size();i++) {
					if(openList.get(i).f < curNode.f) {
						curNode = openList.get(i);
						curIndex = i;
					}
				}

				openList.remove(curIndex);
				closedList.add(curNode);
				if(agentmap.adjustX(curNode.coordinate[0]) == destination[0] && agentmap.adjustY(curNode.coordinate[1]) == destination[1]) {
					ArrayList<int[]> path = new ArrayList<int[]>();
					node current = curNode;
					while(current !=null) {
						path.add(current.move);
						current = current.parent;
					}
					path.remove(path.size()-1);
					return (path);
				}

				ArrayList<node> children = new ArrayList<node>();
//				ArrayList<int[]> ghostchildren = new ArrayList<int[]>();

				
				int[][] options = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
				
				for(int i =0; i<options.length;i++) {
					int[] adjacentNode = {curNode.coordinate[0] + options[i][0], curNode.coordinate[1] + options[i][1]};
//					int[] ghostNode = {curNode.coordinate[0] + options[i][0], curNode.coordinate[1] + options[i][1]};
//					if(adjacentNode[0] >= width) {
//						adjacentNode[0] = adjacentNode[0] - width;
//					}
//					else if(adjacentNode[0] < 0) {
//						adjacentNode[0] = width + adjacentNode[0];
//					}
//					
//					if(adjacentNode[1] >= height) {
//						adjacentNode[1] = adjacentNode[1] - height;
//					}
//					else if(adjacentNode[1] < 0) {
//						adjacentNode[1] = adjacentNode[1] + height;
//					}
					
					if(map[agentmap.adjustX(adjacentNode[0])][agentmap.adjustY(adjacentNode[1])] != -1) { 
						node newNode = new node(adjacentNode, options[i]);
						newNode.parent = curNode;
						children.add(newNode);
//						ghostchildren.add(ghostNode);
					}
				}
				
				//for each child in children
				for(int i = 0;i<children.size();i++) {
					boolean exists = false;
					//if child is in closedList, don't add
					node newChild = children.get(i);
					if(!closedList.contains(children.get(i))){
	
						newChild.g = curNode.g + 1;
						newChild.h = heuristic(children.get(i).coordinate[0], children.get(i).coordinate[1], ghostdestination[0], ghostdestination[1]);
						newChild.f = newChild.g + newChild.h;
						
						for(int j =0;j<openList.size();j++) {
							if(children.get(i).coordinate[0] == openList.get(j).coordinate[0] 
									&& children.get(i).coordinate[1] == openList.get(j).coordinate[1] 
									&& children.get(i).g > openList.get(j).g) {
								exists = true;
							}
						}
						if(!exists) {
							openList.add(newChild);
						}
					}
					
					

				}
				
			}
			return null;
		}
		
		public static double heuristic(int startX, int startY, int endX, int endY) {
			return (Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
		}
		
}