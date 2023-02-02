package maze;

import static jason.asSyntax.ASSyntax.createList;


import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class exploreUnknownMap extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int startX = (int)((NumberTerm)args[0]).solve();
			int startY = (int)((NumberTerm)args[1]).solve();
			
			int height = agentmap.getHeight();
			int width = agentmap.getWidth();
			
			startX = agentmap.adjustX(startX);
			startY = agentmap.adjustY(startY);
			
			int searchRange = 1;
			int[][] map = agentmap.getMap();

			while(true) {
				for (int i = -searchRange; i<searchRange; i++) {
					int aX = agentmap.adjustX(startX+i);
					if(map[aX][agentmap.adjustY(startY+searchRange)] == 0) {
						return un.unifies(getCoords(aX, agentmap.adjustY(startY+searchRange)), args[2]);
					}
					if(map[aX][agentmap.adjustY(startY-searchRange)] ==0) {
						return un.unifies(getCoords(aX, agentmap.adjustY(startY-searchRange)), args[2]);
					}
				}
				
				for (int j = -searchRange; j<searchRange; j++) {
					int aY = agentmap.adjustY(startY+j);

					if(map[agentmap.adjustX(startX-searchRange)][aY] == 0) {
						return un.unifies(getCoords(agentmap.adjustX(startX - searchRange), aY), args[2]); //NEED TO MAKE RETURN A '1' SO IT DOESNT KEEP TRYING TO REACH IT
					}
					if(map[agentmap.adjustX(startX+searchRange)][agentmap.adjustY(startY+j)] == 0) {
						return un.unifies(getCoords(agentmap.adjustX(startX + searchRange), aY), args[2]);
					}
				}
				if(searchRange > width && searchRange > height) {
					break;
				}
				searchRange++;
				
			}


	    	return true;
		}
		
		public static ListTerm getCoords(int i, int j) {
			NumberTermImpl x =  new NumberTermImpl(i);
			NumberTermImpl y =  new NumberTermImpl(j);
			ListTerm coords = createList();
			coords.append(x);
			coords.append(y);
	    	return coords;
		}
		
}