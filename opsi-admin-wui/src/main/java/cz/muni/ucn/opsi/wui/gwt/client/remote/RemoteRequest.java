package cz.muni.ucn.opsi.wui.gwt.client.remote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Abstract class for remote requests
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public abstract class RemoteRequest<T> {

	private RequestBuilder builder;

	/**
	 * Create new instance of request
	 *
	 * @param method method to call
	 * @param url URL to call
	 */
	public RemoteRequest(Method method, String url) {
		super();
		builder = new RequestBuilder(method, url);
	}

	/**
	 * Set Request payload
	 *
	 * @param requestData payload
	 */
	public void setRequestData(String requestData) {
		builder.setRequestData(requestData);
	}

	/**
	 * Set header to request
	 *
	 * @param header Header name to set
	 * @param value Value to set to header
	 */
	public void setHeader(String header, String value) {
		builder.setHeader(header, value);
	}

	/**
	 * Get request builder
	 *
	 * @return the builder
	 */
	public RequestBuilder getBuilder() {
		return builder;
	}

	/**
	 * Execute callback (send request to server).
	 *
	 * @param callback Class handling this callback
	 */
	public void execute(final RemoteRequestCallback<T> callback) {

		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				try {
					T v = processResponse(request, response);
					callback.onRequestSuccess(v);
				} catch (Exception e) {
					GWT.log("onResponseReceived exception: " + e);
					callback.onRequestFailed(e);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				GWT.log("onError: " + exception);
				callback.onRequestFailed(exception);
			}
		});

		try {
			builder.send();
		} catch (RequestException e) {
			GWT.log("Chyba při odesílání požadavku", e);
			callback.onRequestFailed(e);
		}

	}

	/**
	 * Process server response
	 *
	 * @param request original request
	 * @param response server response
	 * @return expected response object
	 */
	protected T processResponse(Request request, Response response) {

		int statusCode = response.getStatusCode();
		if (200 != statusCode) {
			GWT.log("Server odpovedel chybou pri odeslani pozadavku: " + response.getStatusText());
			processError(request, response);
		}

		String text = response.getText();
		if ("".equals(text)) {
			return null;
		}
		return transformResponse(text);

	}

	/**
	 * Process error sent by server
	 *
	 * @param request original request
	 * @param response server response
	 */
	protected void processError(Request request, Response response) {

		String contentType = response.getHeader("Content-Type");
		String statusText = response.getStatusText();

		if (contentType.startsWith("application/json")) {
			JSONObject error = JSONParser.parseStrict(response.getText()).isObject();
			statusText = error.get("message").isString().stringValue();
		}
		throw new RemoteRequestException(statusText);

	}

	/**
	 * Transform string response to expected Object
	 *
	 * @param text server response
	 * @return expected object
	 */
	protected abstract T transformResponse(String text);

}