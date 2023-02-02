package maze;


import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class removePath extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int pathX = (int)((NumberTerm)args[0]).solve();
			int pathY = (int)((NumberTerm)args[1]).solve();
			
			agentmap.removePath(pathX, pathY);
			
			return true;
		}
		
}