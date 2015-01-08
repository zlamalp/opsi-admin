package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * Controller for handling app events associated with Clients.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientController extends Controller {

	// App events associated with clients
	public static final EventType CLIENTS = new EventType();
	public static final EventType CLIENT_NEW = new EventType();
	public static final EventType CLIENT_EDIT = new EventType();
	public static final EventType CLIENT_DELETE = new EventType();
	public static final EventType CLIENT_INSTALL = new EventType();
	// import from main OPSI server (local)
	public static final EventType CLIENT_IMPORT = new EventType();
	// import from secondary OPSI server (remote)
	public static final EventType CLIENT_IMPORT2 = new EventType();
	public static final EventType CLIENT_IMPORT_CSV = new EventType();
	public static final EventType CLIENT_EXPORT_CSV = new EventType();
	public static final EventType CLIENT_HARDWARE = new EventType();
	public static final EventType CLIENT_PRODUCT_PROPERTY = new EventType();

	// Views for handling specific events
	private ClientView clientView;
	private ClientEditView clientEditView;
	private ClientImportView clientImportView;
	private ClientImportCSVView clientImportCSVView;
	private ClientExportCSVView clientExportCSVView;
	private ClientHardwareView clientHardwareView;
	private ClientProductPropertyView clientProductPropertyView;

	/**
	 * Create controller for handling app events associated with Clients.
	 */
	public ClientController() {
		registerEventTypes(ClientController.CLIENTS);
		registerEventTypes(ClientController.CLIENT_NEW);
		registerEventTypes(ClientController.CLIENT_EDIT);
		registerEventTypes(ClientController.CLIENT_DELETE);
		registerEventTypes(ClientController.CLIENT_INSTALL);
		registerEventTypes(ClientController.CLIENT_IMPORT);
		registerEventTypes(ClientController.CLIENT_IMPORT2);
		registerEventTypes(ClientController.CLIENT_IMPORT_CSV);
		registerEventTypes(ClientController.CLIENT_EXPORT_CSV);
		registerEventTypes(ClientController.CLIENT_HARDWARE);
		registerEventTypes(ClientController.CLIENT_PRODUCT_PROPERTY);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		clientView = new ClientView(this);
		clientEditView = new ClientEditView(this);
		clientImportView = new ClientImportView(this);
		clientImportCSVView = new ClientImportCSVView(this);
		clientExportCSVView = new ClientExportCSVView(this);
		clientHardwareView = new ClientHardwareView(this);
		clientProductPropertyView = new ClientProductPropertyView(this);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (ClientController.CLIENTS == type) {
			showClients(event);
		} else if (ClientController.CLIENT_NEW == type) {
			clientNew(event);
		} else if (ClientController.CLIENT_EDIT == type) {
			clientEdit(event);
		} else if (ClientController.CLIENT_DELETE == type) {
			clientDelete(event);
		} else if (ClientController.CLIENT_INSTALL == type) {
			clientInstall(event);
		} else if (ClientController.CLIENT_IMPORT == type) {
			clientImport(event);
		} else if (ClientController.CLIENT_IMPORT2 == type) {
			clientImport2(event);
		} else if (ClientController.CLIENT_IMPORT_CSV == type) {
			clientImportCSV(event);
		} else if (ClientController.CLIENT_EXPORT_CSV == type) {
			clientExportCSV(event);
		} else if (ClientController.CLIENT_HARDWARE == type) {
			clientHardware(event);
		}  else if (ClientController.CLIENT_PRODUCT_PROPERTY == type) {
			clientProductProperty(event);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			onLifecycleEvent(event);
		}
	}

	/**
	 * Forward event to Clients view
	 *
	 * @param event Event to pass
	 */
	private void showClients(AppEvent event) {
		forwardToView(clientView, event);
	}

	/**
	 * Forward event to ClientNew view
	 *
	 * @param event Event to pass
	 */
	private void clientNew(AppEvent event) {
		forwardToView(clientEditView, event);
	}

	/**
	 * Forward event to ClientEdit view
	 *
	 * @param event Event to pass
	 */
	private void clientEdit(AppEvent event) {
		forwardToView(clientEditView, event);
	}

	/**
	 * Forward event to ClientDelete view
	 *
	 * @param event Event to pass
	 */
	private void clientDelete(AppEvent event) {
		forwardToView(clientView, event);
	}

	/**
	 * Forward event to ClientInstall view
	 *
	 * @param event Event to pass
	 */
	private void clientInstall(AppEvent event) {
		forwardToView(clientView, event);
	}

	/**
	 * Forward event to ClientImport view
	 *
	 * @param event Event to pass
	 */
	private void clientImport(AppEvent event) {
		forwardToView(clientImportView, event);
	}

	/**
	 * Forward event to ClientImport2 view
	 *
	 * @param event Event to pass
	 */
	private void clientImport2(AppEvent event) {
		forwardToView(clientImportView, event);
	}

	/**
	 * Forward event to ClientImportCSV view
	 *
	 * @param event Event to pass
	 */
	private void clientImportCSV(AppEvent event) {
		forwardToView(clientImportCSVView, event);
	}

	/**
	 * Forward event to ClientExportCSV view
	 *
	 * @param event Event to pass
	 */
	private void clientExportCSV(AppEvent event) {
		forwardToView(clientExportCSVView, event);
	}

	/**
	 * Forward event to ClientHardware view
	 *
	 * @param event Event to pass
	 */
	private void clientHardware(AppEvent event) {
		forwardToView(clientHardwareView, event);
	}

	/**
	 * Forward event to ClientProductProperty view
	 *
	 * @param event Event to pass
	 */
	private void clientProductProperty(AppEvent event) {
		forwardToView(clientProductPropertyView, event);
	}

	/**
	 * Handle app wide life-cycle events
	 *
	 * @param event Event to pass
	 */
	private void onLifecycleEvent(AppEvent event) {
        LifecycleEventJSO le = (LifecycleEventJSO) event.getData();
        if (!"cz.muni.ucn.opsi.api.client.Client".equals(le.getBeanClass())
        		&& !"cz.muni.ucn.opsi.api.group.Group".equals(le.getBeanClass())) {
                return;
        }
		forwardToView(clientView, event);
	}

}