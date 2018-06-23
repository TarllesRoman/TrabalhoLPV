package br.com.academia.modelo;

import java.sql.Date;

public class AtividadeCompleta extends Atividade {
	private int idCompleta;
	private float velocidadeMedia, velocidadeMaxima, ritmoMedio,ritmoMaximo,
					menorElevacao, maiorElevacao;
	
	public AtividadeCompleta() {	}
	
	public AtividadeCompleta(int id, Aluno aluno, Date data, String tempo, String atividade, double duracao,
			double distancia, double calorias, int passos) {
		super(id, aluno, data, tempo, atividade, duracao, distancia, calorias, passos);
	}

	public AtividadeCompleta(int idCompleta, float velocidadeMedia, float velocidadeMaxima, float ritmoMedio,
			float ritmoMaximo, float menorElevacao, float maiorElevacao) {
		this.idCompleta = idCompleta;
		this.velocidadeMedia = velocidadeMedia;
		this.velocidadeMaxima = velocidadeMaxima;
		this.ritmoMedio = ritmoMedio;
		this.ritmoMaximo = ritmoMaximo;
		this.menorElevacao = menorElevacao;
		this.maiorElevacao = maiorElevacao;
	}

	public AtividadeCompleta(int id, Aluno aluno, Date data, String tempo, String atividade, double duracao,
			double distancia, double calorias, int passos, int idCompleta, float velocidadeMedia,
			float velocidadeMaxima, float ritmoMedio, float ritmoMaximo, float menorElevacao, float maiorElevacao) {
		super(id, aluno, data, tempo, atividade, duracao, distancia, calorias, passos);
		this.idCompleta = idCompleta;
		this.velocidadeMedia = velocidadeMedia;
		this.velocidadeMaxima = velocidadeMaxima;
		this.ritmoMedio = ritmoMedio;
		this.ritmoMaximo = ritmoMaximo;
		this.menorElevacao = menorElevacao;
		this.maiorElevacao = maiorElevacao;
	}

	public int getIdCompleta() {
		return idCompleta;
	}

	public void setIdCompleta(int idCompleta) {
		this.idCompleta = idCompleta;
	}

	public float getVelocidadeMedia() {
		return velocidadeMedia;
	}

	public void setVelocidadeMedia(float velocidadeMedia) {
		this.velocidadeMedia = velocidadeMedia;
	}

	public float getVelocidadeMaxima() {
		return velocidadeMaxima;
	}

	public void setVelocidadeMaxima(float velocidadeMaxima) {
		this.velocidadeMaxima = velocidadeMaxima;
	}

	public float getRitmoMedio() {
		return ritmoMedio;
	}

	public void setRitmoMedio(float ritmoMedio) {
		this.ritmoMedio = ritmoMedio;
	}

	public float getRitmoMaximo() {
		return ritmoMaximo;
	}

	public void setRitmoMaximo(float ritmoMaximo) {
		this.ritmoMaximo = ritmoMaximo;
	}

	public float getMenorElevacao() {
		return menorElevacao;
	}

	public void setMenorElevacao(float menorElevacao) {
		this.menorElevacao = menorElevacao;
	}

	public float getMaiorElevacao() {
		return maiorElevacao;
	}

	public void setMaiorElevacao(float maiorElevacao) {
		this.maiorElevacao = maiorElevacao;
	}
	
}//class AtividadeCompleta
