package br.com.academia.modelo;

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
	
	
}//class Ritmo
