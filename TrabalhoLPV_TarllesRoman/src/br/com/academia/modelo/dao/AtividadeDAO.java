package br.com.academia.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.Atividade;

public class AtividadeDAO {
	public static void inserir(Atividade atividade, Connection con) throws SQLException{
		String sql = "INSERT INTO public.atividade(id_aluno, data, tempo, atividade,"
					 + " duracao, distancia, calorias, passos) "
					 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setInt(1, atividade.getAluno().getId());
		state.setDate(2, atividade.getData());
		state.setString(3, atividade.getTempo());
		state.setString(4, atividade.getAtividade());
		state.setDouble(5, atividade.getDuracao());
		state.setDouble(6, atividade.getDistancia());
		state.setDouble(7, atividade.getCalorias());
		state.setInt(8, atividade.getPassos());

		state.execute();
		state.close();
	}
	
	/**Recupera todas as atividades desse aluno*/
	public static ArrayList<Atividade> selecionar(Aluno aluno, Connection con) throws SQLException{
		String sql = "SELECT id, data, tempo, atividade, duracao, distancia, calorias, passos"
					 + " FROM public.atividade WHERE id_aluno=?";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setInt(1,aluno.getId());
		
		ArrayList<Atividade> atividades = new ArrayList<>();
		ResultSet result = state.executeQuery();
		while(result.next())
			 atividades.add( new Atividade(result.getInt(1), aluno, result.getDate(2),result.getString(3),
					 		 result.getString(4), result.getDouble(5),result.getDouble(6),
					 		 result.getDouble(7), result.getInt(8)));
		
		state.close();
		return atividades;
	}
}//class AtividadeDAO
