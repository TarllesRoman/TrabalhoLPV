package br.com.academia.modelo;

import br.com.academia.utils.HourHandle;

public class Ritmo {
	private int id;
	private double km,
				   tempo;
	
	public Ritmo() {	}

	public Ritmo(int id, double km, double tempo) {
		super();
		this.id = id;
		this.km = km;
		this.tempo = tempo;
	}
	
	public Ritmo(double km, double tempo) {
		this.km = km;
		this.tempo = tempo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getKm() {
		return km;
	}

	public void setKm(double km) {
		this.km = km;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	@Override
	public String toString() {
		return String.format("%.2f Km: %s", km, HourHandle.doubleMinutesToRitmo(tempo));
	}
	
	
	
	
}//class Ritmo
