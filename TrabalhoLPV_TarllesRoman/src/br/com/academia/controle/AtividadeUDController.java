package br.com.academia.controle;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import br.com.academia.Main;
import br.com.academia.modelo.dao.AtividadeDAO;
import br.com.academia.utils.AlertHandler;
import br.com.academia.utils.FileImporter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class AtividadeUDController extends EditarAtividadeController {

	  @FXML private TextArea taAtividade;

	    @FXML
	    private Button btnAtualizar, btnExcluir;

	    @FXML
	    private Label lblNome, lblEmail;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lblNome.setText(atvSelecionada.getAluno().getNome());
		lblEmail.setText(atvSelecionada.getAluno().getEmail());
		
		taAtividade.setText(atvSelecionada.toString());
	}
	
	@FXML
    void actAtualizar() {
		try {
			boolean ok = AlertHandler.showAlertConfirm(Main.TITULO, "Confirma a atualizão dos dados?",
					"Você deseja realmente atualizar as informações de "+ atvSelecionada.getAtividade() + "?");

			if(ok) {
				
				if( !FileImporter.validarAtividade(taAtividade.getText()) ) {
					AlertHandler.showAlertErro(Main.TITULO, "Informações incorretas", "A atividade não pode ser atualizada pois alguma das suas informações"
							+ "está em um estado incorreto");
					return;
				}
				AtividadeDAO.remover(atvSelecionada, Main.conexao);
				FileImporter.importarAtividade(atvSelecionada.getAluno(), taAtividade.getText());
				
				AlertHandler.showAlertInfo(Main.TITULO, "Dados atualizados",
						"As informações de "+atvSelecionada.getAtividade()+" foram atualizadas");
				
				((Stage) lblNome.getScene().getWindow() ).close();
			}else {
				AlertHandler.showAlertInfo(Main.TITULO, "Dados mantidos", "As informações não foram atualizadas");
			}

		} catch (SQLException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Impossível atualizar atividade", "");
		}
    }

    @FXML
    void actExcluir() {
    	try {
			boolean ok = AlertHandler.showAlertConfirm(Main.TITULO, "Confirma a exclusão dos dados?",
					"Você deseja realmente excluir as informações de "+ atvSelecionada.getAtividade() + "?");

			if(ok) {
				
				AtividadeDAO.remover(atvSelecionada, Main.conexao);
				
				AlertHandler.showAlertInfo(Main.TITULO, "Dados excluidos",
						"As informações de "+atvSelecionada.getAtividade()+" foram excluidas");
				
				((Stage) lblNome.getScene().getWindow() ).close();
			}else {
				AlertHandler.showAlertInfo(Main.TITULO, "Dados mantidos", "As informações não foram excluidas");
			}

		} catch (SQLException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Impossível excluir atividade", "");
		}
    }
	
}
