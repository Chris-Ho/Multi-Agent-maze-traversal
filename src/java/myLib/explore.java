package myLib;

import static jason.asSyntax.ASSyntax.createList;

import jason.asSemantics.*;
import jason.asSyntax.*;

@SuppressWarnings("serial")
public class explore extends DefaultInternalAction {
		
		public synchronized Object execute(TransitionSystem ts,
								Unifier un,
								Term[] args) throws Exception {

			int[] t = agentmap.getFrontier();
			if(t.length ==1) {
				t = agentmap.backtrack();
			}
			if(t.length ==1) {
				return false;
			}
			System.out.println("Goint to: " + t[0] + " " + t[1]);

			NumberTermImpl x =  new NumberTermImpl(t[0]);
			NumberTermImpl y =  new NumberTermImpl(t[1]);
			ListTerm coords = createList();
			coords.append(x);
			coords.append(y);
			return un.unifies(coords, args[0]);

		}
		
}