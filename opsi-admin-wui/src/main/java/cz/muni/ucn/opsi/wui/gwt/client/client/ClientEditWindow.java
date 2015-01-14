package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * Window for editing client details like name, ip, mac address etc.
 * It's used also for creating new client !!
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientEditWindow extends Window {

	// regular expression for client name
	private static final String NAME_REGEXP = "^([a-zA-Z0-9\\-\\_]+)\\.([a-zA-Z0-9\\-\\_\\.]+)$";

	private static final String FIELD_SPEC = "-18";

	private final boolean newClient;
	private ClientConstants clientConstants;
	private FormPanel form;

	private FormBinding binding;

	private ClientJSO client;

	/**
	 * Create new instance of Window for creating/editing clients.
	 *
	 * @param newClient TRUE = use empty form in order to create new client / FALSE = edit existing client
	 */
	public ClientEditWindow(boolean newClient) {

		this.newClient = newClient;

		clientConstants = GWT.create(ClientConstants.class);

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(400, 260);
//		setBodyStyle("padding: 0px; ");

//		FormLayout layout = new FormLayout();
//		layout.setLabelAlign(LabelAlign.LEFT);
//		setLayout(layout);
		setLayout(new FitLayout());


		form = new FormPanel();
		form.setHeaderVisible(false);
		form.setLabelWidth(100);
		form.setBodyBorder(false);

		ContentPanel borderPanel = new ContentPanel();
		borderPanel.setLayout(new RowLayout(Orientation.VERTICAL));
		borderPanel.setHeaderVisible(false);
//		rowPanel.setFrame(false);
		borderPanel.setBodyBorder(false);
//		rowPanel.setBorders(false);
		borderPanel.setBodyStyle("background: transparent;");

		form.add(borderPanel, new FormData("100% 100%"));

		ContentPanel formPanel = new ContentPanel();
		FormLayout formPanelLayout = new FormLayout();
		formPanel.setLayout(formPanelLayout);
		formPanel.setHeaderVisible(false);
//		formPanel.setFrame(false);
		formPanel.setBodyBorder(false);
//		formPanel.setBorders(false);
		formPanel.setBodyStyle("background: transparent;");

		borderPanel.add(formPanel, new RowData(1, -1));

		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel(clientConstants.getName());
		name.setMinLength(1);
		name.setAllowBlank(false);
		name.setAutoValidate(true);
		name.setValidator(new Validator() {

			@Override
			public String validate(Field<?> field, String value) {
				if (!value.matches(NAME_REGEXP)) {
					return "Pole nevyhovuje formátu: " + NAME_REGEXP;
				}
				return null;
			}
		});

		if (!newClient) {
			name.setReadOnly(true);
		}

		formPanel.add(name, new FormData(FIELD_SPEC));

		TextField<String> description = new TextField<String>();
		description.setName("description");
		description.setFieldLabel(clientConstants.getDescription());
		formPanel.add(description, new FormData(FIELD_SPEC));

		TextField<String> ipAddress = new TextField<String>();
		ipAddress.setName("ipAddress");
		ipAddress.setFieldLabel(clientConstants.getIpAddress());
		ipAddress.setValidator(new Validator() {

			@Override
			public String validate(Field<?> field, String value) {
				if (value.isEmpty()) {
					return null;
				}
				String[] split = value.split("\\.");
				if (split.length == 4) {
					int errs = 0;
					for (int i = 0; i < split.length; i++) {
						int p = Integer.parseInt(split[i]);
						if (p >= 0 && p <= 255) {
							continue;
						}
						errs++;
					}
					if (0 == errs) {
						return null;
					}
				}
				return "Vyplňte platnou IPv4 adresu.";
			}
		});
		formPanel.add(ipAddress, new FormData(FIELD_SPEC));

		TextField<String> macAddress = new TextField<String>();
		macAddress.setName("macAddress");
		macAddress.setFieldLabel(clientConstants.getMacAddress());
		macAddress.addListener(Events.Change, new Listener<FieldEvent>() {

			@Override
			public void handleEvent(FieldEvent fe) {

				@SuppressWarnings("unchecked")
				TextField<String> field = (TextField<String>) fe.getField();
				String value = field.getValue();
				value = value.replaceAll("[.\\- \\,]", ":");
				if (value.matches("^([0-9a-fA-F]{2}){6}$")) {
					value = value.replaceFirst("^([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$",
							"$1:$2:$3:$4:$5:$6");
				}
				field.setValue(value);
				fe.cancelBubble();
			}
		});
		macAddress.setValidator(new Validator() {

			@Override
			public String validate(Field<?> field, String value) {
				if (value.matches("^([0-9a-fA-F]{1,2}:){5}([0-9a-fA-F]{1,2})$")) {
					return null;
				}
				return "Zadejte platnou MAC adresu ve tvaru 01:23:45:67:89:ab";
			}
		});
		formPanel.add(macAddress, new FormData(FIELD_SPEC));

		TextArea notes = new TextArea();
		notes.setName("notes");
		notes.setFieldLabel(clientConstants.getNotes());
		formPanel.add(notes, new FormData(FIELD_SPEC));

		add(form, new FitData());

		binding = new FormBinding(form, true);
		binding.setUpdateOriginalValue(true);

		generateButtons();

	}

	/**
	 * Generate buttons for submission and canceling input form in this window.
	 */
	private void generateButtons() {

		Button buttonCancel = new Button("Zrušit");
		buttonCancel.setIcon(IconHelper.createStyle("Cancel"));
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				ClientEditWindow.this.hide(ce.getButton());
			}
		});
		addButton(buttonCancel);

		Button buttonOK = new Button("Uložit");
		buttonOK.setIcon(IconHelper.createStyle("OK"));
		buttonOK.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {

				if (!validate()) {
					MessageBox.alert("Nelze uložit", "Formulář obsahuje chyby",
							new Listener<MessageBoxEvent>() {

						@Override
						public void handleEvent(MessageBoxEvent be) {
						}
					});
					return;
				}

				ClientEditWindow.this.disable();

				synchronizeState();

				ClientService clientService = ClientService.getInstance();
				clientService.saveClient(client, new RemoteRequestCallback<Object>() {
					@Override
					public void onRequestSuccess(Object v) {
						ClientEditWindow.this.enable();
						ClientEditWindow.this.hide(ce.getButton());
						Info.display("Klient uložen", client.getName());
					}

					@Override
					public void onRequestFailed(Throwable th) {
						ClientEditWindow.this.enable();
						MessageDialog.showError("Nelze uložit klienta "+client.getName(), th.getMessage());
					}
				});

			}
		});
		addButton(buttonOK);

	}

	/**
	 * Method for setting existing client to this window (for editing)
	 *
	 * @param client Client to edit
	 */
	public void setClientModel(ClientJSO client) {
		this.client = client;
		BeanModel model = BeanModelLookup.get().getFactory(ClientJSO.CLASS_NAME).createModel(client);
		binding.bind(model);
		updateHeading();
	}

	/**
	 * Set proper window heading - creating new client / edit exiting
	 */
	private void updateHeading() {
		if (newClient) {
			setHeadingHtml("Nový klient");
		} else {
			setHeadingHtml("Úprava klienta: " + client.getName());
		}
	}

	/**
	 *
	 */
	protected void synchronizeState() {
	}

	/**
	 * Validate input form of this window
	 *
	 * @return TRUE = valid / FALSE = invalid
	 */
	protected boolean validate() {
		List<Field<?>> fields = form.getFields();
		boolean valid = true;
		for (Field<?> field : fields) {
			if (!field.validate()) {
				valid = false;
			}
		}
		return valid;
	}

}