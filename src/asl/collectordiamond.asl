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
					move(-X, -Y);
					-+newCounter(0);
					while(newCounter(Counter) & counter(ResourcesHeld) & Counter < ResourcesHeld){
						deposit("Diamond");
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
					move(X, Y);
					-+counter(0);
					while(counter(Counter) & Counter < Capacity & Counter<Qt){
						collect("Diamond");
						-+counter(Counter+1);
					};
					-+x(X);
					-+y(Y);
					-g([X,Y, Qty]);
					rover.ia.check_status(Energy);
					?fullBattery(Maxenergy);
					if(Energy > 0.15 * Maxenergy){
						!check;
					}
					else{
						!return;
					}
					. //WORKING ON THIS
//-!collect : true
//		<- .send(searcher, tell, requestMove);
//			+obstructed.
//
//+obstructed(I, J, X2, Y2) : true
//							<- .wait({+moved});
//								.findall([X,Y, Qty], g([X,Y, Qty]), Q);
//								?capacity(Capacity); //to implement for loop
//								.nth(0,Q,C);
//								.nth(0,C,X);
//								.nth(1,C,Y);
//								move(X2, Y2);
//								.send(searcher,tell,moved);
//								collect("Diamond");
//								collect("Diamond");
//								collect("Diamond");
//								collect("Diamond");
//								collect("Diamond");
//								-+x(X);
//								-+y(Y);
//								-g([X,Y, Qty]);
//								rover.ia.check_status(Energy);
//								?fullBattery(Maxenergy);
//								if(Energy > 0.15 * Maxenergy){
//									!check;
//								}.


+!check : true 
				<- scan(1);
					.wait(scanComplete);
					-scanComplete;
					!return.

//can't be atomic otherwise self-scan won't work.
+resource_found(T, Qty, X, Y)[source(searcher)] : true
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

+resource_found(T, Qty, X, Y)[source(searcher)] : true
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
 									
+resource_found(T, Qty, X, Y)[source(percept)] : X == 0 & Y==0 & T == "Diamond"
 								<- ?x(Ax);
 									?y(Ay);
 									+g([Ax,Ay, Qty]);
 									+scanComplete.
 									
+resource_found(T, Qty, X, Y)[source(percept)] : true
 								<- +scanComplete.	

+resource_not_found[source(percept)] : true
							<- +scanComplete.
						

