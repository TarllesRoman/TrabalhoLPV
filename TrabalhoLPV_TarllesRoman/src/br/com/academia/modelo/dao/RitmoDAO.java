package br.com.academia.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.academia.modelo.AtividadeCompleta;
import br.com.academia.modelo.Ritmo;

public class RitmoDAO {
	/**Insere todos os ritmos da atividade no BD*/
	public static void inserir(Connection con,AtividadeCompleta atividadeCompleta) throws SQLException {
		String sql = "INSERT INTO public.ritmo(id_atividade, km, tempo) VALUES (?, ?, ?)";


		for(Ritmo ritmo : atividadeCompleta.getRitmos()) {
			PreparedStatement state = con.prepareStatement(sql);

			state.setInt(1, atividadeCompleta.getId());
			state.setDouble(2, ritmo.getKm());
			state.setDouble(3, ritmo.getTempo());

			state.execute();
			state.close();
		}
	}
	
	/**Busca pelos ritmos que possuirem o id da atividade e os que forem encontrados
	 * são colocados no array de Ritmo da atividade recebida, os ritmos que já estiverem lá
	 * serão perdidos, caso nenhum ritmo seja encontrado o array ficará vazio*/
	public static void selecionar(AtividadeCompleta atividadeCompleta, Connection con) throws SQLException {
		String sql = "SELECT id, km, tempo FROM public.ritmo WHERE id_atividade=?";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setInt(1,atividadeCompleta.getId());
		
		ArrayList<Ritmo> ritmos = new ArrayList<>();
		ResultSet result = state.executeQuery();
		while(result.next())
			 ritmos.add(new Ritmo(result.getInt(1),result.getDouble(2) , result.getDouble(3)));
		
		atividadeCompleta.setRitmos(ritmos);
		state.close();
	}
	
}//class UsuarioDAO
