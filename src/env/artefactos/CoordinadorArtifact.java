// CArtAgO artifact code for project distribucion_de_energia_descentralizado

package artefactos;


import java.util.*;
import cartago.*;

public class CoordinadorArtifact extends Artifact {
		
	private ArrayList<String> prestamos;
	private String dia;
	void init() {
		
	}
	
	@OPERATION
	void procesar(Object[] lista, String dia, OpFeedbackParam<String> mensaje, OpFeedbackParam<String []> listaPrestamos){
		ArrayList<Casa> listaMapeada = mapearLista(lista);
		Collections.sort(listaMapeada, new Comparator<Casa>(){
			public int compare(Casa c1, Casa c2) {
				return c1.compareTo(c2);
			}
		});
		prestamos = new ArrayList<String>(); //se crea una lista vacia para cada dia
		this.dia = dia.toString();
		mensaje.set(repartirEnergia(listaMapeada)); //La lista se llena
		listaPrestamos.set(prestamos.toArray(new String[0]));
	}
	
	private ArrayList<Casa> mapearLista(Object [] lista){
		ArrayList<Casa> result = new ArrayList<Casa>();
		String[] textParts;
		for (Object item : lista) {
			textParts = item.toString().split("\\(|\\)|,");
			result.add(new Casa(textParts[2], Integer.parseInt(textParts[1])));
		}
		return result;
	}
	
	
	private String repartirEnergia(ArrayList<Casa> casas){
		String respuesta = new String("");
		
		int totalDeCasas = casas.size();
		int cantidadCasasConDeficitEnergia = 0;
		@SuppressWarnings("unused")
		int cantidadCasasConExedenteEnergia = 0;
		int energiaNetaTotal = 0;
		
		//La lista de casas esta ordenada descendentemente, por lo tanto
		//la primer casa tiene mas energia que la ultima.
		int indiceCasa1 = 0;
		int indiceCasa2 = totalDeCasas - 1;
		
		
		for (int i = 0; i < totalDeCasas; i++) {
			
			if(casas.get(i).getConsumo() > 0){
				cantidadCasasConExedenteEnergia++;
			}
			else {
				if(casas.get(i).getConsumo() < 0){
					cantidadCasasConDeficitEnergia++;
				}
			}

			energiaNetaTotal += casas.get(i).getConsumo();
		}
		
		if (cantidadCasasConDeficitEnergia == 0) {
			respuesta = new String("No hace falta negociar energia para el dia " + this.dia);
		}
		else {//Hay por lo menos una casa con deficit de energia
			if (energiaNetaTotal == 0) {//Se puede cubrir el deficit
				respuesta = new String("Es posible cubrir las necesidades de todas las casas el dia " + this.dia);
			}
			else if (energiaNetaTotal > 0) {//Se puede cubrir el deficit
				respuesta = new String("Es posible cubrir las necesidades de todas las casas el dia " + this.dia + " y además tengo " + energiaNetaTotal + " KW de sobra para otra division del barrio.");
			}
			else {//Hay por lo menos una casa que puede prestar energia
				respuesta = new String("No es posible satisfacer  las necesidades de todas las casas el dia " + this.dia + ", hace falta " + Math.abs(energiaNetaTotal) + " KW.");
			}
		}
		
		evaluar(indiceCasa1, indiceCasa2, casas);
		
		return respuesta;
	}
	
	private void evaluar(int indiceCasa1, int indiceCasa2, ArrayList<Casa> casas){
		int energiaFinal;
		int energiaCasa1 = casas.get(indiceCasa1).getConsumo();
		int energiaCasa2 = casas.get(indiceCasa2).getConsumo();
		int energiaPrestada = 0;
		while (energiaCasa1 > 0 && energiaCasa2 < 0 && indiceCasa1 != indiceCasa2 ) { //casa1 puede prestar a casa2
			energiaFinal =  energiaCasa1 + energiaCasa2 ;
			if( energiaFinal < energiaCasa1){ //la casa1 presto energia a la casa2
				
				if (energiaFinal == 0) {//Casa1 presto toda su energia extra y la casa 2 cubrio el dia por completo
					energiaPrestada = energiaCasa1;
					prestamos.add("prestamo("+casas.get(indiceCasa1).getNombre()+","+casas.get(indiceCasa2).getNombre()+","+energiaPrestada+",\""+ this.dia +"\", 0)");
					indiceCasa1++;
					indiceCasa2--;
					energiaCasa1 = casas.get(indiceCasa1).getConsumo();
					energiaCasa2 = casas.get(indiceCasa2).getConsumo();
				}
				else if(energiaFinal > 0){ //la casa2 cubrio el dia por completo y a la casa1 le sigue sobrando energia
					energiaPrestada = Math.abs(energiaCasa2);
					prestamos.add("prestamo("+casas.get(indiceCasa1).getNombre()+","+casas.get(indiceCasa2).getNombre()+","+energiaPrestada+",\""+ this.dia +"\", 0)");
					energiaCasa1 += energiaCasa2;
					indiceCasa2--;
					energiaCasa2 = casas.get(indiceCasa2).getConsumo();
				}
				else { //La casa1 se quedo sin energia extra y la casa2 sigue con deficit
					energiaPrestada = energiaCasa1;
					prestamos.add("prestamo("+casas.get(indiceCasa1).getNombre()+","+casas.get(indiceCasa2).getNombre()+","+energiaPrestada+",\""+ this.dia +"\", "+(energiaCasa2 + energiaPrestada)+")");
					indiceCasa1++;
					energiaCasa1 = casas.get(indiceCasa1).getConsumo();
					energiaCasa2 += energiaCasa1;
				}
				
				
			}
			else{
				//No fue necesario el prestamo
			}
			
		}
		
		//Muestro los prestamos que se planificaron en el dia
//		for (String item : prestamos) {
//			System.out.println(item);
//		}
	}
}

class Casa implements Comparable<Casa>{
	private String nombre;
	private int consumo;
	
	public Casa(String nombre, int consumo){
		this.nombre = nombre;
		this.consumo = consumo;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getConsumo() {
		return consumo;
	}
	public void setConsumo(int consumo) {
		this.consumo = consumo;
	}
	
	public int compareTo(Casa casa) {

		int consumo = ((Casa) casa).getConsumo();
		//ascending order
		//return this.consumo - consumo;

		//descending order
		return consumo - this.consumo;

	}
	
}








