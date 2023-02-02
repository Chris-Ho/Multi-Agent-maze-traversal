package myLib;

import jason.asSemantics.*;
import jason.asSyntax.*;
import static jason.asSyntax.ASSyntax.*;


//Converts some relative coordinates to some absolute coordinates and returns them
@SuppressWarnings("serial")
public class convertToAbsLocation extends DefaultInternalAction{

		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {
			
			int intCoords[] = agentmap.getCoords();
			
			int relX = (int)((NumberTerm)args[0]).solve();
			int relY = (int)((NumberTerm)args[1]).solve();
			
			
			NumberTermImpl absx =  new NumberTermImpl(agentmap.adjustX(intCoords[0] + relX));
			NumberTermImpl absy =  new NumberTermImpl(agentmap.adjustY(intCoords[1] + relY));
			
			ListTerm coords = createList();
			coords.append(absx);
			coords.append(absy);
	    	return un.unifies(coords, args[2]);
		}
		

}