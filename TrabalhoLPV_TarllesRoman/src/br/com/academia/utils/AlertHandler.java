package br.com.academia.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertHandler {
	
	/**Exibe um alerta em interface gr�fica. Essa janela � modal e trava a thread EDT
	 * at� que a janela seja fechada. O tipo do alerta � definido por {@link AlertType} tipoAlerta
	 * 
	 * @param tipoAlerta tipo do alerta a ser exibido
	 * @param titulo titulo da janela
	 * @param header cabe�alho da mensagem
	 * @param mensagem mensagem que ser� exibida
	 * 
	 * @return {@link Optional} {@link ButtonType} com o resultado da intera��o do usuario com a janela
	 */
	private static Optional<ButtonType> showAlert(AlertType tipoAlerta, String titulo, String header, String mensagem) {
		Alert alertDialog = new Alert(tipoAlerta);
		alertDialog.setTitle(titulo);
		alertDialog.setHeaderText(header);
		alertDialog.setContentText(mensagem);
		return alertDialog.showAndWait();
	}
	
	
	/**Exibe um alerta de informa��es em interface gr�fica. Essa janela � modal e trava a thread EDT
	 * at� que a janela seja fechada.
	 * 
	 * @param titulo titulo da janela
	 * @param header cabe�alho da mensagem
	 * @param mensagem mensagem que ser� exibida
	 */
	public static void showAlertInfo(String titulo,String header,String mensagem) {
		showAlert(Alert.AlertType.INFORMATION, titulo, header, mensagem);
	}
	
	/**Exibe um alerta de erro em interface gr�fica. Essa janela � modal e trava a thread EDT
	 * at� que a janela seja fechada.
	 * 
	 * @param titulo titulo da janela
	 * @param header cabe�alho da mensagem
	 * @param mensagem mensagem que ser� exibida
	 */
	public static void showAlertErro(String titulo,String header,String mensagem) {
		showAlert(Alert.AlertType.ERROR, titulo, header, mensagem);
	}
	
	/**Exibe um alerta de confirma��o em interface gr�fica. Essa janela � modal e trava a thread EDT
	 * at� que a janela seja fechada. Essa janela possui 2 bot�es OK e CANCELAR.
	 * 
	 * @param titulo titulo da janela
	 * @param header cabe�alho da mensagem
	 * @param mensagem mensagem que ser� exibida
	 * 
	 * @return true se o usuario clicar em OK, false se CANCELAR ou fechar a janela
	 */
	public static boolean showAlertConfirm(String titulo,String header,String mensagem) {
		return ( showAlert(Alert.AlertType.CONFIRMATION, titulo, header, mensagem).get() == ButtonType.OK );
	}

}//class AlertHandler
