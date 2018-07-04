package br.com.academia.utils;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.academia.Main;
import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.Atividade;
import br.com.academia.modelo.AtividadeCompleta;
import br.com.academia.modelo.Ritmo;
import br.com.academia.modelo.dao.AlunoDAO;
import br.com.academia.modelo.dao.AtividadeCompletaDAO;
import br.com.academia.modelo.dao.AtividadeDAO;
import br.com.academia.modelo.dao.RitmoDAO;

public class FileImporter {
	public static boolean importarArquivo(String pathFile) {
		try {
			String conteudoArquivo = "";
			ArquivoTexto fileImportado = new ArquivoTexto();
			//Abre o arquivo de texto a ser importado
			fileImportado.abrir(pathFile);

			//Lê o conteudo do arquivo
			conteudoArquivo = fileImportado.ler();

			Aluno alunoImportado = importarAluno(conteudoArquivo);
			if(alunoImportado == null) return false;
			
			Atividade atividadeImportada = importarAtividade(alunoImportado, conteudoArquivo);
			if(atividadeImportada == null) return false;
			
			return true;
		} catch (FileNotFoundException e) {	}

		return false;
	}

	private static Atividade importarAtividade(Aluno alunoImporatado, String conteudoArquivo) {
		Atividade atividade = new Atividade();
		String aux;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			aux = obterRegex(SettingsKeys.REGEX_DATA_ATIVIDADE.getValue(), conteudoArquivo);
			atividade.setData( new Date( sdf.parse(aux.split(" ")[1]).getTime() ) );
			
			aux = obterRegex(SettingsKeys.REGEX_TEMPO_ATIVIDADE.getValue(), conteudoArquivo);
			atividade.setTempo(aux.substring(aux.indexOf(" ") + 1));
			
			atividade.setAluno(alunoImporatado);
			
			aux = obterRegex(SettingsKeys.REGEX_NAME_EXERCICIO.getValue(), conteudoArquivo);
			atividade.setAtividade(aux.split(" ")[1]);
			
			aux = obterRegex(SettingsKeys.REGEX_DURACAO.getValue(), conteudoArquivo);
			atividade.setDuracao( HourHandle.toDoubleMinutes(aux.split(" ")[1]+":00") );
			
			aux = obterRegex(SettingsKeys.REGEX_DISTANCIA.getValue(), conteudoArquivo);
			atividade.setDistancia( Double.parseDouble(aux.split(" ")[1].replace(".", "").replace(",", ".")) );
			
			aux = obterRegex(SettingsKeys.REGEX_CALORIAS_PERDIDAS.getValue(), conteudoArquivo);
			atividade.setCalorias(Double.parseDouble(aux.split(" ")[2]));
			
			aux = obterRegex(SettingsKeys.REGEX_PASSOS.getValue(), conteudoArquivo);
			atividade.setPassos( Integer.parseInt(aux.split(" ")[1].replace(".", "")) );
			
			AtividadeDAO.inserir(atividade, Main.conexao);
			atividade = AtividadeDAO.selecionar(alunoImporatado, atividade.getData(),
					atividade.getTempo(), Main.conexao);
			
			AtividadeCompleta atividadeCompleta = new AtividadeCompleta(atividade);
			
			boolean isCompleta = false;
			
			try {
				aux = obterRegex(SettingsKeys.REGEX_VELOCIDADE_MEDIA.getValue(), conteudoArquivo);
				atividadeCompleta.setVelocidadeMedia(Double.parseDouble(aux.split(" ")[1]));
				isCompleta = true;
			}catch(RuntimeException e) {	}
			
			try {
				aux = obterRegex(SettingsKeys.REGEX_VELOCIDADE_MAXIMA.getValue(), conteudoArquivo);
				atividadeCompleta.setVelocidadeMaxima(Double.parseDouble(aux.split(" ")[1]));
				isCompleta = true;
			}catch(RuntimeException e) {	}
			
			try {
				aux = obterRegex(SettingsKeys.REGEX_RITMO_MEDIO.getValue(), conteudoArquivo);
				atividadeCompleta.setRitmoMedio(HourHandle.ritmoToDoubleMinutes(aux.split(" ")[1]));
				isCompleta = true;
			}catch(RuntimeException e) {	}
			
			try {
				aux = obterRegex(SettingsKeys.REGEX_RITMO_MAXIMO.getValue(), conteudoArquivo);
				atividadeCompleta.setRitmoMaximo(HourHandle.ritmoToDoubleMinutes(aux.split(" ")[1]));
				isCompleta = true;
			}catch(RuntimeException e) {	}
			
			try {
				aux = obterRegex(SettingsKeys.REGEX_MENOR_ELEVACAO.getValue(), conteudoArquivo);
				atividadeCompleta.setMenorElevacao( 
						Double.parseDouble( aux.split(" ")[1].replace(".","").replace(",", ".") )
						);
				isCompleta = true;
			}catch(RuntimeException e) {	}
			
			try {
				aux = obterRegex(SettingsKeys.REGEX_MAIOR_ELEVACAO.getValue(), conteudoArquivo);
				atividadeCompleta.setMaiorElevacao( 
						Double.parseDouble( aux.split(" ")[1].replace(".","").replace(",", ".") )
						);
				isCompleta = true;
			}catch(RuntimeException e) {	}
			
			if(isCompleta) {
				AtividadeCompletaDAO.inserir(atividadeCompleta, Main.conexao);
				atividade = atividadeCompleta;
			}
			
			importarRitmos(atividadeCompleta,conteudoArquivo);
			
			return atividade;
			
		}catch (SQLException | ParseException e) {
			return null;
		}
	}

	private static void importarRitmos(AtividadeCompleta atividadeCompleta, String conteudoArquivo) {
		Matcher matcher = Pattern.compile(SettingsKeys.REGEX_RITMOS.getValue()).matcher(conteudoArquivo);
		
		Ritmo ritmo;
		int km = 1;
		String aux;
		while(matcher.find()) {
			aux = matcher.group();
			ritmo = new Ritmo(km++,HourHandle.ritmoToDoubleMinutes(aux.split(" ")[1]));
			atividadeCompleta.getRitmos().add( ritmo );
		}
		
		try {
			RitmoDAO.inserir(Main.conexao, atividadeCompleta);
		} catch (SQLException e) {	}
	}

	/**Retorna o aluno que foi importado ou null*/
	private static Aluno importarAluno(String conteudoArquivo) {
		Aluno aluno;
		String aux;

		try {
			aux = obterRegex(SettingsKeys.REGEX_EMAIL.getValue(), conteudoArquivo);
			String email = aux.split(" ")[1];
			
			aluno = AlunoDAO.selecionar(Main.conexao, email);
			if(aluno != null)
				return aluno; //Se o aluno já estiver no bd o retorna
			else
				aluno = new Aluno();

			aux = obterRegex(SettingsKeys.REGEX_NAME_ALUNO.getValue(), conteudoArquivo);
			aluno.setNome(aux.substring(aux.indexOf(" ") + 1));

			aux = obterRegex(SettingsKeys.REGEX_SEXO.getValue(), conteudoArquivo);
			aluno.setSexo(aux.split(" ")[1]);

			aux = obterRegex(SettingsKeys.REGEX_ALTURA.getValue(), conteudoArquivo);
			aux = aux.substring(aux.indexOf(" ") + 1, aux.lastIndexOf(" "));
			aux = aux.replace(",", ".");
			aluno.setAltura(Double.parseDouble(aux));

			aux = obterRegex(SettingsKeys.REGEX_PESO.getValue(), conteudoArquivo);
			aux = aux.substring(aux.indexOf(" ") + 1, aux.lastIndexOf(" "));
			aux = aux.replace(",", ".");
			aluno.setPeso(Double.parseDouble(aux));

			aluno.setEmail(email);

			AlunoDAO.inserir(aluno, Main.conexao);

			return AlunoDAO.selecionar(Main.conexao, email);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**Retorna a primeira ocorrencia da regex no conteudo*/
	private static String obterRegex(String regex, String conteudo) throws RuntimeException {
		Matcher matcher;

		matcher = Pattern.compile(regex).matcher(conteudo);
		if (matcher.find())
			return matcher.group();
		else
			throw new RuntimeException("Regex " + regex +" não encontrado");
	}
}
