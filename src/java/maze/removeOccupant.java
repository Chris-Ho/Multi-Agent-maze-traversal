package maze;

import java.util.ArrayList;
import static jason.asSyntax.ASSyntax.createList;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class removeOccupant extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int x = (int)((NumberTerm)args[0]).solve();
			int y = (int)((NumberTerm)args[1]).solve();

			agentmap.removeOccupant(x,y);	
			
	    	return true;
		}
		
}