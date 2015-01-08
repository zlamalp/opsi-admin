package cz.muni.ucn.opsi.wui.gwt.client.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;

import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;
import cz.muni.ucn.opsi.wui.gwt.client.instalation.InstalaceJSO;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequest;
import cz.muni.ucn.opsi.wui.gwt.client.remote.RemoteRequestCallback;

/**
 * Client side API for calls related to the Clients.
 *
 * @see cz.muni.ucn.opsi.wui.remote.client.ClientController for server side of this API
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientService {

	private static final ClientService INSTANCE = new ClientService();

	private static final String CLIENT_LIST_URL = "remote/clients/list";
	private static final String CLIENT_CREATE_URL = "remote/clients/create";
	private static final String CLIENT_EDIT_URL = "remote/clients/edit";
	private static final String CLIENT_SAVE_URL = "remote/clients/save";
	private static final String CLIENT_DELETE_URL = "remote/clients/delete";
	private static final String CLIENT_INSTALL_URL = "remote/clients/install";
	private static final String CLIENT_IMPORT_LIST_URL = "remote/clients/import/list";
	private static final String CLIENT_HARDWARE_URL = "remote/clients/hardware/list";
	private static final String CLIENT_PRODUCT_PROPERTY_LIST_URL = "remote/clients/productProperty/list";
	private static final String CLIENT_PRODUCT_PROPERTY_UPDATE_URL = "remote/clients/productProperty/update";

	private ClientService() {
	}

	public static ClientService getInstance() {
		return INSTANCE;
	}

	/**
	 * List Clients in group.
	 *
	 * @param group group to list clients for
	 * @param callback Callback to handle response
	 */
	public void listClients(GroupJSO group, RemoteRequestCallback<List<ClientJSO>> callback) {

		RemoteRequest<List<ClientJSO>> request = new RemoteRequest<List<ClientJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_LIST_URL) +
				"?groupUuid=" + URL.encodeQueryString(group.getUuid())) {

			@Override
			protected List<ClientJSO> transformResponse(String text) {
				return transformArrayClient(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * Create an empty client object for group.
	 *
	 * THIS METHOD DOESN'T CREATE CLIENT IN OPSI !!
	 * It just returns Client object with generated UUID and group.
	 *
	 * @param group group to create client for
	 * @param callback Callback to handle response
	 */
	public void createClient(GroupJSO group, RemoteRequestCallback<ClientJSO> callback) {

		RemoteRequest<ClientJSO> request = new RemoteRequest<ClientJSO>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_CREATE_URL) +
				"?groupUuid=" + URL.encodeQueryString(group.getUuid())) {

			@Override
			protected ClientJSO transformResponse(String text) {
				return transform(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * Retrieve Client for editing from opsi-admin app (not OPSI).
	 * This is used to ensure data are up-to date in edit client forms.
	 *
	 * @param client Client to edit.
	 * @param callback Callback to handle response.
	 */
	public void editClient(ClientJSO client, RemoteRequestCallback<ClientJSO> callback) {

		RemoteRequest<ClientJSO> request = new RemoteRequest<ClientJSO>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_EDIT_URL)) {

			@Override
			protected ClientJSO transformResponse(String text) {
				return transform(text);
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuilder requestData = new StringBuilder();
		requestData.append("uuid=");
		requestData.append(URL.encodeQueryString(client.getUuid()));
		request.setRequestData(requestData.toString());

		request.execute(callback);

	}

	/**
	 * Save client to OPSI server.
	 *
	 * Before sending client object make sure you retrieved new one by either #editClient or #createClient method.
	 *
	 * @param client Client to be saved
	 * @param callback Callback to handle response
	 */
	public void saveClient(ClientJSO client, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.PUT,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_SAVE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(client);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * Delete client form OPSI server.
	 *
	 * @param client Client to be deleted
	 * @param callback Callback to handle response
	 */
	public void deleteClient(ClientJSO client, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.DELETE,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_DELETE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(client);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * Trigger installation of some product (OS) to Client in OPSI.
	 *
	 * @param client Client to install to.
	 * @param instalace Product (OS) to install.
	 * @param callback Callback to handle response
	 */
	public void installClient(ClientJSO client, InstalaceJSO instalace, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.PUT,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_INSTALL_URL +
						"?instalaceId=" + instalace.getId())) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		JSONObject userJson = transform(client);

		String data = userJson.toString();
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * List clients for import from OPSI server.
	 *
	 * @param group Group to import clients into
	 * @param master TRUE = import from local OPSI / FALSE = import from remote OPSI
	 * @param callback Callback to handle response
	 */
	public void listClientsForImport(GroupJSO group, boolean master, RemoteRequestCallback<List<ClientJSO>> callback) {

		RemoteRequest<List<ClientJSO>> request = new RemoteRequest<List<ClientJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_IMPORT_LIST_URL) +
				"?groupUuid=" + URL.encodeQueryString(group.getUuid()) +
				"&opsi=" + (master ? "0" : "1" )) {

			@Override
			protected List<ClientJSO> transformResponse(String text) {
				return transformArrayClient(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * @param client
	 * @param callback
	 */
	public void listHardware(ClientJSO client, RemoteRequestCallback<List<HardwareJSO>> callback) {

		RemoteRequest<List<HardwareJSO>> request = new RemoteRequest<List<HardwareJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_HARDWARE_URL) +
				"?uuid=" + URL.encodeQueryString(client.getUuid())) {

			@Override
			protected List<HardwareJSO> transformResponse(String text) {
				return transformArrayHardware(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * List ProductProperties of client from OPSI. They represents configuration of some product for client.
	 * If OPSI is using default values (they were not set manually from this app) no properties are returned !!
	 *
	 * @param client Client to get product properties for.
	 * @param callback Callback to handle response
	 */
	public void listClientProductProperties(ClientJSO client, RemoteRequestCallback<List<ProductPropertyJSO>> callback) {

		RemoteRequest<List<ProductPropertyJSO>> request = new RemoteRequest<List<ProductPropertyJSO>>(RequestBuilder.GET,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_PRODUCT_PROPERTY_LIST_URL) +
						"?client=" + URL.encodeQueryString(client.getName())) {

			@Override
			protected List<ProductPropertyJSO> transformResponse(String text) {
				GWT.log(text);
				return transformArrayProductProperty(text);
			}
		};

		request.execute(callback);

	}

	/**
	 * Set new or update existing ProductProperties in OPSI. Relation to each Product or Client is stored
	 * inside ProductProperty object.
	 *
	 * @param properties ProductProperties to set (may be for different clients, products etc.)
	 * @param callback Callback to handle response
	 */
	public void updateClientProductProperties(List<ProductPropertyJSO> properties, RemoteRequestCallback<Object> callback) {

		RemoteRequest<Object> request = new RemoteRequest<Object>(RequestBuilder.POST,
				URL.encode(GWT.getHostPageBaseURL() + CLIENT_PRODUCT_PROPERTY_UPDATE_URL)) {

			@Override
			protected Object transformResponse(String text) {
				return null;
			}
		};

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		String data = transform(properties);
		request.setRequestData(data);
		request.setHeader("Content-Type", "application/json");

		request.execute(callback);

	}

	/**
	 * Transform JSON to list of ClientJSO
	 *
	 * @param json json to parse
	 * @return List of ClientJSO
	 */
	protected List<ClientJSO> transformArrayClient(String json) {
        JsArray<ClientJSO> array = ClientJSO.fromJSONArray(json);
        List<ClientJSO> clients = new ArrayList<ClientJSO>();
        for(int i = 0; i < array.length(); i++) {
                clients.add(array.get(i));
        }
        return clients;
	}

	/**
	 * Transform JSON to list of HardwareJSO
	 *
	 * @param json json to parse
	 * @return List of HardwareJSO
	 */
	protected List<HardwareJSO> transformArrayHardware(String json) {
		JsArray<HardwareJSO> array = HardwareJSO.fromJSONArray(json);
		List<HardwareJSO> hardware = new ArrayList<HardwareJSO>();
		for(int i = 0; i < array.length(); i++) {
			hardware.add(array.get(i));
		}
		return hardware;
	}

	/**
	 * Transform JSON to list of ProductPropertyJSO
	 *
	 * @param json json to parse
	 * @return List of HardwareJSO
	 */
	protected List<ProductPropertyJSO> transformArrayProductProperty(String json) {
		JsArray<ProductPropertyJSO> array = ProductPropertyJSO.fromJSONArray(json);
		List<ProductPropertyJSO> products = new ArrayList<ProductPropertyJSO>();
		for(int i = 0; i < array.length(); i++) {
			products.add(array.get(i));
		}
		return products;
	}

    /**
     * Transform JSON to ClientJSO object
     *
     * @param json json to parse
     * @return ClientJSO object
     */
    private ClientJSO transform(String json) {
    	return ClientJSO.fromJSON(json);
    }

	/**
	 * Transform ClientJSO to JSON object
	 *
	 * @param client
	 * @return JSON object
	 */
    private JSONObject transform(ClientJSO client) {
    	JSONObject jsonObject = new JSONObject(client);
    	jsonObject.put("$H", null);
    	return jsonObject;
    }

	/**
	 * Transform list of ProductPropertyJSO to JSON array of objects
	 *
	 * @param properties
	 * @return JSON array of objects
	 */
	private String transform(List<ProductPropertyJSO> properties) {

		String data = "["; // "{\"properties\"=[";
		for (int i=0; i<properties.size(); i++) {
			data += transform(properties.get(i)).toString();
			if (i+1 < properties.size()) data += ",";
		}
		data += "]";
		//data += "]}";

		return data;

		/*
		for (int index=0; index<properties.length(); index++) {
			JSONObject object = new JSONObject(properties.get(index));
			object.put("$H", null);
			jsonArray.set(index, object);
		}

		return jsonArray;
		*/
	}

	/**
	 * Transform ProductPropertyJSO to JSON object
	 *
	 * @param property
	 * @return JSON object
	 */
	private JSONObject transform(ProductPropertyJSO property) {
		JSONObject jsonObject = new JSONObject(property);
		jsonObject.put("$H", null);
		return jsonObject;
	}

}