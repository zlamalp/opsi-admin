package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;
import cz.muni.ucn.opsi.wui.gwt.client.event.CometController;
import cz.muni.ucn.opsi.wui.gwt.client.event.LifecycleEventJSO;

/**
 * View for handling events on listing Installations.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class InstallationView extends View {

	private InstallationWindow window;

	/**
	 * Create new instance of view
	 *
	 * @param controller
	 */
	public InstallationView(Controller controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.mvc.View#handleEvent(com.extjs.gxt.ui.client.mvc.AppEvent)
	 */
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (InstallationController.INSTALLATIONS == type) {
			showInstallations();
		} else if (CometController.LIFECYCLE_EVENT_TYPE == type) {
			LifecycleEventJSO lifecycleEventJSO = event.getData();
			onLifecycleEvent(lifecycleEventJSO);
		}
	}

	/**
	 * Method ensuring async loading of UI
	 */
	private void showInstallations() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				if (null == window) {
					window = new InstallationWindow();
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