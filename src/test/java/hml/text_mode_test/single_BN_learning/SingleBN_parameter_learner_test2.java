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

public class SingleBN_parameter_learner_test2 {
    
    public static void main(String[] args)  {
    	SingleBN_parameter_learner t = new SingleBN_parameter_learner();
    	String title = "test";  
    	String sbn = new String( 
	    			"defineNode(mill_modulus, des);{ defineState(Continuous);  p( mill_modulus ) = NormalDist(  0, 0.000001 );}defineNode(pp_cr_r, des);{ defineState(Continuous);  p( pp_cr_r ) = NormalDist(  0, 0.000001 );}defineNode(strength_yp, des);{ defineState(Continuous);  p( strength_yp ) = NormalDist(  0, 0.000001 );}defineNode(temp_pass_rc, des);{ defineState(Continuous);  p( temp_pass_rc ) = NormalDist(  0, 0.000001 );}defineNode(rolling_speed_rm, des);{ defineState(Continuous);  p( rolling_speed_rm ) = NormalDist(  0, 0.000001 );}defineNode(passreduction_th_rc_plan, des);{ defineState(Continuous);  p( passreduction_th_rc_plan ) = NormalDist(  0, 0.000001 );}defineNode(passdiff_width_rc_plan, des);{ defineState(Continuous);  p( passdiff_width_rc_plan ) = NormalDist(  0, 0.000001 );}defineNode(width_pass_rc, des);{ defineState(Continuous);  p( width_pass_rc ) = NormalDist(  0, 0.000001 );}defineNode(th_endproduct_3p_m_ctr, des);{ defineState(Continuous);  p( th_endproduct_3p_m_ctr ) = NormalDist(  0, 0.000001 );}defineNode(rollgap_rm, des);{ defineState(Continuous);  p( rollgap_rm ) = NormalDist(  0, 0.000001 );}defineNode(dna_pass, des);{ defineState(Continuous);  p( dna_pass ) = NormalDist(  0, 0.000001 );}defineNode(rollforce_rm, des);{ defineState(Continuous);  p( rollforce_rm | mill_modulus, pp_cr_r, strength_yp, temp_pass_rc, rolling_speed_rm, passreduction_th_rc_plan, passdiff_width_rc_plan, width_pass_rc, th_endproduct_3p_m_ctr, rollgap_rm, dna_pass ) = mill_modulus+ pp_cr_r+ strength_yp+ temp_pass_rc+ rolling_speed_rm+ passreduction_th_rc_plan+ passdiff_width_rc_plan+ width_pass_rc+ th_endproduct_3p_m_ctr+ rollgap_rm+ dna_pass + NormalDist(  0, 0.000001 );}"
    			); 
    	String csv = "example_data/singleBN/test2.csv";
    	    	
    	String ret = t.run(title, sbn, csv, null);
        System.out.println(ret);
    }
}
