package br.com.academia.controle;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import br.com.academia.Main;
import br.com.academia.modelo.Usuario;
import br.com.academia.modelo.dao.UsuarioDAO;
import br.com.academia.utils.AlertHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class EditarUsuarioController implements Initializable {

    @FXML
    private Button btnAlterar, btnExcluir, btnBuscar, btnCadastrar;

    @FXML
    private TextField tfNome, tfPapel;

    @FXML
    private PasswordField pfSenha;
    
    private List<Usuario> usuarios = new ArrayList<>();
    private Usuario usuarioCarregado;
    private List<String> autoCompleteStrings;
    
    @Override
	public void initialize(URL path, ResourceBundle rb) {
    	try {
			disableAltExc(true);
			
			limpar();

			usuarios = UsuarioDAO.selecionar(Main.conexao);

			autoCompleteStrings = new ArrayList<>();
			for(Usuario u : usuarios) {
				autoCompleteStrings.add(u.getUsuario());
			}
			TextFields.bindAutoCompletion(tfNome, autoCompleteStrings);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    @FXML
    private void actAlterar() {
    	try {
			boolean ok = AlertHandler.showAlertConfirm(Main.TITULO, "Confirma a atualização dos dados?",
					"Você deseja realmente alterar as informações de "+ usuarioCarregado.getUsuario() + "?");

			if(ok) {
				Usuario att = new Usuario();

				att.setId(usuarioCarregado.getId());
				att.setUsuario(tfNome.getText());
				att.setSenha(pfSenha.getText());
				att.setPapel(tfPapel.getText());

				UsuarioDAO.atualizar(att, Main.conexao);
				
				AlertHandler.showAlertInfo(Main.TITULO, "Dados atualizados",
						"As informações de "+usuarioCarregado.getUsuario()+" foram atualizadas");
				
				limpar();
				
			}else {
				AlertHandler.showAlertInfo(Main.TITULO, "Dados mantidos", "As informações não foram atualizadas");
			}

		} catch (SQLException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Impossível atualizar usuário", "Confira os dados e tente novamente");
		}
    }

    @FXML
    private void actBuscar(ActionEvent event) {
    	try {
			usuarioCarregado = usuarios.get( autoCompleteStrings.indexOf(tfNome.getText()) );
    		
			carregarUsuario();
    		disableAltExc(false);
    		btnCadastrar.setDisable(true);
		} catch (IndexOutOfBoundsException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Usuário não encontrado", "Para inserir esse aluno importe um exercício do mesmo");
			limpar();
			disableAltExc(true);
			btnCadastrar.setDisable(false);
		}
    }

    @FXML
    private void actCadastrar(ActionEvent event) {
    	try {
			boolean ok = AlertHandler.showAlertConfirm(Main.TITULO, "Confirma o cadastro dos dados?",
					"Você deseja realmente cadastrar essas informações?");

			if(ok) {
				Usuario att = new Usuario();
				
				att.setUsuario(tfNome.getText());
				att.setSenha(pfSenha.getText());
				att.setPapel(tfPapel.getText());
				
				UsuarioDAO.inserir(att, Main.conexao);
				
				AlertHandler.showAlertInfo(Main.TITULO, "Dados cadastrados",
						"As informações foram salvas com sucesso");
				
				initialize(null, null);
			}else {
				AlertHandler.showAlertInfo(Main.TITULO, "Dados não cadastrados", "As informações não foram salvas");
			}

		} catch (SQLException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Impossível cadastrar usuário",
					"Pode ser que já exista um usuário com essas informações");
		}
    }

    @FXML
    private void actExcluir(ActionEvent event) {
    	try {
			boolean ok = AlertHandler.showAlertConfirm(Main.TITULO, "Confirma a exclusão dos dados?",
					"Você deseja realmente excluir as informações de "+ usuarioCarregado.getUsuario() + "?");

			if(ok) {
				UsuarioDAO.excluir(usuarioCarregado, Main.conexao);
				
				AlertHandler.showAlertInfo(Main.TITULO, "Dados excluidos",
						"As informações de "+usuarioCarregado.getUsuario()+" foram excluidos");
				
				initialize(null, null);
			}else {
				AlertHandler.showAlertInfo(Main.TITULO, "Dados mantidos", "As informações não foram excluidas");
			}

		} catch (SQLException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Impossível excluir usuário", "Confira os dados e tente novamente");
		}
    }
    
    private void carregarUsuario() {
    	tfNome.setText(usuarioCarregado.getUsuario());
    	pfSenha.setText(usuarioCarregado.getSenha());
    	tfPapel.setText(usuarioCarregado.getPapel());
    }

    private void disableAltExc(boolean disable) {
		btnAlterar.setDisable(disable);
		btnExcluir.setDisable(disable);
	}
    
    private void limpar() {
    	tfNome.setText("");
    	pfSenha.setText("");
    	tfPapel.setText("");
    	
    	usuarioCarregado = null;
    	disableAltExc(true);
    	btnCadastrar.setDisable(false);
    }
}

