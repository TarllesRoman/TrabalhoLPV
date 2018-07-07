package br.com.academia.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.academia.modelo.Atividade;
import br.com.academia.modelo.AtividadeCompleta;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class ChartFactory {
	
	public static Chart createBarChart(List<Atividade> atividades, DataSetTypes tipoDataSet, String atividade) {
		
		//Definindo os eixos
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();

		eixoX.setLabel(tipoDataSet.nomeEixoX);
		eixoY.setLabel(tipoDataSet.nomeEixoY);
		
		//criando o gr�fico
		BarChart<String, Number> grafico = new BarChart<>(eixoX, eixoY);
		
		if(atividade == null)
			grafico.setTitle(tipoDataSet.nome);
		else
			grafico.setTitle(tipoDataSet.nome + "(" + atividade + ")");
		
		grafico.setPrefSize(690, 408);
		grafico.setMaxSize(690, 408);
		
		Map<String, XYChart.Series<String, Number>> series = new HashMap<>();
		
		XYChart.Series<String, Number> serieAux;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		//populando as s�ries com dados
		for(Atividade atv : atividades) {
			if( atividade != null && !atv.getAtividade().equals(atividade) ) continue;
			if(series.containsKey(atv.getAtividade()))
				((XYChart.Series<String, Number>)series.get(atv.getAtividade())).getData()
						.add(new Data<>(sdf.format( atv.getData() ),
								tipoDataSet.getValorCorrespondente(atv)));
			else {
				serieAux = new XYChart.Series<>();
				serieAux.setName(atv.getAtividade());
				serieAux.getData().add(new Data<>(sdf.format( atv.getData() ),
										tipoDataSet.getValorCorrespondente(atv)));
				series.put(atv.getAtividade(), serieAux);
			}
		}
		
		grafico.getData().addAll(series.values());
		
		return grafico;
	}
	
	public static Chart createLineChart(List<Atividade> atividades, DataSetTypes tipoDataSet, String atividade) {
		
		//Definindo os eixos
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();

		eixoX.setLabel(tipoDataSet.nomeEixoX);
		eixoY.setLabel(tipoDataSet.nomeEixoY);
		
		//criando o gr�fico
		LineChart<String, Number> grafico = new LineChart<>(eixoX, eixoY);
		
		if(atividade == null)
			grafico.setTitle(tipoDataSet.nome);
		else
			grafico.setTitle(tipoDataSet.nome + "(" + atividade + ")");
		
		grafico.setPrefSize(690, 408);
		grafico.setMaxSize(690, 408);
		
		Map<String, XYChart.Series<String, Number>> series = new HashMap<>();
		
		XYChart.Series<String, Number> serieAux;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		//populando as s�ries com dados
		for(Atividade atv : atividades) {
			if( atividade != null && !atv.getAtividade().equals(atividade) ) continue;
			if(series.containsKey(atv.getAtividade()))
				((XYChart.Series<String, Number>)series.get(atv.getAtividade())).getData()
						.add(new Data<>(sdf.format( atv.getData() ),
								tipoDataSet.getValorCorrespondente(atv)));
			else {
				serieAux = new XYChart.Series<>();
				serieAux.setName(atv.getAtividade());
				serieAux.getData().add(new Data<>(sdf.format( atv.getData() ),
										tipoDataSet.getValorCorrespondente(atv)));
				series.put(atv.getAtividade(), serieAux);
			}
		}
		
		grafico.getData().addAll(series.values());
		
		return grafico;
	}
	
	/**<p>
	 * Essa enumera��o indica o tipo dos valores contidos em um conjunto de dados. Cada enumera��o tem um nome
	 * o nome do eixoX e eixoY que podem ser exibidos no gr�fico.
	 * </p> 
	 * <p>
	 * Para recuperar o valor correspondente ao tipo use o m�todo getValorCorrespondente()
	 * </p>
	 * <p>
	 * Essa enum apenas determina qual atributo, de um objeto, que estar� no conjunto de valores gerados pela {@link FabricaDeDataSet}
	 * </p>
	 * 
	 * @author Tarlles Roman
	 *
	 * @version 1.0
	 */
	public static enum DataSetTypes{
		/**Para todos os exercicios realizados no per�odo estipulado obt�m a sua dura��o e a adiciona ao DataSet.
		 *Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna. 
		 */
		DURACAO_DE_EXERCICIOS("Dura��o de exerc�cios","Datas","Dura��o (minutos)"),
		
		/**Para todos os exercicios realizados no per�odo estipulado obt�m a sua dist�ncia e a adiciona ao DataSet.
		 * Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna.
		 */
		DISTANCIA_PERCORRIDA("Dist�ncia percorrida","Datas","Dist�ncia (Km)"),
		
		/**Para todos os exercicios realizados no per�odo estipulado obt�m as calorias perdidas e a adiciona ao DataSet.
		 * Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna.
		 */
		CALORIAS_PERDIDAS("Calorias perdidas","Datas","Calorias perdidas (Kcal)"),
		
		/**Para todos os exercicios realizados no per�odo estipulado obt�m o numero de passos e o adiciona ao DataSet.
		 * Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna.
		 */
		PASSOS_DADOS("Passos","Datas","Passos"),
		
		/**Para todos os exercicios realizados no per�odo estipulado obt�m a velociade m�dia e a adiciona ao DataSet.
		 * Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna.
		 */
		VELOCIDADE_MEDIA("Velocidade M�dia","Datas","Velocidade M�dia (Km/h)"),
		
		VELOCIDADE_MAXIMA("Velocidade M�xima","Datas","Velocidade M�xima (Km/h)"),
		
		/**Para todos os exercicios realizados no per�odo estipulado obt�m o ritmo m�dio e o adiciona ao DataSet
		 * Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna.
		 */
		RITMO_MEDIO("Ritmo m�dio","Datas","Ritmo M�dio (minutos/Km)"),
		
		/**Para todos os exercicios realizados no per�odo estipulado obt�m a m�dia de dist�ncias e a adiciona ao DataSet.
		 * Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna.
		 */
		DISTANCIA_MEDIA("Dist�ncia m�dia","Datas","M�dia de dist�ncias (Km)"),
		
		/**Para todos os exercicios realizados no per�odo estipulado obt�m a m�dia de calorias perdidas e a adiciona ao DataSet.
		 * Os valores exibidos no gr�fico s�o agrupados por dia e exerc�cios feitos no mesmo dia s�o agrupados na mesma coluna. 
		 */
		CALORIAS_MEDIA("Calorias perdidas m�dia","Datas","M�dia de calorias perdidas (Kcal)");
		
		
		private String nome, nomeEixoX, nomeEixoY;
		
		private DataSetTypes(String nome, String nomeEixoX, String nomeEixoY) {
			this.nome = nome;
			this.nomeEixoX = nomeEixoX;
			this.nomeEixoY = nomeEixoY;
		}

		/**Obt�m o nome desse tipo de dataSet*/
		public String getNome() {
			return nome;
		}
		
		/**Obt�m o nome do eixo X para esse tipo de dataSet*/
		public String getNomeEixoX() {
			return nomeEixoX;
		}
		
		/**Obt�m o nome do eixo Y para esse tipo de dataSet*/
		public String getNomeEixoY() {
			return nomeEixoY;
		}
		
		/**Altera o nome desse tipo de data set*/
		public void setNome(String nome) {
			this.nome = nome;
		}
		
		/**Altera o nome do eixo X para esse tipo de data set*/
		public void setNomeEixoX(String nomeX) {
			this.nomeEixoX = nomeX;
		}
		
		/**Altera o nome do eixo Y para esse tipo de data set*/
		public void setNomeEixoY(String nomeY) {
			this.nomeEixoY = nomeY;
		}
		
		/**Obt�m do objeto o valor ao qual esse dataset est� vinculado
		 * 
		 * @param obj obj de onde ser� retirado o valor
		 * 
		 * @return Double o valor retirado do objeto
		 */
		public Double getValorCorrespondente(Object obj) {
			if(this == DURACAO_DE_EXERCICIOS) 
				return ((Atividade) obj).getDuracao();
			
			if(this == DISTANCIA_PERCORRIDA || this == DISTANCIA_MEDIA) 
				return (double) ((Atividade) obj).getDistancia();
			
			if(this == CALORIAS_PERDIDAS || this == CALORIAS_MEDIA) 
				return (double) ((Atividade) obj).getCalorias();
			
			if(this == PASSOS_DADOS) 
				return (double) ((Atividade) obj).getPassos();
			
			if(this == VELOCIDADE_MEDIA && ((AtividadeCompleta) obj).getVelocidadeMedia() > 0) 
				return (double) ((AtividadeCompleta) obj).getVelocidadeMedia();
			
			if(this == VELOCIDADE_MAXIMA && ((AtividadeCompleta) obj).getVelocidadeMaxima() > 0) 
				return (double) ((AtividadeCompleta) obj).getVelocidadeMaxima();
			
			if(this == RITMO_MEDIO && ((AtividadeCompleta) obj).getRitmoMedio() > 0) 
				return ((AtividadeCompleta) obj).getRitmoMedio();
			
			return 0D;
		}
		
		/**Obt�m o nome de todos os DataSetTypes
		 * 
		 * @return ArrayList de String com o nome
		 */
		public static ArrayList<String> getNomes(){
			ArrayList<String> nomesTipos = new ArrayList<>();
			for(DataSetTypes dts: DataSetTypes.values())
				nomesTipos.add(dts.getNome());
			
			return nomesTipos;
		}
		
		/**Procura pelo tipo de dataset que possui esse nome
		 * 
		 * @param nome nome procurado do dataSet
		 * 
		 * @return <code>DataSetType</code> encontrado ou null se nenhum corresponder ao nome
		 */
		public static DataSetTypes searchDataSetType(String nome) {
			for(DataSetTypes dts: DataSetTypes.values())
				if(dts.getNome().equals(nome))
					return dts;
			
			return null;
		}
		
	}//enum DataSetTypes
	
}
