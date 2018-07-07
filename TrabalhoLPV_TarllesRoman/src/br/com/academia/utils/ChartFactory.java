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
		
		//criando o gráfico
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
		
		//populando as séries com dados
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
		
		//criando o gráfico
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
		
		//populando as séries com dados
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
	 * Essa enumeração indica o tipo dos valores contidos em um conjunto de dados. Cada enumeração tem um nome
	 * o nome do eixoX e eixoY que podem ser exibidos no gráfico.
	 * </p> 
	 * <p>
	 * Para recuperar o valor correspondente ao tipo use o método getValorCorrespondente()
	 * </p>
	 * <p>
	 * Essa enum apenas determina qual atributo, de um objeto, que estará no conjunto de valores gerados pela {@link FabricaDeDataSet}
	 * </p>
	 * 
	 * @author Tarlles Roman
	 *
	 * @version 1.0
	 */
	public static enum DataSetTypes{
		/**Para todos os exercicios realizados no período estipulado obtém a sua duração e a adiciona ao DataSet.
		 *Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna. 
		 */
		DURACAO_DE_EXERCICIOS("Duração de exercícios","Datas","Duração (minutos)"),
		
		/**Para todos os exercicios realizados no período estipulado obtém a sua distância e a adiciona ao DataSet.
		 * Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna.
		 */
		DISTANCIA_PERCORRIDA("Distância percorrida","Datas","Distância (Km)"),
		
		/**Para todos os exercicios realizados no período estipulado obtém as calorias perdidas e a adiciona ao DataSet.
		 * Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna.
		 */
		CALORIAS_PERDIDAS("Calorias perdidas","Datas","Calorias perdidas (Kcal)"),
		
		/**Para todos os exercicios realizados no período estipulado obtém o numero de passos e o adiciona ao DataSet.
		 * Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna.
		 */
		PASSOS_DADOS("Passos","Datas","Passos"),
		
		/**Para todos os exercicios realizados no período estipulado obtém a velociade média e a adiciona ao DataSet.
		 * Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna.
		 */
		VELOCIDADE_MEDIA("Velocidade Média","Datas","Velocidade Média (Km/h)"),
		
		VELOCIDADE_MAXIMA("Velocidade Máxima","Datas","Velocidade Máxima (Km/h)"),
		
		/**Para todos os exercicios realizados no período estipulado obtém o ritmo médio e o adiciona ao DataSet
		 * Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna.
		 */
		RITMO_MEDIO("Ritmo médio","Datas","Ritmo Médio (minutos/Km)"),
		
		/**Para todos os exercicios realizados no período estipulado obtém a média de distâncias e a adiciona ao DataSet.
		 * Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna.
		 */
		DISTANCIA_MEDIA("Distância média","Datas","Média de distâncias (Km)"),
		
		/**Para todos os exercicios realizados no período estipulado obtém a média de calorias perdidas e a adiciona ao DataSet.
		 * Os valores exibidos no gráfico são agrupados por dia e exercícios feitos no mesmo dia são agrupados na mesma coluna. 
		 */
		CALORIAS_MEDIA("Calorias perdidas média","Datas","Média de calorias perdidas (Kcal)");
		
		
		private String nome, nomeEixoX, nomeEixoY;
		
		private DataSetTypes(String nome, String nomeEixoX, String nomeEixoY) {
			this.nome = nome;
			this.nomeEixoX = nomeEixoX;
			this.nomeEixoY = nomeEixoY;
		}

		/**Obtém o nome desse tipo de dataSet*/
		public String getNome() {
			return nome;
		}
		
		/**Obtém o nome do eixo X para esse tipo de dataSet*/
		public String getNomeEixoX() {
			return nomeEixoX;
		}
		
		/**Obtém o nome do eixo Y para esse tipo de dataSet*/
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
		
		/**Obtém do objeto o valor ao qual esse dataset está vinculado
		 * 
		 * @param obj obj de onde será retirado o valor
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
		
		/**Obtém o nome de todos os DataSetTypes
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
