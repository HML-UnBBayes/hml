package hml.text_mode_test.watering_system_dynamic;
 
import java.io.File;
import java.sql.SQLException;
 
import hmlp_tool.OpenMEBNeditor;
import mebn_ln.converter.ConvertFromMTheoryToSBN;
import mebn_ln.core.MTheory_Learning; 
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN; 
import unbbayes.gui.NetworkWindow; 
import unbbayes.io.mebn.UbfIO;
import unbbayes.prs.mebn.MultiEntityBayesianNetwork; 

public class Learning_test_for_watering_system_dynamic {
    public void run() throws SQLException {
        MRoot mroot = new MRoot();
        String schema = "wateringsystem_dynamic";
        String addr = "Localhost";
        String rt = "root";		// <- Change DB user name
        String pw = "jesus";	// <- Change DB password 
                
        RDB.This().connect(addr, rt, pw);
        RDB.This().init(schema);
        MTheory m = new RM_To_MEBN(RDB.This()).run();
        
        System.out.println("*After MEBN-RM *************************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        System.out.println("****************************************************************");
        
        Rules_watering_system_dynamic rule = new Rules_watering_system_dynamic();
        rule.setRules(m);
        
        System.out.println("*After expert knowledge *****************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        System.out.println("****************************************************************");
        
        // update default contexts  
        rule.updateContexts();
 		
     	// update default class local distributions (CLDs)
 		m.updateCLDs();
 		
        mroot.setMTheories(new MTheory[]{m});
        new MTheory_Learning().run(mroot);
        System.out.println("****************************************************************");
        System.out.println("Learning Completed!");
        System.out.println("****************************************************************");
         
        // Convert from m to a MEBN in UnBBayes 
        NetworkWindow netWindow = (NetworkWindow) new OpenMEBNeditor().run(m); 
  	
		UbfIO ubf = UbfIO.getInstance(); 
		try{
			File file = new File("example_data/watering_system_dynamic/learnedMEBN.ubf"); 
			ubf.saveMebn(file, (MultiEntityBayesianNetwork)netWindow.getNet());
		}
		catch(Exception e){
			e.printStackTrace(); 
		} 
		
		System.out.println(m);
		
		// Generate an SSBN from m
		String file = new ConvertFromMTheoryToSBN().save(m , "SSBN");
        System.out.println("************************************************************************************");
        System.out.println(file);
        System.out.println("************************************************************************************");
      
		 
//        System.out.println(m); 
    }

    public static void main(String[] args) throws SQLException {
        Learning_test_for_watering_system_dynamic t = new Learning_test_for_watering_system_dynamic();
        t.run();
    }
}

