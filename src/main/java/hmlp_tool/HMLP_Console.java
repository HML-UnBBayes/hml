package hmlp_tool;

import java.awt.BorderLayout; 
import java.awt.Frame; 
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup; 
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel; 
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane; 
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import edu.gmu.seor.prognos.unbbayesplugin.continuous.prs.ContinuousResidentNode;
import hmlp_tool.panel.TreePanel_Container;
import hmlp_tool.panel.TreePanel_Left;
import hmlp_tool.panel.TreePanel_Right;
import mebn_ln.core.MTheory_Learning;
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MIsANode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
import unbbayes.controller.mebn.MEBNController;
import unbbayes.gui.InternalErrorDialog;
import unbbayes.gui.UnBBayesFrame;
import unbbayes.gui.mebn.DescriptionPane;
import unbbayes.gui.mebn.MEBNNetworkWindow;
import unbbayes.gui.mebn.extension.editor.IMEBNEditionPanelBuilder;
import unbbayes.prs.Edge;
import unbbayes.prs.Node;
import unbbayes.prs.mebn.Argument;
import unbbayes.prs.mebn.IResidentNode;
import unbbayes.prs.mebn.InputNode;
import unbbayes.prs.mebn.MultiEntityBayesianNetwork;
import unbbayes.prs.mebn.OrdinaryVariable;
import unbbayes.prs.mebn.ResidentNode;
import unbbayes.prs.mebn.ResidentNodePointer;
import unbbayes.prs.mebn.entity.ObjectEntity;
import unbbayes.prs.mebn.entity.ObjectEntityContainer;
import unbbayes.prs.mebn.exception.OVDontIsOfTypeExpected;
import unbbayes.util.Debug;
import unbbayes.util.extension.UnBBayesModule; 

public class HMLP_Console extends GeneralDialog {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
  
	
	// Added for UnBBayes Plugin [START]
	/** If this frame's content pane is included to another frame, this attribute will be a reference to it */
	private JFrame upperFrame = null;

	private UnBBayesFrame unbbayesFrame = null;
	
	public UnBBayesFrame getUnbbayesFrame() {
		return unbbayesFrame;
	}

	public void setUnbbayesFrame(UnBBayesFrame unbbayesFrame) {
		this.unbbayesFrame = unbbayesFrame;
	}

	public void setUpperFrame(JFrame upperFrame) {
		this.upperFrame = upperFrame;	
	}
	// Added for UnBBayes Plugin [END]
	
	// Sharing data
	public String selectedDB = "";
	public MTheory mTheory = null; 
	
	public enum windowMode {
		CONNECT_DB, SELECT_DB, EDIT_DB, ADD_PARENTS, JOIN_RELATIONS, ADD_CLD, LEARNING, EVALUATION
	};

	public windowMode wMode = windowMode.SELECT_DB;
	public HMLP_TextPane textInputArea;
	static public HMLP_TextPane textOutputArea;
	 
	JTextPane textIn = null;
	JTextPane textOut = null;
	JPanel btns = null;
	TreePanel_Container treeContainer = null; 
	TreePanel_Left leftTree;
	TreePanel_Right rightTree;
	JPanel center = null; 
	
	Action setLoad;
	Action setSave;
	Action setbtn1;
	Action setbtn2;
	Action setbtn3;
	Action setbtn4;
	Action setbtn5;
	Action setbtn6;	
	Action setbtn7;
	Action setExit;
	
	FileChooserDialog ufd = new FileChooserDialog();
	
	static int widthFrame = 1000;
	static int heightFrame = 800;

	static HMLP_Console CPSLD = null;
 
	static public HMLP_Console This() {
		if (CPSLD == null) {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(false);

			HMLP_Console d = new HMLP_Console(frame, "");
			d.init(windowMode.SELECT_DB);
			d.setVisible(true);
		}

		return CPSLD; 
	}

	public HMLP_Console(Frame owner, String str) {
		super(owner, str, widthFrame, heightFrame);
		CPSLD = this;  
		
		textIn = createTextIn();
		textOut = createTextOut();
 		//btns = createButtonPane();
 		center = createPaneV(textOut, textIn);
 		
 		leftTree = new TreePanel_Left(this); 
 		rightTree = new TreePanel_Right(this, leftTree); 
 		treeContainer = new TreePanel_Container(this, leftTree, rightTree);
 		    
		getContentPane().add(treeContainer, BorderLayout.WEST); 
		getContentPane().add(center, BorderLayout.CENTER);
		//getContentPane().add(btns, BorderLayout.SOUTH);
		
	}
	  
	public void init(windowMode mode) {
		wMode = mode;
		
		if (wMode == windowMode.CONNECT_DB) { 
			treeContainer.setVisible(false);
			showConnectDB();
		} else if (wMode == windowMode.SELECT_DB) { 
		} else if (wMode == windowMode.EDIT_DB) { 
		} else if (wMode == windowMode.ADD_PARENTS) { 
			if (mTheory == null)
				mTheory =  new RM_To_MEBN(RDB.This()).run();
		} else if (wMode == windowMode.JOIN_RELATIONS) { 
		} else if (wMode == windowMode.ADD_CLD) { 
			mTheory.updateContexts();
			showMTheory();
		} else if (wMode == windowMode.LEARNING) { 
			mTheory.updateCLDs();
			MRoot mroot = new MRoot();
			mroot.setMTheories(mTheory);  
			new MTheory_Learning().run(mroot);  
			showMTheory_CLD();
			
			// Starting UnBBayes MEBN window
			System.out.print("\n\n################################ Opening MEBN window...\n"); 
			
			// Show MEBN window 
			openMEBNeditor(mTheory);
//			UnBBayesModule module = new OpenMEBNeditor().run(mTheory);
//			this.getUnbbayesFrame().addWindow(module);
		} else if (wMode == windowMode.EVALUATION) { 
			// Starting UnBBayes MEBN window
			System.out.print("\n\n################################ Opening MEBN window...\n"); 
			
			// Show MEBN window 
			openMEBNeditor(mTheory);
//			UnBBayesModule module = new OpenMEBNeditor().run(mTheory);
//			this.getUnbbayesFrame().addWindow(module);
		}  
 		   
 		treeContainer.init();	
 		
		this.invalidate();
		//this.setVisible(true); // Comment added for UnBBayes plugin
		this.repaint();
	} 
	

	/*
	 * A converter for Script MEBN to Graph MEBN 
	 */
	public void openMEBNeditor(MTheory mTheory) {
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
				
				// Resident Nodes
				for (MNode r : m.arrayResidentNodes) {
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

    	                // Table Function
    					CLD l = r.getBestCLD();
	                    List<String> inclusions = new ArrayList<String>();
	                    inclusions.add("CLD");
	                    String lcld = l.toString(inclusions);
	                    lcld = lcld.substring(4, lcld.length()-3);
	                    
	                    // Use square brackets
//	                    lcld = "[" + lcld + "]";
	                   	                    
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
	                    lcld = "[" + lcld + "]";
	                    
	                    
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
	        }
			 
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
		 
		// Show MEBN window
		UnBBayesModule module = resultNet; //wb.buildUnBBayesModule();
		this.getUnbbayesFrame().addWindow(module);
	}
	 
    public void keyUpdated (KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    		leftTree.username = textInputArea.getConntents("Username: ");
    		leftTree.password = textInputArea.getConntents("Password: ");
    		
    		if (leftTree.username.isEmpty()) {
				leftTree.username = "root";
    			leftTree.password = "jesus";
    		}
    		
    		textOutputArea.clear();
    		textInputArea.clear();
    		
    		init(windowMode.SELECT_DB);
    	}  
    }
	
	public void showConnectDB() { 
		insertLast("**********************************************************************************************************");
		insertLast("To connect a DB, insert a username and a password for the DB.");
		
		textInputArea.insertTextOut("Username: ", 1);
		textInputArea.insertTextOut("Password: ", 2);
		 
	} 
	  
	public void showMTheory() { 
		insertLast("**********************************************************************************************************");
		insertLast(mTheory.toString("MFrag", "MNode"));
	} 
	
	public void showMTheory_CLD() { 
		insertLast("**********************************************************************************************************");
		insertLast(mTheory.toString("MFrag", "MNode", "CLD"));
	} 
	
	public void setScript(String str) {
		textInputArea.setText(str);
	}

	public JTextPane createTextIn() {
		JTextPane textPane = new JTextPane();
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
		textInputArea = new HMLP_TextPane(this);
		textInputArea.setEditable(true);
		textPane.add(new JScrollPane(textInputArea));
		return textPane;
	}

	public JTextPane createTextOut() {
		JTextPane textPane = new JTextPane();
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
		textOutputArea = new HMLP_TextPane(this);
		textOutputArea.setEditable(false);
		textPane.add(new JScrollPane(textOutputArea));
		return textPane;
	}

	public void insertTextOut(String str) {
		textOutputArea.insertTextOut(str);
	}

	public void insertTextOut(String str, int i) {
		textOutputArea.insertTextOut(str, i);
	}

	public void insertLast(String str) {
		textOutputArea.insertLast(str);
	}

	public void clearTextOut() {
		textOutputArea.clear();
	}

	public JPanel createButtonPane() {
		ButtonGroup group = new ButtonGroup();

		// Text Button Panel
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new BoxLayout(ButtonPanel, BoxLayout.X_AXIS));
		ButtonPanel.setBorder(border5);
 
		// Buttons
		JPanel p2 = createHorizontalPanel(false);
		ButtonPanel.add(p2);
		p2.setBorder(
				new CompoundBorder(new TitledBorder(null, "File", TitledBorder.LEFT, TitledBorder.TOP), border5));

		// Add Button
		JButton btn;
		btn = createLoadButton();
		p2.add(btn);
		group.add(btn);
		p2.add(Box.createRigidArea(VGAP5));

		btn = createSaveButton();
		p2.add(btn);
		group.add(btn);
		p2.add(Box.createRigidArea(VGAP5));
	 
		// Add Button
		JButton btn2; 

		// Window area
		JPanel p4 = createHorizontalPanel(false);
		ButtonPanel.add(p4);
		p4.setBorder(
				new CompoundBorder(new TitledBorder(null, "Menu", TitledBorder.LEFT, TitledBorder.TOP), border5));
		
		btn2 = createButton1(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton2(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton3(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton4(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton5(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton6(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton7(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createExitButton(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));

		return ButtonPanel;
	}
  
	// Button [Load, Save]
	public JButton createLoadButton() {
		setLoad = new AbstractAction("Load") {
			public void actionPerformed(ActionEvent e) {
				String strFile;
				strFile = ufd.loadFile(new Frame(), "Open...", ".\\", "*.sbn");
				if (strFile != null) {
					StringBuffer contents = new StringBuffer();
					BufferedReader reader = null;
					try {
						File file = new File(strFile);
						reader = new BufferedReader(new FileReader(file));
						if (file == null || reader == null)
							return;
						String text = null;

						// repeat until all lines is read
						while ((text = reader.readLine()) != null) {
							contents.append(text).append(System.getProperty("line.separator"));
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					} finally {
						try {
							if (reader != null)
								reader.close();
						} catch (IOException e3) {
							e3.printStackTrace();
						}
					}
 
				}
			}
		};
		return createButton(setLoad);
	}

	public JButton createSaveButton() {
		setSave = new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				String strFile;
				strFile = ufd.loadFile(new Frame(), "Save...", ".\\", "*.sbn");
				if (strFile != null) {
					File file = new File(strFile);
					Writer writer = null;

					try {
						writer = new BufferedWriter(new FileWriter(file));
						writer.write(textInputArea.getText());
						JOptionPane.showMessageDialog(null, "The SBN file was saved!");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					} finally {
						try {
							if (writer != null)
								writer.close();
						} catch (IOException e3) {
							e3.printStackTrace();
						}
					}

				}
			}
		};
		return createButton(setSave);
	} 
	
	// Buttons [Start, Cancel] 
	public JButton createExitButton() {
		setExit = new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		return createButton(setExit);
	}
	
	public JButton createButton1() {
		setbtn1 = new AbstractAction("Select DB") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.SELECT_DB);
			}
		};
		return createButton(setbtn1);
	}
	
	public JButton createButton2() {
		setbtn2 = new AbstractAction("Create World Model") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.EDIT_DB);
			}
		};
		return createButton(setbtn2);
	}
	
	public JButton createButton3() {
		setbtn3 = new AbstractAction("Select Parents") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.ADD_PARENTS);
			}
		};
		return createButton(setbtn3);
	}
	  	
	public JButton createButton4() {
		setbtn4 = new AbstractAction("Select Context") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.JOIN_RELATIONS);
			}
		};
		return createButton(setbtn4);
	}
	
	public JButton createButton5() {
		setbtn5 = new AbstractAction("Select CLD") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.ADD_CLD);
			}
		};
		return createButton(setbtn5);
	}
	
	public JButton createButton6() {
		setbtn6 = new AbstractAction("Learning") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.LEARNING);
			}
		};
		return createButton(setbtn6);
	}
	
	public JButton createButton7() {
		setbtn7 = new AbstractAction("Evaluation") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.EVALUATION);
			}
		};
		return createButton(setbtn7);
	}

	/**
	 * TextArea Pane
	 */
	class TextOutArea extends JTextArea {
		public TextOutArea() {
			super(null, 0, 0);
			setEditable(false);
			setText("");
		}

		public float getAlignmentX() {
			return LEFT_ALIGNMENT;
		}

		public float getAlignmentY() {
			return TOP_ALIGNMENT;
		}
	}

	public static void main(String[] args) {
		HMLP_Console d = new HMLP_Console(null, "HMLP Tool V.1");
		d.init(windowMode.CONNECT_DB);

		// frame.getContentPane().add(d);
		// d.insertTextOut("1. test");
		// d.insertTextOut("========", 3);
		d.setVisible(true);
	}

}
