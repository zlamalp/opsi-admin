package cz.muni.ucn.opsi.wui.gwt.client.group;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;

import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequest;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * Client side API for calls related to the Groups.
 *
 * @see cz.muni.ucn.opsi.wui.remote.group.GroupController for server side of this API
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupService {

	private static final GroupService INSTANCE = new GroupService();

	private static final String GROUPS_LIST_URL = "remote/groups/list";
	private static final String GROUP_CREATE_URL = "remote/groups/create";
	private static final String GROUP_EDIT_URL = "remote/groups/edit";
	private static final String GROUP_SAVE_URL = "remote/groups/save";
	private static final String GROUP_DELETE_URL = "remote/groups/delete";

	private GroupService() {
	}

	public static GroupService getInstance() {
		return INSTANCE;
	}

	/**
	 * List all groups in OPSI-ADMIN
	 *
	 * @param callback Callback to handle response
	 */
	public void listGroups(RemoteRequestCallback<List<GroupJSO>> callback) {

		RemoteRequest<List<GroupJSO>> listGroupsRequest = new RemoteRequest<List<GroupJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + GROUPS_LIST_URL)) {

			@Override
			protected List<GroupJSO> transformResponse(String text) {
				return transformArrayGroup(text);
			}
		};
		listGroupsRequest.execute(callback);

	}

	/**
	 * Create an empty client object for group.
	 *
	 * THIS METHOD DOESN'T CREATE GROUP IN OPSI-ADMIN !!
	 * It just returns Group object with generated UUID.
	 *
	 * @param callback Callback to handle response
	 */
	public void createGroup(RemoteRequestCallback<GroupJSO> callback) {

		RemoteRequest<GroupJSO> request = new RemoteRequest<GroupJSO>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_CREATE_URL)) {

			@Override
			protected GroupJSO transformResponse(String text) {
				return transform(text);
			}
		};
		request.execute(callback);

	}

	/**
	 * Retrieve Group for editing from opsi-admin app (not OPSI).
	 * This is used to ensure data are up-to date in edit group forms.
	 *
	 * @param group Group to edit.
	 * @param callback Callback to handle response.
	 */
	public void editGroup(GroupJSO group, RemoteRequestCallback<GroupJSO> callback) {

		RemoteRequest<GroupJSO> request = new RemoteRequest<GroupJSO>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_EDIT_URL)) {

			@Override
			protected GroupJSO transformResponse(String text) {
				return transform(text);
			}
		};
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuilder requestData = new StringBuilder();
		requestData.append("uuid=");
		requestData.append(URL.encodeQueryString(group.getUuid()));
		request.setRequestData(requestData.toString());

		request.execute(callback);

	}

	/**
	 * Save group to OPSI-ADMIN server.
	 *
	 * Before sending group object make sure you retrieved new one by either #editGroup or #createGroup method.
	 *
	 * @param group Group to be saved
	 * @param callback Callback to handle response
	 */
	public void saveGroup(GroupJSO group, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.PUT,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_SAVE_URL)) {
			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject groupJson = transform(group);

		String data = groupJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * Delete group from OPSI-admin server.
	 *
	 * @param group Group to be deleted
	 * @param callback Callback to handle response
	 */
	public void deleteGroup(GroupJSO group, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.DELETE,
				URL.encode(GWT.getHostPageBaseURL() + GROUP_DELETE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(group);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * Transform JSON to list of GroupJSO
	 *
	 * @param json json to parse
	 * @return List of GroupJSO
	 */
	protected List<GroupJSO> transformArrayGroup(String json) {
        JsArray<GroupJSO> array = GroupJSO.fromJSONArray(json);
        List<GroupJSO> users = new ArrayList<GroupJSO>();
        for(int i = 0; i < array.length(); i++) {
                users.add(array.get(i));
        }
        return users;
	}

	/**
	 * Transform JSON to GroupJSO object
	 *
	 * @param json json to parse
	 * @return GroupJSO object
	 */
    private GroupJSO transform(String json) {
    	return GroupJSO.fromJSON(json);
    }

	/**
	 * Transform GroupJSO to JSON object
	 *
	 * @param group group to convert
	 * @return JSON object
	 */
    private JSONObject transform(GroupJSO group) {
    	JSONObject jsonObject = new JSONObject(group);
    	jsonObject.put("$H", null);
    	return jsonObject;
    }

}