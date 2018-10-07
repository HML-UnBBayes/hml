 
package hml.text_mode_test.watering_system_dynamic;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mebn_ln.probabilistic_rule.ProbabilisticRules;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MCNode;
import mebn_rm.MEBN.MNode.MDNode;
import mebn_rm.MEBN.MNode.MIsANode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.util.StringUtil;

public class Rules_watering_system_dynamic
extends ProbabilisticRules {
	MTheory mTheory = null;
    public void setRules(MTheory m) {
    	mTheory =  m;
    	
    	List<String> nodeList = new ArrayList<String>();
    	
    	// 1. land_state.land_state_Dry
        nodeList.add("plant.plant_PlantType");
        nodeList.add("plant_state.plant_state_Health"); 
      	nodeList.add("region.region_LocationType");
      	nodeList.add("time.time_Season"); 
      	nodeList.add("time.time_DayTime");
      	nodeList.add("environmental_factors.environmental_factors_Temperature");
      	nodeList.add("environmental_factors.environmental_factors_Light");
      	nodeList.add("environmental_factors.environmental_factors_Wind");
      	nodeList.add("environmental_factors.environmental_factors_Humidity");
      	nodeList.add("environmental_factors.environmental_factors_Rain"); 
      	nodeList.add("land_state.land_state_Dry");
      	addParents_hardcoded("land_state.land_state_Dry", nodeList);   
    } 
     
    public void addParents_hardcoded(String c, List<String> ps) {
        String mFrag = new StringUtil().getLeft(c);
        String mNode = new StringUtil().getRight(c);
        String combParents = "";
        boolean bOtherMFrag = false;
          
        for (String p : ps) {
            String mFragP = new StringUtil().getLeft(p); 
            if (!mFrag.equalsIgnoreCase(mFragP) && !bOtherMFrag) { 
                bOtherMFrag = true;
            } 
        }
        
        combParents = mNode;
        
        MFrag f = mTheory.getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);  
        
        if (bOtherMFrag) {
            String sql = "SELECT\r\n";
            String sqlFrom = "";
            
            sql = sql + f.getTableName() + "." + childMNode.getAttributeName() + " as " + mNode + ",\r\n";
            sqlFrom = sqlFrom + f.getTableName() + ", ";
            
            MFrag newMFrag = new MFrag(mTheory, combParents);
            newMFrag.setTableName(f.getTableName());
            
            MNode newChild = null;
            if (childMNode.isContinuous()) {
                newChild = new MCNode(childMNode);
            } else if (childMNode.isDiscrete()) {
                newChild = new MDNode(childMNode);
            }
            newMFrag.setMNodes(newChild);
            
            List<String> keys = newMFrag.getRDBKeys();
            
            // Add Isa Context Nodes for the new MFrag from the current MFrag
            for (String key : keys) {
            	String originEntity = mTheory.rdb.getOriginFromKey(newMFrag.table, key);
                OVariable ov = new OVariable(f.getTableName(), key, originEntity);
        		new MIsANode(newMFrag, ov);
            }

            // Add parent nodes
            List<MFrag> parentMFrags = new ArrayList<MFrag>();
            for (String p2 : ps) {
                String mFragP = new StringUtil().getLeft(p2);
                String mNodeP = new StringUtil().getRight(p2);

                MFrag fp = mTheory.getMFrag(mFragP);
                MNode parentMNode = fp.getMNode(mNodeP);
                
                // Add parent MFrag into a list
                if (!parentMFrags.contains(fp)) {
                	parentMFrags.add(fp);
                }
                
                // Check the parent node which is the same type of the childMNode
                // Recursive MNode: land_state_Dry
                if (parentMNode.name.equalsIgnoreCase("land_state_Dry")) {
                	System.out.println(parentMNode.name);
                	
                	String newTableName = fp.getTableName()+"_P";
                	
   	                // create a joining sql for child and parent nodes
   	                sql += newTableName + "." + parentMNode.getAttributeName() + " as " + newTableName + "_" + mNodeP + ",\r\n";
                    
   	                List<String> keys2 = fp.getRDBKeys();
   	                List<OVariable> listOV = new ArrayList<OVariable>(); 
   	                
   	                // Add Isa Context Nodes for the new MFrag from the parent MFrags 
   	                OVariable ov = null;
   	                ov = new OVariable("land_state_P", "idRegion", "Region");
                    new MIsANode(newMFrag, ov);
                    listOV.add(ov);
                    
                    ov = new OVariable("land_state_P", "idPrevTime", "Time");
                    new MIsANode(newMFrag, ov);
                    listOV.add(ov);
   	                
   	                // Create a new parent input MNode from a current parent MNode
   	                // Both have different OVs
                    MNode ip = null; 
                    if (parentMNode.isContinuous()) {
   	                	ip = new MCNode(fp, parentMNode, listOV);
                    } else if (parentMNode.isDiscrete()) {
   	                	ip = new MDNode(fp, parentMNode, listOV);
                    } 
   	                
   	                // The new column name for the training csv data is set 
   	                ip.columnName = newTableName + "_" + mNodeP;
   	                
   	                // Change the MFrag of the recursive node ip to the new MFrag of the current node, because they are in the same MFrag. 
   	                ip.mFrag = newMFrag;
   	                
   	                newChild.setInputParents(ip);
                } else { // The parent node is not the same type of the childMNode. 
                	 
	                // create a joining sql for child and parent nodes
	             
		            sql += fp.getTableName() + "." + parentMNode.getAttributeName() + " as " + mNodeP + ",\r\n"; 
	                
	                List<String> keys2 = fp.getRDBKeys();
	                List<OVariable> listOV = new ArrayList<OVariable>(); 
	                
	                // Add Isa Context Nodes for the new MFrag from the parent MFrags
	                for (String key2 : keys2) {
	                    String originEntity = mTheory.rdb.getOriginFromKey(fp.getTableName(), key2);
	                    OVariable ov = new OVariable(fp.getTableName(), key2, originEntity);
	                    new MIsANode(newMFrag, ov);
	                    listOV.add(ov);
	                }
	                
	                // Create a new parent input MNode from a current parent MNode
	                // Both have different OVs
	                MNode ip = null;
	                if (parentMNode.isContinuous()) {
	                	ip = new MCNode(fp, parentMNode, listOV);
	                } else if (parentMNode.isDiscrete()) {
	                	ip = new MDNode(fp, parentMNode, listOV);
	                } 
	                
	                // The new column name for the training csv data is set 
   	                ip.columnName = ip.name;
	                
	                newChild.setInputParents(ip);
                }
            }
             
            sql = sql.substring(0, sql.length() - 3);
            sql = sql + "\r\nFROM\r\n";
            
            for (MFrag fp : parentMFrags){
	            if (fp.joiningSQL == null){
	            	if (fp.getTableName().equalsIgnoreCase("land_state")) { 
	                	String newTableName = fp.getTableName()+"_P";
	                	sqlFrom += fp.getTableName() +" as " + newTableName + ", ";
	            	} else 
	            		sqlFrom += fp.getTableName() + ", ";
	            } else { // is a TimedMFrag 
	                sqlFrom += " ( " +  fp.joiningSQL + " ) " + fp.getTableName();  
	                sqlFrom += ", ";
	            }
            }
            
            sqlFrom = sqlFrom.substring(0, sqlFrom.length() - 2);
            sql = sql + (String)sqlFrom + ", predecessor \r\n"; // The predecessor was hard-coded.
            sql += "WHERE \r\n"
            		+ "predecessor.idPrevTime = land_state_P.idTime && \r\n"
            		+ "predecessor.idTime = land_state.idTime && \r\n"
            		+ "environmental_factors.idRegion = land_state.idRegion && \r\n" 
            		+ "environmental_factors.idTime = predecessor.idPrevTime && \r\n" 
            		+ "region.idRegion = land_state.idRegion && \r\n"
            		+ "time.idTime = predecessor.idTime && \r\n" 
            		+ "plant_state.idTime = predecessor.idPrevTime && \r\n"
            		+ "plant_state.idRegion = land_state.idRegion && \r\n" 
            		+ "plant_state.idPlant = plant.idPlant \r\n" 
            		; 
          	
            // set a where clause
            newMFrag.joiningSQL = sql;
            
            // If there is no more node, then delete this MFrag 
            if (f.removeMNode(childMNode)) {
            	mTheory.removeMFrag(f);
            }
        } else { // If added parents are in the same MFrag
            for (String p3 : ps) {
                String mFragP = new StringUtil().getLeft(p3);
                String mNodeP = new StringUtil().getRight(p3);
                MNode parentMNode = f.getMNode(mNodeP);
                childMNode.setParents(parentMNode);
            }
        }
    }
    
    public void updateContexts() {
        for (MFrag f : mTheory.mfrags.keySet()) {
            ArrayList<MIsANode> removeList;
            Map<String, String> surviveMap;
            OVariable ov;
            OVariable ov2;
  
            if (f.name.equalsIgnoreCase("land_state_Dry")) {
            	MIsANode land_state_idTime = f.getIsaMNode("IsA(land_state_idTime, TIME)");
            	MIsANode land_state_P_idPrevTime = f.getIsaMNode("IsA(land_state_P_idPrevTime, TIME)");
            	MIsANode land_state_idRegion = f.getIsaMNode("IsA(land_state_idRegion, REGION)");
            	MIsANode plant_state_idPlant = f.getIsaMNode("IsA(plant_state_idPlant, PLANT)");
            	 
            	// For predecessor
            	MNode predecessor = mTheory.getMNode("predecessor.predecessor");
            	MNode inputpredecessor = new MNode(f, predecessor.name);
            	inputpredecessor.originalMNode = predecessor;
            	inputpredecessor.ovs.add(land_state_P_idPrevTime.ovs.get(0));
            	inputpredecessor.ovs.add(land_state_idTime.ovs.get(0)); 
            	f.arrayContextNodes.add(inputpredecessor); 
            	
            	// For land_state_Dry
            	MNode land_state_Dry = mTheory.getMNode("land_state_Dry.land_state_Dry");
            	land_state_Dry.ovs.clear();
            	land_state_Dry.ovs.add(land_state_idRegion.ovs.get(0));
            	land_state_Dry.ovs.add(land_state_idTime.ovs.get(0)); 
            	
            	MNode land_state_Dry_P = land_state_Dry.getInputParentNode("land_state_Dry");
            	land_state_Dry_P.ovs.clear();
            	land_state_Dry_P.ovs.add(land_state_idRegion.ovs.get(0));
            	land_state_Dry_P.ovs.add(land_state_P_idPrevTime.ovs.get(0)); 
            	 
            	MNode environmental_factors_Rain = land_state_Dry.getInputParentNode("environmental_factors_Rain");
            	environmental_factors_Rain.ovs.clear();
            	environmental_factors_Rain.ovs.add(land_state_idRegion.ovs.get(0));
            	environmental_factors_Rain.ovs.add(land_state_P_idPrevTime.ovs.get(0)); 
            	
            	MNode environmental_factors_Humidity = land_state_Dry.getInputParentNode("environmental_factors_Humidity");
            	environmental_factors_Humidity.ovs.clear();
            	environmental_factors_Humidity.ovs.add(land_state_idRegion.ovs.get(0));
            	environmental_factors_Humidity.ovs.add(land_state_P_idPrevTime.ovs.get(0));
            	
            	MNode environmental_factors_Wind = land_state_Dry.getInputParentNode("environmental_factors_Wind");
            	environmental_factors_Wind.ovs.clear();
            	environmental_factors_Wind.ovs.add(land_state_idRegion.ovs.get(0));
            	environmental_factors_Wind.ovs.add(land_state_P_idPrevTime.ovs.get(0));
            	
            	MNode environmental_factors_Light = land_state_Dry.getInputParentNode("environmental_factors_Light");
            	environmental_factors_Light.ovs.clear();
            	environmental_factors_Light.ovs.add(land_state_idRegion.ovs.get(0));
            	environmental_factors_Light.ovs.add(land_state_P_idPrevTime.ovs.get(0));
            	
            	MNode environmental_factors_Temperature = land_state_Dry.getInputParentNode("environmental_factors_Temperature");
            	environmental_factors_Temperature.ovs.clear();
            	environmental_factors_Temperature.ovs.add(land_state_idRegion.ovs.get(0));
            	environmental_factors_Temperature.ovs.add(land_state_P_idPrevTime.ovs.get(0));
            	 
            	MNode plant_PlantType = land_state_Dry.getInputParentNode("plant_PlantType");
            	plant_PlantType.ovs.clear();
            	plant_PlantType.ovs.add(plant_state_idPlant.ovs.get(0)); 
            	
              	MNode plant_state_Health = land_state_Dry.getInputParentNode("plant_state_Health");
              	plant_state_Health.ovs.clear();
              	plant_state_Health.ovs.add(plant_state_idPlant.ovs.get(0)); 
              	plant_state_Health.ovs.add(land_state_idRegion.ovs.get(0));
              	plant_state_Health.ovs.add(land_state_P_idPrevTime.ovs.get(0));
            	  
              	MNode region_LocationType = land_state_Dry.getInputParentNode("region_LocationType");
              	region_LocationType.ovs.clear(); 
              	region_LocationType.ovs.add(land_state_idRegion.ovs.get(0)); 
            	
              	MNode time_Season = land_state_Dry.getInputParentNode("time_Season");
              	time_Season.ovs.clear(); 
              	time_Season.ovs.add(land_state_P_idPrevTime.ovs.get(0));
              	
             	MNode time_DayTime = land_state_Dry.getInputParentNode("time_DayTime");
             	time_DayTime.ovs.clear(); 
             	time_DayTime.ovs.add(land_state_P_idPrevTime.ovs.get(0));
              	   
            	f.removeAllUnnecessaryIsANodes();
            	
            	System.out.println(f);
               
            } else             
            if (f.joiningSQL != null) { 
            	// Step 1. Add "where" clause to the SQL script of this MFrag
                // e.g.,)
                // [F: land_state_Dry
//        				[C: IsA(land_state_idRegion, REGION)]
//        				[C: IsA(land_state_idTime, TIME)]
//        				[C: IsA(environmental_factors_idRegion, REGION)]
//        				[C: IsA(environmental_factors_idTime, TIME)]
//        				[C: IsA(land_state_P_idRegion, REGION)]
//        				[C: IsA(land_state_P_idPrevTime, TIME)]
//        				[R: land_state_Dry(land_state_idRegion, land_state_idTime)
//        					[IP: environmental_factors_Rain(land_state_idRegion, land_state_idTime), land_state_Dry(land_state_idRegion, land_state_idTime)]		]
//        			]
                //
                List<String> dubList = new ArrayList<String>();  
                for (int i = 0; i < f.arrayIsaContextNodes.size(); i++) {
                	MIsANode isa = f.arrayIsaContextNodes.get(i);
                	ov = (OVariable)isa.ovs.get(0);
                	MFrag orgF = mTheory.getMFrag(ov.originMFrag);
                	
                	// timedPrimaryKey will be skipped.
         			if (orgF != null &&orgF.timedPrimaryKey != null && orgF.timedPrimaryKey.equalsIgnoreCase(ov.originKey)){
        				continue;
        			}
        			
                	for (int j = 0; j < i+1; j++) {
                		MIsANode isa2 = f.arrayIsaContextNodes.get(j);
                    	ov2 = (OVariable)isa2.ovs.get(0);
                    	if (!ov.originMFrag.equalsIgnoreCase(ov2.originMFrag)){
                    		if (ov.originKey.equalsIgnoreCase(ov2.originKey)){ 
	                    		String curKey = ov.originMFrag + "." + ov.originKey;
	                    		String otherKey = ov2.originMFrag + "." + ov2.originKey;
	                    		String wh = curKey + " = " + otherKey;
	                    		
	                    		if(wh.equalsIgnoreCase("fm_pass.PASS_NO = rm_pass.PASS_NO")){
	                    			System.out.println();
	                    		} 
                			}
                    	}
                    }
                } 
                 
                // Step 2. remove redundant OVs, if they are same OVs
	            // e.g.) IsA(t1, TIME), IsA(t2, TIME)
	            // => IsA(t1, TIME)
	            // Also, resident nodes using these OVs changes to not having redundant OVs
                surviveMap = new HashMap<String, String>();
                removeList = new ArrayList<MIsANode>();
                
                for (MIsANode isa : f.arrayIsaContextNodes) {
                    ov = (OVariable)isa.ovs.get(0); 
                    if (surviveMap.containsKey(ov.entityType)) {
                        removeList.add(isa);
                        continue;
                    }
                    surviveMap.put(ov.entityType, ov.name);
                }
                
                mTheory.updateParentNodesOVs(f, removeList, surviveMap);
                
                if (removeList.size() > 0) {
                    f.arrayIsaContextNodes.removeAll(removeList);
                } 
                
            }  
        }
    }
    
    public void addParents(String c, List<String> ps) {
        String mFrag = new StringUtil().getLeft(c);
        String mNode = new StringUtil().getRight(c);
        String combParents = "";
        boolean bOtherMFrag = false;
          
        for (String p : ps) {
            String mFragP = new StringUtil().getLeft(p); 
            if (!mFrag.equalsIgnoreCase(mFragP) && !bOtherMFrag) { 
                bOtherMFrag = true;
            } 
        }
        
        combParents = mNode;
        
        MFrag f = mTheory.getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);  
        
        if (bOtherMFrag) {
            String sql = "SELECT\r\n";
            String sqlFrom = "";
            
            sql = sql + f.getTableName() + "." + childMNode.getAttributeName() + " as " + mNode + ",\r\n";
            sqlFrom = sqlFrom + f.getTableName() + ", ";
            
            MFrag newMFrag = new MFrag(mTheory, combParents);
            newMFrag.setTableName(f.getTableName());
            
            MNode newChild = null;
            if (childMNode.isContinuous()) {
                newChild = new MCNode(childMNode);
            } else if (childMNode.isDiscrete()) {
                newChild = new MDNode(childMNode);
            }
            newMFrag.setMNodes(newChild);
            
            List<String> keys = newMFrag.getRDBKeys();
            
            // Add Isa Context Nodes for the new MFrag from the current MFrag
            for (String key : keys) {
            	String originEntity = mTheory.rdb.getOriginFromKey(newMFrag.table, key);
                OVariable ov = new OVariable(f.getTableName(), key, originEntity);
        		new MIsANode(newMFrag, ov);
            }

            // Add parent nodes
            List<MFrag> parentMFrags = new ArrayList<MFrag>();
            for (String p2 : ps) {
                String mFragP = new StringUtil().getLeft(p2);
                String mNodeP = new StringUtil().getRight(p2);

                MFrag fp = mTheory.getMFrag(mFragP);
                MNode parentMNode = fp.getMNode(mNodeP);
                if (!parentMFrags.contains(fp)) {
                	parentMFrags.add(fp);
                }

                // create a joining sql for child and parent nodes
                if (!fp.isTimedMFrag()){
	                sql += fp.getTableName() + "." + parentMNode.getAttributeName() + " as " + mNodeP + ",\r\n";
//	                sqlFrom += fp.getTableName() + ", ";
                } else { // is a TimedMFrag 
                	sql += fp.getTableName() + "." + mNodeP + " as " + mNodeP + ",\r\n";
//	                sqlFrom += " ( " +  fp.joiningSQL + " ) " + fp.getTableName();  
//	                sqlFrom += ", ";
                }
                
                List<String> keys2 = fp.getRDBKeys();
                List<OVariable> listOV = new ArrayList<OVariable>(); 
                
                // Add Isa Context Nodes for the new MFrag from the parent MFrags
                for (String key2 : keys2) {
                    String originEntity = mTheory.rdb.getOriginFromKey(fp.getTableName(), key2);
                    OVariable ov = new OVariable(fp.getTableName(), key2, originEntity);
                    new MIsANode(newMFrag, ov);
                    listOV.add(ov);
                }
                
                // Create a new parent input MNode from a current parent MNode
                // Both have different OVs
                MNode ip = null;
                if (parentMNode.isContinuous()) {
                	ip = new MCNode(fp, parentMNode, listOV);
                } else if (parentMNode.isDiscrete()) {
                	ip = new MDNode(fp, parentMNode, listOV);
                }
                
                newChild.setInputParents(ip);
            }
             
            sql = sql.substring(0, sql.length() - 3);
            sql = sql + "\r\nFROM\r\n";
            
            for (MFrag fp : parentMFrags){
	            if (fp.joiningSQL == null){
	                sqlFrom += fp.getTableName() + ", ";
	            } else { // is a TimedMFrag 
	                sqlFrom += " ( " +  fp.joiningSQL + " ) " + fp.getTableName();  
	                sqlFrom += ", ";
	            }
            }
            
            sqlFrom = sqlFrom.substring(0, sqlFrom.length() - 2);
            sql = sql + (String)sqlFrom + "\r\n";
            
            // set a where clause
            
            newMFrag.joiningSQL = sql;
            
            // If there is no more node, then delete this MFrag 
            if (f.removeMNode(childMNode)) {
            	mTheory.removeMFrag(f);
            }
        } else { // If added parents are in the same MFrag
            for (String p3 : ps) {
                String mFragP = new StringUtil().getLeft(p3);
                String mNodeP = new StringUtil().getRight(p3);
                MNode parentMNode = f.getMNode(mNodeP);
                childMNode.setParents(parentMNode);
            }
        }
    }
}

