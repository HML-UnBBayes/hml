/**
 * 
 */
package unbbayes.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import hmlp_tool.HMLP_Console;
import hmlp_tool.HMLP_Console.windowMode;
import unbbayes.controller.mebn.MEBNController;
import unbbayes.gui.mebn.MEBNNetworkWindow;
import unbbayes.io.BaseIO;
import unbbayes.io.mebn.UbfIO;
import unbbayes.io.mebn.exceptions.IOMebnException;
import unbbayes.prs.Graph;
import unbbayes.prs.mebn.MultiEntityBayesianNetwork;
import unbbayes.util.extension.UnBBayesModule;

/**
 * HMLP plugin for UnBBayes
 * @author Ricardo Carvalho
 *
 */
public class HMLPNetworkWindow extends UnBBayesModule {

	private HMLP_Console frame;
	private MEBNController controller = null;
	private MultiEntityBayesianNetwork mebn = null;
	
	
	public HMLPNetworkWindow() {
		super();
		this.setFrame(new HMLP_Console(null, "HMLP Tool V.1"));
		this.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setJMenuBar(this.getFrame().getJMenuBar());

		this.add(this.getFrame().getContentPane());
		this.getFrame().init(windowMode.CONNECT_DB);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see unbbayes.util.extension.UnBBayesModule#setUnbbayesFrame(unbbayes.gui.UnBBayesFrame)
	 */
	public void setUnbbayesFrame(UnBBayesFrame unbbayesFrame) {
		super.setUnbbayesFrame(unbbayesFrame);
		try {
			this.getFrame().setUpperFrame(unbbayesFrame);
			this.getFrame().setUnbbayesFrame(unbbayesFrame);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the frame
	 */
	public HMLP_Console getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	protected void setFrame(HMLP_Console frame) {
		this.frame = frame;
	}

	public BaseIO getIO() {
		return null;
	}

	public Graph getPersistingGraph() {
		return null;
	}

	public String getModuleName() {
		return "HMLP";
	}

	public UnBBayesModule openFile(File file) throws IOException {
		return this;
	}

}
