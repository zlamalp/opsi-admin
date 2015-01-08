package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * Import CSV file dialog used to load data to server and return parsed clients to WUI.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientImportCSVFileDialog extends Dialog {

	private static final String IMPORT_CSV_URL = "remote/clients/importCsv";
	private FormPanel form;
	private ClientConstants clientConstants;
	protected String returnedData;

	public static final EventType IMPORT_EVENT_TYPE = new EventType();

	/**
	 * Create import CSV file dialog window.
	 *
	 * @group Group to import Clients into
	 */
	public ClientImportCSVFileDialog(GroupJSO group) {

		super();

		setButtons("");

		clientConstants = GWT.create(ClientConstants.class);
		setHeadingHtml(clientConstants.getClientImportCSV());

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(350, 120);
		setLayout(new FitLayout());

		form = new FormPanel();
		form.setHeaderVisible(false);
		form.setLabelWidth(100);
		form.setBodyBorder(false);
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);
		form.setAction(GWT.getHostPageBaseURL() + IMPORT_CSV_URL);

		FileUploadField uploadField = new FileUploadField();
		uploadField.setName("importFile");
		uploadField.setAllowBlank(false);
		uploadField.setFieldLabel("CSV soubor");
		uploadField.getMessages().setBrowseText("Procházet...");

		form.add(uploadField, new FormData("100%"));

		HiddenField<String> groupUuid = new HiddenField<String>();
		groupUuid.setValue(group.getUuid());
		groupUuid.setName("groupUuid");
		form.add(groupUuid);

		// Once form is submitted and file processed, take result (string data in JSON)
		// and pass it as normal import event
		form.addListener(Events.Submit, new Listener<FormEvent>() {
			@Override
			public void handleEvent(FormEvent fe) {
				String data = fe.getResultHtml();
				ClientImportCSVFileDialog.this.returnedData = data;
				GWT.log(data);
				ClientImportCSVFileDialog.this.fireEvent(IMPORT_EVENT_TYPE);
				ClientImportCSVFileDialog.this.hide();
			}
		});

		add(form, new FitData());

	}

	/*
	* (non-Javadoc)
    *
    * @see com.extjs.gxt.ui.client.widget.Dialog#createButtons()
    */
	@Override
	protected void createButtons() {
		super.createButtons();

		Button cancel = new Button("Storno");
		cancel.setItemId(CANCEL);
		cancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				onButtonPressed(ce.getButton());
			}
		});

		Button upload = new Button("Nahrát");
		upload.setItemId(OK);
		upload.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				onButtonPressed(ce.getButton());
			}
		});

		addButton(upload);
		addButton(cancel);

	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Dialog#onButtonPressed(com.extjs.gxt.ui.client.widget.button.Button)
	 */
	@Override
	protected void onButtonPressed(Button button) {

		super.onButtonPressed(button);
		if (button == getButtonBar().getItemByItemId(OK)) {
			form.submit();
		}
		if (button == getButtonBar().getItemByItemId(CANCEL)) {
			this.hide();
		}

	}

	/**
	 * Get data returned from server (parsed CSV file content)
	 *
	 * @return Data in JSON format, should be list of Clients
	 */
	public String getReturnedData() {
		return returnedData;
	}

}