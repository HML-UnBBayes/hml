package hml.text_mode_test.watering_system_dynamic;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Driver;

import edu.gmu.seor.cps2.CPSCompilerMain;
import edu.gmu.seor.cps2.datastructure.EDBUnit;
import mebn_rm.RDB.MySQL_Interface;
 
public class Data_Generating_watering_system_dynamic {

	public Data_Generating_watering_system_dynamic() {
	}

	String net = "";

	/* https://thingspeak.com/channels/130691
	 * https://thingspeak.com/channels/429987
	 * https://thingspeak.com/channels/14664
	 * 
	 * 
	 */
	void createNet(){
		net += "defineNode(Temperature, desc);"+
				"{ defineState(Continuous);" +
				"p( Temperature ) =  NormalDist(36, 1); " +
				"}" ;
		
		net += "defineNode(Humidity, desc);"+
				"{ defineState(Continuous);" +
				"p( Humidity ) =  NormalDist(50, 1); " +
				"}" ;
		
		net += "defineNode(Light, desc);"+
				"{ defineState(Continuous);" +
				"p( Light ) =  NormalDist(580, 1); " +
				"}" ;
		 
		net += "defineNode(Wind, desc);"+
				"{ defineState(Continuous);" +
				"p( Wind ) =  NormalDist(5, 1); " +
				"}" ;
		 
		net += "defineNode(Rain, desc);"+
				"{ defineState(Continuous);" +
				"p( Rain ) =  NormalDist(10,1); " +
				"}" ;
		 
		net += "defineNode(Health, desc);"+
				"{ defineState(Continuous);" +
				"p( Health ) =  NormalDist(100, 1); " +
				"}" ;
		 
		net += "defineNode(PlantType, desc);"+
				"{ defineState(Discrete, GardenPlant, Others);" +
				"p( PlantType ) = { GardenPlant : 0.5; Others : 0.5; } " + 
				"}" ;
		
		net += "defineNode(LocationType, desc);"+
				"{ defineState(Discrete, Outdoor, Indoor);" +
				"p( LocationType ) = { Outdoor : 0.5; Indoor : 0.5; } " + 
				"}" ;
		
		net += "defineNode(Season, desc);"+
				"{ defineState(Discrete, Winter, Fall, Summer, Spring);" +
				"p( Season ) = { Winter : 0.25; Fall : 0.25; Summer : 0.25; Spring : 0.25;} " + 
				"}" ;
		 
		net += "defineNode(DayTime, desc);"+
				"{ defineState(Discrete, H24_6, H6_12, H12_18, H18_24);" +
				"p( DayTime ) = { H24_6 : 0.25; H6_12 : 0.25; H12_18 : 0.25; H18_24 : 0.25;}" +
				"}" ;
		
		net += "defineNode(SoilHumidity_prev, desc);"+
				"{ defineState(Continuous);" +
				"p( SoilHumidity_prev ) =  NormalDist(100, 1); " +
				"}" ;
		 
		net += "defineNode(SoilHumidity, desc);"+
				"{ defineState(Continuous);" +
				"p( SoilHumidity | Season, DayTime, LocationType, PlantType, Health, Rain, Wind, Light, Humidity, Temperature, SoilHumidity_prev) = "+
				"     if (Season == Winter && DayTime == H24_6 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H24_6 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H24_6 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H24_6 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Winter && DayTime == H6_12 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H6_12 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H6_12 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H6_12 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Winter && DayTime == H12_18 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H12_18 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H12_18 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H12_18 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +

				"else if (Season == Winter && DayTime == H18_24 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.53*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H18_24 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.53*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H18_24 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.53*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Winter && DayTime == H18_24 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.53*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +

				
				"else if (Season == Fall && DayTime == H24_6 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H24_6 && LocationType == Outdoor && PlantType == Others)			{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H24_6 && LocationType == Indoor && PlantType == GardenPlant)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H24_6 && LocationType == Indoor && PlantType == Others)			{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Fall && DayTime == H6_12 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H6_12 && LocationType == Outdoor && PlantType == Others)			{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H6_12 && LocationType == Indoor && PlantType == GardenPlant)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H6_12 && LocationType == Indoor && PlantType == Others)			{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Fall && DayTime == H12_18 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H12_18 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H12_18 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H12_18 && LocationType == Indoor && PlantType == Others)			{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Fall && DayTime == H18_24 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.53*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H18_24 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.53*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H18_24 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.53*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Fall && DayTime == H18_24 && LocationType == Indoor && PlantType == Others)			{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.53*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				
				"else if (Season == Summer && DayTime == H24_6 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H24_6 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H24_6 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H24_6 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Summer && DayTime == H6_12 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H6_12 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H6_12 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H6_12 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Summer && DayTime == H12_18 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H12_18 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H12_18 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H12_18 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Summer && DayTime == H18_24 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.51*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H18_24 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.51*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H18_24 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.51*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Summer && DayTime == H18_24 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.51*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				
				"else if (Season == Spring && DayTime == H24_6 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H24_6 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H24_6 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H24_6 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Spring && DayTime == H6_12 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H6_12 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.49*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H6_12 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H6_12 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.49*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Spring && DayTime == H12_18 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H12_18 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.5*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H12_18 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H12_18 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.5*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				
				"else if (Season == Spring && DayTime == H18_24 && LocationType == Outdoor && PlantType == GardenPlant)	{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.55*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H18_24 && LocationType == Outdoor && PlantType == Others)		{ -0.01*Health + 8.5*Rain - 0.1*Wind  - 0.2*Light + 1.55*Humidity - 1.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H18_24 && LocationType == Indoor && PlantType == GardenPlant)	{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.55*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				"else if (Season == Spring && DayTime == H18_24 && LocationType == Indoor && PlantType == Others)		{ -0.01*Health + 3.5*Rain - 0.1*Wind  - 0.175*Light + 1.55*Humidity - 0.2*Temperature + SoilHumidity_prev + NormalDist(0,  1);}" +
				 
				"}" ;
		 
		System.out.println(net);

	} 
	 
	MySQL_Interface rdb = new MySQL_Interface(); 
 
	public void createRDB(boolean bTraining) {
		rdb.connect("localhost", "root","jesus");

		// Data for training 
		if (bTraining) {
			rdb.connectSchema("wateringsystem_dynamic");
		} else { 		
		// Data for test
			rdb.connectSchema("wateringsystem_dynamic_test");
		}
		
		rdb.deleteAllRows("environmental_factors");
		rdb.deleteAllRows("land_state");
		rdb.deleteAllRows("plant_state");
		rdb.deleteAllRows("predecessor");
		rdb.deleteAllRows("time");
		
//		rdb.addValue("time", "idTime, Season, DayTime", "'t111', 'Fall', 'H24_6'");
	}
	 
	public void run(boolean bTraining) {
 		createRDB(bTraining);
		createNet();
		
		List<Double> results = new ArrayList<Double>();
		List<String> list_daytime = new ArrayList<String>();  
		list_daytime.add("H24_6");
		list_daytime.add("H6_12");
		list_daytime.add("H12_18");
		list_daytime.add("H18_24");
				
		List<String> list_Season = new ArrayList<String>();
		list_Season.add("Winter");
		list_Season.add("Fall");
		list_Season.add("Summer");
		list_Season.add("Spring");
		 
		List<String> list_LocationType = new ArrayList<String>();
		list_LocationType.add("Outdoor");
		list_LocationType.add("Indoor"); 
		
		List<String> list_PlantType = new ArrayList<String>();
		list_PlantType.add("GardenPlant");
		list_PlantType.add("Others"); 
		
		int sample_size = 2;
		Integer time = 100000;
		
		for (int j = 0; j < sample_size; j++) {
			
			int day_size = 7;
			
			for (String Season: list_Season) { 
				for (String LocationType: list_LocationType) { 
					for (String PlantType: list_PlantType) { 
			
						Double SoilHumidity_prev = 100.0;
						String curTime = "";
						String preTime = null;
						
						for (int i = 0; i < day_size; i++) {
							for (String d: list_daytime) { 
								String evidence =  "defineEvidence( Season, " + Season + ");";  
								evidence +=  "defineEvidence( LocationType, " + LocationType + " );";
								evidence +=  "defineEvidence( PlantType, " + PlantType + " );";
								evidence +=  "defineEvidence( DayTime, "+ d + ");";
								
								evidence +=  "defineEvidence( SoilHumidity_prev, "+ SoilHumidity_prev + ");";
								 
								CPSCompilerMain cpsMain = new CPSCompilerMain(); 
								cpsMain.InitCompiler(); 
								cpsMain.compile(net + " "+ evidence +" run (LW, arg(1500, 10000));");  
//								cpsMain.compile(net + " "+ evidence +" run (DMP);");
								
								// RDB: Set new time information
								curTime = "t"+(time++);
								rdb.addValue("time", "idTime, Season, DayTime", "'"+curTime+"', '"+Season+"', '" + d + "'");
								
								// RDB: Set new time information
								if (preTime != null) {
									rdb.addValue("predecessor", "idPrevTime, idTime", "'"+preTime+"', '"+curTime+ "'");
								}
								preTime = curTime;
								
								//showing result 
								Double Temperature = 0.0, Light = 0.0, Wind = 0.0, Humidity = 0.0, Rain = 0.0, Dry = 0.0, Health = 0.0;
								EDBUnit nodes = cpsMain.edb.get("ROOT.ENGINES.LW.NODES"); 
//								EDBUnit nodes = cpsMain.edb.get("ROOT.ENGINES.DMP.NODES");
								EDBUnit ISA = nodes.getRel("ISA");  
								for( String str : ISA.getMap().keySet() ){
									EDBUnit node = ISA.map.get(str);
									EDBUnit type = node.get("INFO.TYPE");
									EDBUnit bel = node.get("BEL");
									 
									if (type.getData().equalsIgnoreCase("Continuous")) {
										if (str.equalsIgnoreCase("SoilHumidity")) {
											Dry = bel.get("MU").getMatrixData();
											results.add(bel.get("MU").getMatrixData());
											SoilHumidity_prev = bel.get("MU").getMatrixData();
										} else if (str.equalsIgnoreCase("Temperature")) { 
											Temperature = bel.get("MU").getMatrixData();
										} else if (str.equalsIgnoreCase("Light")) {
											Light = bel.get("MU").getMatrixData();
										} else if (str.equalsIgnoreCase("Wind")) {
											Wind = bel.get("MU").getMatrixData();
										} else if (str.equalsIgnoreCase("Humidity")) {
											Humidity = bel.get("MU").getMatrixData();
										} else if (str.equalsIgnoreCase("Rain")) {
											Rain = bel.get("MU").getMatrixData();
										} else if (str.equalsIgnoreCase("Health")) {
											Health = bel.get("MU").getMatrixData();
										}  
									}
								}
							 
								String R = "";
								String P = "";
								if (LocationType.equalsIgnoreCase("Outdoor"))
									R = "r1";
								else 
									R = "r2"; 

								if (PlantType.equalsIgnoreCase("GardenPlant"))
									P = "p2";
								else 
									P = "p3"; 

								// RDB: Set new time information
								rdb.addValue("environmental_factors", "idTime, idRegion, Temperature, Light, Wind, Humidity, Rain", 
											"'"+curTime+"', '"+R+
											"', '" + Temperature +
											"', '" + Light +
											"', '" + Wind +
											"', '" + Humidity +
											"', '" + Rain + "'");
								
								// RDB: Set new time information
								rdb.addValue("land_state", "idRegion, idTime, Dry", 
											"'"+R+"', '"+curTime+   
											"', '" + Dry + "'");
	
								// RDB: Set new time information
								rdb.addValue("plant_state", "idPlant, idTime, idRegion, Health", 
											"'"+P+"', '"+curTime+"', '"+R+   
											"', '" + Health + "'");
	 
							}	
						}  
					}
				} 
			} 
		}
		
		for (Double r: results) {
			System.out.println(r);
		}
	}

	public static void main(String[] args) {
		boolean bTraining = true;
		new Data_Generating_watering_system_dynamic().run(bTraining);
	}
}
