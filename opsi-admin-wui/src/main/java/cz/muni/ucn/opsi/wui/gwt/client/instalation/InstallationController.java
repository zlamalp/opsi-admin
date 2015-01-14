package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * Controller for handling app events associated with Installations.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class InstallationController extends Controller {

	public static final EventType INSTALLATIONS = new EventType();
	public static final EventType INSTALLATIONS_SAVE = new EventType();

	private InstallationView installationView;

	/**
	 * Create new instance of Controller
	 */
	public InstallationController() {
		registerEventTypes(InstallationController.INSTALLATIONS);
		registerEventTypes(InstallationController.INSTALLATIONS_SAVE);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (InstallationController.INSTALLATIONS == type) {
			showInstallations(event);
		} else if (InstallationController.INSTALLATIONS_SAVE == type) {
				saveInstallations(event);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			onLifecycleEvent(event);
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		installationView = new InstallationView(this);
	}

	/**
	 * Forward event to InstallationView
	 *
	 * @param event Event to pass
	 */
	private void showInstallations(AppEvent event) {
		forwardToView(installationView, event);
	}

	/**
	 * Forward event to InstallationView
	 *
	 * @param event Event to pass
	 */
	private void saveInstallations(AppEvent event) {
		forwardToView(installationView, event);
	}

	/**
	 * Handle app wide life-cycle events
	 *
	 * @param event Event to pass
	 */
	private void onLifecycleEvent(AppEvent event) {
        LifecycleEventJSO le = (LifecycleEventJSO) event.getData();
        if (!"cz.muni.ucn.opsi.api.instalation.Installation".equals(le.getBeanClass())) {
                return;
        }
		forwardToView(installationView, event);
	}

}