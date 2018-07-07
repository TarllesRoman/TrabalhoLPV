package br.com.academia.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.academia.modelo.Atividade;
import br.com.academia.modelo.AtividadeCompleta;

public class AtividadeCompletaDAO {

	/**Insere a atividade completa e a atividade*/
	public static void inserir(AtividadeCompleta atividadeCompleta, Connection con) throws SQLException{
		AtividadeDAO.inserir(atividadeCompleta, con);
		inserirApenas(atividadeCompleta, con);
	}
	
	/**Insere a atividade completa*/
	public static void inserirApenas(AtividadeCompleta atividadeCompleta, Connection con) throws SQLException{
		String sql = "INSERT INTO public.atividade_completa(id_atividade, velocidade_media,"
				+ " velocidade_maxima, ritmo_medio, ritmo_maximo, menor_elevacao, maior_elevacao)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement state = con.prepareStatement(sql);

		state.setInt(1, atividadeCompleta.getId());
		state.setDouble(2, atividadeCompleta.getVelocidadeMedia());
		state.setDouble(3, atividadeCompleta.getVelocidadeMaxima());
		state.setDouble(4, atividadeCompleta.getRitmoMedio());
		state.setDouble(5, atividadeCompleta.getRitmoMaximo());
		state.setDouble(6, atividadeCompleta.getMenorElevacao());
		state.setDouble(7, atividadeCompleta.getMaiorElevacao());

		state.execute();
		state.close();
	}

	
	/**Busca a parte completa para essa atividade,
	 *  se n�o houver retorna null
	 *  Tamb�m busca e carrega, na atividade retornada, os ritmos referentes
	 * @throws SQLException */
	public static AtividadeCompleta selecionar(Atividade atividade, Connection con) throws SQLException {
		try {
		String sql = "SELECT id, velocidade_media, velocidade_maxima, ritmo_medio,"
				+ " ritmo_maximo, menor_elevacao, maior_elevacao FROM public.atividade_completa WHERE id_atividade=?";
		
		PreparedStatement state = con.prepareStatement(sql);
		
		state.setInt(1,atividade.getId());
		
		ResultSet result = state.executeQuery();
		
		AtividadeCompleta atvCompleta = new AtividadeCompleta(atividade);
		
		if(result.next()) {
			atvCompleta.setIdCompleta(result.getInt(1));
			atvCompleta.setVelocidadeMedia(result.getDouble(2));
			atvCompleta.setVelocidadeMaxima(result.getDouble(3));
			atvCompleta.setRitmoMedio(result.getDouble(4));
			atvCompleta.setRitmoMaximo(result.getDouble(5));
			atvCompleta.setMenorElevacao(result.getDouble(6));
			atvCompleta.setMaiorElevacao(result.getDouble(7));
			
			RitmoDAO.selecionar(atvCompleta, con);
			
			state.close();
			return atvCompleta;
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}//class AtividadeCompletaDAO
