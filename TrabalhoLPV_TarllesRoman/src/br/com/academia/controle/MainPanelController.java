package br.com.academia.controle;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainPanelController implements Initializable{
	//Variáveis referentes a área 0. Área 0 == menu superior
	@FXML private MenuItem miImportarEF;

	//Variáveis referentes a área 2. Área 2 == canto médio esquerdo
	@FXML private TextField tfCliente;
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

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");;

	private List<Aluno> alunos;
	private Aluno alunoCarregado;
	private List<Atividade> exercicios;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			limparTela();
			
			exercicios = new ArrayList<>();
			
			tbvAtividades.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("atividade"));
			tbvAtividades.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
			
			dtpDe.getEditor().setText(sdf.format(new Date().getTime() - 6.048e+8));
			dtpAte.getEditor().setText(sdf.format(new Date()));

			//setando tipos gráficos
			cbGraficos.setItems(FXCollections.observableArrayList(DataSetTypes.getNomes()));
			cbGraficos.getSelectionModel().select(0);

			alunos = AlunoDAO.selecionar(Main.conexao);

			List<String> autoCompleteStrings = new ArrayList<>();
			for(Aluno a : alunos) {
				autoCompleteStrings.add(a.getNome() + " - " + a.getEmail());
			}
			TextFields.bindAutoCompletion(tfCliente, autoCompleteStrings);

			tfCliente.setText(autoCompleteStrings.get(0));
			alunoCarregado = alunos.get(0);
			
			onactCarregar();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private Node createPage(int pageIndex) {
		int from = pageIndex * 4,
			to = Math.min(from + 4, exercicios.size());
		
		tbvAtividades.setItems(FXCollections.observableArrayList(exercicios.subList(from, to)));
		
		return tbvAtividades;
	}
	
	@FXML
	public void onactCarregar() {
		try {
			alunoCarregado = AlunoDAO.selecionar(Main.conexao,
							tfCliente.getText().split(" - ")[1]);
			
			carregarAluno();
			carregarGrafico();
			pullExercicios();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void carregarAluno() {
		limparArea2();

		//Coloca os atributos do cliente em seus lugares na cena
		lblNome.setText(alunoCarregado.getNome());
		lblSexo.setText(alunoCarregado.getSexo());
		lblPeso.setText( String.format("%.2f Kg", alunoCarregado.getPeso()) );
		lblAltura.setText( String.format("%.2f m", alunoCarregado.getAltura()) );
		lblDataN.setText(sdf.format( alunoCarregado.getDataNascimento() ));
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
						new java.sql.Date( sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao),	typeDataSet, atividade);
			}else {
				chart = ChartFactory.createLineChart(AtividadeDAO.selecionar(
						alunoCarregado,
						new java.sql.Date( sdf.parse(dtpDe.getEditor().getText()).getTime() ),
						new java.sql.Date( sdf.parse(dtpAte.getEditor().getText()).getTime() ),
						Main.conexao),	typeDataSet, atividade);
			}

			paneGrafico.getChildren().add(chart);
			
		}catch(SQLException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**Acao do menu item importar, exibe um filechooser e importa os arquivos escolhidos*/
	@FXML
	public void acaoImportar() {
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
