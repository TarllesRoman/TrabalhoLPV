package br.com.academia.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.academia.Main;
import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.Atividade;
import br.com.academia.modelo.AtividadeCompleta;
import br.com.academia.utils.ChartFactory.DataSetTypes;

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

	public static void remover(Atividade atividade, Connection con) throws SQLException{
		String sql = "DELETE FROM public.atividade WHERE id=?";

		PreparedStatement state = con.prepareStatement(sql);

		state.setInt(1, atividade.getId());

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

		int i = 0;
		AtividadeCompleta atvAux;
		for(Atividade atv : atividades) {
			atvAux = AtividadeCompletaDAO.selecionar(atv, con);
			if(atvAux != null) {
				atividades.set(i, atvAux);
			}
			i++;
		}

		return atividades;
	}

	/**Recupera todas as atividades*/
	public static ArrayList<Atividade> selecionar(Connection con) throws SQLException{
		String sql = "SELECT id, id_aluno, data, tempo, atividade, duracao, distancia, calorias, passos"
				+ " FROM public.atividade";

		PreparedStatement state = con.prepareStatement(sql);

		ArrayList<Atividade> atividades = new ArrayList<>();
		ResultSet result = state.executeQuery();
		while(result.next())
			atividades.add( new Atividade(result.getInt(1), AlunoDAO.selecionar(result.getInt(2), con),
					result.getDate(3),result.getString(4),
					result.getString(5), result.getDouble(6),result.getDouble(7),
					result.getDouble(8), result.getInt(9)));

		state.close();

		int i = 0;
		AtividadeCompleta atvAux;
		for(Atividade atv : atividades) {
			atvAux = AtividadeCompletaDAO.selecionar(atv, con);
			if(atvAux != null) {
				atividades.set(i, atvAux);
			}
			i++;
		}

		return atividades;
	}

	/**Recupera a atividade desse aluno nessa data nesse tempo ou null*/
	public static Atividade selecionar(Aluno aluno,Date data,String tempo, Connection con) throws SQLException{
		String sql = "SELECT id, data, tempo, atividade, duracao, distancia, calorias, passos"
				+ " FROM public.atividade WHERE id_aluno=? AND data=? AND tempo=?";

		PreparedStatement state = con.prepareStatement(sql);

		state.setInt(1,aluno.getId());
		state.setDate(2, data);
		state.setString(3, tempo);

		Atividade atividade = null;
		ResultSet result = state.executeQuery();
		if(result.next())
			atividade = new Atividade(result.getInt(1), aluno, result.getDate(2),result.getString(3),
					result.getString(4), result.getDouble(5),result.getDouble(6),
					result.getDouble(7), result.getInt(8));

		state.close();
		return atividade;
	}

	/**Recupera todas as atividades no intervalo estipulado*/
	public static List<Atividade> selecionar(Date dataInicio, Date dataFim, Connection con) throws SQLException{
		String sql = "SELECT id, id_aluno, data, tempo, atividade, duracao, distancia, calorias, passos"
				+ " FROM public.atividade WHERE data BETWEEN ? AND ?";

		PreparedStatement state = con.prepareStatement(sql);

		state.setDate(1, dataInicio);
		state.setDate(2, dataFim);

		List<Atividade> atividades = new ArrayList<>();
		ResultSet result = state.executeQuery();
		while(result.next())
			atividades.add( new Atividade(result.getInt(1), AlunoDAO.selecionar(result.getInt(2), Main.conexao),
					result.getDate(3),result.getString(4),
					result.getString(5), result.getDouble(6),result.getDouble(7),
					result.getDouble(8), result.getInt(9)) );

		state.close();

		int i = 0;
		AtividadeCompleta atvAux;
		for(Atividade atv : atividades) {
			atvAux = AtividadeCompletaDAO.selecionar(atv, con);
			if(atvAux != null) {
				atividades.set(i, atvAux);
			}
			i++;
		}

		return atividades;
	}

	/**Recupera todas as atividades do aluno no intervalo estipulado*/
	public static List<Atividade> selecionar(Aluno aluno, Date dataInicio, Date dataFim, Connection con) throws SQLException{
		String sql = "SELECT id, id_aluno, data, tempo, atividade, duracao, distancia, calorias, passos"
				+ " FROM public.atividade WHERE id_aluno=? AND data BETWEEN ? AND ?";

		PreparedStatement state = con.prepareStatement(sql);

		state.setInt(1, aluno.getId());
		state.setDate(2, dataInicio);
		state.setDate(3, dataFim);

		List<Atividade> atividades = new ArrayList<>();
		ResultSet result = state.executeQuery();
		while(result.next())
			atividades.add( new Atividade(result.getInt(1), aluno,
					result.getDate(3),result.getString(4),
					result.getString(5), result.getDouble(6),result.getDouble(7),
					result.getDouble(8), result.getInt(9)) );

		state.close();

		int i = 0;
		AtividadeCompleta atvAux;
		for(Atividade atv : atividades) {
			atvAux = AtividadeCompletaDAO.selecionar(atv, con);
			if(atvAux != null) {
				atividades.set(i, atvAux);
			}
			i++;
		}

		return atividades;
	}

	/**Recupera todas as atividades com esse nome no intervalo estipulado*/
	public static List<Atividade> selecionar(String atividade, Date dataInicio, Date dataFim, Connection con) throws SQLException{
		String sql = "SELECT id, id_aluno, data, tempo, atividade, duracao, distancia, calorias, passos"
				+ " FROM public.atividade WHERE atividade=? AND data BETWEEN ? AND ?";

		PreparedStatement state = con.prepareStatement(sql);

		state.setString(1, atividade);
		state.setDate(2, dataInicio);
		state.setDate(3, dataFim);

		List<Atividade> atividades = new ArrayList<>();
		ResultSet result = state.executeQuery();
		while(result.next())
			atividades.add( new Atividade(result.getInt(1), AlunoDAO.selecionar(result.getInt(2), Main.conexao),
					result.getDate(3),result.getString(4),
					result.getString(5), result.getDouble(6),result.getDouble(7),
					result.getDouble(8), result.getInt(9)) );

		state.close();

		int i = 0;
		AtividadeCompleta atvAux;
		for(Atividade atv : atividades) {
			atvAux = AtividadeCompletaDAO.selecionar(atv, con);
			if(atvAux != null) {
				atividades.set(i, atvAux);
			}
			i++;
		}

		return atividades;
	}

	/**Encontra o maior valor, de acordo com os tipos recebidos por par�metro, para o determinado aluno.
	 * 
	 * ATEN��O�: Para DataSet com valores m�dios ser� retornado o mesmo resultado do convencional ou m�ximo
	 * ATEN��O�: Caso ocorra algum erro ao obter um valor o mesmo conter� uma chave "null" em sua respectiva posi�ao
	 * 
	 * @return Um map de chave String e valor Atividade, onde a chave � uma breve descri��o sobre o valor encontrado e a atividade
	 * correspondente ao mesmo. As atividades ser�o inseridas no map, na mesma ordem em que foram recebidos os DataSetTypes
	 */
	public static Atividade selecionarMaiorValor(Connection con, Aluno aluno, DataSetTypes tipoValor){
		String sql = "SELECT id, id_aluno, data, tempo, atividade, duracao, distancia, calorias, passos FROM public.atividade "
				+ "WHERE id_aluno=? AND ?=(SELECT MAX(?) FROM public.atividade WHERE id_aluno=?)";

		String valor = "";

		valor = obterValor(tipoValor);

		if(valor.isEmpty()) {
			return null;
		}

		if(tipoValor == DataSetTypes.VELOCIDADE_MAXIMA) {
			return obterVelocidadeMaxima(aluno,con);
		}

		try {
			PreparedStatement state = con.prepareStatement(sql);

			state.setInt(1, aluno.getId());
			state.setString(2, valor);
			state.setString(3, valor);
			state.setInt(4, aluno.getId());

			Atividade atividade = null;
			ResultSet result = state.executeQuery();
			if(result.next())
				atividade = new Atividade(result.getInt(1), aluno,
						result.getDate(3),result.getString(4),
						result.getString(5), result.getDouble(6),result.getDouble(7),
						result.getDouble(8), result.getInt(9));

			state.close();

			return atividade;

		} catch (SQLException e) {
			return null;
		}
	}

	private static Atividade obterVelocidadeMaxima(Aluno aluno, Connection con) {
		try {
			AtividadeCompleta aux,
			aux2 = null;
			Double max_vel = 0d;

			for(Atividade a : AtividadeDAO.selecionar(aluno, Main.conexao)) {
				aux = AtividadeCompletaDAO.selecionar(a, con);
				if(aux != null) {
					if(aux.getVelocidadeMaxima() > max_vel) {
						aux2 = aux;
						max_vel = aux.getVelocidadeMaxima();
					}//if
				}//if
			}//for

			return aux2;

		} catch (SQLException e) {
			return null;
		}
	}

	private static String obterValor(DataSetTypes dt) {
		switch(dt) {
		case CALORIAS_MEDIA:
			return "calorias";
		case CALORIAS_PERDIDAS:
			return "calorias";
		case DISTANCIA_MEDIA:
			return "distancia";
		case DISTANCIA_PERCORRIDA:
			return "distancia";
		case DURACAO_DE_EXERCICIOS:
			return "duracao";
		case PASSOS_DADOS:
			return "passos";
		case VELOCIDADE_MAXIMA:
			return "velocidade_maxima";
		default:
			return "";
		}
	}

	public static String obterDescricao(DataSetTypes dt) {
		switch(dt) {
		case CALORIAS_MEDIA:
			return "M�ximo de Calorias perdidas";
		case CALORIAS_PERDIDAS:
			return "M�ximo de Calorias perdidas";
		case DISTANCIA_MEDIA:
			return "Maior dist�ncia percorrida";
		case DISTANCIA_PERCORRIDA:
			return "Maior dist�ncia percorrida";
		case DURACAO_DE_EXERCICIOS:
			return "Maior dura��o de um exerc�cio";
		case PASSOS_DADOS:
			return "Maior numero de passos dados";
		case VELOCIDADE_MAXIMA:
			return "Maior velocidade alcan�ada";
		default:
			return "";
		}
	}

}//class AtividadeDAO
