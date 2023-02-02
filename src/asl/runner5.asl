// Agent blank in project ia_submission

/* Initial beliefs and rules */
x(0).
y(0).
pathEntranceX(0).
pathEntranceY(0).
noNegotiation.
onPath(false).
responses(0).
/* Initial goals */

!setup.

/* Plans */

@setup[atomic]
+!setup : true 
				<- rover.ia.check_config(Capacity, Scanrange, Resourcetype);
					rover.ia.get_map_size(Width,Height);
					maze.agentmap(Height, Width);
					maze.getWidth(L);
					+width(Width);
					+scanrange(Scanrange).
					

+!scan : true
				<- .broadcast(tell, scanning(runner5));
					.wait(not motion(_));
					.wait(not scanning(_));
					scan(1);
					?pathEntranceX(PathEntranceX);
					?pathEntranceY(PathEntranceY);
					?x(X);
					?y(Y);
					maze.addScan(X,Y);					
					
					maze.checkPaths(X,Y,NewPaths); //Check if there are any new paths in current location.
					.length(NewPaths, NumberOfNewPaths);
					-+counter(0);
					while(counter(Counter) & Counter<NumberOfNewPaths){
						.nth(Counter, NewPaths, CurrentlyChecking);
						.broadcast(tell, check(CurrentlyChecking));
					
						.wait(responses(4));
						-+responses(0);
						if(clash){
							+clashesAt(Counter);
						}
						
						.abolish(clash);
						.abolish(noClash);
						-+counter(Counter+1);
					}
					.findall(Index, clashesAt(Index), IndexesToDelete);
					.length(IndexesToDelete, NumberOfClashes);

					
					if(NumberOfNewPaths - NumberOfClashes ==0){
						.broadcast(tell, finishedScan);
						!findNewPath;									
					}
					elif(NumberOfNewPaths - NumberOfClashes == 1){ //If there is only one path.
						.nth(0, NewPaths, CurrentPath);
						.nth(0, CurrentPath, Nx);
						.nth(1, CurrentPath, Ny);
						-+currentPath([Nx, Ny]);
						maze.removePath(Nx, Ny);
						.broadcast(tell, finishedScan);
						!pathing;
					}
					else{ //MORE THAN ONE PATH
						-+responses(0);
						.broadcast(tell, pathsFound(NewPaths));
						
						.wait(responses(4));
						
						-responses;
						.findall([TheirMoves, AgentName], participatingAgents([AgentName, TheirMoves]), ListOfParticipatingAgents);
						.length(ListOfParticipatingAgents, NumberOfParticipants);
						if(NumberOfParticipants > 0){
							-+counter(0);
							-+counterForPaths(0);
							.sort(ListOfParticipatingAgents, SortedList);
							while(counter(Counter) & Counter<NumberOfParticipants){
								.nth(Counter, SortedList, CurrentAgent);
								.nth(1, CurrentAgent, CurName); //get agent
								if(Counter<NumberOfNewPaths-NumberOfClashes-1){ //If agent should be allocated path
								
									while(counterForPaths(SecondCounter) & clashesAt(SecondCounter)){
										-+counterForPaths(SecondCounter+1);
									}
									?counterForPaths(SecondCounter);
									.nth(SecondCounter, NewPaths, AllocatedPath);
									.send(CurName, tell, currentPath(AllocatedPath));
									-+counterForPaths(SecondCounter+1);
									-+counter(Counter+1);
								}
								else{ //Agent isn't allocated a path
									.send(CurName, tell, noPathAllocated);
									-+counter(Counter+1);
								}
							} //END OF WHILE LOOP
							
													
							.abolish(busy);
							.abolish(participatingAgents(_));
							.abolish(negotiating(_));
							-+responses(0);
							-negotiationStarted;
							
							while(counterForPaths(SecondCounter) & clashesAt(SecondCounter)){
								-+counterForPaths(SecondCounter+1);
							}
							?counterForPaths(SecondCounter);
							.nth(SecondCounter, NewPaths, MyPath);
							-+currentPath(MyPath);
							.nth(0, MyPath, MyPathX);
							.nth(1, MyPath, MyPathY);
							maze.removePath(MyPathX, MyPathY);
							.abolish(clashesAt(_));
							.broadcast(tell, finishedScan);
							!pathing; //TODO
						}
						//If there are no free agents to take this path.
						else{
							-+counterForPaths(0);
							.abolish(busy);
							.abolish(participatingAgents(_));
							.abolish(negotiating(_));
							-+responses(0);
							-negotiationStarted;
							while(counterForPaths(SecondCounter) & clashesAt(SecondCounter)){
								-+counterForPaths(SecondCounter+1);
							}
							?counterForPaths(SecondCounter);
							.nth(SecondCounter, NewPaths, MyPath);
							-+currentPath(MyPath);
							.nth(0, MyPath, MyPathX);
							.nth(1, MyPath, MyPathY);
							maze.removePath(MyPathX, MyPathY);
							.abolish(clashesAt(_));
							.broadcast(tell, finishedScan);
							!pathing; //TODO
							//add path to list of unoccupied paths
						}
					}
					.

+!pathing : true 
			<-  .wait(not motion(runner2) & not motion(runner1) & not motion(runner3) & not motion(runner4));
				.broadcast(tell, motion(runner5));
				?currentPath(MyPath);
				?x(X);
				?y(Y);
				.nth(0,MyPath, Dx);
				.nth(1,MyPath, Dy);
				maze.removeOccupant(X,Y);
				maze.navigatemap(X,Y,Dx,Dy,Moves);
				
				.length(Moves, NoMoves);
				-+counter(0);
				while(counter(Counter) & Counter<NoMoves){
					?x(Ax);
					?y(Ay);
					.nth(Counter, Moves, ThisMove);
					.nth(0, ThisMove, Mx);
					.nth(1, ThisMove, My);
					move(Mx, My);
					maze.updateLocation(Ax,Ay,Mx,My,Nl);
					.nth(0, Nl, NewX0);
					.nth(1, Nl, NewY0);
					maze.removePath(NewX0, NewY0);
					-+x(NewX0);
					-+y(NewY0);
					-+counter(Counter+1);
				}
				?x(NewX);
				?y(NewY);

				maze.markVisited(NewX,NewY);
				.broadcast(tell, occupied(NewX, NewY));
				.broadcast(tell, finishedPathing);
				!scan;
				.

+finishedPathing[source(Other)] : true
			<-  .abolish(finishedPathing[source(Other)]);
				.abolish(motion(_)[source(Other)]).
	
+finishedScan[source(Other)] : true
		<- .abolish(finishedScan[source(Other)]);
			.abolish(scanning(_)[source(Other)]).	
				
+!advance(Mx,My) : true
			<- move(Mx,My);
				?x(X);
				?y(Y);
				maze.updateLocation(X,Y, Mx, My, NewLocation);
				.nth(0, NewLocation, CurrentX);
				.nth(1, NewLocation, CurrentY);
				maze.checkIfUnscanned(CurrentX, CurrentY, Unscanned);
				if(Unscanned == 1){
					!scan;
				}
				else{
					advance(Mx,My);
				}
				.			

+occupied([X,Y])[source(Other)] : true
				<-  .abolish(occupied([Q,C])[source(Other)] & Q\==X & C\==Y);
					maze.addOccupant(X,Y).

@obstructed[atomic]
+obstructed(X1,Y1,X2,Y2) : true
					<- 
						move(X2,Y2).

-!advance : true
			<- .print("failed advancing").				


+!findNewPath : true
			<- ?x(X);
				?y(Y);
				-+onPath(false);
				maze.findNewPath(X,Y, NewPath);
				.length(NewPath, N);
				if(N > 0){
					-+currentPath(NewPath);
					!pathing;
				}
				.

+!lock : true
			<- .wait({+unlock}).
			
			
@clash[atomic]
+clash[source(Other)] : true
				<- ?responses(R);
					-+responses(R+1);
					.
@noClash[atomic]
+noClash[source(Other)] : true
				<- ?responses(R);	
					-+responses(R+1);
					.

@check[atomic]
+check(Coords)[source(Other)] : true
				<-
					if(onPath(T) & T == true){
						?currentPath(Test);
						.nth(0, Coords, CheckX);
						.nth(1, Coords, CheckY);
						?x(X);
						?y(Y);
						if(Test == Coords | (CheckX == X & CheckY == Y)){
							.send(Other, tell, clash);
							.abolish(check(_));
						}
						else{
							.send(Other, tell, noClash);
							.abolish(check(_));
						}
					}
					else{
						.send(Other, tell, noClash);
						.abolish(check(_));
					}
					.
					
							
+currentPath(MyPath)[source(Other)] : Other \== self & Other \== "runner5"
				<-  
					.nth(0, MyPath, X);
					.nth(1,MyPath, Y);
					maze.removePath(X,Y);
					-+onPath(true);
					
				
					!pathing.
					
+noPathAllocated : True
			<- -noPathAllocated.

+resource_found(T, Qty, X, Y) : T == "Obstacle"
						<-	?x(AgentX);
							?y(AgentY);
							maze.addObstacle(AgentX,AgentY,X,Y);.

@negotiating[atomic]
+negotiating(Offer) : True
		<- ?responses(R);
			-+responses(R+1);
			.nth(0, Offer, AgentName);
			.nth(1, Offer, TheirMoves);
			+participatingAgents([AgentName, TheirMoves]).

@busy[atomic]
+busy[source(Other)] : Other \== "runner5"
		<- ?responses(R);
			-+responses(R+1);
			-busy.


@pathsFound[atomic]
+pathsFound(NewPaths)[source(Name)] : onPath(T) & T=false
						<-  
							.length(NewPaths, N);
							?width(Width);
							?x(X);
							?y(Y);
							-+counter(0);
							.nth(0, NewPaths, Path);
							.nth(0, Path, DestinationX);
							.nth(1, Path, DestinationY);
							maze.navigatemap(X,Y,DestinationX, DestinationY, Moves);
							.length(Moves,NumberOfMoves);
							if(NumberOfMoves < Width/2){
								Offer = ["runner5", NumberOfMoves];
								.send(Name, tell, negotiating(Offer));
							}
							else{
								.send(Name, tell, busy);
							}
							.abolish(pathsFound(_)[source(Name)]);
							.

+pathsFound(NewPaths)[source(Name)] : onPath(T) & T=true
						<- .send(Name, tell, busy);
							.abolish(pathsFound(_)[source(Name)]);
							.