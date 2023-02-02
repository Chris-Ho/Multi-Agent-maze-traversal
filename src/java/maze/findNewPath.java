package maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static jason.asSyntax.ASSyntax.createList;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class findNewPath extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int x = (int)((NumberTerm)args[0]).solve();
			int y = (int)((NumberTerm)args[1]).solve();

			int[] start = {x,y};
			
			int[][] map = agentmap.getMap();
			LinkedList<int[]> paths = agentmap.getPaths();
			
			int min = 10000;
			
			for(int i =0;i<paths.size();i++) {
				int[] destination = {paths.get(i)[0],paths.get(i)[1]};
				ArrayList<int[]> path = navigatemap.pathfind(destination, map, start);
				if(path.size() < min) {
					min = path.size();
				}
			}
			
			ListTerm coords = createList();

			if(paths.size()>0) {
				int[] newPath = {paths.get(min)[0], paths.get(min)[1]};
				agentmap.removePath(paths.get(min)[0], paths.get(min)[1]);
				NumberTermImpl absx =  new NumberTermImpl(newPath[0]);
				NumberTermImpl absy =  new NumberTermImpl(newPath[1]);
				coords.append(absx);
				coords.append(absy);
				
			}
			
	    	return un.unifies(coords, args[2]);
		}
		
}