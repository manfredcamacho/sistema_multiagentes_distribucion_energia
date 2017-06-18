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
!inicializar("planificacion"). //Meta inicial
/* Plans */

+!inicializar(Id)
   <-
   		.concat("sch_",Id,SchName); //Creo el nombre de mi esquema "sch_planificacion"
      makeArtifact(SchName, "ora4mas.nopl.SchemeBoard",["src/org/org.xml", distribucion_sch],SchArtId); //creo el artefacto del esquema y obtengo el id en SchArtId
      debug(inspector_gui(on))[artifact_id(SchArtId)];//activo el inspector del esquema
      setArgumentValue(distribuir_energia,"Id",Id)[artifact_id(SchArtId)];//seteamos el id del esquema en los argumentos del artefacto para usarlo despues
      .my_name(Me); setOwner(Me)[artifact_id(SchArtId)];  //hacemos que el coordinador sea dueño del esquema
      focus(SchArtId); 
      addScheme(SchName);  // hacemos que el grupo sea responsable por el esquema
      commitMission(mCoordinador)[artifact_id(SchArtId)]. //comprometemos al coordinador a cumplir la mision mCoordinador

/* plans for organizational goals */

+!pedir_planificaciones_diarias[scheme(Sch)]
		<-
				?goalArgument(Sch,distribuir_energia,"Id",Id);//Recuperamos el id del esquema en Id
				makeArtifact(Id, "artefactos.CoordinadorArtifact", [], ArtId); // creamos el arteffacto del coordinador coordinadorArtifact
				Sch::focus(ArtId);
				.broadcast(tell, planificar_consumo_semanal);
				//Sch::planificar_consumo_semanal				
				for(.range(D, 0, 6)){
					+pedir_planificacion_diaria(D);	
				}.
			
+pedir_planificacion_diaria(D): dia(D,Dia) <-
			.println("*******************   Pidiendo planificación para el dia ", Dia, "   *******************");
			.broadcast(tell, planificacion_del_dia(D));
			.wait(2000).

+negociar_energia_dia(_,D)
			:
			dia(D,Dia) &
			.findall(e(Consumo, Casa), negociar_energia_dia(Consumo, _)[source(Casa)], ListaConsumoDiarioPorCasa) &
			.length(ListaConsumoDiarioPorCasa,4) //Espero a que lleguen 4 planificaciones
			<-
			procesar(ListaConsumoDiarioPorCasa, Dia, Mensaje, ListaPrestamos);
			.print(Mensaje);
			if(not .substring("No hace falta", Mensaje)){
				.print("Enviando resultados de la negociación a cada casa.");
			}
			for ( .member(X,ListaPrestamos) ) {
        .term2string(T, X);
        .broadcast(tell, T);
     	}.
		
+oblUnfulfilled( obligation(Ag,_,done(Sch,planificar_consumo_semanal,Ag),_ ) )[artifact_id(AId)]
   <- .print("Participante ",Ag," no planifico a tiempo!");
       admCommand("goalSatisfied(planificar_consumo_semanal)")[artifact_id(AId)].
       
+oblUnfulfilled( obligation(Ag,_,done(Sch,pedir_planificaciones_diarias,Ag),_ ) )[artifact_id(AId)]
   <- .print("No envio la planificacion diaria");
       admCommand("goalSatisfied(pedir_planificaciones_diarias)")[artifact_id(AId)].


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") } 
{ include("$jacamoJar/templates/org-obedient.asl") }
