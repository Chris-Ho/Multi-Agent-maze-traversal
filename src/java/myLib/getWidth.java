package myLib;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class getWidth extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {
			
			double width = agentmap.getWidth();
			NumberTermImpl w =  new NumberTermImpl(width);
	    	return un.unifies(w, args[0]);

		}
		
		
}