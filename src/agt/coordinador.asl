// Agent coordinador in project distribucion_de_energia_descentralizado

/* Initial beliefs and rules */
	dia(0, "Lunes").
	dia(1, "Martes").
	dia(2, "Miercoles").
	dia(3, "Jueves").
	dia(4, "Viernes").
	dia(5, "Sabado").
	dia(6, "Domingo").
/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Comenzando la planificación...");
									.broadcast(tell, planificar_consumo_semanal);
									.wait(500);
									for(.range(D, 0, 6)){
										+pedir_planificacion_diaria(D);										
									}.
+pedir_planificacion_diaria(D): dia(D,Dia) <-
			.println("Pidiendo planificación para el dia ", Dia);
			.broadcast(tell, planificacion_del_dia(D));
			.wait(2000).
			
+negociar_energia_dia(_)
			:
			.findall(e(Consumo, Casa), negociar_energia_dia(Consumo)[source(Casa)], ListaConsumoDiarioPorCasa) &
			.length(ListaConsumoDiarioPorCasa,4) //Espero a que lleguen 4 planificaciones
			<-
			procesar(ListaConsumoDiarioPorCasa, Mensaje, ListaPrestamos);
			.print(Mensaje);
			for ( .member(X,ListaPrestamos) ) {
        .print(X);
     }.
			

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have a agent that always complies with its organization  
//{ include("$jacamoJar/templates/org-obedient.asl") }
