package cz.muni.ucn.opsi.wui.gwtLogin.client.login;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.KeyboardEvents;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.json.client.JSONObject;

/**
 * Dialog for handling user login
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LoginDialog extends Dialog {

	protected TextField<String> userName;
	protected TextField<String> password;
	protected Button reset;
	protected Button login;
	protected Status status;
	private FormPanel formPanel;
	private LoginService loginService = LoginService.getInstance();

	/**
	 * Create new instance of login dialog
	 */
	public LoginDialog() {

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(100);
		layout.setDefaultWidth(200);
		setLayout(layout);

		setButtonAlign(HorizontalAlignment.LEFT);
		setButtons("");
		setIcon(IconHelper.createStyle("user"));
		setHeadingHtml("OPSI Admin");
		setModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(340);
		setResizable(false);

		KeyListener keyListener = new KeyListener() {
			@Override
			public void componentKeyUp(ComponentEvent event) {
				validate();
			}
		};

		formPanel = new FormPanel();
		formPanel.setBorders(false);
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);

		userName = new TextField<String>();
		userName.setMinLength(4);
		userName.setFieldLabel("Uživatel");
		userName.addKeyListener(keyListener);
		userName.setName("j_username");
		userName.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyPress(ComponentEvent event) {
				if(event.getKeyCode() == KeyboardEvents.Enter.getEventCode()) {
					password.focus();
				}
			}
		});
		formPanel.add(userName);

		password = new TextField<String>();
		password.setMinLength(4);
		password.setPassword(true);
		password.setFieldLabel("Heslo");
		password.addKeyListener(keyListener);
		password.setName("j_password");
		password.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyPress(ComponentEvent event) {
				if(event.getKeyCode() == KeyboardEvents.Enter.getEventCode()) {
					onSubmit();
				}
			}
		});
		formPanel.add(password);

		add(formPanel);

		setFocusWidget(userName);
	}

	@Override
	protected void createButtons() {

		super.createButtons();
		status = new Status();
		status.setBusy("prosím čekejte...");
		status.hide();
		status.setAutoWidth(true);
		getButtonBar().add(status);

		getButtonBar().add(new FillToolItem());

		reset = new Button("Reset");
		reset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				userName.reset();
				password.reset();
				validate();
				userName.focus();
			}

		});

		login = new Button("Přihlásit");
		login.disable();
		login.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				onSubmit();
			}
		});

		addButton(reset);
		addButton(login);

	}

	/**
	 * Check if field has value
	 *
	 * @param field field to check
	 * @return TRUE = has value / FALSE = is empty
	 */
	protected boolean hasValue(TextField<String> field) {
		return field.getValue() != null && field.getValue().length() > 0;
	}

	/**
	 * Validate form
	 */
	protected void validate() {
		login.setEnabled(hasValue(userName) && hasValue(password));
	}

	/**
	 * Disable buttons onSubmiting the form
	 */
	protected void onSubmit() {

		status.show();
		getButtonBar().disable();

		loginService.login(userName.getValue(), password.getValue(), new LoginService.LoginCallback() {
			@Override
			public void onLoginOk(JSONObject loginStatus) {
				Dispatcher.forwardEvent(LoginController.LOGIN_OK, loginStatus);
				LoginDialog.this.hide();
				enableButtons();
			}

			@Override
			public void onLoginFailed(String message) {
				showError(message);
			}
		});

	}

	/**
	 * Show error message
	 *
	 * @param message error message
	 */
	private void showError(String message) {
		enableButtons();
		MessageBox.alert("Neúspěšné přihlášení", "Neplatné jméno nebo heslo<br/>\n"+message,
				new Listener<MessageBoxEvent>() {
					@Override
					public void handleEvent(MessageBoxEvent be) {
					}
				});
	}

	/**
	 * Enable buttons
	 */
	private void enableButtons() {
		getButtonBar().enable();
		status.hide();
		password.setValue("");
		validate();
		userName.focus();
		userName.selectAll();
	}

}