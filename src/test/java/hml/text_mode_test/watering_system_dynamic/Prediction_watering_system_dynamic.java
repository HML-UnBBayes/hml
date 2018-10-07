package hml.text_mode_test.watering_system_dynamic;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.data.DiscreteVariable;
import edu.gmu.seor.cps2.CPSCompilerMain;
import edu.gmu.seor.cps2.datastructure.EDBUnit;
import mebn_rm.RDB.MySQL_Interface;
import mebn_rm.RDB.RDB;
import mebn_rm.util.TextFile;

public class Prediction_watering_system_dynamic {
	MySQL_Interface rdb = new MySQL_Interface();

	public Prediction_watering_system_dynamic() {

	}

	public void initRDB() {
		rdb.connect("localhost", "root", "jesus");
		// Data for test
		rdb.connectSchema("wateringsystem_dynamic_test");
	}

	String sql = "SELECT\r\n" + "land_state.Dry as land_state_Dry,\r\n" + "plant.PlantType as plant_PlantType,\r\n"
			+ "plant_state.Health as plant_state_Health,\r\n" + "region.LocationType as region_LocationType,\r\n"
			+ "time.Season as time_Season,\r\n" + "time.DayTime as time_DayTime,\r\n"
			+ "environmental_factors.Temperature as environmental_factors_Temperature,\r\n"
			+ "environmental_factors.Light as environmental_factors_Light,\r\n"
			+ "environmental_factors.Wind as environmental_factors_Wind,\r\n"
			+ "environmental_factors.Humidity as environmental_factors_Humidity,\r\n"
			+ "environmental_factors.Rain as environmental_factors_Rain,\r\n"
			+ "land_state_P.Dry as land_state_P_land_state_Dry\r\n" + "FROM\r\n"
			+ "land_state, plant, plant_state, region, time, environmental_factors, land_state as land_state_P, predecessor \r\n"
			+ "WHERE \r\n" + "predecessor.idPrevTime = land_state_P.idTime && \r\n"
			+ "predecessor.idTime = land_state.idTime && \r\n"
			+ "environmental_factors.idRegion = land_state.idRegion && \r\n"
			+ "environmental_factors.idTime = predecessor.idPrevTime && \r\n"
			+ "region.idRegion = land_state.idRegion && \r\n" + "time.idTime = predecessor.idTime && \r\n"
			+ "plant_state.idTime = predecessor.idPrevTime && \r\n"
			+ "plant_state.idRegion = land_state.idRegion && \r\n" + "plant_state.idPlant = plant.idPlant \r\n" + "";

	public void test() throws SQLException, IOException {
		// Initialize RDB
		initRDB();

		// Load learned SSBN
		String sbn = "projects//ROOT_MTheory_1//Output//ROOT_MTheory_1SSBN_ssbn.txt";
		String net = new TextFile().load(sbn);

		System.out.println(net);

		// Get a csv training dataset from the rdb wateringsystem_dynamic_test
		ResultSet rs = null;

		rs = rdb.get(sql);
		String csvFile = rdb.toExcel("land_state_Dry_test", "ROOT_MTheory_1", rs);

		Iterable<CSVRecord> records;
		boolean b = false; 
		records = CSVFormat.RFC4180.parse(new FileReader(csvFile));

		// Perform experiment using csv
		List<Double> listActual = new ArrayList<Double>();
		List<Double> listPrediction = new ArrayList<Double>();
		
		HashMap<Integer, String> mapHeader = new HashMap<Integer, String>();
	 
		for (CSVRecord record : records) {
			// Headers
			if (!b) {
				b = true;
				for (int j = 0; j < record.size(); j++) {
					String column = record.get(j);
					if (!column.isEmpty()) {
						mapHeader.put(j, column);
					}
				}
			}

			// Data
			else { 

				Double targetValue = 0.0;
				Double predictedValue = 0.0;
				String evidence = "";
				for (int j = 0; j < record.size(); j++) {
					String column = record.get(j);
					if (!column.isEmpty()) {
						String columnName = mapHeader.get(j);

						if (columnName.equalsIgnoreCase("plant_PlantType")
								|| columnName.equalsIgnoreCase("plant_state_Health")
								|| columnName.equalsIgnoreCase("region_LocationType")
								|| columnName.equalsIgnoreCase("time_Season")
								|| columnName.equalsIgnoreCase("time_DayTime")
								|| columnName.equalsIgnoreCase("environmental_factors_Temperature")
								|| columnName.equalsIgnoreCase("environmental_factors_Light")
								|| columnName.equalsIgnoreCase("environmental_factors_Wind")
								|| columnName.equalsIgnoreCase("environmental_factors_Humidity")
								|| columnName.equalsIgnoreCase("environmental_factors_Rain")
								|| columnName.equalsIgnoreCase("land_state_P_land_state_Dry")) {
							evidence += "defineEvidence( " + columnName + " , " + column + "); \n";
						} else if (columnName.equalsIgnoreCase("land_state_Dry")) {
							targetValue = Double.valueOf(column);
						}
					}
				}

				CPSCompilerMain cpsMain = new CPSCompilerMain();
				cpsMain.InitCompiler();
				cpsMain.compile(net + evidence + " run(DMP);");

				EDBUnit nodes = cpsMain.edb.get("ROOT.ENGINES.DMP.NODES");
				EDBUnit ISA = nodes.getRel("ISA");
				for (String str : ISA.getMap().keySet()) {
					EDBUnit node = ISA.map.get(str);
					EDBUnit type = node.get("INFO.TYPE");
					EDBUnit bel = node.get("BEL");

					if (str.equalsIgnoreCase("land_state_Dry")) {
						predictedValue = bel.get("MU").getMatrixData();
					}
				}
				
				listActual.add(targetValue);
				listPrediction.add(predictedValue);
			} 
		}
		
		// Calculate Evaluations
		Double sum = 0.0;
		Double sum_MSE = 0.0;
		for (int i = 0; i < listActual.size(); i++) {
			Double targetValue = listActual.get(i);
			Double predictedValue = listPrediction.get(i);
			Double diff = Math.abs(predictedValue - targetValue);
			System.out.println("targetValue: " + targetValue + " predictedValue: " + predictedValue + " diff: " + diff);
			sum += diff;
			sum_MSE += diff*diff;
		}
		
		// Calculate Mean absolute error
		Double MAE = sum/listActual.size();
		System.out.println(" MAE: " + MAE);
		
		// Calculate Mean squared error
		Double MSE = sum_MSE/listActual.size();
		System.out.println(" MSE: " + MSE); 
		
	}

	public static void main(String[] args) throws SQLException, IOException {
		new Prediction_watering_system_dynamic().test();
	}
}
