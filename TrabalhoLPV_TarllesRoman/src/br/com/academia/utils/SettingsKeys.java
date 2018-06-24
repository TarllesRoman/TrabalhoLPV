package br.com.academia.utils;

/**Enumeração das chaves de configuração padrão, assim como seus respectivos valores. 
 * Todos os valores das configurações aqui expressadas não estão em um arquivo de configurações por motivos de segurança.
 * Esses valores são críticos para o correto funcionamento do aplicativo, portanto esses valores podem ser consultados
 * mas nunca alterados por um usuario leigo.
 * 
 * @author Tarlles Roman
 * @version 2.0
 */
public enum SettingsKeys {
	/**Caracteres que separam valores dentro de uma mesma chave*/
	SEPARATOR_VALUES(" ; "),
	
	REGEX_NAME_EXERCICIO("Exercício: ([^\\n])+)"),
	REGEX_NAME_ALUNO("Nome: ([a-zA-Z]| )+"),
	REGEX_SEXO("Sexo: ([mM]asculino|[fF]eminino)"),
	REGEX_ALTURA("Altura: ([0-9]+),([0-9]+) m"),
	REGEX_PESO("Peso: ([0-9]+),([0-9]+) Kg"),
	REGEX_DATA_NASCIMENTO(" Data de nascimento: [0-9]{2}/[0-9]{2}/[0-9]{4}"),
	REGEX_EMAIL("E-mail: (([a-zA-Z]|\\.|-|_)+)@([a-zA-Z]+)\\.([a-z]+)"),
	REGEX_DATA_ATIVIDADE("Data: [0-9]{2}/[0-9]{2}/[0-9]{4}"),
	REGEX_TEMPO_ATIVIDADE("Tempo: [0-9]{2}:[0-9]{2} - [0-9]{2}:[0-9]{2}"),
	REGEX_DURACAO("Duração: [0-9]{2}:[0-9]{2}:[0-9]{2}"),
	REGEX_DISTANCIA("Distância: ([0-9|\\.]+),([0-9]+) Km"),
	REGEX_CALORIAS_PERDIDAS("Calorias perdidas: ([0-9|\\.]+) Kcal"),
	REGEX_PASSOS("Passos: ([0-9]|\\.)+");
	
	private String value;

	private SettingsKeys(String value) {
		this.value = value;
	}

	/**Retorna o valor*/
	public String getValue() {
		return value;
	}
	
	/**Altera o valor*/
	public void setValue(String value) {
		this.value = value;
	}
}
