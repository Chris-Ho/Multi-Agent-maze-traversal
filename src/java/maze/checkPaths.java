package maze;

import java.util.ArrayList;
import static jason.asSyntax.ASSyntax.createList;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class checkPaths extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int x = (int)((NumberTerm)args[0]).solve();
			int y = (int)((NumberTerm)args[1]).solve();

		
			ArrayList<int[]> paths = agentmap.checkPaths(x, y);
			
			ListTerm moves = createList();
			
			for(int i =0;i<paths.size();i++) {				
				NumberTermImpl tempx =  new NumberTermImpl(paths.get(i)[0]);
				NumberTermImpl tempy =  new NumberTermImpl(paths.get(i)[1]);
				ListTerm tempCoords = createList();
				tempCoords.append(tempx);
				tempCoords.append(tempy);
				moves.append(tempCoords);
			}
	    	return un.unifies(moves, args[2]);
		}
		
}