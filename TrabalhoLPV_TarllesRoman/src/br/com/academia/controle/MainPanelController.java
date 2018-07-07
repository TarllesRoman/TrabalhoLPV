package br.com.academia.controle;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

public class MainPanelController implements Initializable{
	//Vari�veis referentes a �rea 0. �rea 0 == menu superior
	@FXML private MenuItem miImportarEF;

	//Vari�veis referentes a �rea 2. �rea 2 == canto m�dio esquerdo
	@FXML private TextField tfAluno;
	@FXML private Button btnBuscar;
	@FXML private Label lblNome, lblSexo, lblPeso, lblAltura, lblDataN, lblEmail, lblNaoEncontrado;

	//Variaveis referentes a area 3. �rea 3 == cando inferior esquerdo
	@FXML private TableView<Atividade> tbvAtividades;
	@FXML private Button btnRecordes;
	@FXML private Pagination pgTabela;

	//Vari�veis referentes a �rea 4. �rea 4 == canto superior direito
	@FXML private Pane paneGrafico;
	@FXML private ToggleGroup tgTiposGraficos;
	@FXML private ComboBox<String> cbGraficos, cbExercicios;
	@FXML private DatePicker dtpDe, dtpAte;

	//Vari�veis referentes a �rea 5. �rea 5 == canto inferior direito
	@FXML private Label lblPassTotal, lblPassMedia, lblDistMedia, lblDistTotal, lblCalMedia, lblCalTotal;

	private List<Aluno> alunos;
	private Aluno alunoCarregado;
	private List<Atividade> atividades;
	private AutoCompletionBinding<String> acb = null;

	private DataSetTypes recordes[] = {DataSetTypes.DURACAO_DE_EXERCICIOS, DataSetTypes.DISTANCIA_PERCORRIDA,
			DataSetTypes.CALORIAS_PERDIDAS, DataSetTypes.PASSOS_DADOS, DataSetTypes.VELOCIDADE_MAXIMA};

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			limparTela();

			atividades = new ArrayList<>();

			tbvAtividades.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("atividade"));
			tbvAtividades.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));

			ajustarIntervalo();

			//setando tipos gr�ficos
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
		} catch (Exception e) {
			AlertHandler.showAlertErro(Main.TITULO, "Usu�rio n�o encontrado", "Experimente usar o auto-complete");	
		}
	}

	@FXML
	private void requestAtividade(MouseEvent event) {
		if(tbvAtividades.getSelectionModel().getSelectedIndex() < 0) return;

		if( !event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() != 2) return;

		int index = tbvAtividades.getSelectionModel().getSelectedIndex() + (Main.ITENS_POR_PAGINA * pgTabela.getCurrentPageIndex());
		Atividade atvSelecionada = atividades.get(index);

		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/TextAreaDetalhes.fxml"));
			Scene scene = new Scene(root,420,314);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
			Stage stageDetalhes = new Stage();

			for(Node n : root.getChildren()) {
				if(n.getId() == null)continue;
				if(n.getId().equals("taDetalhes")) {
					TextArea ta = ((TextArea) n);
					ta.setText(atvSelecionada.toString());
					break;
				}
			}

			stageDetalhes.setTitle(Main.TITULO + ": Detalhes");
			stageDetalhes.centerOnScreen();
			stageDetalhes.initModality(Modality.APPLICATION_MODAL);
			stageDetalhes.setResizable(false);
			stageDetalhes.setScene(scene);
			stageDetalhes.showAndWait();

		} catch(Exception e) {	}
	}

	@FXML
	private void carregarGrafico() {
		limparGrafico();
		limparArea5();

		if(  dtpDe.getValue().isAfter(dtpAte.getValue()) ||  dtpDe.getValue().isEqual(dtpAte.getValue()) ) {
			ajustarIntervalo();
		}

		DataSetTypes typeDataSet = DataSetTypes.searchDataSetType(cbGraficos.getSelectionModel().getSelectedItem());
		if(typeDataSet == null) return;
		try {
			String atividade = (cbExercicios.getSelectionModel().getSelectedIndex() > 0)? cbExercicios.getSelectionModel().getSelectedItem() : null;

			List<Atividade> dataSet = AtividadeDAO.selecionar(
					alunoCarregado,
					new java.sql.Date( Main.sdf.parse(dtpDe.getEditor().getText()).getTime() ),
					new java.sql.Date( Main.sdf.parse(dtpAte.getEditor().getText()).getTime() ),
					Main.conexao);

			Chart chart;
			if( ((RadioButton)tgTiposGraficos.getSelectedToggle()).getText().equals("Colunas") ) {
				chart = ChartFactory.createBarChart(dataSet,	typeDataSet, atividade);
			}else {
				chart = ChartFactory.createLineChart(dataSet,	typeDataSet, atividade);
			}

			paneGrafico.getChildren().add(chart);

			Map<String, Double> statictics = ChartFactory.createStatistics(dataSet); 
			if(statictics == null) return;

			for(String s : statictics.keySet()) {
				if(s.equals("totalPassos")) {
					lblPassTotal.setText(String.format("%d", statictics.get(s).intValue() ));
					continue;
				}

				if(s.equals("mediaPassos")) {
					lblPassMedia.setText(String.format("%d", statictics.get(s).intValue() ));
					continue;
				}

				if(s.equals("totalDistancia")) {
					lblDistTotal.setText(String.format("%.2f", statictics.get(s)));
					continue;
				}

				if(s.equals("mediaDistancia")) {
					lblDistMedia.setText(String.format("%.2f", statictics.get(s)));
					continue;
				}

				if(s.equals("totalCalorias")) {
					lblCalTotal.setText(String.format("%.2f", statictics.get(s)));
					continue;
				}

				if(s.equals("mediaCalorias")) {
					lblCalMedia.setText(String.format("%.2f", statictics.get(s)));
					continue;
				}
			}

		}catch(SQLException | ParseException | NullPointerException e) {	}
	}

	/**Acao do menu item importar, exibe um filechooser e importa os arquivos escolhidos*/
	@FXML
	private void acaoImportar() {
		FileChooser fileChooser = new FileChooser ();
		fileChooser.setTitle ("Importar exerc�cio f�sico");

		//Setando o diretorio inicial para o diretorio do usuario
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		//Setando filtros para as extens�es suportadas, nesse caso apenas '.txt'
		fileChooser.getExtensionFilters().add(new ExtensionFilter("TXT", "*.txt"));

		//abrindo o file chooser para escolher 1 ou mais arquivos
		List<File> files = fileChooser.showOpenMultipleDialog (paneGrafico.getScene().getWindow());
		if(files == null) return; //janela foi fechada
		importarArquivos(files);
	}//acao importar

	@FXML
	private void onactEditarAluno() {
		showModalView("/br/com/academia/view/EditarAluno.fxml", 440,406, ": Editar Aluno");
		
		initialize(null, null);
	}

	@FXML
	private void onactEditarAtividade() {
		showModalView("/br/com/academia/view/EditarAtividade.fxml", 669,386, ": Editar Atividade");
		
		initialize(null, null);
	}

	@FXML
	private void onactEditarUsuarios() {
		showModalView("/br/com/academia/view/EditarUsuario.fxml", 435,225, ": Editar Usu�rios");
	}

	@FXML
	private void onactCompararAlunos() {
		showModalView("/br/com/academia/view/CompararAlunos.fxml", 869, 545, ": CompararAlunos");
	}

	@FXML
	private void sair() {
		System.out.println("bye");
	}

	@FXML
	private void onactRecordes() {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/academia/view/TextAreaDetalhes.fxml"));
			Scene scene = new Scene(root,420,314);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());
			Stage stageDetalhes = new Stage();

			TextArea ta = null;
			for(Node n : root.getChildren()) {
				if(n.getId() == null)continue;
				if(n.getId().equals("taDetalhes")) {
					ta = ((TextArea) n);
					break;
				}
			}

			if(ta==null)return;

			StringBuilder sb = new StringBuilder();

			sb.append("De todos os exerc�cios de " + alunoCarregado.getNome() + " o que pussui o(a):\n\n");

			Atividade atv;
			String s;
			for(DataSetTypes dt : recordes) {
				atv = AtividadeDAO.selecionarMaiorValor(Main.conexao, alunoCarregado, dt);
				s = AtividadeDAO.obterDescricao(dt);
				if(atv != null)
					sb.append(s+": "+dt.getValorCorrespondente(atv) + " - " + atv.getAtividade() + " - " + Main.sdf.format(atv.getData()) + "\n" );
				else
					sb.append(s+": nenhum valor encontrado\n");
			}

			ta.setText(sb.toString());

			stageDetalhes.setTitle(Main.TITULO + ": Detalhes");
			stageDetalhes.centerOnScreen();
			stageDetalhes.initModality(Modality.APPLICATION_MODAL);
			stageDetalhes.setResizable(false);
			stageDetalhes.setScene(scene);
			stageDetalhes.showAndWait();

		} catch(Exception e) {	}
	}
	
	private void showModalView(String view, int width, int height, String subtitle) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource(view));
			Scene scene = new Scene(root,width,height);
			scene.getStylesheets().add(getClass().getResource("/br/com/academia/view/DefaultCSS.css").toExternalForm());

			Stage stageEditUsuarios = new Stage();

			stageEditUsuarios.setTitle(Main.TITULO + subtitle);
			stageEditUsuarios.centerOnScreen();
			stageEditUsuarios.initModality(Modality.APPLICATION_MODAL);
			stageEditUsuarios.setResizable(false);
			stageEditUsuarios.setScene(scene);
			stageEditUsuarios.showAndWait();
		} catch(Exception e) {	}
	}

	private Node createPage(int pageIndex) {
		int from = pageIndex * Main.ITENS_POR_PAGINA,
				to = Math.min(from + Main.ITENS_POR_PAGINA, atividades.size());

		tbvAtividades.setItems(FXCollections.observableArrayList(atividades.subList(from, to)));

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
			atividades = AtividadeDAO.selecionar(alunoCarregado, Main.conexao);

			ArrayList<String> aux = new ArrayList<>();
			aux.add("Todos os exerc�cios");
			for(Atividade a : atividades) {
				if(!aux.contains(a.getAtividade()))
					aux.add(a.getAtividade());
			}
			cbExercicios.setItems(FXCollections.observableArrayList(aux));
			cbExercicios.getSelectionModel().select(0);

			if(atividades.size() == 0 ) {
				pgTabela.setPageCount(1);
				pgTabela.setPageFactory(this::createPage);
				return;
			}
			pgTabela.setPageCount( (atividades.size()%4 > 0)? (atividades.size()/4+1) : atividades.size()/4 );
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
		relatorio.append(" arquivos n�o importados:\n");
		concatenate(relatorio, filesErros);

		AlertHandler.showAlertInfo("Importar exerc�cio f�sico", "Importa��o concluida", relatorio.toString());
		initialize(null, null);
	}

	private void ajustarIntervalo() {
		dtpDe.getEditor().setText(Main.sdf.format(new Date().getTime() - 6.048e+8));
		dtpAte.getEditor().setText(Main.sdf.format(new Date()));

		String strDate = Main.sdf.format( new Date().getTime() - 6.048e+8 );
		dtpDe.setValue(new LocalDateStringConverter().fromString(strDate));
		dtpAte.setValue( new LocalDateStringConverter().fromString( Main.sdf.format(new Date()) ) );
	}

	/**Altera todos os labels de informa��es para o valor: "-" e retira o gr�fico do pane */
	private void limparTela() {
		limparArea2();
		limparGrafico();
		limparArea5();
		alunoCarregado = null;
		pgTabela.setPageCount(1);
		tbvAtividades.setItems(FXCollections.observableArrayList());
	}

	//idem ao limparTela mas apenas para �rea 2
	private void limparArea2() {
		lblNome.setText("-");
		lblSexo.setText("-");
		lblPeso.setText("-");
		lblAltura.setText("-");
		lblDataN.setText("-");
		lblEmail.setText("-");
	}

	//idem ao limparTela mas apenas para �rea do gr�fico	
	private void limparGrafico() {
		paneGrafico.getChildren().clear();
	}

	//idem ao limparTela mas apenas para �rea 5
	private void limparArea5(){
		lblDistMedia.setText("-");
		lblDistTotal.setText("-");
		lblCalMedia.setText("-");
		lblCalTotal.setText("-");
		lblPassTotal.setText("-");
		lblPassMedia.setText("-");
	}

	/**Concatena o array list, no string builder. Strings ser�o separadas por ', ' e terminadas em '.'*/
	private void concatenate(StringBuilder builder, ArrayList<String> strings) {
		for(String s : strings)
			builder.append(String.format("%s, ", s));
		if(strings.size() > 0)
			builder.setCharAt(builder.lastIndexOf(","), '.');
	}

}//class MainPanelController
