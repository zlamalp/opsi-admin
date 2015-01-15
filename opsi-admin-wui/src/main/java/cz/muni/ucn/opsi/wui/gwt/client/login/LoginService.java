package cz.muni.ucn.opsi.wui.gwt.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

/**
 * Client side API for calls related to the logging-in and logging-out.
 *
 * @see cz.muni.ucn.opsi.wui.remote.authentication.LoginStatusController for server side of this API
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LoginService {

	private static final String LOGOUT_URL = "j_spring_security_logout";

	private static final LoginService INSTANCE = new LoginService();

	private LoginService() {
	}

	public static LoginService getInstance() {
		return LoginService.INSTANCE;
	}

	/**
	 * Logout user from server.
	 *
	 * @param callback Callback to handle response
	 */
	public void logout(final LogoutCallback callback) {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(GWT.getHostPageBaseURL() + LOGOUT_URL));

		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				if (200 != response.getStatusCode()) {
					GWT.log("Server odpověděl chybou při odhlášení: " + response.getStatusText());
					callback.onLogoutFailed(response.getStatusText());
				} else {
					callback.onLogoutOk();
				}

			}

			@Override
			public void onError(Request request, Throwable exception) {
				GWT.log("Nelze odeslat požadavek na odhlášení", exception);
				callback.onLogoutFailed(exception.getMessage());
			}

		});

		try {
			builder.send();
		} catch (RequestException e) {
			GWT.log("Chyba při odesílání požadavku", e);
			callback.onLogoutFailed(e.getMessage());
		}


	}

	/**
	 * Interface for logout callback class
	 *
	 * @author Jan Dosoudil
	 */
	public interface LogoutCallback {

		/**
		 * Called when logout is successful
		 */
		void onLogoutOk();

		/**
		 * Called when logout fails
		 */
		void onLogoutFailed(String message);

	}

}