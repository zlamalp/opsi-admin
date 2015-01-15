package cz.muni.ucn.opsi.wui.gwtLogin.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

/**
 * Client side API for calls related to the logging-in for "LoginApp"
 *
 * @see cz.muni.ucn.opsi.wui.remote.authentication.LoginStatusController for server side of this API
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LoginService {

	private static final String LOGIN_URL = "j_spring_security_check";
	private static final String LOGIN_STATUS_URL = "remote/loginStatus";

	private static final LoginService INSTANCE = new LoginService();

	private LoginService() {
	}

	public static LoginService getInstance() {
		return LoginService.INSTANCE;
	}

	/**
	 * Log-in user to the application - authenticated against LDAP
	 *
	 * @param username UCO from MU (ActiveDirectory)
	 * @param password secondary password
	 * @param callback Callback handling response
	 */
	public void login(String username, String password, final LoginCallback callback) {

		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, GWT.getHostPageBaseURL() + LOGIN_URL);
		rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
		rb.setRequestData("j_username=" + URL.encode(username)
				+ "&j_password=" + URL.encode(password)
				+ "&ajax=true");

		rb.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response.getStatusCode() != Response.SC_OK) {
					callback.onLoginFailed(response.getStatusText());
					return;
				}

				JSONObject jsonObject = (JSONObject) JSONParser.parseStrict(response.getText());
				JSONString resultString = (JSONString) jsonObject.get("status");

				if ("OK".equalsIgnoreCase(resultString.stringValue())) {
					callback.onLoginOk(jsonObject);
				} else {
					String message = ((JSONString)jsonObject.get("message")).stringValue();
					callback.onLoginFailed(message);
				}

			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onLoginFailed(exception.getMessage());
			}

		});

		try {
			rb.send();
		} catch (RequestException e) {
		}

	}

	/**
	 * Perform check if user is authenticated
	 *
	 * @param callback Callback handling response
	 */
	public void getLoginStatus(final LoginStatusCallback callback) {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(GWT.getHostPageBaseURL() + LOGIN_STATUS_URL));

		builder.setCallback(new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 != response.getStatusCode()) {
						GWT.log("Server odpovedel chybou pri ziskani stavu prihlasseni: " + response.getStatusText());
						callback.onStatusFailed(response.getStatusText());
						return;
					}

					JSONObject object = (JSONObject) JSONParser.parseStrict(response.getText());
					callback.onStatusOk(object);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					GWT.log("Nelze odeslat pozadavek na zjisteni stavu prihlaseni", exception);
					callback.onStatusFailed(exception.getMessage());
				}
			});
		try {
			builder.send();
		} catch (RequestException e) {
			GWT.log("Chyba při odesílání požadavku", e);
			callback.onStatusFailed(e.getMessage());
		}

	}

	/**
	 * Interface for login callback class
	 *
	 * @author Jan Dosoudil
	 */
	public interface LoginCallback {

		/**
		 * Called when login is OK
		 *
		 * @param loginStatus logging-in status
		 */
		void onLoginOk(JSONObject loginStatus);

		/**
		 * Called when login fails
		 *
		 * @param message error message
		 */
		void onLoginFailed(String message);

	}

	/**
	 * Interface for login-status callback class
	 *
	 * @author Jan Dosoudil
	 */
	public interface LoginStatusCallback {

		/**
		 * Called when user is logged in OK
		 *
		 * @param status status
		 */
		void onStatusOk(JSONObject status);

		/**
		 * Called when user is not logged in or error
		 *
		 * @param message error message
		 */
		void onStatusFailed(String message);

	}

}