package br.com.academia.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.academia.modelo.Aluno;

public class AlunoDAO {
	public static void inserir(Aluno aluno, Connection con) throws SQLException{
		String sql = "INSERT INTO public.aluno(nome, sexo, email, cpf, whatsapp,"
					 +" altura, peso, \"dataNascimento\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setString(1, aluno.getNome());
		state.setString(2, aluno.getSexo());
		state.setString(3, aluno.getEmail());
		state.setString(4, aluno.getCpf());
		state.setString(5, aluno.getWhatsapp());
		state.setDouble(6, aluno.getAltura());
		state.setDouble(7, aluno.getPeso());
		state.setDate(8, aluno.getDataNascimento());

		state.execute();
		state.close();
	}
	
	/**Retorna o aluno que possui esse id ou null*/
	public static Aluno selecionar(int id, Connection con) throws SQLException {
		String sql = "SELECT id, nome, sexo, email, cpf, whatsapp, altura, peso, \"dataNascimento\""
					 + " FROM public.aluno WHERE id=?";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setInt(1,id);
		
		Aluno aluno = null;
		ResultSet result = state.executeQuery();
		if(result.next())
			 aluno = new Aluno(result.getInt(1), result.getString(2), result.getString(3),
					 	result.getString(4), result.getString(5), result.getString(6),
					 	result.getDouble(7), result.getDouble(8), result.getDate(9));
		
		
		state.close();
		return aluno;
	}
	
	/**Retorna os alunos que possuem esse nome*/
	public static ArrayList<Aluno> selecionar(String nome, Connection con) throws SQLException {
		String sql = "SELECT id, nome, sexo, email, cpf, whatsapp, altura, peso, \"dataNascimento\""
					 + " FROM public.aluno WHERE nome=?";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setString(1,nome);
		
		ArrayList<Aluno> alunos = new ArrayList<>();
		ResultSet result = state.executeQuery();
		while(result.next())
			 alunos.add( new Aluno(result.getInt(1), result.getString(2), result.getString(3),
					 	result.getString(4), result.getString(5), result.getString(6),
					 	result.getDouble(7), result.getDouble(8), result.getDate(9)) );
		
		
		state.close();
		return alunos;
	}
}//class AlunoDAO
