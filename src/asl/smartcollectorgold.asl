// Agent blank in project ia_submission

/* Initial beliefs and rules */
agents(1).
/* Initial goals */
x(0).
y(0).

!setup.

/* Plans */

+!setup : true 
				<- rover.ia.check_config(Capacity, Scanrange, Resourcetype);
					rover.ia.check_status(E);
					+fullBattery(E);
					rover.ia.get_distance_from_base(X,Y);
					rover.ia.get_map_size(Width,Height);
					myLib.agentmap(Height, Width);
					+capacity(Capacity);
					+width(Width);
					+height(Height);
					!loop.

+!loop : true
			<- .findall([X,Y, Qty], g([X,Y, Qty]), Q);
				.length(Q, N);
				if(N <1){
					.wait(resource);
					-resource;
				}
				!collect;
				.

+!return : true 
				<- ?x(X);
					?y(Y);
					?capacity(Capacity);
					
					.findall([Rx,Ry, C], moved([Rx,Ry, C]), ReturnMoves);
					.length(ReturnMoves, NoMoves);
					-+newCounter(0);
					while(newCounter(Counter0) & Counter0<NoMoves){
						.nth(Counter0, ReturnMoves, CurrentMove);
						.nth(0, CurrentMove, CurrentRx);
						.nth(1, CurrentMove, CurrentRy);
						move(CurrentRx, CurrentRy);
						-+newCounter(Counter0+1);
					}
					
					
					
					-+newCounter(0);
					while(newCounter(Counter) & counter(ResourcesHeld) & Counter < ResourcesHeld){
						deposit("Gold");
						-+newCounter(Counter+1);
					};
					if(g(X,Y)){
						-+x(0);
						-+y(0);
						!collect;
					}
					else{
						-+x(0);
						-+y(0);
						!loop;
					}.
			
+!collect : true <- .findall([X,Y, Qty], g([X,Y, Qty]), Q);
					?capacity(Capacity); //to implement for loop
					.nth(0,Q,C);
					.nth(0,C,X);
					.nth(1,C,Y);
					.nth(2,C,Qt);
					
					?x(AgentX);
					?y(AgentY);
					myLib.navigateresource(X,Y,AgentX,AgentY,MovementsQueue);
					-+count(0);
					.length(MovementsQueue, N)
					while(count(Counter) & Counter<N){
						.nth(Counter, MovementsQueue, Move);
						.nth(0, Move, Mx);
						.nth(1, Move, My);	
						move(Mx, My);
						+moved([-Mx, -My, Counter]);
						-+count(Counter+1);
						?x(Cx);
						?y(Cy);
						-+x(Cx + Mx);
						-+y(Cy + My);
					};
					
					//SECTION TO ADJUST COORDINATES
					?x(Ax);
					?y(Ay);
					?width(Width);
					?height(Height);
					-+x(Ax + X);
					-+y(Ay + Y);
							
					?x(CurrentAgentX);
					?y(CurrentAgentY);
					if(CurrentAgentX <0){
						-+x(CurrentAgentX + Width);
					}
					if(CurrentAgentX > Width){
						-+x(CurrentAgentX - Width);
					}
					if(CurrentAgentY < 0){
						-+y(CurrentAgentY + Height);
					}
					if(CurrentAgentY > Height){
						-+y(CurrentAgentY - Height);
					}
					//END OF ADJUSTING COORDINATES
					
					?x(AdjustedX);
					?y(AdjustedY);
					
					-+counter(0);
					while(counter(Counter) & Counter < Capacity & Counter<Qt){
						collect("Gold");
						-+counter(Counter+1);
					};
					-g([X,Y, Qty]);
					!check;
					. 
-!collect : true
		<- .print("fail collect").

+obstructed(X,Y,X2,Y2) : true
						<- //update agent's absolute location before anything else
						
							//SECTION TO ADJUST COORDINATES
							?x(Ax);
							?y(Ay);
							?width(Width);
							?height(Height);
							-+x(Ax + X);
							-+y(Ay + Y);
							
							?x(CurrentAgentX);
							?x(CurrentAgentY);
							if(CurrentAgentX <0){
								-+x(CurrentAgentX + Width);
							}
							if(CurrentAgentX > Width){
								-+x(CurrentAgentX - Width);
							}
							if(CurrentAgentY < 0){
								-+y(CurrentAgentY + Height);
							}
							if(CurrentAgentY > Height){
								-+y(CurrentAgentY - Height);
							}
							//END OF ADJUSTING COORDINATES
						
							
							//Perform scan to perceive surroundings
							scan(1);
							
							?x(Agenyx);
							?y(Agenty);

							myLib.navigateresource(X2,Y2,Agentx, Agenty,MovementsQueue);
							
							.print(MovementsQueue);
							//Attempt to move following new path
							-+count(0);
							.length(MovementsQueue, N)
							while(count(Counter) & Counter<N){
								.print("attempting");
								.nth(Counter, MovementsQueue, Move);
								.nth(0, Move, Mx);
								.nth(1, Move, My);
								
								move(Mx, My);
								+moved([Mx, My, Counter]);
								?x(Cx);
								?y(Cy);
								-+x(Cx + Mx);
								-+y(Cy + My);
								-+count(Counter+1);
							}.


+!check : true 
				<- scan(1);
					.wait(scanComplete);
					-scanComplete;
					!return.

//can't be atomic otherwise self-scan won't work.
+resource_found(T, Qty, X, Y)[source(navigator)] : true
 								<-  .findall([X,Y,Qt], g([X,Y,Qt]), Q);
 									+counter(0);
 									.length(Q, N);
 									while(counter(C) & C < N){
 										.nth(C,Q,Coords);
 										if (Coords == [X,Y]){
 											+existingCoords;
 										}
 										-+counter(C+1);
 									}
 									
 									if(not existingcoords){
 										+g([X, Y, Qty]);
 										+resource;
 									}
 									.

+resource_found(T, Qty, X, Y)[source(navigator)] : true
 								<-  .findall([X,Y,Qt], g([X,Y,Qt]), Q);
 									+counter(0);
 									.length(Q, N);
 									while(counter(C) & C < N){
 										.nth(C,Q,Coords);
 										if (Coords == [X,Y]){
 											+existingCoords;
 										}
 										-+counter(C+1);
 									}
 									
 									if(not existingcoords){
 										+g([X, Y, Qty]);
 										+resource;
 									}
 									.
 									
+resource_found(T, Qty, X, Y)[source(percept)] : X == 0 & Y==0 & T == "Gold"
 								<- 	?x(Ax);
 									?y(Ay);
 									+g([Ax,Ay, Qty]);
 									+scanComplete.
 									
+resource_found(T, Qty, X, Y)[source(percept)] : true
 								<- +scanComplete.	

+resource_not_found[source(percept)] : true
							<- +scanComplete.
						

