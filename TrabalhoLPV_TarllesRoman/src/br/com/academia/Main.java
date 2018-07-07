package br.com.academia;
	
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import br.com.academia.utils.FabricaDeConexao;
import br.com.academia.utils.FileImporter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static Connection conexao;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	public static final String TITULO = "ACADEMIA JEDI";
	public static final int ITENS_POR_PAGINA = 4;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/MainPanel.fxml"));
			Scene scene = new Scene(root,1074,630);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					try {
						conexao.close();
					} catch (SQLException e) {
						//event.consume();  //Evita que a janela se feche
						e.printStackTrace();
					}
				}
			});
			
			primaryStage.setTitle(TITULO);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			conexao = FabricaDeConexao.fabricarConexao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		FileImporter.importarArquivo("C:\\Users\\tarll\\Desktop\\ArquivosTeste\\Exercicio1.new");
		
		launch(args);
	}
}
