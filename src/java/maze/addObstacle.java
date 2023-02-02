package maze;

import jason.asSemantics.*;
import jason.asSyntax.*;


@SuppressWarnings("serial")
public class addObstacle extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int agentX = (int)((NumberTerm)args[0]).solve();
			int agentY = (int)((NumberTerm)args[1]).solve();
			
			int relX = (int)((NumberTerm)args[2]).solve();
			int relY = (int)((NumberTerm)args[3]).solve();
			
			int x = agentX + relX;
			int y = agentY + relY;
			
			int width = agentmap.getWidth();
			int height = agentmap.getHeight();
			
			if(x<0) {
				x= x + width;
			}
			else if(x>=width) {
				x=x - width;
			}
			
			if(y<0) {
				y= y +height;
			}
			else if(y>=height) {
				y = y - height;
			}
			agentmap.addObstacle(x, y);
			return true;
		}
		
		
}