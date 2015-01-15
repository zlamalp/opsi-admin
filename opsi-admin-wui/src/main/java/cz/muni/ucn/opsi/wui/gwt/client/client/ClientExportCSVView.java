package cz.muni.ucn.opsi.wui.gwt.client.client;

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

import java.util.ArrayList;
import java.util.List;

/**
 * View class handling exporting of clients into CSV.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientExportCSVView extends View {

	/**
	 * Create new instance of view
	 *
	 * @param controller
	 */
	public ClientExportCSVView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		List<ClientJSO> clients = new ArrayList<ClientJSO>();
		if (ClientController.CLIENT_EXPORT_CSV == type) {
			List<BeanModel> cls = event.getData("clients");
			for (BeanModel beanModel : cls) {
				clients.add((ClientJSO) beanModel.getBean());
			}
		}
		clientExportCSV(clients);
	}

	public EventType getWindowEventType() {
		return ClientController.CLIENT_EXPORT_CSV;
	}

	/**
	 * Method ensuring async loading of UI
	 *
	 * @param clients clients to be exported
	 */
	private void clientExportCSV(final List<ClientJSO> clients) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				clientExportCSVAsync(clients);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());

			}
		});
	}

	/**
	 * Trigger showing ExportCSV window (must be called asynchronously).
	 *
	 * @param clients clients to export
	 */
	protected void clientExportCSVAsync(List<ClientJSO> clients) {
		clientExportCSVWindow(clients);
	}

	/**
	 * Show ExportCSV window (attach it to the desktop)
	 *
	 * @param clients client to export
	 */
	protected void clientExportCSVWindow(List<ClientJSO> clients) {
		ClientExportCSVWindow w = createWindow(clients);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/**
	 * Return new instance of ExportCSV window
	 *
	 * @param clients clients to export
	 * @return new instance of ClientExportCSVWindow
	 */
	protected ClientExportCSVWindow createWindow(List<ClientJSO> clients) {
		return new ClientExportCSVWindow(clients);
	}

}