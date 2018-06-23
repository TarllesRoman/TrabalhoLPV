package br.com.academia.modelo;

public class Usuario {
	private int id;
	private String usuario,senha,papel;
	
	public Usuario() {	}

	public Usuario(int id, String usuario, String senha, String papel) {
		this.id = id;
		this.usuario = usuario;
		this.senha = senha;
		this.papel = papel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}
	
	
}//class Usuario
