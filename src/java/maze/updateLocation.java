package maze;

import static jason.asSyntax.ASSyntax.createList;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class updateLocation extends DefaultInternalAction{


		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {
			
			int x = (int)((NumberTerm)args[0]).solve();
			int y = (int)((NumberTerm)args[1]).solve();
			int mX = (int)((NumberTerm)args[2]).solve();
			int mY = (int)((NumberTerm)args[3]).solve();
			
			int newX = agentmap.adjustX(x+mX);
			int newY = agentmap.adjustY(y+mY);
			
			NumberTermImpl realx =  new NumberTermImpl(newX);
			NumberTermImpl realy =  new NumberTermImpl(newY);
			ListTerm coords = createList();
			coords.append(realx);
			coords.append(realy);
	    	return un.unifies(coords, args[4]);
		}
		
}