package myLib;

import jason.asSemantics.*;
import jason.asSyntax.*;


//returns RELATIVE coordinates to collector agent (assumed 0,0 for early scenarios) 
@SuppressWarnings("serial")
public class calcResourceXY extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int agentPos = (int)((NumberTerm)args[0]).solve();
			int resourcePos = (int)((NumberTerm)args[1]).solve();
			
			int sum = agentPos + resourcePos;			


			NumberTermImpl result =  new NumberTermImpl(normalise(sum));
	    	return un.unifies(result, args[2]);

		}
		
		public static int normalise(int sum) {
			int width = agentmap.getWidth();

			if(sum > width/2) {
				sum = sum - width;
			}
			
			return sum;
		}
		
}