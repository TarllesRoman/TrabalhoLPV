package br.com.academia.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.academia.modelo.Usuario;

public class UsuarioDAO {
	public static void inserir(Usuario usuario, Connection con) throws SQLException {
		String sql = "INSERT INTO public.usuario(usuario, senha, papel) VALUES (?, ?, ?)";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setString(1, usuario.getUsuario());
		state.setString(2, usuario.getSenha());
		state.setString(3, usuario.getPapel());

		state.execute();
		state.close();
	}
	
	/**Retorna o usuario que possui esse id ou null*/
	public static Usuario selecionar(int id, Connection con) throws SQLException {
		String sql = "SELECT id, usuario, senha, papel FROM public.usuario WHERE id=?";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setInt(1,id);
		
		Usuario usuario = null;
		ResultSet result = state.executeQuery();
		if(result.next())
			 usuario = new Usuario(result.getInt(1), result.getString(2), result.getString(3),
					 				result.getString(4));
		
		
		state.close();
		return usuario;
	}
	
	/**Retorna o usuario que possui essa combinação, de usuario e senha, ou null*/
	public static Usuario logar(String usuario,String senha, Connection con) throws SQLException {
		String sql = "SELECT id, usuario, senha, papel FROM public.usuario WHERE usuario=? AND senha=?";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setString(1,usuario);
		state.setString(2, senha);
		
		Usuario user = null;
		ResultSet result = state.executeQuery();
		if(result.next())
			 user = new Usuario(result.getInt(1), result.getString(2), result.getString(3),
					 				result.getString(4));
		
		
		state.close();
		return user;
	}

}//class UsuarioDAO
