package br.com.academia.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertHandler {
	
	/**Exibe um alerta em interface gráfica. Essa janela é modal e trava a thread EDT
	 * até que a janela seja fechada. O tipo do alerta é definido por {@link AlertType} tipoAlerta
	 * 
	 * @param tipoAlerta tipo do alerta a ser exibido
	 * @param titulo titulo da janela
	 * @param header cabeçalho da mensagem
	 * @param mensagem mensagem que será exibida
	 * 
	 * @return {@link Optional} {@link ButtonType} com o resultado da interação do usuario com a janela
	 */
	private static Optional<ButtonType> showAlert(AlertType tipoAlerta, String titulo, String header, String mensagem) {
		Alert alertDialog = new Alert(tipoAlerta);
		alertDialog.setTitle(titulo);
		alertDialog.setHeaderText(header);
		alertDialog.setContentText(mensagem);
		return alertDialog.showAndWait();
	}
	
	
	/**Exibe um alerta de informações em interface gráfica. Essa janela é modal e trava a thread EDT
	 * até que a janela seja fechada.
	 * 
	 * @param titulo titulo da janela
	 * @param header cabeçalho da mensagem
	 * @param mensagem mensagem que será exibida
	 */
	public static void showAlertInfo(String titulo,String header,String mensagem) {
		showAlert(Alert.AlertType.INFORMATION, titulo, header, mensagem);
	}
	
	/**Exibe um alerta de erro em interface gráfica. Essa janela é modal e trava a thread EDT
	 * até que a janela seja fechada.
	 * 
	 * @param titulo titulo da janela
	 * @param header cabeçalho da mensagem
	 * @param mensagem mensagem que será exibida
	 */
	public static void showAlertErro(String titulo,String header,String mensagem) {
		showAlert(Alert.AlertType.ERROR, titulo, header, mensagem);
	}
	
	/**Exibe um alerta de confirmação em interface gráfica. Essa janela é modal e trava a thread EDT
	 * até que a janela seja fechada. Essa janela possui 2 botões OK e CANCELAR.
	 * 
	 * @param titulo titulo da janela
	 * @param header cabeçalho da mensagem
	 * @param mensagem mensagem que será exibida
	 * 
	 * @return true se o usuario clicar em OK, false se CANCELAR ou fechar a janela
	 */
	public static boolean showAlertConfirm(String titulo,String header,String mensagem) {
		return ( showAlert(Alert.AlertType.CONFIRMATION, titulo, header, mensagem).get() == ButtonType.OK );
	}

}//class AlertHandler
