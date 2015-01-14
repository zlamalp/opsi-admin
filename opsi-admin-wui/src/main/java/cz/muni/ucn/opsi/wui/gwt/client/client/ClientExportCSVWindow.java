package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Window for exporting clients to CSV.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientExportCSVWindow extends Window {

	private List<ClientJSO> clients = new ArrayList<ClientJSO>();

	/**
	 * Create window for exporting clients
	 *
	 * @param clients to export
	 */
	public ClientExportCSVWindow(List<ClientJSO> clients) {

		this.clients = clients;

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(540, 350);
		setHeadingHtml("Export klientů do CSV");

		setLayout(new FitLayout());

		TextArea textform = new TextArea();

		String originalValue = "";
		for (ClientJSO client : clients) {
			// MAC address is optional, NAME isn't
			String mac = client.getMacAddress();
			if (mac == null || mac.equals("null")) {
				mac = "";
			}
			String description = client.getDescription();
			if (description == null || description.equals("null")) {
				description = "";
			}
			String notes = client.getNotes();
			if (notes == null || notes.equals("null")) {
				notes = "";
			}
			String ip = client.getIpAddress();
			if (ip == null || ip.equals("null")) {
				ip = "";
			}
			originalValue += client.getName() + ";" + mac + ";" + description + ";" + notes + ";" + ip + "\n";
		}
		textform.setValue(originalValue);

		add(textform, new FitData());

		generateButtons();

	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Component#onLoad()
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
	}

	/**
	 * Generate cancel button
	 */
	private void generateButtons() {

		Button buttonCancel = new Button("Zavřít");
		buttonCancel.setIcon(IconHelper.createStyle("cancel"));
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ClientExportCSVWindow.this.hide(ce.getButton());
			}
		});
		addButton(buttonCancel);

	}

}