package br.com.academia.utils;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**Manipulador de horas. Fornece operações que podem ser realizadas em horas presentes em uma {@link String}
 * A hora a ser manipulada deve estar no formato hh:mm:ss ou para determinados métodos hh:mm
 * 
 * @author Tarlles Roman
 *
 * @version 1.0
 */
public class HourHandle {
	public static final String SEPARADOR = ":";
	
	/**Converte o horario passado por parâmetro em seu equivalente em minutos
	 * 
	 * @param horario String no formato hh:mm:ss
	 * 
	 * @return um double representando os minutos que estao na hr do parâmetro
	 */
	public static double toDoubleMinutes(String horario) {
		String hh = horario.split(SEPARADOR)[0];
		String mm = horario.split(SEPARADOR)[1];
		String ss = horario.split(SEPARADOR)[2];
		
		Double minutes = Integer.parseInt(hh)*60D;
		minutes += Double.parseDouble(mm);
		minutes += Double.parseDouble("0."+ss);
		
		return fitDoubleMinutes(minutes);
	}
	
	/**Verifica se os minutos representados em double estão corretos. Os minutos estarão incorretos quando
	 * sua parte flutuante for maior que 0.60. Esse método ira corrigir os minutos passando
	 * todos os segundos que completarem 1 minuto(60") para a parte inteira do double.
	 * 
	 * @param doubleMinutes os minutos em double a serem verificados
	 * 
	 * @return o numero ajustado ou o o mesmo valor caso já esteja correto
	 */
	public static double fitDoubleMinutes(Double doubleMinutes) {
		try {
			Double newDM;
			String aux = doubleMinutes.toString();
			long secs = Integer.parseInt(aux.substring(aux.indexOf(".")+1));
		
			if(secs > 60) {
				newDM = (double) (secs/60);
				newDM += doubleMinutes.intValue() + Double.parseDouble( String.format("0.%02d", (secs%60)) );
			}else
				newDM = doubleMinutes;
			return newDM;
		}catch(NumberFormatException e) {
			return doubleMinutes;
		}
		
	}
	
	/**Dada um horario no formato hh:mm:ss obtém o campo hora(hh) como um inteiro
	 * 
	 * @param horario horario do qual deseja-se retirar a hora
	 * @return a hora como um inteiro
	 */
	public static int getHora(String horario) {
		return Integer.parseInt( horario.split(SEPARADOR)[0] );
	}
	
	/**Dada um horario no formato hh:mm:ss obtém o campo minutos(mm) como um inteiro
	 * 
	 * @param horario horario do qual deseja-se retirar os minutos
	 * @return os minutos como um inteiro
	 */
	public static int getMinutos(String horario) {
		return Integer.parseInt( horario.split(SEPARADOR)[1] );
	}
	
	/**Dada um horario no formato hh:mm:ss obtém o campo segundos como um inteiro
	 * 
	 * @param horario horario do qual deseja-se retirar os segundos
	 * @return os segundos como um inteiro
	 */
	public static int getSegundos(String horario) {
		return Integer.parseInt( horario.split(SEPARADOR)[2] );
	}
	
	/**Converte o ritmo passado por parâmetro em seu equivalente em double
	 * 
	 * @param ritmo {@link String} no formato mm'ss"
	 * 
	 * @return um double representando os minutos que estao na hr do parâmetro
	 */
	public static double ritmoToDoubleMinutes(String ritmo) {
		String mm = ritmo.split("'")[0];
		String ss = ritmo.split("'")[1].substring(0,2);
		
		Double minutes = Double.parseDouble(mm);
		minutes += Double.parseDouble("0."+ss);
		
		return fitDoubleMinutes(minutes);
	}
	
	/**Obtém a hora do sistema operacional
	 * 
	 * @return uma String com a hora no formato hh:min
	 */
	public static String horaAtual(){
		Calendar agora = Calendar.getInstance();
		
		return String.format("%02d%s%02d", agora.get(HOUR_OF_DAY),SEPARADOR,agora.get(MINUTE));
	}
	
	/**Compara a hora 1 com a hora 2 
	 * 
	 * @param hora1 hora no formato hh:mm
	 * @param hora2 hora no formato hh:mm
	 * 
	 * @return -1 se hora1 menor que hora2, 0 se hora1 igua a hora2, +1 se hora1 maior que hora2
	 * @throws ParseException se as datas não estiverem no formato aceito pelo handler
	 */
	public static int compareHours(String hora1, String hora2) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("hh"+SEPARADOR+"mm");
		Date hr1 = sdf.parse(hora1), hr2 = sdf.parse(hora2);
		
		return hr1.compareTo(hr2);
	}
}
