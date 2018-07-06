package br.com.academia.controle;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import br.com.academia.Main;
import br.com.academia.modelo.Aluno;
import br.com.academia.modelo.Atividade;
import br.com.academia.modelo.dao.AlunoDAO;
import br.com.academia.modelo.dao.AtividadeDAO;
import br.com.academia.utils.AlertHandler;
import br.com.academia.utils.ChartFactory;
import br.com.academia.utils.ChartFactory.DataSetTypes;
import br.com.academia.utils.FileImporter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainPanelController implements Initializable{
	//Variáveis referentes a área 0. Área 0 == menu superior
	@FXML private MenuItem miImportarEF;

	//Variáveis referentes a área 2. Área 2 == canto médio esquerdo
	@FXML private TextField tfAluno;
	@FXML private Button btnBuscar;
	@FXML private Label lblNome, lblSexo, lblPeso, lblAltura, lblDataN, lblEmail, lblNaoEncontrado;
	
	//Variaveis referentes a area 3. Área 3 == cando inferior esquerdo
	@FXML private TableView<Atividade> tbvAtividades;
	@FXML private Button btnRelatorio;
	@FXML private Pagination pgTabela;

	//Variáveis referentes a área 4. Área 4 == canto superior direito
	@FXML private Pane paneGrafico;
	@FXML private ToggleGroup tgTiposGraficos;
	@FXML private ComboBox<String> cbGraficos, cbExercicios;
	@FXML private DatePicker dtpDe, dtpAte;

	private List<Aluno> alunos;
	private Aluno alunoCarregado;
	private List<Atividade> exercicios;
	private AutoCompletionBinding<String> acb = null;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			limparTela();
			
			exercicios = new ArrayList<>();
			
			tbvAtividades.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("atividade"));
			tbvAtividades.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
			
			dtpDe.getEditor().setText(Main.sdf.format(new Date().getTime() - 6.048e+8));
			dtpAte.getEditor().setText(Main.sdf.format(new Date()));

			//setando tipos gráficos
			cbGraficos.setItems(FXCollections.observableArrayList(DataSetTypes.getNomes()));
			cbGraficos.getSelectionModel().select(0);

			alunos = AlunoDAO.selecionar(Main.conexao);

			List<String> autoCompleteStrings = new ArrayList<>();
			for(Aluno a : alunos) {
				autoCompleteStrings.add(a.getNome() + " - " + a.getEmail());
			}
			
			if(acb != null)
				acb.dispose();
			
			acb = TextFields.bindAutoCompletion(tfAluno, autoCompleteStrings);

			if(autoCompleteStrings.isEmpty()) {
				lblNaoEncontrado.setText("Nenhum cliente encontrado");
				lblNaoEncontrado.setVisible(true);
				return;
			}else {
				lblNaoEncontrado.setVisible(false);
			}
			
			tfAluno.setText(autoCompleteStrings.get(0));
			alunoCarregado = alunos.get(0);
			
			onactCarregar();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	@FXML
	private void onactCarregar() {
		try {
			limparTela();
			
			alunoCarregado = AlunoDAO.selecionar(Main.conexao,
							tfAluno.getText().split(" - ")[1]);
			
			carregarAluno();
			carregarGrafico();
			pullExercicios();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			AlertHandler.showAlertErro(Main.TITULO, "Usuário não encontrado", "Experimente usar o auto-complete");	
		}
	}

	@FXML
	private void requestAtividade() {
		AlertHandler.showAlertConfirm("", "", "Funcionou?");
	}
	
	@FXML
	private void carregarGrafico() {
		limparGrafico();

		DataSetTypes typeDataSet = DataSetTypes.searchDataSetType(cbGraficos.getSelectionModel().getSelectedItem());
		if(typeDataSet == null) return;
		try {
			String atividade = (cbExercicios.getSelectionModel().getSelectedIndex() > 0)?
								cbExercicios.getSelectionModel().getSelectedItem() : null;
			
			Chart chart;
			if( ((RadioButton)tgTiposGraficos.getSelectedToggle()).getText().equals("Colunas") ) {
				chart = ChartFactory.createBarChart(AtividadeDAO.selecionar(
						alunoCarregado,
						new java.sql.Date( Main.sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( Main.sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao),	typeDataSet, atividade);
			}else {
				chart = ChartFactory.createLineChart(AtividadeDAO.selecionar(
						alunoCarregado,
						new java.sql.Date( Main.sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( Main.sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao),	typeDataSet, atividade);
			}

			paneGrafico.getChildren().add(chart);
			
		}catch(SQLException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**Acao do menu item importar, exibe um filechooser e importa os arquivos escolhidos*/
	@FXML
	private void acaoImportar() {
		FileChooser fileChooser = new FileChooser ();
		fileChooser.setTitle ("Importar exercício físico");

		//Setando o diretorio inicial para o diretorio do usuario
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		//Setando filtros para as extensões suportadas, nesse caso apenas '.txt'
		fileChooser.getExtensionFilters().add(new ExtensionFilter("TXT", "*.txt"));

		//abrindo o file chooser para escolher 1 ou mais arquivos
		List<File> files = fileChooser.showOpenMultipleDialog (paneGrafico.getScene().getWindow());
		if(files == null) return; //janela foi fechada
		importarArquivos(files);
	}//acao importar
	
	@FXML
	private void onactEditarAluno() {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/EditarAluno.fxml"));
			Scene scene = new Scene(root,440,406);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
			
			Stage stageEditAluno = new Stage();
			
			stageEditAluno.setTitle(Main.TITULO + ": Editar Aluno");
			stageEditAluno.centerOnScreen();
			stageEditAluno.initModality(Modality.APPLICATION_MODAL);
			stageEditAluno.setResizable(false);
			stageEditAluno.setScene(scene);
			stageEditAluno.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onactEditarUsuarios() {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/EditarUsuario.fxml"));
			Scene scene = new Scene(root,435,225);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
			
			Stage stageEditAluno = new Stage();
			
			stageEditAluno.setTitle(Main.TITULO + ": Editar Usuários");
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
	
	@FXML
	public void sair() {
		System.out.println("bye");
	}
	
	private Node createPage(int pageIndex) {
		int from = pageIndex * 4,
			to = Math.min(from + 4, exercicios.size());
		
		tbvAtividades.setItems(FXCollections.observableArrayList(exercicios.subList(from, to)));
		
		return tbvAtividades;
	}
	
	private void carregarAluno() {
		limparArea2();

		//Coloca os atributos do cliente em seus lugares na cena
		lblNome.setText(alunoCarregado.getNome());
		lblSexo.setText(alunoCarregado.getSexo());
		lblPeso.setText( String.format("%.2f Kg", alunoCarregado.getPeso()).replace(",", ".") );
		lblAltura.setText( String.format("%.2f m", alunoCarregado.getAltura()).replace(",", ".") );
		lblDataN.setText(Main.sdf.format( alunoCarregado.getDataNascimento() ));
		lblEmail.setText(alunoCarregado.getEmail());
	}
	
	private void pullExercicios() {
		try {
			exercicios = AtividadeDAO.selecionar(alunoCarregado, Main.conexao);
			
			ArrayList<String> aux = new ArrayList<>();
			aux.add("Todos os exercícios");
			for(Atividade a : exercicios) {
				if(!aux.contains(a.getAtividade()))
					aux.add(a.getAtividade());
			}
			cbExercicios.setItems(FXCollections.observableArrayList(aux));
			cbExercicios.getSelectionModel().select(0);
			
			pgTabela.setPageCount( (exercicios.size()%4 > 0)? (exercicios.size()/4+1) : exercicios.size()/4 );
			pgTabela.setPageFactory(this::createPage);
			
		} catch (SQLException e) {	}
	}

	/**Importa arquivos '.txt' com exercicios fisicos e no final mostra em um alert os arquivos que foram
	 * importados.
	 */
	private void importarArquivos(List<File> files) {
		//Separa os arquivos em:
		ArrayList<String> filesOk = new ArrayList<>(), // importados corretamente
				filesErros = new ArrayList<>(); // nao importados
		String name;
		for(File file : files) {
			name = file.getName();
			//Importando o arquivo e verificando o resultado
			if(FileImporter.importarArquivo(file.getAbsolutePath())) 
				filesOk.add(name);
			else
				filesErros.add(name);

		}
		StringBuilder relatorio = new StringBuilder();

		relatorio.append(filesOk.size());
		relatorio.append(" arquivos importados:\n");
		concatenate(relatorio, filesOk);

		relatorio.append("\n" + filesErros.size());
		relatorio.append(" arquivos não importados:\n");
		concatenate(relatorio, filesErros);

		AlertHandler.showAlertInfo("Importar exercício físico", "Importação concluida", relatorio.toString());
		initialize(null, null);
	}

	/**Altera todos os labels de informações para o valor: "-" e retira o gráfico do pane */
	private void limparTela() {
		limparArea2();
		limparGrafico();
		alunoCarregado = null;
	}

	//idem ao limparTela mas apenas para área 2
	private void limparArea2() {
		lblNome.setText("-");
		lblSexo.setText("-");
		lblPeso.setText("-");
		lblAltura.setText("-");
		lblDataN.setText("-");
		lblEmail.setText("-");
	}

	//idem ao limparTela mas apenas para área do gráfico	
	private void limparGrafico() {
		paneGrafico.getChildren().clear();
	}

	/**Concatena o array list, no string builder. Strings serão separadas por ', ' e terminadas em '.'*/
	private void concatenate(StringBuilder builder, ArrayList<String> strings) {
		for(String s : strings)
			builder.append(String.format("%s, ", s));
		if(strings.size() > 0)
			builder.setCharAt(builder.lastIndexOf(","), '.');
	}

}//class MainPanelController
