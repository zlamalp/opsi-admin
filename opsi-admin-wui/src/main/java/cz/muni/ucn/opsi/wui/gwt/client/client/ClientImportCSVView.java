package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import com.extjs.gxt.ui.client.mvc.Dispatcher;
import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * View for handling events on importing clients from CSV
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientImportCSVView extends ClientImportView {

	protected String data;

	/**
	 * Create new instance of this view
	 *
	 * @param controller
	 */
	public ClientImportCSVView(Controller controller) {
		super(controller);
	}

	public EventType getWindowEventType() {
		return ClientController.CLIENT_IMPORT_CSV;
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		GroupJSO group = (GroupJSO) event.getData("group");
		if (getWindowEventType() == type) {
			importClientCSVAsync(group);
		}
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.wui.gwt.client.client.ClientImportView#importClientAsync(cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO)
	 */
	protected void importClientCSVAsync(final GroupJSO group) {

		final ClientImportCSVFileDialog dialog = new ClientImportCSVFileDialog(group);
		dialog.addListener(ClientImportCSVFileDialog.IMPORT_EVENT_TYPE, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				ClientImportCSVView.this.data = dialog.getReturnedData();
				importCSVWindow(group);
			}
		});
		dialog.show();
	}

	/**
	 * Show ClientImportCSV window (attach it to the desktop)
	 *
	 * @param group Group to import Clients into
	 */
	protected void importCSVWindow(GroupJSO group) {
		ClientImportCSVWindow w = createWindow(group);
		Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		w.show();
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.wui.gwt.client.client.ClientImportView#createWindow(cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO)
	 */
	protected ClientImportCSVWindow createWindow(GroupJSO group) {
		return new ClientImportCSVWindow(group, this.data);
	}

}
