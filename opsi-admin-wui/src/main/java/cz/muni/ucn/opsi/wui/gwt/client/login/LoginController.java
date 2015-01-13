package cz.muni.ucn.opsi.wui.gwt.client.login;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;

import cz.muni.ucn.opsi.wui.gwt.client.MessageDialog;

/**
 * Controller for handling app events associated with logging-off. Logging-in is handled by own GWT module "LoginApp"
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LoginController extends Controller {

	public static final EventType LOGOUT = new EventType();
	public static final EventType LOGGED_OUT = new EventType();


	/**
	 * New instance of controller
	 */
	public LoginController() {
		registerEventTypes(LoginController.LOGOUT);
		registerEventTypes(LoginController.LOGGED_OUT);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGOUT == type) {
			onLogoutEvent(event);
		}
	}

	/**
	 * Handle logout event
	 *
	 * @param event Event to handle
	 */
	protected void onLogoutEvent(AppEvent event) {

		LoginService service = LoginService.getInstance();
		service.logout(new LoginService.LogoutCallback() {

			@Override
			public void onLogoutOk() {
				Dispatcher.forwardEvent(LoginController.LOGGED_OUT);
			}

			@Override
			public void onLogoutFailed(String message) {
				MessageDialog.showError("Chyba při odhlašování", message);
			}
		});

	}

}