package br.com.academia.modelo;

public class Ritmo {
	private int id;
	private int km;
	private float tempo;
	
	public Ritmo() {	}

	public Ritmo(int id, int km, float tempo) {
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

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public float getTempo() {
		return tempo;
	}

	public void setTempo(float tempo) {
		this.tempo = tempo;
	}
	
	
}//class Ritmo
