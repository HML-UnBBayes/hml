/**
 * 
 */
package unbbayes.gui;

import unbbayes.util.extension.UnBBayesModule;
import unbbayes.util.extension.builder.NetworkWindowBuilder;

/**
 * @author Shou Matsumoto
 *
 */
public class HMLPNetworkWindowBuilder extends NetworkWindowBuilder {


	/*
	 * (non-Javadoc)
	 * @see unbbayes.util.extension.UnBBayesModuleBuilder#buildUnBBayesModule()
	 */
	public UnBBayesModule buildUnBBayesModule() {
		return new HMLPNetworkWindow();
	}

}
