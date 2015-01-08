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
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstalaceJSO;

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
		InstalaceJSO instalace = null;
		if (ClientController.CLIENT_PRODUCT_PROPERTY == type) {
			List<BeanModel> cls = event.getData("clients");

			for (BeanModel beanModel : cls) {
				clients.add((ClientJSO) beanModel.getBean());
			}
			instalace = event.getData("instalace");
		}
		getClientProductProperty(clients, instalace);
	}

	/**
	 * @param clients
	 * @param instalace
	 */
	private void getClientProductProperty(final List<ClientJSO> clients, final InstalaceJSO instalace) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				getClientProductPropertyAsync(clients, instalace);
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
	protected void getClientProductPropertyAsync(List<ClientJSO> clients, InstalaceJSO instalace) {
		clientProductProperty(clients, instalace);
	}

	/**
	 * @param clients
	 * @param instalace
	 */
	protected void clientProductProperty(List<ClientJSO> clients, InstalaceJSO instalace) {
		ClientProductPropertyWindow w = createWindow(clients, instalace);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * @param clients
	 * @param instalace
	 * @return
	 */
	private ClientProductPropertyWindow createWindow(List<ClientJSO> clients, InstalaceJSO instalace) {
		return new ClientProductPropertyWindow(clients, instalace);
	}

}