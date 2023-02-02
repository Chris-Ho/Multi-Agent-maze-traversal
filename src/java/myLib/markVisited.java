package myLib;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class markVisited extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int x = (int)((NumberTerm)args[0]).solve();
			int y = (int)((NumberTerm)args[1]).solve();
			System.out.println(x + " " + y);
		
			agentmap.markVisited(x, y);
	    	return true;

		}
		
}