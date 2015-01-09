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
		MessageBox.alert(title, message, new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
			}});
	}

	/**
	 * Show dialog for informative messages.
	 *
	 * @param title Dialog title
	 * @param message Dialog message
	 */
	public static void showMessage(String title, String message) {
		MessageBox.info(title, message, new Listener<MessageBoxEvent>(){
			@Override
			public void handleEvent(MessageBoxEvent be) {
			}
		});
	}

}