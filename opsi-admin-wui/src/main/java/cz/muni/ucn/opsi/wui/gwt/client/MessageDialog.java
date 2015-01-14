package cz.muni.ucn.opsi.wui.gwt.client;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;

/**
 * Helping class used to simplified code when showing message dialog window.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class MessageDialog {

	/**
	 * Show dialog for errors.
	 *
	 * @param title Dialog title
	 * @param message Dialog message
	 */
	public static void showError(String title, String message) {

		showError(title, message, new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
			}
		});

	}

	/**
	 * Show dialog for errors with custom event
	 *
	 * @param title Dialog title
	 * @param message Dialog message
	 * @param listener Custom event listener
	 */
	public static void showError(String title, String message, Listener<MessageBoxEvent> listener) {

		if ("Access is denied".equals(message)) message = "Přístup zamítnut. Potřebujete práva administátora.";
		MessageBox.alert(title, "<p>"+message, listener);

	}

	/**
	 * Show dialog for informative messages.
	 *
	 * @param title Dialog title
	 * @param message Dialog message
	 */
	public static void showMessage(String title, String message) {
		MessageBox.info(title, "<p>"+message, new Listener<MessageBoxEvent>(){
			@Override
			public void handleEvent(MessageBoxEvent be) {
			}
		});
	}

}