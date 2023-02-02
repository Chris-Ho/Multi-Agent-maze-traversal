package maze;

import jason.asSemantics.*;
import jason.asSyntax.*;
import static jason.asSyntax.ASSyntax.*;

//Gets the absolute coordinates of wherever the agent is currently
@SuppressWarnings("serial")
public class getLocation extends DefaultInternalAction{

		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {
			int intCoords[] = agentmap.getCoords();
			

			NumberTermImpl x =  new NumberTermImpl(intCoords[0]);
			NumberTermImpl y =  new NumberTermImpl(intCoords[1]);
			ListTerm coords = createList();
			coords.append(x);
			coords.append(y);
	    	return un.unifies(coords, args[2]);
		}
		

}