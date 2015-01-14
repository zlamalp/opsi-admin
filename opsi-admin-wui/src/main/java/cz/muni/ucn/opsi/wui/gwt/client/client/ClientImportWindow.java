package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelComparer;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelFactory;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * Window for importing clients.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientImportWindow extends Window {

	private ClientConstants clientConstants;
	//	private FormPanel form;

	// group to import clients into
	private GroupJSO group;
	// import from local or remote OPSI
	private boolean master;
	protected ListStore<BeanModel> clientStore;
	protected Grid<BeanModel> clientsGrid;
	protected BeanModelFactory clientFactory;
	// import counter
	private int importCount = 0;
	private Button buttonOK;
	private ClientImportWindow window = this;

	/**
	 * Create window for importing clients
	 *
	 * @param group Group to import clients into
	 * @param master TRUE = import from local OPSI / FALSE = import from remote OPSI
	 */
	public ClientImportWindow(GroupJSO group, boolean master) {

		this.group = group;
		this.master = master;

		clientConstants = GWT.create(ClientConstants.class);

		clientFactory = BeanModelLookup.get().getFactory(ClientJSO.CLASS_NAME);

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(700, 350);
		setHeadingHtml("Import klientů do skupiny " + group.getName());
//		setBodyStyle("padding: 0px; ");

//		FormLayout layout = new FormLayout();
//		layout.setLabelAlign(LabelAlign.LEFT);
//		setLayout(layout);
		setLayout(new FitLayout());

		clientStore = new ListStore<BeanModel>();
		clientStore.sort("name", SortDir.ASC);
		clientStore.setKeyProvider(new ModelKeyProvider<BeanModel>() {

			@Override
			public String getKey(BeanModel model) {
				return model.get("uuid");
			}
		});
		clientStore.setModelComparer(new ModelComparer<BeanModel>() {

			@Override
			public boolean equals(BeanModel m1, BeanModel m2) {
				if (m1 == m2) {
					return true;
				}
				if (m1 == null || m2 == null) {
					return false;
				}
				return m1.get("uuid").equals(m2.get("uuid"));
			}
		});

		ColumnConfig name = new ColumnConfig("name", clientConstants.getName(), 180);
		ColumnConfig description = new ColumnConfig("description", clientConstants.getDescription(), 80);
		ColumnConfig macAddress = new ColumnConfig("macAddress", clientConstants.getMacAddress(), 140);
		ColumnConfig notes = new ColumnConfig("notes", clientConstants.getNotes(), 180);
		ColumnConfig ipAddress = new ColumnConfig("ipAddress", clientConstants.getIpAddress(), 80);

		final CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();

		List<ColumnConfig> config = new ArrayList<ColumnConfig>();

		config.add(sm.getColumn());
		config.add(name);
		config.add(macAddress);
		config.add(description);
		config.add(notes);
		config.add(ipAddress);

		final ColumnModel cm = new ColumnModel(config);

		clientsGrid = new Grid<BeanModel>(clientStore, cm);
		clientsGrid.setBorders(true);
		clientsGrid.setColumnReordering(true);
		clientsGrid.setSelectionModel(sm);
		clientsGrid.addPlugin(sm);

		GridFilters filters = new GridFilters();
		filters.setLocal(true);

		filters.addFilter(new StringFilter("name"));
		filters.addFilter(new StringFilter("macAddress"));
		filters.addFilter(new StringFilter("description"));
		filters.addFilter(new StringFilter("notes"));
		filters.addFilter(new StringFilter("ipAddress"));

		clientsGrid.addPlugin(filters);

		add(clientsGrid, new FitData());

		clientsGrid.mask(GXT.MESSAGES.loadMask_msg());

		SelectionChangedListener<BeanModel> selectionListener = new SelectionChangedListener<BeanModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				int selectionSize = se.getSelection().size();
				if (selectionSize >= 1) {
					buttonOK.enable();
				} else {
					buttonOK.disable();
				}
			}
		};
		clientsGrid.getSelectionModel().addSelectionChangedListener(selectionListener);

		generateButtons();

	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Component#onLoad()
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
		loadData();
	}

	/**
	 * Retrieve data from OPSI for import
	 */
	protected void loadData() {

		ClientService clientService = ClientService.getInstance();

		clientService.listClientsForImport(this.group, master, new RemoteRequestCallback<List<ClientJSO>>() {
			@Override
			public void onRequestSuccess(List<ClientJSO> clients) {
				List<BeanModel> clientModels = clientFactory.createModel(clients);
				clientStore.removeAll();
				clientStore.add(clientModels);
				clientsGrid.unmask();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání seznamu klientů pro import", th.getMessage(), new Listener<MessageBoxEvent>() {
					@Override
					public void handleEvent(MessageBoxEvent be) {
						window.hide();
					}
				});
			}

		});

	}

	/**
	 * Generate Import (save) and cancel buttons
	 */
	private void generateButtons() {

		Button buttonCancel = new Button("Zavřít");
		buttonCancel.setIcon(IconHelper.createStyle("Cancel"));
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				ClientImportWindow.this.hide(ce.getButton());
			}
		});
		addButton(buttonCancel);

		buttonOK = new Button("Importovat vybrané položky");
		buttonOK.setIcon(IconHelper.createStyle("OK"));
		buttonOK.disable();
		buttonOK.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {

				clientsGrid.mask(GXT.MESSAGES.loadMask_msg());
				ClientImportWindow.this.disable();

				synchronizeState();

				ClientService clientService = ClientService.getInstance();

				List<BeanModel> selectedItems = clientsGrid.getSelectionModel().getSelectedItems();

				importCount = selectedItems.size();

				for (final BeanModel beanModel : selectedItems) {
					final ClientJSO client = beanModel.getBean();
					clientService.saveClient(client, new RemoteRequestCallback<Object>() {

						@Override
						public void onRequestSuccess(Object v) {
							if (--importCount <= 0) {
								ClientImportWindow.this.enable();
								clientsGrid.unmask();
							}
//							ClientImportWindow.this.hide(ce.getButton());
							ClientImportWindow.this.clientStore.remove(beanModel);
							Info.display("Klient importován", client.getName());
						}

						@Override
						public void onRequestFailed(Throwable th) {
							if (--importCount <= 0) {
								ClientImportWindow.this.enable();
								clientsGrid.unmask();
							}
							MessageDialog.showError("Nelze importovat klienta "+client.getName(), th.getMessage());
						}
					});
				}

			}
		});
		addButton(buttonOK);

	}

	/**
	 *
	 */
	protected void synchronizeState() {
	}

}