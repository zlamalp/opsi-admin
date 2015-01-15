package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequest;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * Client side API for calls related to the Installations.
 *
 * @see cz.muni.ucn.opsi.wui.remote.instalation.InstallationController for server side of this API
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class InstallationService {

	private static final InstallationService INSTANCE = new InstallationService();

	private static final String INSTALLATION_LIST_URL = "remote/installation/list";
	private static final String INSTALLATION_LIST_ALL_URL = "remote/installation/listAll";
	private static final String INSTALLATION_SAVE_URL = "remote/installation/save";

	private InstallationService() {
	}

	public static InstallationService getInstance() {
		return INSTANCE;
	}

	/**
	 * List available installations form OPSI admin.
	 *
	 * @param callback Callback to handle response
	 */
	public void listInstallations(RemoteRequestCallback<List<InstallationJSO>> callback) {
		RemoteRequest<List<InstallationJSO>> request = new RemoteRequest<List<InstallationJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + INSTALLATION_LIST_URL)) {

			@Override
			protected List<InstallationJSO> transformResponse(String text) {
				return transformInstallation(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * List all installations form OPSI server.
	 *
	 * @param callback Callback to handle response
	 */
	public void listInstallationsAll(RemoteRequestCallback<List<InstallationJSO>> callback) {
		RemoteRequest<List<InstallationJSO>> request = new RemoteRequest<List<InstallationJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + INSTALLATION_LIST_ALL_URL)) {

			@Override
			protected List<InstallationJSO> transformResponse(String text) {
				return transformInstallation(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * Save list of available installations to OPSI admin.
	 *
	 * @param installations Installations to save (allow for install)
	 * @param callback Callback to handle response
	 */
	public void saveInstallations(List<InstallationJSO> installations, RemoteRequestCallback<Object> callback) {
		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + INSTALLATION_SAVE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONArray installJson = transform(installations);

		String data = installJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * Transform list of InstallationJSO to JSONArray
	 *
	 * @param installations Installations to convert
	 * @return JSON array
	 */
	private JSONArray transform(List<InstallationJSO> installations) {
		JSONArray jsonArray = new JSONArray();
		for (InstallationJSO i : installations) {
			jsonArray.set(jsonArray.size(), transform(i));
		}
		return jsonArray;
	}

	/**
	 * Transform InstallationJSO to JSON object
	 *
	 * @param installation installation to convert
	 * @return JSON object
	 */
	private JSONValue transform(InstallationJSO installation) {
		JSONObject jsonObject = new JSONObject(installation);
		jsonObject.put("$H", null);
		return jsonObject;
	}

	/**
	 * Transform JSON to list of InstallationJSO
	 *
	 * @param json json to parse
	 * @return List of GroupJSO
	 */
	protected List<InstallationJSO> transformInstallation(String json) {
        JsArray<InstallationJSO> array = InstallationJSO.fromJSONArray(json);
        List<InstallationJSO> insts = new ArrayList<InstallationJSO>();
        for(int i = 0; i < array.length(); i++) {
                insts.add(array.get(i));
        }
        return insts;
	}

}