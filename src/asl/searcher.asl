// Agent blank in project ia_submission

/* Initial beliefs and rules */

/* Initial goals */

!setup.

/* Plans */


+!setup : true 
				<- rover.ia.check_config(Capacity, Scanrange, Resourcetype);
					rover.ia.get_map_size(Width,Height);
					.print(Width, Height);
					myLib.agentmap(Height, Width);
					myLib.getWidth(L);
					+scanrange(Scanrange);
					myLib.getSections(Scanrange, M);
					+max(M);
					+level(0);
					+x(0);
					+y(0);
					scan(Scanrange);
					!loop.
@loop[priority(1)]
+!loop : true
		<-  !move;
			!loop.
			
@move[priority(1), atomic]
+!move: (max(M) & level(N) & M==N) //WORKING ON THIS
					<- .wait(1000);
						move(-7,6);
						-+level(0)
						myLib.updatelocation(-7,6);
						
						?x(Ax);
						?y(Ay);
						-+x(Ax - 7);
						-+y(Ay + 6);
						
						scan(6). 

+!move : (max(M) & level(N) & M\==N)   
				<-  .wait(1000);
					?scanrange(Scanrange);
					move((Scanrange * 2) + 2,0);
					?level(N);
					-+level(N+1);
					myLib.updatelocation((Scanrange * 2) + 2, 0);
					
					
					?x(Ax);
					?y(Ay);
					-+x(Ax + (Scanrange * 2) + 2);
					-+y(Ay + 0);
					?x(Nx);
					?y(Ny);
					
					scan(6).
					
//+obstructed(X,Y,X2,Y2) : true
//						<- move(-1,1)
//							.send(collector, tell, moved)
//							.wait({+moved})
//							move(1,-1);
//							move(X2,Y2);
//							?level(N);
//							-+level(N+1);
//							myLib.updatelocation(X2, Y2);
//							scan(6).
//
//+requestMove[source(collector)] : true
//								<- move(1,-1);
//									.send(collector, tell, moved)
//									.wait({+moved})
//									move(-1,1).

@resource_found[priority(5), atomic]
+resource_found(T, Qty, X, Y) : T == "Gold"
 								<- myLib.getLocation(L);
 									.nth(0,L[A,B],Agentx);
 									.nth(1,L[C,D],Agenty);
 									myLib.calcResourceXY(Agentx, X, ResourceX);
 									myLib.calcResourceY(Agenty, Y, ResourceY);
 									.send(collector, tell, resource_found(T, Qty, ResourceX, ResourceY)).

+resource_found(T, Qty, X, Y) : T == "Diamond"
 								<- myLib.getLocation(L);
 									.nth(0,L[A,B],Agentx);
 									.nth(1,L[C,D],Agenty);
 									myLib.calcResourceXY(Agentx, X, ResourceX);
 									myLib.calcResourceY(Agenty, Y, ResourceY);
 									.send(collectordiamond, tell, resource_found(T, Qty, ResourceX, ResourceY)).


+resource_found(T, Qty, X, Y) : T == "Obstacle"
 								<- myLib.getLocation(L);
 									.nth(0,L[A,B],Agentx);
 									.nth(1,L[C,D],Agenty);
 									myLib.calcResourceXY(Agentx, X, ResourceX);
 									myLib.calcResourceY(Agenty, Y, ResourceY);
 									myLib.addObstacle(Agentx, Agenty, X, Y).

//+resource_not_found : true <- !move.
