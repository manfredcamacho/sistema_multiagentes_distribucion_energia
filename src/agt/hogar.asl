// Agent hogar in project distribucion_de_energia_descentralizado

/* Initial beliefs and rules */
	dia(0, "Lunes").
	dia(1, "Martes").
	dia(2, "Miercoles").
	dia(3, "Jueves").
	dia(4, "Viernes").
	dia(5, "Sabado").
	dia(6, "Domingo").
/* Initial goals */

/* Plans */

+planificar_consumo_semanal <- 
		planificarConsumoSemanal;
		.print("Mi planificación semanal esta lista.").
		
		
+planificacion_del_dia(D) <-
		getConsumoPlanificadoPorDia(D, Consumo);
		+enviar_planificacion_del_dia(D, Consumo).
		
		
+enviar_planificacion_del_dia(D, Consumo): dia(D, Dia) <-
		if(Consumo > 0){
			.print("Envié mi planificacion para el dia ", Dia, ", me sobran ", Consumo, " KW.");
		}
		else{ 
			if(Consumo < 0){
				.print("Envié mi planificacion para el dia ", Dia, ", me faltan ", math.abs(Consumo), " KW.");
			}
			else{
				.print("Envié mi planificacion para el dia ", Dia, ", cubrí mi consumo.");
			}
		}
		.send(coordinador, tell, negociar_energia_dia(Consumo));
		.wait(500);
		//Con esto evito el error de perdida de mensajes
		.send(coordinador, untell, negociar_energia_dia(Consumo)).
		

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have a agent that always complies with its organization  
//{ include("$jacamoJar/templates/org-obedient.asl") }
