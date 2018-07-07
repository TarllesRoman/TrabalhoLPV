package br.com.academia.modelo;

import java.sql.Date;

import br.com.academia.Main;
import br.com.academia.utils.HourHandle;

public class Atividade {
	private int id;
	private Aluno aluno;
	private Date data;
	private String tempo, atividade;
	private double duracao,distancia,calorias;
	private int passos;
	
	public Atividade() {	}

	public Atividade(int id, Aluno aluno, Date data, String tempo, String atividade, double duracao, double distancia,
			double calorias, int passos) {
		this.id = id;
		this.aluno = aluno;
		this.data = data;
		this.tempo = tempo;
		this.atividade = atividade;
		this.duracao = duracao;
		this.distancia = distancia;
		this.calorias = calorias;
		this.passos = passos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public double getDuracao() {
		return duracao;
	}

	public void setDuracao(double duracao) {
		this.duracao = duracao;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public double getCalorias() {
		return calorias;
	}

	public void setCalorias(double calorias) {
		this.calorias = calorias;
	}

	public int getPassos() {
		return passos;
	}

	public void setPassos(int passos) {
		this.passos = passos;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("------ Detalhes do exercício ------");
		sb.append("\nExercício: "+atividade);
		sb.append("\nData: " + Main.sdf.format(data));
		sb.append("\nTempo: "+tempo);
		sb.append("\nDuração: "+HourHandle.doubleToMinutes(duracao));
		sb.append("\nDistância: "+String.format("%.2f Km", distancia));
		sb.append("\nCalorias perdidas: "+String.format("%.2f Kcal", calorias));
		sb.append("\nPassos: "+passos);
		
		return sb.toString();
	}
	
	
}//class Atividade
