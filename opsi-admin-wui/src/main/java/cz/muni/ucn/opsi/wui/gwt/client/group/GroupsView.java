package cz.muni.ucn.opsi.wui.gwt.client.group;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * View for handling events on listing and deleting groups Groups.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupsView extends View {

	private GroupsWindow window;

	/**
	 * Create new instance of view
	 *
	 * @param controller
	 */
	public GroupsView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (GroupController.GROUPS == type) {
			showGroups();
		} else if (GroupController.GROUP_DELETE == type) {
			GroupJSO group = event.getData();
			groupDelete(group);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			LifecycleEventJSO lifecycleEventJSO = (LifecycleEventJSO)event.getData();
			onLifecycleEvent(lifecycleEventJSO);
		}
	}

	/**
	 * Method ensuring async loading of UI
	 */
	private void showGroups() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				if (null == window) {
					window = new GroupsWindow();
				}
				Dispatcher.forwardEvent(DesktopController.WINDOW_CREATED, window);
				if (window.isVisible()) {
					window.toFront();
				} else {
					window.show();
				}
			}
			@Override
			public void onFailure(Throwable reason) {
				MessageDialog.showError("Akci nelze provést", reason.getMessage());
			}
		});

	}

	/**
	 * Call to delete group.
	 *
	 * @param group Group to delete
	 */
	private void groupDelete(final GroupJSO group) {
		MessageBox.confirm("Odstranit skupinu?", "Opravdu chcete odstranit skupinu " + group.getName() + " ?", new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				if (!Dialog.YES.equals(be.getButtonClicked().getItemId())) {
					return;
				}
				GroupService.getInstance().deleteGroup(group, new RemoteRequestCallback<Object>() {
					@Override
					public void onRequestSuccess(Object v) {
						Info.display("Skupina odstraněna", group.getName());
					}
					@Override
					public void onRequestFailed(Throwable th) {
						MessageDialog.showError("Chyba při ostraňování skupiny "+group.getName(), th.getMessage());
					}
				});
			}
		});

	}

	/**
	 * Handle app wide life-cycle events. Pass them to windows.
	 *
	 * @param lifecycleEventJSO Event to pass
	 */
	private void onLifecycleEvent(LifecycleEventJSO lifecycleEventJSO) {
		if (null == window) {
			return;
		}
		window.onLifecycleEvent(lifecycleEventJSO);
	}

}