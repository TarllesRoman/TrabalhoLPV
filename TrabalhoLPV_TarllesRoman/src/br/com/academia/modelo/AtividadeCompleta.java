package br.com.academia.modelo;

import java.sql.Date;
import java.util.ArrayList;

public class AtividadeCompleta extends Atividade {
	private int idCompleta;
	private double velocidadeMedia, velocidadeMaxima, ritmoMedio,ritmoMaximo,
					menorElevacao, maiorElevacao;
	
	private ArrayList<Ritmo> ritmos;
	
	public AtividadeCompleta() {
		this.ritmos = new ArrayList<>();
	}
	
	public AtividadeCompleta(int id, Aluno aluno, Date data, String tempo, String atividade, double duracao,
			double distancia, double calorias, int passos) {
		super(id, aluno, data, tempo, atividade, duracao, distancia, calorias, passos);
		this.ritmos = new ArrayList<>();
	}
	
	public AtividadeCompleta(Atividade simple) {
		super(simple.getId(), simple.getAluno(), simple.getData(), simple.getTempo(),
				simple.getAtividade(), simple.getDuracao(), simple.getDistancia(),
				simple.getCalorias(), simple.getPassos());
		
		this.ritmos = new ArrayList<>();
	}

	public AtividadeCompleta(int idCompleta, double velocidadeMedia, double velocidadeMaxima, double ritmoMedio,
			double ritmoMaximo, double menorElevacao, double maiorElevacao) {
		this();
		this.idCompleta = idCompleta;
		this.velocidadeMedia = velocidadeMedia;
		this.velocidadeMaxima = velocidadeMaxima;
		this.ritmoMedio = ritmoMedio;
		this.ritmoMaximo = ritmoMaximo;
		this.menorElevacao = menorElevacao;
		this.maiorElevacao = maiorElevacao;
	}

	public AtividadeCompleta(int id, Aluno aluno, Date data, String tempo, String atividade, double duracao,
			double distancia, double calorias, int passos, int idCompleta, double velocidadeMedia,
			double velocidadeMaxima, double ritmoMedio, double ritmoMaximo, double menorElevacao, double maiorElevacao) {
		super(id, aluno, data, tempo, atividade, duracao, distancia, calorias, passos);
		this.idCompleta = idCompleta;
		this.velocidadeMedia = velocidadeMedia;
		this.velocidadeMaxima = velocidadeMaxima;
		this.ritmoMedio = ritmoMedio;
		this.ritmoMaximo = ritmoMaximo;
		this.menorElevacao = menorElevacao;
		this.maiorElevacao = maiorElevacao;
		this.ritmos = new ArrayList<>();
	}

	public AtividadeCompleta(int idCompleta, double velocidadeMedia, double velocidadeMaxima, double ritmoMedio,
			double ritmoMaximo, double menorElevacao, double maiorElevacao, ArrayList<Ritmo> ritmos) {
		this.idCompleta = idCompleta;
		this.velocidadeMedia = velocidadeMedia;
		this.velocidadeMaxima = velocidadeMaxima;
		this.ritmoMedio = ritmoMedio;
		this.ritmoMaximo = ritmoMaximo;
		this.menorElevacao = menorElevacao;
		this.maiorElevacao = maiorElevacao;
		this.ritmos = ritmos;
	}

	public int getIdCompleta() {
		return idCompleta;
	}

	public void setIdCompleta(int idCompleta) {
		this.idCompleta = idCompleta;
	}

	public double getVelocidadeMedia() {
		return velocidadeMedia;
	}

	public void setVelocidadeMedia(double velocidadeMedia) {
		this.velocidadeMedia = velocidadeMedia;
	}

	public double getVelocidadeMaxima() {
		return velocidadeMaxima;
	}

	public void setVelocidadeMaxima(double velocidadeMaxima) {
		this.velocidadeMaxima = velocidadeMaxima;
	}

	public double getRitmoMedio() {
		return ritmoMedio;
	}

	public void setRitmoMedio(double ritmoMedio) {
		this.ritmoMedio = ritmoMedio;
	}

	public double getRitmoMaximo() {
		return ritmoMaximo;
	}

	public void setRitmoMaximo(double ritmoMaximo) {
		this.ritmoMaximo = ritmoMaximo;
	}

	public double getMenorElevacao() {
		return menorElevacao;
	}

	public void setMenorElevacao(double menorElevacao) {
		this.menorElevacao = menorElevacao;
	}

	public double getMaiorElevacao() {
		return maiorElevacao;
	}

	public void setMaiorElevacao(double maiorElevacao) {
		this.maiorElevacao = maiorElevacao;
	}

	public ArrayList<Ritmo> getRitmos() {
		return ritmos;
	}

	public void setRitmos(ArrayList<Ritmo> ritmos) {
		this.ritmos = ritmos;
	}
	
}//class AtividadeCompleta
