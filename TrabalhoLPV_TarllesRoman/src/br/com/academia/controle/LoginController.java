package br.com.academia.controle;

import java.sql.SQLException;

import br.com.academia.Main;
import br.com.academia.modelo.Usuario;
import br.com.academia.modelo.dao.UsuarioDAO;
import br.com.academia.utils.AlertHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController {

    @FXML
    private TextField tfUsuario;

    @FXML
    private PasswordField pfSenha;

    @FXML
    private Button btnEntrar;

    @FXML
    void actEntrar() {
    	if(tfUsuario.getText().isEmpty() || pfSenha.getText().isEmpty()) {
    		AlertHandler.showAlertErro(Main.TITULO, "Campos não preenchidos", "Preencha os campos para realizar o login");
    		return;
    	}
    	
    	try {
			Usuario user = UsuarioDAO.logar(tfUsuario.getText(), pfSenha.getText(), Main.conexao);
			if(user == null) {
				AlertHandler.showAlertErro(Main.TITULO, "Campos incorretos", "Essa combinação de usuário e senha não está cadastrada");
	    		return;
			}
			
			Main.usuario = user;
			
			try {
				AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/MainPanel.fxml"));
				Scene scene = new Scene(root,1074,630);
				scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
				
				Stage mainStage = new Stage();
				
				mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						try {
							Main.conexao.close();
						} catch (SQLException e) {	}
					}
				});
				
				mainStage.setTitle(Main.TITULO);
				mainStage.setResizable(false);
				mainStage.setScene(scene);
				mainStage.show();
				
				((Stage)tfUsuario.getScene().getWindow()).close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {	}
    	
    }

}