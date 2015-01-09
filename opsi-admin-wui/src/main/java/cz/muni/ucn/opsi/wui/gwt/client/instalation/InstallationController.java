/**
 *
 */
package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * @author Jan Dosoudil
 *
 */
public class InstallationController extends Controller {

	public static final EventType INSTALATIONS = new EventType();
	public static final EventType INSTALATIONS_SAVE = new EventType();

	private InstallationView installationView;

	/**
	 *
	 */
	public InstallationController() {
		registerEventTypes(InstallationController.INSTALATIONS);
		registerEventTypes(InstallationController.INSTALATIONS_SAVE);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (InstallationController.INSTALATIONS == type) {
			showInstallations(event);
		} else if (InstallationController.INSTALATIONS_SAVE == type) {
				saveInstallations(event);
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			onLifecycleEvent(event);
		}
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		installationView = new InstallationView(this);
	}

	/**
	 * @param event
	 */
	private void showInstallations(AppEvent event) {
		forwardToView(installationView, event);
	}

	/**
	 * @param event
	 */
	private void saveInstallations(AppEvent event) {
		forwardToView(installationView, event);
	}

	/**
	 * @param event
	 */
	private void onLifecycleEvent(AppEvent event) {
        LifecycleEventJSO le = (LifecycleEventJSO) event.getData();
        if (!"cz.muni.ucn.opsi.api.instalation.Instalation".equals(le.getBeanClass())) {
                return;
        }
		forwardToView(installationView, event);
	}

}
