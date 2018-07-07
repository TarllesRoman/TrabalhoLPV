package br.com.academia.controle;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import br.com.academia.Main;
import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.Atividade;
import br.com.academia.modelo.dao.AlunoDAO;
import br.com.academia.modelo.dao.AtividadeDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditarAtividadeController implements Initializable{

	@FXML private ToggleGroup tgFiltro;

	@FXML private Pagination pgTabela;

	@FXML private RadioButton rbTudo, rbAtividade, rbAluno;

	@FXML private ComboBox<String> cbFiltros;

	@FXML private DatePicker dtpDe, dtpAte;

	@FXML private TableView<Atividade> tbvAtividades;

	private List<Atividade> atividades;
	private List<Aluno> alunos;
	
	static Atividade atvSelecionada;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			dtpDe.getEditor().setText(Main.sdf.format(new Date().getTime() - 6.048e+8));
			dtpAte.getEditor().setText(Main.sdf.format(new Date()));
			
			tbvAtividades.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("atividade"));
			tbvAtividades.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
			tbvAtividades.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("tempo"));
			tbvAtividades.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("aluno"));

			tgFiltro.selectToggle(rbTudo);
			
			alunos = AlunoDAO.selecionar(Main.conexao);
			
			actToggle();
			actFiltrar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void actToggle() {
		if(tgFiltro.getSelectedToggle() == rbTudo) {
			cbFiltros.setDisable(true);
			dtpAte.setDisable(true);
			dtpDe.setDisable(true);
			carregarTudo();
			return;
		}
		
		if(tgFiltro.getSelectedToggle() == rbAtividade) {
			cbFiltros.setDisable(false);
			dtpAte.setDisable(false);
			dtpDe.setDisable(false);
			
			carregarTudo();
			
			ArrayList<String> aux = new ArrayList<>();
			aux.add("Todas as atividades");
			for(Atividade a : atividades) {
				if(!aux.contains(a.getAtividade()))
					aux.add(a.getAtividade());
			}
			cbFiltros.setItems(FXCollections.observableArrayList(aux));
			cbFiltros.getSelectionModel().select(0);
			return;
		}
		
		if(tgFiltro.getSelectedToggle() == rbAluno) {
			cbFiltros.setDisable(false);
			dtpAte.setDisable(false);
			dtpDe.setDisable(false);
			
			ArrayList<String> aux = new ArrayList<>();
			aux.add("Todos os alunos");
			for(Aluno a : alunos) {
				if(!aux.contains(a.getNome() + " - " + a.getEmail()))
					aux.add(a.getNome() + " - " + a.getEmail());
			}
			cbFiltros.setItems(FXCollections.observableArrayList(aux));
			cbFiltros.getSelectionModel().select(0);
			
			carregarAluno();
			
			return;
		}
	}

	@FXML
	private void actFiltrar() {
		atividades = null;
		
		if(tgFiltro.getSelectedToggle() == rbTudo) {
			carregarTudo();
		}
		
		if(tgFiltro.getSelectedToggle() == rbAtividade) {
			carregarAtividade();
		}
		
		if(tgFiltro.getSelectedToggle() == rbAluno) {
			carregarAluno();
		}
	}
	
	@FXML
	private void requestAtividade(MouseEvent event) {
		if(tbvAtividades.getSelectionModel().getSelectedIndex() < 0) return;
		
		if( !event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() != 2) return;
		
		int index = tbvAtividades.getSelectionModel().getSelectedIndex() + (Main.ITENS_POR_PAGINA * pgTabela.getCurrentPageIndex());
		atvSelecionada = atividades.get(index);
		
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/AtividadeUD.fxml"));
			Scene scene = new Scene(root,318,420);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
			
			Stage stageEditAluno = new Stage();
			
			stageEditAluno.setTitle(Main.TITULO + ": Detalhes Atividade");
			stageEditAluno.centerOnScreen();
			stageEditAluno.initModality(Modality.APPLICATION_MODAL);
			stageEditAluno.setResizable(false);
			stageEditAluno.setScene(scene);
			stageEditAluno.showAndWait();
			
			initialize(null, null);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void carregarAluno() {
		try {
			if( cbFiltros.getSelectionModel().getSelectedIndex() == 0) {
				atividades = AtividadeDAO.selecionar(
						new java.sql.Date( Main.sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( Main.sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao);
			}else {
				atividades = AtividadeDAO.selecionar(alunos.get(cbFiltros.getSelectionModel().getSelectedIndex() - 1),
						new java.sql.Date( Main.sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( Main.sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao);
			}
		
			if(atividades.size() == 0 ) {
				pgTabela.setPageCount(1);
				return;
			}
			pgTabela.setPageCount( (atividades.size()%4 > 0)? (atividades.size()/4+1) : atividades.size()/4 );
			pgTabela.setPageFactory(this::createPage);
		} catch (SQLException | ParseException | IndexOutOfBoundsException e) {	}
	}
	
	private void carregarAtividade() {
		try {
			if( cbFiltros.getSelectionModel().getSelectedIndex() == 0) {
				atividades = AtividadeDAO.selecionar(
						new java.sql.Date( Main.sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( Main.sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao);
			}else {
				atividades = AtividadeDAO.selecionar(cbFiltros.getSelectionModel().getSelectedItem(),
						new java.sql.Date( Main.sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( Main.sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao);
			}
		
			if(atividades.size() == 0 ) {
				pgTabela.setPageCount(1);
				return;
			}
			pgTabela.setPageCount( (atividades.size()%4 > 0)? (atividades.size()/4+1) : atividades.size()/4 );
			pgTabela.setPageFactory(this::createPage);
		} catch (SQLException | ParseException e) {	}
	}

	private void carregarTudo() {
		try {
			atividades = AtividadeDAO.selecionar(Main.conexao);
			
			if(atividades.size() == 0 ) {
				pgTabela.setPageCount(1);
				return;
			}
			pgTabela.setPageCount( (atividades.size()%4 > 0)? (atividades.size()/4+1) : atividades.size()/4 );
			pgTabela.setPageFactory(this::createPage);
		} catch (SQLException e) {	}
	}
	
	private Node createPage(int pageIndex) {
		int from = pageIndex * 4,
				to = Math.min(from + 4, atividades.size());

		tbvAtividades.setItems(FXCollections.observableArrayList(atividades.subList(from, to)));

		return tbvAtividades;
	}

}//class EditarAtividadeController
