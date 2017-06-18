// CArtAgO artifact code for project distribucion_de_energia_descentralizado

package artefactos;

import cartago.*;

public class CasaArtifact extends Artifact {
	
	private int[] consumoSemanal;
	
	void init(int initialValue) {
		planificarConsumoSemanal();
	}
	
	@OPERATION
	void getConsumoPlanificadoPorDia(int dia,OpFeedbackParam<Integer> consumo){
		planificarConsumoSemanal();
		consumo.set(this.consumoSemanal[dia]);
	}
	
	@OPERATION
	void planificarConsumoSemanal(){
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

