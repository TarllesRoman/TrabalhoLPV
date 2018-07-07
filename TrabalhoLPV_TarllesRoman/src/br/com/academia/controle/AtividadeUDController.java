package br.com.academia.controle;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

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

    }

    @FXML
    void actExcluir() {

    }
	
}
