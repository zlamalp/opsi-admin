package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.google.gwt.json.client.JSONObject;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelFactory;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstallationJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

import java.util.*;

/**
 * Window with installation parameters configuration
 *
 * @author Pavel Zlámal
 */
public class ClientProductPropertyWindow extends Window {

	private List<ClientJSO> clients;
	private InstallationJSO installation;
	private FormPanel form;
	private FormBinding binding;
	protected BeanModelFactory propertyFactory;
	SimpleComboBox size;
	private int installCounter = 0;

	final SimpleComboBox simpleComboBox = new SimpleComboBox();
	final SimpleComboBox win10Type = new SimpleComboBox();
	final SimpleComboBox win10Lang = new SimpleComboBox();
	final SimpleComboBox win10Profile = new SimpleComboBox();

	private static final String FIELD_SPEC = "-18";

	public ClientProductPropertyWindow(final List<ClientJSO> clients, final InstallationJSO installation) {

		this.clients = clients;
		this.installation = installation;

		this.propertyFactory = BeanModelLookup.get().getFactory(ProductPropertyJSO.CLASS_NAME);

		//setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(340, 250);
		setHeadingHtml("Instalace: " + installation.getName());

		setLayout(new FitLayout());

		form = new FormPanel();
		form.setHeaderVisible(false);
		form.setLabelWidth(100);
		form.setBodyBorder(false);
		form.setWidth("100%");

		ContentPanel borderPanel = new ContentPanel();
		borderPanel.setLayout(new RowLayout(Style.Orientation.VERTICAL));
		borderPanel.setHeaderVisible(false);
		borderPanel.setBodyBorder(false);
		borderPanel.setBodyStyle("background: transparent;");

		form.add(borderPanel, new FormData("100% 100%"));

		ContentPanel formPanel = new ContentPanel();
		FormLayout formPanelLayout = new FormLayout();
		formPanel.setLayout(formPanelLayout);
		formPanel.setHeaderVisible(false);
		formPanel.setBodyBorder(false);
		formPanel.setBodyStyle("background: transparent;");

		borderPanel.add(formPanel, new RowData(1, -1));

		simpleComboBox.setTriggerAction(ComboBox.TriggerAction.ALL);
		simpleComboBox.add("Bez změny (podle klienta)");
		simpleComboBox.add("1 disk / 1 partition (výchozí)");
		simpleComboBox.add("1 disk / 2 partition");
		simpleComboBox.add("2 disky / 2 partition");

		simpleComboBox.setFieldLabel("Typ instalace");
		simpleComboBox.setSelection(simpleComboBox.getStore().getRange(0, 1));

		/*

		final CheckBoxGroup group = new CheckBoxGroup();
		group.setFieldLabel("Možnosti D:\\");
		group.setOrientation(Style.Orientation.VERTICAL);

		final CheckBox checkBox = new CheckBox();
		checkBox.setBoxLabel("Vytvořit DATA partition");
		checkBox.setName("data_partition_create");

		final CheckBox checkBox2 = new CheckBox();
		checkBox2.setBoxLabel("Zachovat existující DATA partition");
		checkBox2.setName("data_partition_preserve");

		group.add(checkBox);
		group.add(checkBox2);

		*/

		size = new SimpleComboBox();
		size.setTriggerAction(ComboBox.TriggerAction.ALL);
		for (int i=10; i<=100; i=i+10) {
			size.add(i+"%");
		}
		size.add("10G");
		size.add("20G");
		size.add("30G");
		size.add("40G");
		size.add("50G");
		size.add("100G");
		size.add("200G");
		size.add("500G");

		size.setFieldLabel("Velikost C:\\");
		size.setSelection(size.getStore().getRange(15, 16));
		size.setVisible(false);

		final Text info = new Text();
		final String installNoChange = "Instalace klientů bude provedena podle jejich posledního (nebo výchozího) nastavení pro " + installation.getName() + ".";
		info.setText(installNoChange);
		info.setAutoHeight(true);

		formPanel.add(simpleComboBox);
		formPanel.add(size);
                if (installation.getId().startsWith("win10")) {
			win10Type.setTriggerAction(ComboBox.TriggerAction.ALL);
			
			win10Type.add("Education");
			win10Type.add("Pro");
			win10Type.add("Home");
			win10Type.setFieldLabel("Varianta");
			//win10Type.setSelection(win10Type.getStore().getRange(1, 2));

			win10Lang.setTriggerAction(ComboBox.TriggerAction.ALL);
			win10Lang.add("Čeština");
			win10Lang.add("Angličtina");
			win10Lang.setFieldLabel("Jazyk");
			//win10Lang.setSelection(win10Lang.getStore().getRange(0, 1));

			win10Profile.setTriggerAction(ComboBox.TriggerAction.ALL);
			win10Profile.add("C:\\Users");
			win10Profile.add("D:\\Users");
			win10Profile.setFieldLabel("Umístění profilu");
			//win10Profile.setSelection(win10Profile.getStore().getRange(0, 1));
			
			win10Type.disable();
			win10Lang.disable();
			win10Profile.disable();
			formPanel.add(win10Type);
			formPanel.add(win10Lang);
			formPanel.add(win10Profile);
                }
		
		
		formPanel.add(info);
		//formPanel.add(group);
		//formPanel.setAutoHeight(true);

		simpleComboBox.addSelectionChangedListener(new SelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent selectionChangedEvent) {

				if (simpleComboBox.getSelectedIndex() == 0) {
					info.setText(installNoChange);
					win10Profile.disable();
				} else if (simpleComboBox.getSelectedIndex() == 1) {
					info.setText("Všichni vybraní klienti budou přeinstalováni s nastavením: 1 disk / 1 partition (C:/ = 100%)");
					win10Profile.disable();
				} else if (simpleComboBox.getSelectedIndex() == 2) {
					info.setText("Všichni vybraní klienti budou přeinstalováni s nastavením: 1 disk / 2 partition (C:/ = ? GB a D:/ = zbytek prostoru disku)");
					win10Profile.enable();
				} else if (simpleComboBox.getSelectedIndex() == 3) {
					info.setText("Všichni vybraní klienti budou přeinstalováni s nastavením: 2 disky / 2 partition (C:/ = 100%)");
					win10Profile.enable();
				}

				size.setVisible(simpleComboBox.getSelectedIndex() == 2);
				
				if (simpleComboBox.getSelectedIndex() == 0) {
					win10Type.disable();
					win10Lang.disable();
				} else {
					win10Type.enable();
					win10Lang.enable();
				}
				/*

				if (simpleComboBox.getSelectedIndex() == 0) {
					checkBox.setValue(false);
					checkBox2.setValue(false);
				} else if (simpleComboBox.getSelectedIndex() == 1) {
					checkBox.setValue(true);
					checkBox2.setValue(true);
				} else if (simpleComboBox.getSelectedIndex() == 2) {
					checkBox.setValue(false);
					checkBox2.setValue(true);
				}

				*/

			}

			@Override
			public void handleEvent(SelectionChangedEvent se) {
				super.handleEvent(se);
			}

		});

		add(form, new FitData());

		binding = new FormBinding(form, true);
		binding.setUpdateOriginalValue(true);

		Button buttonInstall = new Button("Instalovat");
		buttonInstall.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {

				ClientProductPropertyWindow.this.hide(ce.getButton());

				MessageBox.confirm("Provést instalaci?", "Opravdu provést instalaci " + installation.getName() + " na "
						+ clients.size() + " počítačů?", new Listener<MessageBoxEvent>() {
					@Override
					public void handleEvent(MessageBoxEvent be) {
						if (be.getButtonClicked() == null) {
							return;
						}
						if (!Dialog.YES.equals(be.getButtonClicked().getItemId())) {
							return;
						}

						installClientsWithProperties(clients, installation);

					}

				});

			}
		});
		addButton(buttonInstall);

		Button buttonCancel = new Button("Storno");
		//buttonCancel.setIcon(IconHelper.createStyle("Cancel"));
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ClientProductPropertyWindow.this.hide(ce.getButton());
			}
		});
		addButton(buttonCancel);

	}

	/**
	 * Perform client installation.
	 *
	 * This will update OPSI and start installing selected product (OS) to all passed Clients.
	 *
	 * @param clients Clients to start installation for
	 * @param installation Product (OS) to install.
	 */
	private void installClientsWithProperties(List<ClientJSO> clients, final InstallationJSO installation) {

		ClientService clientService = ClientService.getInstance();
		form.mask();

		for (final ClientJSO client : clients) {

			// CUSTOMIZED INSTALLATIONS
			if (simpleComboBox.getSelectedIndex() != 0) {

				List<ProductPropertyJSO> properties = constructProperties(client, installation);

				clientService.updateClientProductProperties(properties, new RemoteRequestCallback<Object>() {
					@Override
					public void onRequestSuccess(Object productProperties) {

						ClientService.getInstance().installClient(client, installation, new RemoteRequestCallback<Object>() {
							@Override
							public void onRequestSuccess(Object v) {
								Info.display("Instalace spuštěna", client.getName());
								unmaskForm();
							}

							@Override
							public void onRequestFailed(Throwable th) {
								MessageDialog.showError("Chyba při spouštění instalace klienta "+client.getName(), th.getMessage());
								unmaskForm();
							}
						});

					}

					@Override
					public void onRequestFailed(Throwable th) {
						MessageDialog.showError("Chyba při ukládání nastavení instalace "+client.getName()+". Instalace nebyla spuštěna!", th.getMessage());
						unmaskForm();
					}
				});

			} else {

				// DEFAULT INSTALLATION
				ClientService.getInstance().installClient(client, installation, new RemoteRequestCallback<Object>() {
					@Override
					public void onRequestSuccess(Object v) {
						Info.display("Instalace spuštěna", client.getName());
						unmaskForm();
					}

					@Override
					public void onRequestFailed(Throwable th) {
						MessageDialog.showError("Chyba při spouštění instalace klienta "+client.getName(), th.getMessage());
						unmaskForm();
					}
				});

			}

			installCounter++;

		}

	}

	/**
	 * Try to unmask the form but only once all requests are finished.
	 */
	private void unmaskForm() {
		if (installCounter > 0) installCounter--;
		form.unmask();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		loadData();
	}

	/**
	 * Method used to load data from opsi - for now unused
	 */
	protected void loadData() {

		ClientService clientService = ClientService.getInstance();

		clientService.listClientProductProperties(this.clients.get(0), new RemoteRequestCallback<List<ProductPropertyJSO>>() {
			@Override
			public void onRequestSuccess(List<ProductPropertyJSO> productProperties) {
				form.unmask();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání nastavení instalace", th.getMessage());
				form.unmask();
			}
		});

	}

	/**
	 * This method creates list of properties based on selection in form
	 *
	 * @return list of properties
	 */
	protected List<ProductPropertyJSO> constructProperties(ClientJSO client, InstallationJSO installation) {

		List<ProductPropertyJSO> properties = new ArrayList<ProductPropertyJSO>();

		// when "skip config and use original values" is selected
		if (simpleComboBox.getSelectedIndex() == 0) return properties;

		ProductPropertyJSO windows_partition_size = new JSONObject().getJavaScriptObject().cast();
		windows_partition_size.setObjectId(client.getName());
		windows_partition_size.setProductId(installation.getId());
		windows_partition_size.setPropertyId("windows_partition_size");
		windows_partition_size.addValue((simpleComboBox.getSelectedIndex() == 2) ? ((String)size.getSimpleValue()) : "100%");

		ProductPropertyJSO data_partition_preserve = new JSONObject().getJavaScriptObject().cast();
		data_partition_preserve.setObjectId(client.getName());
		data_partition_preserve.setProductId(installation.getId());
		data_partition_preserve.setPropertyId("data_partition_preserve");
		
		if (installation.getId().startsWith("win10")) {
			data_partition_preserve.addValue((simpleComboBox.getSelectedIndex() == 2) ? "if_possible" : "never");
		} else {
			data_partition_preserve.addValue((simpleComboBox.getSelectedIndex() == 2) ? "always" : "never");
		}

		ProductPropertyJSO data_partition_create = new JSONObject().getJavaScriptObject().cast();
		data_partition_create.setObjectId(client.getName());
		data_partition_create.setProductId(installation.getId());
		data_partition_create.setPropertyId("data_partition_create");
		data_partition_create.addValue("true");

		properties.add(windows_partition_size);
		properties.add(data_partition_preserve);
		properties.add(data_partition_create);
		
		if (installation.getId().startsWith("win10")) {
			ProductPropertyJSO image_name = new JSONObject().getJavaScriptObject().cast();
			image_name.setObjectId(client.getName());
			image_name.setProductId(installation.getId());
			image_name.setPropertyId("imagename");
			if (win10Type.getSimpleValue().toString() == "Education") {
				image_name.addValue("Windows 10 Education");
			} else if (win10Type.getSimpleValue().toString() == "Pro") {
				image_name.addValue("Windows 10 Pro");
			} else if (win10Type.getSimpleValue().toString() == "Home") {
				image_name.addValue("Windows 10 Home");
			} else { // default will be Pro version of Win10
				image_name.addValue("Windows 10 Pro");
			}

			ProductPropertyJSO system_keyboard_layout = new JSONObject().getJavaScriptObject().cast();
			system_keyboard_layout.setObjectId(client.getName());
			system_keyboard_layout.setProductId(installation.getId());
			system_keyboard_layout.setPropertyId("system_keyboard_layout");
			ProductPropertyJSO system_language = new JSONObject().getJavaScriptObject().cast();
			system_language.setObjectId(client.getName());
			system_language.setProductId(installation.getId());
			system_language.setPropertyId("system_language");
			if (win10Lang.getSimpleValue().toString() == "Čeština") {
				system_keyboard_layout.addValue("0405:00000405;0405:00000409");
				system_language.addValue("cs-CZ");
			} else if (win10Lang.getSimpleValue().toString() == "Angličtina") {
				system_keyboard_layout.addValue("0409:00000409;0409:00000405");
				system_language.addValue("en-US");
			} else { // default will be czech language
				system_keyboard_layout.addValue("0405:00000405;0405:00000409");
				system_language.addValue("cs-CZ");
			}
			
			ProductPropertyJSO orgname = new JSONObject().getJavaScriptObject().cast();
			orgname.setObjectId(client.getName());
			orgname.setProductId(installation.getId());
			orgname.setPropertyId("orgname");
			if (win10Profile.getSimpleValue().toString().length() > 0) {
				orgname.addValue(win10Profile.getSimpleValue().toString());
			} else { // default will be C:\Users
				orgname.addValue("C:\\Users");
			}

			properties.add(image_name);
			properties.add(system_keyboard_layout);
			properties.add(system_language);
			properties.add(orgname);
		}

		return properties;

	}

}