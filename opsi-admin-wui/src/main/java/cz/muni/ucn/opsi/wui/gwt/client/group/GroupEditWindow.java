package cz.muni.ucn.opsi.wui.gwt.client.group;

import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.form.TextField;
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
 * Window used to edit Group details. It's used also to create new groups.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupEditWindow extends Window {

	private static final String FIELD_SPEC = "-18";

	private final boolean newGroup;
	private GroupConstants groupConstants;
	private FormPanel form;

	private FormBinding binding;

	private GroupJSO group;

	/**
	 * Create new instance of Window.
	 * Group model is set by #setGroupModel()
	 *
	 * @param newGroup TRUE = create new group / FALSE = edit existing group
	 */
	public GroupEditWindow(boolean newGroup) {

		this.newGroup = newGroup;

		groupConstants = GWT.create(GroupConstants.class);

		setIcon(IconHelper.createStyle("icon-grid"));
		setMinimizable(true);
		setMaximizable(true);
		setSize(400, 150);

		setLayout(new FitLayout());

		form = new FormPanel();
		form.setHeaderVisible(false);
		form.setLabelWidth(100);
		form.setBodyBorder(false);

		ContentPanel borderPanel = new ContentPanel();
		borderPanel.setLayout(new RowLayout(Orientation.VERTICAL));
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

		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel(groupConstants.getName());
		name.setMinLength(1);
		name.setAllowBlank(false);
		name.setAutoValidate(true);
		formPanel.add(name, new FormData(FIELD_SPEC));

		TextField<String> role = new TextField<String>();
		role.setName("role");
		role.setFieldLabel(groupConstants.getRole());
		formPanel.add(role, new FormData(FIELD_SPEC));

		add(form, new FitData());

		binding = new FormBinding(form, true);
		binding.setUpdateOriginalValue(true);

		generateButtons();

	}

	/**
	 * Generate buttons for form.
	 */
	private void generateButtons() {

		Button buttonCancel = new Button("Zrušit");
		buttonCancel.setIcon(IconHelper.createStyle("Cancel"));
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				GroupEditWindow.this.hide(ce.getButton());
			}
		});
		addButton(buttonCancel);

		Button buttonOK = new Button("Uložit");
		buttonOK.setIcon(IconHelper.createStyle("OK"));
		buttonOK.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(final ButtonEvent ce) {

				if (!validate()) {
					MessageBox.alert("Nelze uložit", "Formulář obsahuje chyby", new Listener<MessageBoxEvent>() {
						@Override
						public void handleEvent(MessageBoxEvent be) {
						}
					});
					return;
				}

				GroupEditWindow.this.disable();

				synchronizeState();

				GroupService groupService = GroupService.getInstance();
				groupService.saveGroup(group, new RemoteRequestCallback<Object>() {
					@Override
					public void onRequestSuccess(Object v) {
						GroupEditWindow.this.enable();
						GroupEditWindow.this.hide(ce.getButton());
						Info.display("Skupina vytvořena", group.getName());
					}

					@Override
					public void onRequestFailed(Throwable th) {
						GroupEditWindow.this.enable();
						MessageDialog.showError("Nelze uložit skupinu", th.getMessage());
					}
				});

			}
		});
		addButton(buttonOK);

	}

	/**
	 * Set Group model associated with this editing form.
	 *
	 * @param group Group to edit or create
	 */
	public void setGroupModel(GroupJSO group) {
		this.group = group;
		BeanModel model = BeanModelLookup.get().getFactory(GroupJSO.CLASS_NAME).createModel(group);
		binding.bind(model);
		updateHeading();
	}

	/**
	 * Update heading based on state - creating or editing group.
	 */
	private void updateHeading() {
		if (newGroup) {
			setHeadingHtml("Nová skupina");
		} else {
			setHeadingHtml("Úprava skupiny: " + group.getName());
		}
	}

	/**
	 *
	 */
	protected void synchronizeState() {
	}

	/**
	 * Validate form fields.
	 *
	 * @return TRUE = valid / FALSE = not valid
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