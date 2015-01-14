package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelComparer;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.DualListField.Mode;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelFactory;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelLookup;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * Window for managing Installation
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class InstallationWindow extends Window {

	private BeanModelFactory factory;
	private InstallationConstants installationConstants;
	private Button buttonSave;
	private ListStore<BeanModel> fromStore;
	private ListStore<BeanModel> toStore;
	private boolean loadedFrom = false;
	private boolean loadedTo = false;
	private DualListField<BeanModel> lists;

	/**
	 * Create new instance of installation Window
	 */
	public InstallationWindow() {

		factory = BeanModelLookup.get().getFactory(InstallationJSO.CLASS_NAME);
		installationConstants = GWT.create(InstallationConstants.class);

		// set window properties
		setMinimizable(true);
		setMaximizable(true);
		setHeadingHtml("Správa instalací");
		setSize(840, 400);
		setLayout(new FitLayout());

		// create new toolbar
		ToolBar toolbar = createToolbar();
		setTopComponent(toolbar);

		// set model provider
		ModelKeyProvider<BeanModel> keyProvider = new ModelKeyProvider<BeanModel>() {
			@Override
			public String getKey(BeanModel model) {
				return model.get("id");
			}
		};
		ModelComparer<BeanModel> comparer = new ModelComparer<BeanModel>() {

			@Override
			public boolean equals(BeanModel m1, BeanModel m2) {
				if (m1 == m2) {
					return true;
				}
				if (m1 == null || m2 == null) {
					return false;
				}
				return m1.get("id").equals(m2.get("id"));
			}
		};

		// lists of installations
	    lists = new DualListField<BeanModel>();
	    lists.setMode(Mode.APPEND);
	    lists.setFieldLabel("instalace");

	    ListField<BeanModel> from = lists.getFromList();
	    from.setDisplayField("name");
	    fromStore = new ListStore<BeanModel>();
	    fromStore.sort("name", SortDir.ASC);
	    fromStore.setKeyProvider(keyProvider);
	    fromStore.setModelComparer(comparer);
	    from.setStore(fromStore);

	    ListField<BeanModel> to = lists.getToList();
	    to.setDisplayField("name");
	    toStore = new ListStore<BeanModel>();
	    toStore.sort("name", SortDir.ASC);
	    toStore.setKeyProvider(keyProvider);
	    toStore.setModelComparer(comparer);
	    to.setStore(toStore);

		lists.mask(GXT.MESSAGES.loadMask_msg());
	    InstallationService groupService = InstallationService.getInstance();

		// load all installations
		groupService.listInstallationsAll(new RemoteRequestCallback<List<InstallationJSO>>() {
			@Override
			public void onRequestSuccess(List<InstallationJSO> instalations) {
				List<BeanModel> groupModels = factory.createModel(instalations);
				fromStore.removeAll();
				fromStore.add(groupModels);
				loadedFrom = true;
				updateState();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání seznamu všech instalací", th.getMessage());
			}
		});

		// load available installations
		groupService.listInstallations(new RemoteRequestCallback<List<InstallationJSO>>() {
			@Override
			public void onRequestSuccess(List<InstallationJSO> instalations) {
				List<BeanModel> groupModels = factory.createModel(instalations);
				toStore.removeAll();
				toStore.add(groupModels);
				loadedTo = true;
				updateState();
			}

			@Override
			public void onRequestFailed(Throwable th) {
				MessageDialog.showError("Chyba při získávání seznamu instalací", th.getMessage());
			}
		});

		add(lists);

	}

	/**
	 * Move installations between stores (allowed / not allowed)
	 */
	protected void updateState() {
		if (!loadedFrom || !loadedTo) {
			return;
		}
		for (int i = 0; i < toStore.getCount(); i++) {
			BeanModel m = toStore.getAt(i);
			fromStore.remove(m);
		}
		lists.unmask();
	}

	/**
	 * Create Toolbar buttons
	 *
	 * @return Instance of ToolBar
	 */
	private ToolBar createToolbar() {

		ToolBar toolbar = new ToolBar();

		buttonSave = new Button(installationConstants.getInstallationsSave());
		buttonSave.setIcon(IconHelper.createStyle("save"));
		buttonSave.setData("event", InstallationController.INSTALATIONS_SAVE);
		buttonSave.addSelectionListener(new SaveButtonListener());
		toolbar.add(buttonSave);

		return toolbar;

	}

	/**
	 * Handle app-wide life-cycle events. This method ensures that data about clients are up-to-date.
	 *
	 * @param le life-cycle events
	 */
	public void onLifecycleEvent(LifecycleEventJSO le) {

		// LIFE-CYCLE EVENTS ARE NOT SUPPORTED ON INSTALLATIONS
		BeanModel model = factory.createModel(le.getBean());
		if (LifecycleEventJSO.CREATED == le.getEventType()) {
			//store.add(model);
		} else if (LifecycleEventJSO.MODIFIED == le.getEventType()) {
			//store.update(model);
		} else if (LifecycleEventJSO.DELETED == le.getEventType()) {
			//store.remove(model);
		}

	}

	/**
	 * Save button listener
	 *
	 * @author Jan Dosoudil
	 */
	private final class SaveButtonListener extends SelectionListener<ButtonEvent> {
		@Override
		public void componentSelected(ButtonEvent ce) {
			List<BeanModel> installations = toStore.getModels();

			InstallationService installationService = InstallationService.getInstance();
			List<InstallationJSO> install = new ArrayList<InstallationJSO>();
			for (BeanModel beanModel : installations) {
				install.add((InstallationJSO) beanModel.getBean());
			}
			installationService.saveInstallations(install, new RemoteRequestCallback<Object>() {
				@Override
				public void onRequestSuccess(Object v) {
					Info.display("Seznam instalací byl uložen", "");
				}
				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze uložit seznam instalací", th.getMessage());
				}
			});
		}
	}

}