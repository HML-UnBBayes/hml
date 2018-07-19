package hmlp_tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.gmu.seor.prognos.unbbayesplugin.continuous.prs.ContinuousResidentNode;
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MFrag.MFrag.MFragType;
import mebn_rm.MEBN.MNode.MIsANode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.MEBN.MTheory.OVariable;
import unbbayes.controller.mebn.FormulaTreeController;
import unbbayes.controller.mebn.MEBNController; 
import unbbayes.gui.mebn.DescriptionPane;
import unbbayes.gui.mebn.MEBNNetworkWindow;
import unbbayes.gui.mebn.extension.editor.IMEBNEditionPanelBuilder;
import unbbayes.prs.Edge;
import unbbayes.prs.Node;
import unbbayes.prs.mebn.ContextNode;
import unbbayes.prs.mebn.IResidentNode;
import unbbayes.prs.mebn.InputNode;
import unbbayes.prs.mebn.MultiEntityBayesianNetwork;
import unbbayes.prs.mebn.OrdinaryVariable;
import unbbayes.prs.mebn.ResidentNode;
import unbbayes.prs.mebn.ResidentNodePointer;
import unbbayes.prs.mebn.context.EnumSubType;
import unbbayes.prs.mebn.context.EnumType;
import unbbayes.prs.mebn.context.NodeFormulaTree;
import unbbayes.prs.mebn.entity.ObjectEntity;
import unbbayes.prs.mebn.entity.ObjectEntityContainer;
import unbbayes.prs.mebn.exception.MFragDoesNotExistException;

public class OpenMEBNeditor {

	public OpenMEBNeditor() {

	}
	
	public MEBNNetworkWindow run(MTheory mTheory) {
		// Creating UnBBayes MEBN
		MultiEntityBayesianNetwork mebn = new MultiEntityBayesianNetwork(mTheory.name);
		
		// Creating UnBBayes MEBN Window
		MEBNNetworkWindow resultNet = new MEBNNetworkWindow(mebn);
		resultNet.setModuleName("MEBN");
		resultNet.setTitle(mTheory.name);
		
		// Mappers to track OV and Resident Nodes
		HashMap<String, OrdinaryVariable> mapOVariable = new HashMap<String, OrdinaryVariable>();
		HashMap<String, ResidentNode> mapResNode = new HashMap<String, ResidentNode>();

		// Creating MEBN Controller
		MEBNController mebnController = resultNet.getMebnEditionPane().getMebnController();
		
		
		// Starting conversion from HMLP-MEBN to UnBBayes-MEBN
		try {
			// Entities
			ObjectEntityContainer mebnObjEntCont = mebn.getObjectEntityContainer(); 

			//1. Add resident nodes
			for (MFrag m : mTheory.mfrags.keySet()) {
				int NodePosition = 1;
				
				// Mapper for Edges - to be added in the end of each MFrag conversion
				HashMap<String, String> edgeNodes = new HashMap<String, String>();
				
	            // MFrag
	            mebnController.insertDomainMFrag();
	            mebnController.renameMFrag(mebnController.getCurrentMFrag(), m.name);
				unbbayes.prs.mebn.MFrag mfrag = mebnController.getCurrentMFrag();
				
				for (MIsANode c : m.arrayIsaContextNodes){
					mebn.setCurrentMFrag(mfrag);
		    		
					// Entity of isA context nodes
					String entityTypeName = ((OVariable) c.ovs.get(0)).entityType;
					ObjectEntity objEntMebn = mebnObjEntCont.createObjectEntity(entityTypeName, (ObjectEntity) mebnObjEntCont.getRootObjectEntity());
					mebn.getNamesUsed().add(entityTypeName);
					mebnController.getMebnEditionPane().getToolBarOVariable().updateListOfTypes();
					mebnController.getMebnEditionPane().firePropertyChange(IMEBNEditionPanelBuilder.MEBN_EDITION_PANEL_CHANGE_PROPERTY, true, false);
					
			        // OV and Context Nodes
		    		OrdinaryVariable ov = mebnController.insertOrdinaryVariable(1, NodePosition);
		    		NodePosition = NodePosition + 60;
		    		ov.setDescription(((OVariable) c.ovs.get(0)).name);
		    		ov.setName(((OVariable) c.ovs.get(0)).name);
		    		ov.setValueType(objEntMebn.getType());
		    		ov.setSize(400, 60);
		    		ov.updateLabel();
		    		mapOVariable.put(ov.getName(), ov);
				}
				
				if (m.mFragType == MFragType.COMMON) {
					// Resident Nodes
					for (MNode r : m.arrayResidentNodes) {
						System.out.println("Converting the resident " + r.name);
						// Current Resident Node - either discrete or continuous resident node
						Node currentNode = null;
						
		                // DISCRETE Resident Node
	                	if(r.isDiscrete()){
	                		// DISCRETE
	                		ResidentNode rNode = (ResidentNode) mebnController.insertResidentNode(1, NodePosition);
	                		NodePosition = NodePosition + 60;
	                		rNode.setSize(400, 60);
	                		rNode.setName(r.name);
	    					rNode.setDescription(r.name);
	    					currentNode = rNode;
	    					
	    	                // Argument
	    	                for(OVariable o : r.ovs){ 
	    	                	rNode.addArgument(mapOVariable.get(o.name), true);
	    	                }
	
	    	                if (r.name.equalsIgnoreCase("land_state_Dry")) {
	    	                	System.out.println(r.name); 
	    	                } 
	    	                
	    	                // Table Function
	    					CLD l = r.getBestCLD();
		                    List<String> inclusions = new ArrayList<String>();
		                    inclusions.add("CLD");
		                    String lcld = l.toString(inclusions);
		                    lcld = lcld.substring(4, lcld.length()-3);
		                    
		                    // Use square brackets
		                    //lcld = "[" + lcld + "]";
		                   	                    
		                    rNode.setTableFunction(lcld);
		                
		                    // Add states
		                    for(String catState : r.getCategories()){
		                    	mebnController.addPossibleValue(rNode, catState);
		                    	rNode.setTypeOfStates(IResidentNode.CATEGORY_RV_STATES);
		                    }
		                    
		                    // Add to Map
				    		mapResNode.put(rNode.getName(), rNode);
	                	}
	                	// CONTINUOUS Resident Node
	                	else {
	                		// CONTINUOUS
	    					ContinuousResidentNode cRnode = new ContinuousResidentNode(r.name, mfrag);
	    					cRnode.setMediator(mebnController);
	    					currentNode = cRnode;
	    					
	    					// Argument
	    	                for(OVariable o : r.ovs){
	    	                	cRnode.addArgument(mapOVariable.get(o.name), true);
	    	                }
	    	                
	    	                // Table Function
	    					CLD l = r.getBestCLD();
		                    List<String> inclusions = new ArrayList<String>();
		                    inclusions.add("CLD");
		                    String lcld = l.toString(inclusions);
		                    lcld = lcld.substring(4, lcld.length()-3);
	
		                    // Use square brackets
		                    //lcld = "[" + lcld + "]";
		                    
		                    
		                    cRnode.setTableFunction(lcld);
		                    
		                    // Adding to controller
							mebn.getNamesUsed().add(r.name); 
							cRnode.setDescription(cRnode.getName());
							cRnode.setPosition(1, NodePosition);
							NodePosition = NodePosition + 60;
							cRnode.setSize(400, 60);
							mfrag.addResidentNode(cRnode);
							mebnController.setActiveResidentNode(cRnode);
							
							// Updating panels
							mebnController.getMebnEditionPane().setEditArgumentsTabActive(cRnode);
							mebnController.getMebnEditionPane().setResidentNodeTabActive(cRnode);
							mebnController.getMebnEditionPane().setArgumentTabActive();
							mebnController.getMebnEditionPane().setResidentBarActive();
							mebnController.getMebnEditionPane().setTxtNameResident(((ResidentNode)cRnode).getName());
							mebnController.getMebnEditionPane().setDescriptionText(cRnode.getDescription(), DescriptionPane.DESCRIPTION_PANE_RESIDENT); 
							
							mebnController.getMebnEditionPane().getMTheoryTree().addNode(mfrag, cRnode); 
											
							// Add to Map
				    		mapResNode.put(cRnode.getName(), cRnode);
	                	} 
		            }  
				} else if (m.mFragType == MFragType.REFERENCE) {
					// DISCRETE Resident Nodes
					for (MNode r : m.arrayResidentNodes) {
						System.out.println("Converting the resident " + r.name);  
                		ResidentNode rNode = (ResidentNode) mebnController.insertResidentNode(1, NodePosition);
                		NodePosition = NodePosition + 60;
                		rNode.setSize(400, 60);
                		rNode.setName(r.name);
    					rNode.setDescription(r.name); 
    					
    	                // Argument
    	                for(OVariable o : r.ovs){  
    	                	rNode.addArgument(mapOVariable.get(o.name), true);
    	                }

    	                if (r.name.equalsIgnoreCase("predecessor")) {
    	                	System.out.println(r.name); 
    	                } 
    	                  
	                    // Add states 
    	                mebnController.addBooleanAsPossibleValue(rNode);
    	                rNode.setTypeOfStates(IResidentNode.BOOLEAN_RV_STATES);
				        
	                    // Add to Map
			    		mapResNode.put(rNode.getName(), rNode); 
					}
				}
	        }
			
			//2. Add context nodes 
			int index = 1;
			for (MFrag m : mTheory.mfrags.keySet()) {
				int NodePosition = 1;
				
				MultiEntityBayesianNetwork curMEBN = (MultiEntityBayesianNetwork)mebnController.getNetwork();
				unbbayes.prs.mebn.MFrag mfrag = curMEBN.getMFragByName(m.name);
				 
				for (MNode c : m.arrayContextNodes){ 
					String name = "contextNodeName" + (index++);
					
					ResidentNode residentNode = mapResNode.get(c.name); 
					ContextNode cnode = new ContextNode(name, mfrag);
					mebn.getNamesUsed().add(name); 
					NodePosition = NodePosition + 60;
					cnode.setPosition(200, NodePosition);
					cnode.setSize(400, 60);
					cnode.setDescription(c.name);
					mfrag.addContextNode(cnode);
					
					// Create a new formula tree.  
					FormulaTreeController formulaTreeController = new FormulaTreeController(mebnController, cnode, null);	
					NodeFormulaTree rootFormula = new NodeFormulaTree("formula", EnumType.FORMULA, EnumSubType.NOTHING, null);
					cnode.setFormulaTree(rootFormula);
					formulaTreeController.getFormulaTree().setNodeFormulaActive(rootFormula);
					formulaTreeController.addNode(residentNode); 
					
					ResidentNodePointer residentNodePointer = null;
					residentNodePointer = (ResidentNodePointer)formulaTreeController.getFormulaTree().getNodeFormulaActive().getNodeVariable();
	
					for (int j = 0; j < c.ovs.size(); j++) {
						OVariable ov = c.ovs.get(j);
						OrdinaryVariable o = mfrag.getOrdinaryVariableByName(ov.name);
						residentNodePointer.addOrdinaryVariable(o, j);
					}  
				}
			}
			
			//3. Add input nodes and link parents 
			for (MFrag m : mTheory.mfrags.keySet()) {
				int NodePosition = 1;
				
				// Mapper for Edges - to be added in the end of each MFrag conversion
				HashMap<String, String> edgeNodes = new HashMap<String, String>();
				
				MultiEntityBayesianNetwork curMEBN = (MultiEntityBayesianNetwork)mebnController.getNetwork();
				unbbayes.prs.mebn.MFrag mfrag = curMEBN.getMFragByName(m.name);
				 
				// Resident Nodes
				for (MNode r : m.arrayResidentNodes) {  
					// Input Nodes - Pointing to current Resident Node
					ResidentNode currentNode = mapResNode.get(r.name);
					NodePosition = (int) currentNode.getPosition().y;
					
					for (MNode i : r.inputParentMNodes) {
	                    
	                    // Create Input Node
						ResidentNode crNodeOfInput = mapResNode.get(i.name);
	                    InputNode iNode = (InputNode) mebnController.insertInputNode(1, NodePosition);
	                    NodePosition = NodePosition + 60;
	                    mebnController.setInputInstanceOf(iNode, crNodeOfInput);
	                    // For input nodes, the following two functions are not necessary.
	                    //iNode.setName(crNodeOfInput.getName());
	                    //iNode.setDescription(crNodeOfInput.getDescription());
	                    iNode.setSize(400, 60);
	                    
	                    // Add arguments
	                    int argNr = 0;
	                    for (String argName: i.toStringOVs().split(",")) {
	                    	iNode.getResidentNodePointer().addOrdinaryVariable(mapOVariable.get(argName.trim()), argNr);
	                    	argNr++;
	                     }
	                    
	                    // Link Input Node as parent of Current Node
	                    Edge auxEdge = new Edge(iNode, currentNode);
	                    mebnController.insertEdge(auxEdge);
	                    mebnController.setCurrentMFrag(mfrag);
	                }
					
					// Edges for current Resident Node as child
					for (MNode er : r.parentMNodes) {
						// Store nodes to include edges after all nodes in current MFrag are already mapped
						edgeNodes.put(er.name, currentNode.getName());
	                }
	            }
				
				// Effectively include the edges for all nodes in current MFrag
				for (Map.Entry<String, String> entry : edgeNodes.entrySet()) {
				    Node parentNode = mapResNode.get(entry.getKey());
				    Node childNode = mapResNode.get(entry.getValue());
				    
				    Edge auxEdge = new Edge(parentNode, childNode);
                    mebnController.insertEdge(auxEdge);
                    mebnController.setCurrentMFrag(mfrag);
				}
	        }
			 
			// Update pane and put MTheory view as final active
			mebnController.getMebnEditionPane().getNetworkWindow().getGraphPane().update();
			mebnController.getMebnEditionPane().setMTheoryTreeActive();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultNet;
	}

}
