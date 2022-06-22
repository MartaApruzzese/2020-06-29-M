package it.polito.tdp.imdb.model;

public class DirettoriAdiacenti {

	private Director direttore;
	private double peso;
	
	
	public DirettoriAdiacenti(Director direttore, double peso) {
		super();
		this.direttore = direttore;
		this.peso = peso; 
	}
	
	
	public Director getDirettore() {
		return direttore;
	}
	public double getPeso() {
		return peso;
	}
	
	
}
