package br.com.academia.controle;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import br.com.academia.Main;
import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.dao.AlunoDAO;
import br.com.academia.utils.AlertHandler;
import br.com.academia.utils.MaskHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;

public class EditarAlunoController implements Initializable {
	@FXML
	private Button btnAlterar, btnExcluir, btnBuscar;

	@FXML
	private TextField tfNome, tfSexo, tfAltura, tfPeso, tfEmail, tfCPF, tfWPP;

	@FXML
	private DatePicker dpNascimento;

	private List<Aluno> alunos;
	private Aluno alunoCarregado;
	List<String> autoCompleteStrings;

	@Override
	public void initialize(URL path, ResourceBundle rb) {
		try {
			disableAltExc(true);

			alunos = AlunoDAO.selecionar(Main.conexao);

			MaskHandler.numericField(tfAltura);
			MaskHandler.numericField(tfPeso);
			MaskHandler.cpfField(tfCPF);
			MaskHandler.foneField(tfWPP);

			autoCompleteStrings = new ArrayList<>();
			for(Aluno a : alunos) {
				autoCompleteStrings.add(a.getNome());
			}
			TextFields.bindAutoCompletion(tfNome, autoCompleteStrings);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void carregarAluno() {
		//Coloca os atributos do cliente em seus lugares na cena
		tfNome.setText(alunoCarregado.getNome());
		tfSexo.setText(alunoCarregado.getSexo());
		tfPeso.setText( String.format("%.2f", alunoCarregado.getPeso()).replace(",", ".") );
		tfAltura.setText( String.format("%.2f", alunoCarregado.getAltura()).replace(",", ".") );
		tfCPF.setText(alunoCarregado.getCpf());
		tfWPP.setText(alunoCarregado.getWhatsapp());

		String strDate = Main.sdf.format( alunoCarregado.getDataNascimento() );
		dpNascimento.setValue(new LocalDateStringConverter().fromString(strDate) );

		tfEmail.setText(alunoCarregado.getEmail());
	}

	@FXML
	private void onactAlterar(ActionEvent event) {
		try {

			boolean ok = AlertHandler.showAlertConfirm(Main.TITULO, "Confirma a atualiza��o dos dados?",
					"Voc� deseja realmente alterar as informa��es de "+ alunoCarregado.getNome() + "?");

			if(ok) {
				Aluno att = new Aluno();

				att.setId(alunoCarregado.getId());
				att.setNome(tfNome.getText());
				att.setSexo(tfSexo.getText());
				att.setPeso(Double.parseDouble(tfPeso.getText()));
				att.setAltura(Double.parseDouble(tfAltura.getText()));
				att.setCpf(tfCPF.getText());
				att.setWhatsapp(tfWPP.getText());
				att.setDataNascimento( new Date(Main.sdf.parse(dpNascimento.getEditor().getText()).getTime()) );
				att.setEmail(tfEmail.getText());

				AlunoDAO.atualizar(Main.conexao, att);
				
				AlertHandler.showAlertInfo(Main.TITULO, "Dados atualizados",
						"As informa��es de "+alunoCarregado.getNome()+" foram atualizadas");
				
				limpar();
				
			}else {
				AlertHandler.showAlertInfo(Main.TITULO, "Dados mantidos", "As informa��es n�o foram atualizadas");
			}

		} catch (SQLException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Imposs�vel atualizar aluno", "Confira os dados e tente novamente");
		} catch (ParseException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Imposs�vel atualizar aluno", "Confira a data e tente novamente");
		}
	}

	@FXML
	private void onactBuscar(ActionEvent event) {
		try {
			alunoCarregado = alunos.get(autoCompleteStrings.indexOf(tfNome.getText()));

			carregarAluno();
			disableAltExc(false);
		} catch(IndexOutOfBoundsException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Aluno n�o encontrado", "Para inserir esse aluno importe um exerc�cio do mesmo");
			limpar();
			disableAltExc(true);
		}
	}

	@FXML
	private void onactExcluir(ActionEvent event) {

	}

	private void disableAltExc(boolean disable) {
		btnAlterar.setDisable(disable);
		btnExcluir.setDisable(disable);
	}

	private void limpar() {
		tfNome.setText("");
		tfSexo.setText("");
		tfPeso.setText( "" );
		tfAltura.setText( "" );
		tfCPF.setText("");
		tfWPP.setText("");
		dpNascimento.getEditor().setText("");
		tfEmail.setText("");

		alunoCarregado = null;
	}

}