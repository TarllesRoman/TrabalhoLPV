package br.com.academia.modelo;

import java.sql.Date;

public class Aluno {
	private int id;
	private String nome,sexo,email,cpf,whatsapp;
	private double altura, peso;
	private Date dataNascimento;
	
	public Aluno() {	}

	public Aluno(int id, String nome, String sexo, String email, String cpf, String whatsapp, double altura,
			double peso, Date dataNascimento) {
		this.id = id;
		this.nome = nome;
		this.sexo = sexo;
		this.email = email;
		this.cpf = cpf;
		this.whatsapp = whatsapp;
		this.altura = altura;
		this.peso = peso;
		this.dataNascimento = dataNascimento;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}//class Aluno
