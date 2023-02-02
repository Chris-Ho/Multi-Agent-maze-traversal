package maze;


import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class checkIfUnscanned extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int x = (int)((NumberTerm)args[0]).solve();
			int y = (int)((NumberTerm)args[1]).solve();
			x= agentmap.adjustX(x);
			y= agentmap.adjustX(y);

			if(agentmap.checkIfUnscanned(x, y)) {
				NumberTermImpl result =  new NumberTermImpl(1);
				return un.unifies(result, args[2]);
			}
			else {
				NumberTermImpl result =  new NumberTermImpl(0);
				return un.unifies(result, args[2]);
			}
		}
		
}