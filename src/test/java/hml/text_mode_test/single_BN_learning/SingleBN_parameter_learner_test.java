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

public class SingleBN_parameter_learner_test {
    
    public static void main(String[] args)  {
    	SingleBN_parameter_learner t = new SingleBN_parameter_learner();
    	String title = "test";  
    	String sbn = new String( 
	    			"defineNode(X, DescriptionU);"+
	    			"{ defineState(Continuous);" +
	    			"p( X ) = NormalDist(  8.333333333333334, 14.333333333333332 ); }" + 
	    			
	    			"defineNode(Y, DescriptionY);"+
	    			"{ defineState(Continuous);" +
	    			"p( Y | X ) = 0.2441860465116279 * X + NormalDist( 6.965116279069767, 0.14534883720930236); }"
    			); 
    	String csv = "example_data/singleBN/test.csv";
    	    	
    	String ret = t.run(title, sbn, csv, null);
        System.out.println(ret);
    }
}
