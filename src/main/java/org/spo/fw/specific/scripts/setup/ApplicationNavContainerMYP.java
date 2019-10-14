/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.setup;


import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.ApplicationNavigationModel;
import org.spo.fw.navigation.itf.NavigationServiceProvider;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;

/**
 * 
 * @author prem
 * achieves transformations on the webdriver based on applicationnavmodel query results.
 * Also stateful.
 * workflows can use this api's getModel() for any in memory manipualation to the  app nav model
 * It can be used statefully to navigate through the pages start point and reset point is init()
 *
 */

//TODO may be consider refactoring
public class ApplicationNavContainerMYP extends ApplicationNavContainerImpl implements NavigationServiceProvider {


	protected Logger1 log =new Logger1("ApplicationNavContainerMYP");

	public void init(){
		model=new ApplicationNavModelMYP();
		model.init();

	}



	public ApplicationNavModelMYP getDefaulModel() {
		return (ApplicationNavModelMYP)model;
	}

	public void setDefaultModel(ApplicationNavModelMYP model) {
		this.model = model;
	}	


	public ApplicationNavigationModel getModel() {
		return model;
	}

	public void setModel(ApplicationNavigationModel model) {
		this.model = model;
	}	





}


