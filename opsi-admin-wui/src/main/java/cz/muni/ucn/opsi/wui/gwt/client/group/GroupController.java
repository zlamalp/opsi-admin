package cz.muni.ucn.opsi.wui.gwt.client.group;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * Controller for handling app events associated with Groups.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupController extends Controller {

	// Available events for Groups
	public static final EventType GROUPS = new EventType();
	public static final EventType GROUP_NEW = new EventType();
	public static final EventType GROUP_EDIT = new EventType();
	public static final EventType GROUP_DELETE = new EventType();

	// Views associated with events
	private GroupsView groupsView;
	private GroupEditView groupEditView;

	/**
	 * Create controller for handling app events associated with Groups.
	 */
	public GroupController() {
		registerEventTypes(GroupController.GROUPS);
		registerEventTypes(GroupController.GROUP_NEW);
		registerEventTypes(GroupController.GROUP_EDIT);
		registerEventTypes(GroupController.GROUP_DELETE);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (GroupController.GROUPS == type) {
			showGroups(event);
		} else if (GroupController.GROUP_NEW == type) {
			groupNew(event);
		} else if (GroupController.GROUP_EDIT == type) {
			groupEdit(event);
		} else if (GroupController.GROUP_DELETE == type) {
			groupDelete(event);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			onLifecycleEvent(event);
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		groupsView = new GroupsView(this);
		groupEditView = new GroupEditView(this);
	}

	/**
	 * Forward event to Groups view
	 *
	 * @param event Event to pass
	 */
	private void showGroups(AppEvent event) {
		forwardToView(groupsView, event);
	}

	/**
	 * Forward event to GroupNew view
	 *
	 * @param event Event to pass
	 */
	private void groupNew(AppEvent event) {
		forwardToView(groupEditView, event);
	}

	/**
	 * Forward event to GroupEdit view
	 *
	 * @param event Event to pass
	 */
	private void groupEdit(AppEvent event) {
		forwardToView(groupEditView, event);
	}

	/**
	 * Forward event to GroupDelete view
	 *
	 * @param event Event to pass
	 */
	private void groupDelete(AppEvent event) {
		forwardToView(groupsView, event);
	}

	/**
	 * Handle app wide life-cycle events
	 *
	 * @param event Event to pass
	 */
	private void onLifecycleEvent(AppEvent event) {
		LifecycleEventJSO le = (LifecycleEventJSO) event.getData();
		if (!"cz.muni.ucn.opsi.api.group.Group".equals(le.getBeanClass())) {
			return;
		}
		forwardToView(groupsView, event);
	}

}