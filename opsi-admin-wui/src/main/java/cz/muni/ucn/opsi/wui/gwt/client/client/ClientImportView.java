package cz.muni.ucn.opsi.wui.gwt.client.client;

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

/**
 * View for handling events on importing clients.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientImportView extends View {

	/**
	 * Create new instance of this view
	 *
	 * @param controller
	 */
	public ClientImportView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		GroupJSO group = (GroupJSO) event.getData("group");
		if (getWindowEventType() == type) {
			clientImport(group, true);
		} else if (getWindowEventType2() == type) {
			clientImport(group, false);
		}
	}

	public EventType getWindowEventType() {
		// import from local OPSI
		return ClientController.CLIENT_IMPORT;
	}
	public EventType getWindowEventType2() {
		// import from remote OPSI
		return ClientController.CLIENT_IMPORT2;
	}

	/**
	 * Method ensuring async loading of UI
	 *
	 * @param group Group to import Clients into
	 * @param master TRUE = import from local OPSI / FALSE = import from remote OPSI
	 */
	private void clientImport(final GroupJSO group, final boolean master) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				importClientAsync(group, master);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());

			}
		});
	}

	/**
	 * Trigger showing ClientImport window (must be called asynchronously).
	 *
	 * @param group Group to import Clients into
	 * @param master TRUE = import from local OPSI / FALSE = import from remote OPSI
	 */
	protected void importClientAsync(GroupJSO group, boolean master) {
		importGroupWindow(group, master);
	}

	/**
	 * Show ClientImport window (attach it to the desktop)
	 *
	 * @param group Group to import Clients into
	 * @param master TRUE = import from local OPSI / FALSE = import from remote OPSI
	 */
	protected void importGroupWindow(GroupJSO group, boolean master) {
		ClientImportWindow w = createWindow(group, master);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * Return instance of ClientImport window.
	 *
	 * @param group Group to import clients into.
	 * @param master TRUE = import from local OPSI / FALSE = import from remote OPSI
	 * @return new instance of ClientImportWindow
	 */
	protected ClientImportWindow createWindow(GroupJSO group, boolean master) {
		return new ClientImportWindow(group, master);
	}

}
