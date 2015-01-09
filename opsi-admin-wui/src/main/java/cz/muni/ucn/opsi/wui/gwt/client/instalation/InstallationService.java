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
 * @author Jan Dosoudil
 *
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
	 *
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
	 *
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
	 *
	 */
	public void saveInstallations(List<InstallationJSO> instalations, RemoteRequestCallback<Object> callback) {
		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + INSTALLATION_SAVE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONArray instalaceJson = transform(instalations);

		String data = instalaceJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * @param installations
	 * @return
	 */
	private JSONArray transform(List<InstallationJSO> installations) {
		JSONArray jsonArray = new JSONArray();
		for (InstallationJSO i : installations) {
			jsonArray.set(jsonArray.size(), transform(i));
		}
		return jsonArray;
	}

	/**
	 * @param i
	 * @return
	 */
	private JSONValue transform(InstallationJSO i) {
		JSONObject jsonObject = new JSONObject(i);
		jsonObject.put("$H", null);
		return jsonObject;
	}

	/**
	 * @param text
	 * @return
	 */
	protected List<InstallationJSO> transformInstallation(String text) {

        JsArray<InstallationJSO> array = InstallationJSO.fromJSONArray(text);
        List<InstallationJSO> insts = new ArrayList<InstallationJSO>();
        for(int i = 0; i < array.length(); i++) {
                insts.add(array.get(i));
        }

        return insts;
	}

}