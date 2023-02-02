package maze;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class addScan extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int x = (int)((NumberTerm)args[0]).solve();
			int y = (int)((NumberTerm)args[1]).solve();

		
			agentmap.addScan(x, y);
	    	return true;

		}
		
}