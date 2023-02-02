package myLib;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class getSections extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {
			
			int width = agentmap.getWidth();
			int scanrange = (int)((NumberTerm)args[0]).solve();
			int result = (width / (2*scanrange + 1));
			NumberTermImpl w =  new NumberTermImpl(result);
	    	return un.unifies(w, args[1]);

		}
		
		
}