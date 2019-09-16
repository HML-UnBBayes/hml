package hml.text_mode_test.single_BN_learning;
  
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  
import edu.cmu.tetrad.bayes.BayesIm;
import edu.cmu.tetrad.data.DataSet;
import edu.gmu.seor.cps2.CPSCompilerMain;
import edu.gmu.seor.cps2.datastructure.EDBUnit;
import hmlp_tool.SingleBN_parameter_learner;
import mebn_ln.core.MFrag_Learning;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MCNode;
import mebn_rm.MEBN.MNode.MDNode;
import mebn_rm.MEBN.MNode.MNode;

public class SingleBN_parameter_learner_test3 {
    
    public static void main(String[] args)  {
    	SingleBN_parameter_learner t = new SingleBN_parameter_learner();
    	String title = "test";  
    	String sbn = new String( 
    			"defineNode(mill_modulus , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( mill_modulus ) = \r\n" + 
    			"NormalDist( 693.03846, 316.95126);}\r\n" + 
    			"defineNode(pp_cr_r , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( pp_cr_r ) = \r\n" + 
    			"NormalDist( 45.40769, 292.00794);}\r\n" + 
    			"defineNode(temp_pass_rc , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( temp_pass_rc ) = \r\n" + 
    			"NormalDist( 924.80769, 2735.36154);}\r\n" + 
    			"defineNode(rolling_speed_rm , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( rolling_speed_rm ) = \r\n" + 
    			"NormalDist( -216.03846, 7300.19846);}\r\n" + 
    			"defineNode(passreduction_th_rc_plan , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( passreduction_th_rc_plan ) = \r\n" + 
    			"NormalDist( -2.79627, 1.72693);}\r\n" + 
    			"defineNode(passdiff_width_rc_plan , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( passdiff_width_rc_plan ) = \r\n" + 
    			"NormalDist( 0.19231, 0.01274);}\r\n" + 
    			"defineNode(width_pass_rc , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( width_pass_rc ) = \r\n" + 
    			"NormalDist( 3025.69615, 161409.26438);}\r\n" + 
    			"defineNode(th_endproduct_3p_m_ctr , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( th_endproduct_3p_m_ctr ) = \r\n" + 
    			"NormalDist( 16.57423, 133.37943);}\r\n" + 
    			"defineNode(rollgap_rm , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( rollgap_rm ) = \r\n" + 
    			"NormalDist( 13.12885, 156.86223);}\r\n" + 
    			"defineNode(rollforce_rm , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( rollforce_rm ) = \r\n" + 
    			"NormalDist( 4459.78846, 829526.12826);}\r\n" + 
    			"defineNode(acc_rolling_weight , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( acc_rolling_weight ) = \r\n" + 
    			"NormalDist( 1.67619, 5.93855);}\r\n" + 
    			"defineNode(dna_set , Desc); \r\n" + 
    			"{\r\n" + 
    			"defineState(Continuous); \r\n" + 
    			"p( dna_set | mill_modulus , pp_cr_r , temp_pass_rc , rolling_speed_rm , passreduction_th_rc_plan , passdiff_width_rc_plan , width_pass_rc , th_endproduct_3p_m_ctr , rollgap_rm , rollforce_rm , acc_rolling_weight  ) = \r\n" + 
    			"0.0183 * mill_modulus + -0.0002 * pp_cr_r + 0.00254 * temp_pass_rc + 0.00099 * rolling_speed_rm + 0.105 * passreduction_th_rc_plan + 0.83412 * passdiff_width_rc_plan + -0.00062 * width_pass_rc + 0.08466 * th_endproduct_3p_m_ctr + -0.09481 * rollgap_rm + -0.00023 * rollforce_rm + 0.00501 * acc_rolling_weight + NormalDist( -9.30396, 0);}\r\n" + 
    			""
    			); 
    	String csv = "example_data/singleBN/test3.csv";
    	    	
    	String ret = t.run(title, sbn, csv, null);
        System.out.println(ret);
    }
}
