package br.com.academia.utils;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.academia.Main;
import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.dao.AlunoDAO;

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
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return false;
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

			return aluno;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**Retorna a primeira ocorrencia da regex no conteudo*/
	private static String obterRegex(String regex, String conteudo) {
		Matcher matcher;

		matcher = Pattern.compile(regex).matcher(conteudo);
		matcher.find();
		return matcher.group();
	}
}
