// Agent blank in project ia_submission

/* Initial beliefs and rules */

/* Initial goals */

!setup.

/* Plans */


+!setup : true 
				<- rover.ia.check_config(Capacity, Scanrange, Resourcetype);
					rover.ia.get_map_size(Width,Height);
					myLib.agentmap(Height, Width);
					myLib.getWidth(L);
					+scanrange(Scanrange);
					+max(M);
					+level(0);
					+x(0);
					+y(0);
					scan(4).
//					!move.

			
@move[priority(1)]

+!move : true  
				<-  .wait(10);
					?scanrange(Scanrange);
					myLib.getLocation(Currentcoords);
					.nth(0, Currentcoords, X);
					.nth(1, Currentcoords, Y);
					.print(X);
					.print(Y);
					scan(Scanrange);
					myLib.addscan(X,Y, Scanrange);
					myLib.explore(Destination);
					.nth(0, Destination, Dx);
					.nth(1, Destination, Dy);

				
					myLib.navigatemap(X,Y,Dx,Dy,MovementsQueue);
					-+count(0);
					.length(MovementsQueue, N)
					while(count(Counter) & Counter<N){
						.nth(Counter, MovementsQueue, Move);
						.nth(0, Move, Mx);
						.nth(1, Move, My);	
						move(Mx, My);
						-+count(Counter+1);
						myLib.updatelocation(Mx,My);
						myLib.getLocation(NewCoords);
						.nth(0, NewCoords, NewX);
						.nth(1, NewCoords, NewY);
						myLib.markVisited(NewX,NewY);
					};
					!move.

-!move : true
	<- !move.
					
+obstructed(UX,UY,UX2,UY2) : true
						<- !move.
							


@resource_found[priority(5), atomic]
+resource_found(T, Qty, X, Y) : T == "Obstacle"
 								<- myLib.getLocation(L);
 									.nth(0,L[A,B],Agentx);
 									.nth(1,L[C,D],Agenty);
 									myLib.addObstacle(Agentx, Agenty, X, Y).
 									
+resource_found(T, Qty, X, Y) : T == "Gold"
 								<- myLib.getLocation(L);
 									.nth(0,L[A,B],Agentx);
 									.nth(1,L[C,D],Agenty);
 									myLib.calcResourceXY(Agentx, X, ResourceX);
 									myLib.calcResourceY(Agenty, Y, ResourceY);
 									.send(smartcollectorgold, tell, resource_found(T, Qty, ResourceX, ResourceY)).

+resource_found(T, Qty, X, Y) : T == "Diamond"
 								<- myLib.getLocation(L);
 									.nth(0,L[A,B],Agentx);
 									.nth(1,L[C,D],Agenty);
 									myLib.calcResourceXY(Agentx, X, ResourceX);
 									myLib.calcResourceY(Agenty, Y, ResourceY);
 									.send(smartcollectordiamond, tell, resource_found(T, Qty, ResourceX, ResourceY)).


//+resource_not_found : true <- !move.
