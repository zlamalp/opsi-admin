package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstallationJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * View for handling events related to Clients (create /update / delete / install).
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientView extends View {

	private ClientWindow window;

	/**
	 * Create instance of this view
	 *
	 * @param controller
	 */
	public ClientView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (ClientController.CLIENTS == type) {
			showClients();
		} else if (ClientController.CLIENT_DELETE == type) {
			List<BeanModel> clients = event.getData("clients");
			deleteClients(clients);
		} else if (ClientController.CLIENT_INSTALL == type) {
			List<BeanModel> clients = event.getData("clients");
			InstallationJSO install = event.getData("install");
			installClients(clients, install);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			LifecycleEventJSO lifecycleEventJSO = (LifecycleEventJSO)event.getData();
			onLifecycleEvent(lifecycleEventJSO);
		}
	}

	/**
	 * Method ensuring async loading of UI
	 */
	private void showClients() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				if (null == window) {
					window = new ClientWindow();
				}
				Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, window);
				if (window.isVisible()) {
					window.toFront();
				} else {
					window.show();
				}
			}
			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Akci nelze provést", reason.getMessage());
			}
		});

	}

	/**
	 * Delete Clients from OPSI.
	 *
	 * @param clients Clients to be deleted
	 */
	private void deleteClients(final List<BeanModel> clients) {

		String clientsStr = "";
		for (BeanModel beanModel : clients) {
			ClientJSO c = beanModel.getBean();
			if (!clientsStr.isEmpty()) {
				clientsStr += ", ";
			}
			clientsStr += c.getName();
		}

		String message = (clients.size()>1) ? "Odstranit klienty?" : "Odstranit klienta?";
		String message2 = (clients.size()>1) ? "Opravdu chcete odstranit klienty "+clientsStr+" ?" : "Opravdu chcete odstranit klienta "+clientsStr+" ?";

		MessageBox.confirm(message, message2, new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				if (!Dialog.YES.equals(be.getButtonClicked().getItemId())) {
					return;
				}
				for (BeanModel beanModel : clients) {
					final ClientJSO client = beanModel.getBean();
					ClientService.getInstance().deleteClient(client, new RemoteRequestCallback<Object>() {
						@Override
						public void onRequestSuccess(Object v) {
							Info.display("Klient odstraněn", client.getName());
						}

						@Override
						public void onRequestFailed(Throwable th) {
							MessageDialog.showError("Chyba při odstraňování klienta "+client.getName(), th.getMessage());
						}
					});
				}
			}
		});

	}

	/**
	 * Perform client installation.
	 *
	 * This will update OPSI and start installing selected product (OS) to all passed Clients.
	 *
	 * @param clients Clients to start installation for
	 * @param installation Product (OS) to install.
	 */
	private void installClients(List<BeanModel> clients, InstallationJSO installation) {
		for (BeanModel beanModel : clients) {
			final ClientJSO client = beanModel.getBean();
			ClientService.getInstance().installClient(client, installation, new RemoteRequestCallback<Object>() {
				@Override
				public void onRequestSuccess(Object v) {
					Info.display("Instalace spuštěna", client.getName());
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Chyba při spouštění instalace klienta "+client.getName(), th.getMessage());
				}
			});
		}
	}

	/**
	 * Handle app wide life-cycle events. Pass them to windows.
	 *
	 * @param lifecycleEventJSO Event to pass
	 */
	private void onLifecycleEvent(LifecycleEventJSO lifecycleEventJSO) {
		if (null == window) {
			return;
		}
		window.onLifecycleEvent(lifecycleEventJSO);
	}

}