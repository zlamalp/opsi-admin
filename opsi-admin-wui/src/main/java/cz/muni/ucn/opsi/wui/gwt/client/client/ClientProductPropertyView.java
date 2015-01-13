package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstallationJSO;

/**
 * View class for handling ProductProperty setting and installation
 *
 * @author Pavel Zlámal
 */
public class ClientProductPropertyView extends View {

	/**
	 * Create ProductProperty view
	 *
	 * @param controller
	 */
	public ClientProductPropertyView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		List<ClientJSO> clients = new ArrayList<ClientJSO>();
		InstallationJSO installation = null;
		if (ClientController.CLIENT_PRODUCT_PROPERTY == type) {
			List<BeanModel> cls = event.getData("clients");

			for (BeanModel beanModel : cls) {
				clients.add((ClientJSO) beanModel.getBean());
			}
			installation = event.getData("instalace");
		}
		getClientProductProperty(clients, installation);
	}

	/**
	 * Method ensuring async loading of UI
	 *
	 * @param clients clients to install
	 * @param installation installation to install
	 */
	private void getClientProductProperty(final List<ClientJSO> clients, final InstallationJSO installation) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				getClientProductPropertyAsync(clients, installation);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());

			}
		});
	}

	/**
	 * Create new instance of Window for installing and attach it to the desktop.
	 * Must be called from async method.
	 *
	 * @param clients Clients to install on
	 * @param installation Installation to perform
	 * @return new instance of window
	 */
	protected void getClientProductPropertyAsync(List<ClientJSO> clients, InstallationJSO installation) {
		clientProductProperty(clients, installation);
	}

	/**
	 * Create new instance of Window for installing and attach it to the desktop.
	 *
	 * @param clients Clients to install on
	 * @param installation Installation to perform
	 * @return new instance of window
	 */
	protected void clientProductProperty(List<ClientJSO> clients, InstallationJSO installation) {
		ClientProductPropertyWindow w = createWindow(clients, installation);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * Create new instance of Window for installing.
	 *
	 * @param clients Clients to install on
	 * @param installation Installation to perform
	 * @return new instance of window
	 */
	private ClientProductPropertyWindow createWindow(List<ClientJSO> clients, InstallationJSO installation) {
		return new ClientProductPropertyWindow(clients, installation);
	}

}