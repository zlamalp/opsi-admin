package cz.muni.ucn.opsi.wui.gwt.client.group;

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * View for handling events on creating and editing Groups.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupEditView extends View {

	// multiple editing windows
	private Map<GroupJSO, GroupEditWindow> windows = new HashMap<GroupJSO, GroupEditWindow>();

	/**
	 * Create instance of the View
	 *
	 * @param controller
	 */
	public GroupEditView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (GroupController.GROUP_NEW == type) {
			editGroup(null);
		} else if (GroupController.GROUP_EDIT == type) {
			editGroup((GroupJSO) event.getData());
		}
	}

	/**
	 * Method ensuring async loading of UI
	 *
	 * @param group Group to create / edit
	 */
	private void editGroup(final GroupJSO group) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				editGroupAsync(group);
			}

			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Operaci nelze provést", reason.getMessage());
			}
		});

	}

	/**
	 * Call to retrieve data from OPSI. THIS ENSURE THAT DATA IN FORM ARE UP-TO-DATE !!
	 * For new groups, server side creates UUID.
	 *
	 * @param group Group to create/edit
	 */
	protected void editGroupAsync(final GroupJSO group) {

		GroupService groupService = GroupService.getInstance();
		if (null == group) {
			groupService.createGroup(new RemoteRequestCallback<GroupJSO>() {
				@Override
				public void onRequestSuccess(GroupJSO group) {
					editGroupWindow(group, true);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze založit skupinu", th.getMessage());
				}
			});
		} else {
			groupService.editGroup(group, new RemoteRequestCallback<GroupJSO>() {
				@Override
				public void onRequestSuccess(GroupJSO group) {
					editGroupWindow(group, false);
				}

				@Override
				public void onRequestFailed(Throwable th) {
					MessageDialog.showError("Nelze upravit skupinu "+group.getName(), th.getMessage());
				}
			});
		}

	}

	/**
	 * Create and attach Group editing window to the desktop.
	 * It should be called fro async method.
	 *
	 * @param group Group to create/edit
	 * @param newGroup TRUE = creation of new group / FALSE = editing of existing
	 */
	protected void editGroupWindow(GroupJSO group, boolean newGroup) {
		GroupEditWindow w;
		if (windows.containsKey(group)) {
			w = windows.get(group);
			w.setGroupModel(group);
		} else {
			w = createWindow(newGroup);
			windows.put(group, w);
			Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, w);
		}
		w.setGroupModel(group);
		if (w.isVisible()) {
			w.toFront();
		} else {
			w.show();
		}
	}

	/**
	 * Create new instance of Window for editing Group
	 *
	 * @param newGroup TRUE = creation of new group / FALSE = editing of existing
	 * @return new instance of window
	 */
	private GroupEditWindow createWindow(boolean newGroup) {
		return new GroupEditWindow(newGroup);
	}

}