package hml.text_mode_test.spam;
 
import java.io.File;
import java.sql.SQLException;
 
import hmlp_tool.OpenMEBNeditor;
import mebn_ln.core.MTheory_Learning; 
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN; 
import unbbayes.gui.NetworkWindow; 
import unbbayes.io.mebn.UbfIO;
import unbbayes.prs.mebn.MultiEntityBayesianNetwork; 

public class Learning_test_for_spam {
    public void run() throws SQLException {
        //1. Connect Relational DB (RDB)
        String schema = "spamdatabase";
        String addr = "Localhost";
        String rt = "root";		// <- Change DB user name
        String pw = "jesus";	// <- Change DB password 
                
        RDB.This().connect(addr, rt, pw);
        RDB.This().init(schema);
        
        //2. Convert a MEBN model from RDB using MEBN-RM        
        MTheory m = new RM_To_MEBN(RDB.This()).run();
        
        System.out.println("*After MEBN-RM *************************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        System.out.println("****************************************************************");
        
        //3. Set causal rules using expert knowledge
        Rules_spam rule = new Rules_spam();
        rule.setRules(m);
        
        System.out.println("*After expert knowledge *****************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        System.out.println("****************************************************************");
        
        // update default contexts  
        //rule.updateContexts();
        m.updateContexts();
 		
     	// update default class local distributions (CLDs)
 		m.updateCLDs();
 		
 		//4. Perform MEBN learning  
 		MRoot mroot = new MRoot();
        mroot.setMTheories(new MTheory[]{m});
        new MTheory_Learning().run(mroot);
        System.out.println("****************************************************************");
        System.out.println("Learning Completed!");
        System.out.println("****************************************************************");
         
        //5. save learned MEBN in the ubf file, then we can open it using UnBBayes-MEBN  
        NetworkWindow netWindow = (NetworkWindow) new OpenMEBNeditor().run(m); 
  	
		UbfIO ubf = UbfIO.getInstance(); 
		try{
			File file = new File("example_data/spam/learnedMEBN.ubf"); 
			ubf.saveMebn(file, (MultiEntityBayesianNetwork)netWindow.getNet());
		}
		catch(Exception e){
			e.printStackTrace(); 
		} 
    }

    public static void main(String[] args) throws SQLException {
        Learning_test_for_spam t = new Learning_test_for_spam();
        t.run();
    }
}

