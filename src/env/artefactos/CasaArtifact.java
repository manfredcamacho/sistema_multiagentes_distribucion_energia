// CArtAgO artifact code for project distribucion_de_energia_descentralizado

package artefactos;

import cartago.*;

public class CasaArtifact extends Artifact {
	
	private int[] consumoSemanal;
	
	void init(int initialValue) {
		planificarConsumoSemanal();
		//defineObsProperty("running", true);
	}
	
	@OPERATION
	void getConsumoPlanificadoPorDia(int dia,OpFeedbackParam<Integer> consumo){
		consumo.set(this.consumoSemanal[dia]);
	}
	
	@OPERATION
	void planificarConsumoSemanal(){
		//La idea es que dependiendo del valor de casa lea el archivo 
		//correspondiente a esa casa y planifique el consumo
		
		//Valores precalculados para la 4ta semana en base al consumo 
		//de las 3 semanas anteriores.
		//Para mas detalles revisar excel adjunto
		String casa = this.getCurrentOpAgentId().getAgentName(); 
				
		if (casa.equals("casa_1"))
			this.consumoSemanal = new int[] {-1,  0,  0,  1,  3,  2, -3};
		if (casa.equals("casa_2"))
			this.consumoSemanal = new int[] { 0,  3, -1,  4,  3, -2,  4};
		if (casa.equals("casa_3"))
			this.consumoSemanal = new int[] { 0,  2,  2,  0, -1,  0,  0};
		if (casa.equals("casa_4"))
			this.consumoSemanal = new int[] { 1,  5,  6,  6,  1,  0, -2};
	}
}

