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
 * @author Pavel Zlámal
 */
public class ClientProductPropertyView extends View {

	/**
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
		InstallationJSO instalation = null;
		if (ClientController.CLIENT_PRODUCT_PROPERTY == type) {
			List<BeanModel> cls = event.getData("clients");

			for (BeanModel beanModel : cls) {
				clients.add((ClientJSO) beanModel.getBean());
			}
			instalation = event.getData("instalace");
		}
		getClientProductProperty(clients, instalation);
	}

	/**
	 * @param clients
	 * @param instalation
	 */
	private void getClientProductProperty(final List<ClientJSO> clients, final InstallationJSO instalation) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				getClientProductPropertyAsync(clients, instalation);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());

			}
		});
	}

	/**
	 * @param clients
	 * @param instalace
	 */
	protected void getClientProductPropertyAsync(List<ClientJSO> clients, InstallationJSO instalace) {
		clientProductProperty(clients, instalace);
	}

	/**
	 * @param clients
	 * @param instalace
	 */
	protected void clientProductProperty(List<ClientJSO> clients, InstallationJSO instalace) {
		ClientProductPropertyWindow w = createWindow(clients, instalace);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * @param clients
	 * @param instalace
	 * @return
	 */
	private ClientProductPropertyWindow createWindow(List<ClientJSO> clients, InstallationJSO instalace) {
		return new ClientProductPropertyWindow(clients, instalace);
	}

}