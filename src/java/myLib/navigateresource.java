package myLib;

import static jason.asSyntax.ASSyntax.createList;

import java.util.ArrayList;
import java.util.Collections;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class navigateresource extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int destinationX = (int)((NumberTerm)args[0]).solve();
			int destinationY = (int)((NumberTerm)args[1]).solve();
			
			destinationX = agentmap.adjustX(destinationX);
			destinationY = agentmap.adjustY(destinationY);
			
			int startX = (int)((NumberTerm)args[2]).solve();
			int startY = (int)((NumberTerm)args[3]).solve();
			
			startX = agentmap.adjustX(startX);
			startY = agentmap.adjustY(startY);


			int[] destination = {destinationX, destinationY};
			int[] start = {startX, startY};

			
			int[][] map = agentmap.getMap();

			
			ArrayList<int[]> path = navigatemap.pathfind(destination, map, start);
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
		
		
}