package br.com.academia;
	
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import br.com.academia.modelo.Usuario;
import br.com.academia.utils.FabricaDeConexao;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	
	public static Connection conexao;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	public static final String TITULO = "ACADEMIA JEDI";
	public static final int ITENS_POR_PAGINA = 4;
	public static Usuario usuario;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/Login.fxml"));
			Scene scene = new Scene(root,359,182);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					try {
						if(usuario == null)
							conexao.close();
					} catch (SQLException e) {
					}
				}
			});
			
			primaryStage.centerOnScreen();
			primaryStage.setTitle(TITULO + ": Login");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	@Override
	public void start(Stage primaryStage) {
		
	}*/
	
	public static void main(String[] args) {
		try {
			conexao = FabricaDeConexao.fabricarConexao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		launch(args);
	}
}
