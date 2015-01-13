package cz.muni.ucn.opsi.wui.gwtLogin.client.login;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;

/**
 * View for handling login event
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LoginView extends View {

	private LoginDialog dialog;

	/**
	 * Create new instance
	 *
	 * @param controller
	 */
	public LoginView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (LoginController.LOGIN == type) {
			dialog.show();
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		dialog = new LoginDialog();
		dialog.setClosable(false);
	}

}