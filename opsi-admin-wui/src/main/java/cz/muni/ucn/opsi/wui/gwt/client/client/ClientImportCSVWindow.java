package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * Extension class for importing clients from CSV.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientImportCSVWindow extends ClientImportWindow {

	private final String data;

	/**
	 * Create window for importing clients from CSV
	 *
	 * @param group group
	 * @param data data about clients in JSON format
	 */
	public ClientImportCSVWindow(GroupJSO group, String data) {
		super(group, true);
		this.data = data;
	}

	/**
	 * Converts JSON string with clients to JSO objects and set them to Window
	 */
	protected void loadData() {

		JsArray<ClientJSO> clientsA = ClientJSO.fromJSONArray(data);
		List<ClientJSO> clients = new ArrayList<ClientJSO>();
		for(int i = 0; i < clientsA.length(); i++) {
			clients.add(clientsA.get(i));
		}

		List<BeanModel> clientModels = clientFactory.createModel(clients);
		clientStore.add(clientModels);
		clientsGrid.unmask();

	}

}