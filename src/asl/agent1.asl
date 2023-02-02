// Agent blank in project ia_submission

/* Initial beliefs and rules */
agents(1).
/* Initial goals */

!setup.

/* Plans */

+!setup : true 
				<- rover.ia.check_config(Capacity, Scanrange, Resourcetype);
					rover.ia.get_distance_from_base(X,Y);
					rover.ia.get_map_size(Width,Height);
					myLib.agentmap(Height, Width);
					.broadcast(tell, count).

+!delegate : true
				<- .broadcast(tell, Gold). 

+!return : true 
				<- rover.ia.get_distance_from_base(X, Y);
					move(X, Y);
					rover.ia.log_movement(X, Y);
					deposit("Gold");
					deposit("Gold");
					deposit("Gold");
					rover.ia.clear_movement_log;
					!move;
					.

+resource_found(T, Qty, X, Y) : true
 								<- .print("found");
 									move(X, Y);
 									rover.ia.log_movement(X, Y);
 									collect("Gold");
 									collect("Gold");
 									collect("Gold");
									!return.

+resource_not_found : true <- !move.

+count: true <- ?agents(N);
				-+agents(N+1);
				?agents(Q);
				.print(Q).