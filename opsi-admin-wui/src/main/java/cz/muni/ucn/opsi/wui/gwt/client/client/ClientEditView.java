package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * View for handling events on creating and editing clients.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientEditView extends View {

	// mapping clients to their editing window
	private Map<ClientJSO, ClientEditWindow> windows = new HashMap<ClientJSO, ClientEditWindow>();

	/**
	 * Create new instance of this view
	 *
	 * @param controller
	 */
	public ClientEditView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		GroupJSO group = (GroupJSO) event.getData("group");
		if (ClientController.CLIENT_NEW == type) {
			clientEdit(group, null);
		} else if (ClientController.CLIENT_EDIT == type) {
			List<BeanModel> clients = event.getData("clients");
			for (BeanModel beanModel : clients) {
				ClientJSO client = beanModel.getBean();
				clientEdit(group, client);
			}
		}
	}

	/**
	 * Method ensuring async loading of UI
	 *
	 * @param group Group to create / edit Client in.
	 * @param client Client to edit or NULL when creating new.
	 */
	private void clientEdit(final GroupJSO group, final ClientJSO client) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				editClientAsync(group, client);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());
			}
		});

	}

	/**
	 * Call to retrieve data from OPSI. THIS ENSURE THAT DATA IN FORM ARE UP-TO-DATE !!
	 * For new clients, server side creates UUID and set group. To store created client see "saveClient" method in api.
	 *
	 * @param group Group to create new client in
	 * @param client Client to edit
	 */
	protected void editClientAsync(GroupJSO group, ClientJSO client) {
		ClientService clientService = ClientService.getInstance();
		if (null == client) {
			clientService.createClient(group, new RemoteRequestCallback<ClientJSO>() {
				@Override
				public void onRequestSuccess(ClientJSO client) {
					editGroupWindow(client, true);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze založit klienta", th.getMessage());
				}
			});
		} else {
			clientService.editClient(client, new RemoteRequestCallback<ClientJSO>() {
				@Override
				public void onRequestSuccess(ClientJSO client) {
					editGroupWindow(client, false);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze upravit klienta", th.getMessage());
				}
			});

		}
	}

	/**
	 * Show ClientEdit window (attach it to the desktop) or switch focus in already opened.
	 *
	 * @param client Client to edit / create
	 * @param newClient TRUE if creating new client / FALSE editing existing
	 */
	protected void editGroupWindow(ClientJSO client, boolean newClient) {
		ClientEditWindow w;
		if (windows.containsKey(client)) {
			w = windows.get(client);
			w.setClientModel(client);
		} else {
			w = createWindow(newClient);
			windows.put(client, w);
			Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		}
		// set Client model to window !!
		w.setClientModel(client);
		if (w.isVisible()) {
			w.toFront();
		} else {
			w.show();
		}

	}

	/**
	 * Return new instance of ClientEdit window.
	 *
	 * @param newClient TRUE = creating new client mode / FALSE = editing existing client (object must be set to window later !!)
	 * @return new instance of ClientEditWindow
	 */
	private ClientEditWindow createWindow(boolean newClient) {
		return new ClientEditWindow(newClient);
	}

}