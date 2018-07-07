package br.com.academia.controle;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import br.com.academia.Main;
import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.Atividade;
import br.com.academia.modelo.dao.AlunoDAO;
import br.com.academia.modelo.dao.AtividadeDAO;
import br.com.academia.utils.ChartFactory;
import br.com.academia.utils.ChartFactory.DataSetTypes;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.util.converter.LocalDateStringConverter;

public class CompararAlunosController implements Initializable {

	@FXML MenuButton mbAlunos;

	@FXML
	private ComboBox<String> cbAtividades;

	@FXML
	private DatePicker dpDe, dpAte;

	@FXML
	private Pane paneGrafico;

	private List<CheckMenuItem> checkAlunos;
	private List<Aluno> alunos;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ajustarIntervalo();

		List<Atividade> atividades;
		try {
			atividades = AtividadeDAO.selecionar(Main.conexao);

			ArrayList<String> aux = new ArrayList<>();
			aux.add("Todos os exercícios");
			for(Atividade a : atividades) {
				if(!aux.contains(a.getAtividade()))
					aux.add(a.getAtividade());
			}
			cbAtividades.setItems(FXCollections.observableArrayList(aux));
			cbAtividades.getSelectionModel().select(0);

			alunos = new ArrayList<>();

			checkAlunos = new ArrayList<>();
			CheckMenuItem todos = new CheckMenuItem("Todos"); 

			//setando a ação do check menu item todos, para que quando selecionado/deselecionado isso reflita nos demais
			todos.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					mbAlunos_selecionarTudo(todos.isSelected());
					actCarregar();
				}
			});

			checkAlunos.add(todos);
			for(Aluno a : AlunoDAO.selecionar(Main.conexao)) {
				checkAlunos.add(new CheckMenuItem(String.format("%s - %s",a.getNome(), a.getEmail())) );
				alunos.add(a);
			}

			mbAlunos.getItems().addAll(checkAlunos);
			mbAlunos_selecionarTudo(true);
			
			for(MenuItem mi : mbAlunos.getItems()) {
				CheckMenuItem cmi = (CheckMenuItem)mi;
				if(mi == todos) continue;
				cmi.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						actCarregar();
					}
				});
			}

			actCarregar();

		} catch (SQLException e) {	}

	}

	@SuppressWarnings("unchecked")
	@FXML
	void actCarregar() {
		if(  dpDe.getValue().isAfter(dpAte.getValue()) ||  dpDe.getValue().isEqual(dpAte.getValue()) ) {
			ajustarIntervalo();
		}
		try {
			paneGrafico.getChildren().clear();

			List<Atividade> atividadesDuracao = new ArrayList<>();
			List<Atividade> atividadesCalorias = new ArrayList<>();

			for(int i = 1 ; i < checkAlunos.size() ; i++) {
				if(!checkAlunos.get(i).isSelected()) continue;

				Atividade atvDuracao, atvCalorias;
				if(cbAtividades.getSelectionModel().getSelectedIndex() == 0) {
					atvDuracao = AtividadeDAO.selecionarMaiorValor(Main.conexao, alunos.get(i -1),
							new java.sql.Date( Main.sdf.parse(dpDe.getEditor().getText()).getTime() ),
							new java.sql.Date( Main.sdf.parse(dpAte.getEditor().getText()).getTime() ),
							DataSetTypes.DURACAO_DE_EXERCICIOS);
					
					atvCalorias = AtividadeDAO.selecionarMaiorValor(Main.conexao, alunos.get(i -1),
							new java.sql.Date( Main.sdf.parse(dpDe.getEditor().getText()).getTime() ),
							new java.sql.Date( Main.sdf.parse(dpAte.getEditor().getText()).getTime() ),
							DataSetTypes.CALORIAS_PERDIDAS);
				}else {
					atvDuracao = AtividadeDAO.selecionarMaiorValor(Main.conexao, cbAtividades.getSelectionModel().getSelectedItem(), alunos.get(i -1),
							new java.sql.Date( Main.sdf.parse(dpDe.getEditor().getText()).getTime() ),
							new java.sql.Date( Main.sdf.parse(dpAte.getEditor().getText()).getTime() ),
							DataSetTypes.DURACAO_DE_EXERCICIOS);
					
					atvCalorias = AtividadeDAO.selecionarMaiorValor(Main.conexao, cbAtividades.getSelectionModel().getSelectedItem(), alunos.get(i -1),
							new java.sql.Date( Main.sdf.parse(dpDe.getEditor().getText()).getTime() ),
							new java.sql.Date( Main.sdf.parse(dpAte.getEditor().getText()).getTime() ),
							DataSetTypes.CALORIAS_PERDIDAS);
				}
				
				if(atvDuracao != null)
					atividadesDuracao.add(atvDuracao);
				
				if(atvCalorias != null)
					atividadesCalorias.add(atvCalorias);
				
				paneGrafico.getChildren().add(ChartFactory.createMultipleLineChart(
						"Alunos X Duração e Calorias Perdidas",
						"Duração e Calorias",
						"Alunos",
						new DataSetTypes[]{DataSetTypes.DURACAO_DE_EXERCICIOS, DataSetTypes.CALORIAS_PERDIDAS},
						new List[] {atividadesDuracao, atividadesCalorias} ));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void ajustarIntervalo() {
		dpDe.getEditor().setText(Main.sdf.format(new Date().getTime() - 6.048e+8));
		dpAte.getEditor().setText(Main.sdf.format(new Date()));

		String strDate = Main.sdf.format( new Date().getTime() - 6.048e+8 );
		dpDe.setValue(new LocalDateStringConverter().fromString(strDate));
		dpAte.setValue( new LocalDateStringConverter().fromString( Main.sdf.format(new Date()) ) );
	}

	/**Seleciona ou retira a seleção de todos os itens do menu box gráficos
	 * 
	 * @param select true para selecionar todos ou false para desmarcar todos
	 */
	private void mbAlunos_selecionarTudo(boolean select) {
		for(MenuItem mi : mbAlunos.getItems()) {
			CheckMenuItem cmi = (CheckMenuItem)mi;
			cmi.setSelected(select);
		}
	}

}
