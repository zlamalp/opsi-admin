/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 *
 * http://extjs.com/license
 */
package cz.muni.ucn.opsi.wui.gwt.client;

import com.extjs.gxt.themes.client.Slate;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.google.gwt.core.client.EntryPoint;

import cz.muni.ucn.opsi.wui.gwt.client.client.ClientController;
import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupController;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstallationController;
import cz.muni.ucn.opsi.wui.gwt.client.login.LoginController;

/**
 * Entry point of OPSI Admin WUI. User must be logged in or is redirected to LoginApp.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class OpsiAdminApp implements EntryPoint {

	public void onModuleLoad() {

		ThemeManager.register(Slate.SLATE);
		GXT.setDefaultTheme(Slate.SLATE, true);

		Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(new LoginController());
		dispatcher.addController(new DesktopController());
		dispatcher.addController(new CometController());

		dispatcher.addController(new GroupController());
		dispatcher.addController(new ClientController());
		dispatcher.addController(new InstallationController());

		dispatcher.dispatch(DesktopController.INIT);

		GXT.hideLoadingPanel("loading");
	}

}