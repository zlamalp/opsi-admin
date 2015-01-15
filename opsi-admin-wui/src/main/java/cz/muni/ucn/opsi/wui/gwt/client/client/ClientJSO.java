package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;
import cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO;

/**
 * JSO wrapper around Client object.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ClientJSO extends JavaScriptObject implements BeanModelTag {

	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.client.ClientJSO";

	/*
	private String uuid;
	private String name;
	private String description;
	private String notes;
	private String ipAddress;
	private String macAddress;
	private GroupJSO group;
	*/

	protected ClientJSO() {
	}

	/**
	 * Get instance of this object from JSON string
	 *
	 * @param source JSON source
	 * @return Object parsed from JSON
	 */
	public static final native ClientJSO fromJSON(String source) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
		return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
	}-*/;

	/**
	 * Get instance of JsArray of this objects from JSON string
	 *
	 * @param source JSON source
	 * @return List of objects parsed from JSON
	 */
	public static final native JsArray<ClientJSO> fromJSONArray(String source) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
		return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
	}-*/;

	/**
	 * Get UUID of Client. It's value is generated by opsi-admin-wui RPC (not OPSI !!)
	 *
	 * @return the UUID
	 */
	public final native String getUuid() /*-{
		return this.uuid;
	}-*/;

	/**
	 * Set UUID of Client. It's value must be generated on the server (RPC side), but not in OPSI.
	 *
	 * @param uuid the UUID to set
	 */
	public final native void setUuid(String uuid) /*-{
		this.uuid = uuid;
	}-*/;

	/**
	 * Get Client name (usually hostname)
	 *
	 * @return the name (usually hostname)
	 */
	public final native String getName() /*-{
		return this.name;
	}-*/;

	/**
	 * Set Client name (usually hostname)
	 *
	 * @param name the name to set
	 */
	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	/**
	 * Get Client description
	 *
	 * @return the description
	 */
	public final native String getDescription() /*-{
		return this.description;
	}-*/;

	/**
	 * Set Client description
	 *
	 * @param description the description to set
	 */
	public final native void setDescription(String description) /*-{
		this.description = description;
	}-*/;

	/**
	 * Get Client notes
	 *
	 * @return the notes
	 */
	public final native String getNotes() /*-{
		return this.notes;
	}-*/;

	/**
	 * Set Client notes
	 *
	 * @param notes the notes to set
	 */
	public final native void setNotes(String notes) /*-{
		this.notes = notes;
	}-*/;

	/**
	 * Get Client IP address
	 *
	 * @return the IP address
	 */
	public final native String getIpAddress() /*-{
		return this.ipAddress;
	}-*/;

	/**
	 * Set Client IP address
	 *
	 * @param ipAddress the IP address to set
	 */
	public final native void setIpAddress(String ipAddress) /*-{
		this.ipAddress = ipAddress;
	}-*/;

	/**
	 * Get Client MAC address
	 *
	 * @return the MAC address
	 */
	public final native String getMacAddress() /*-{
		return this.macAddress;
	}-*/;

	/**
	 * Set Client MAC address
	 *
	 * @param macAddress the MAC address to set
	 */
	public final native void setMacAddress(String macAddress) /*-{
		this.macAddress = macAddress;
	}-*/;

	/**
	 * Get Group this Client belongs to.
	 *
	 * @return the Group
	 */
	public final native GroupJSO getGroup() /*-{
		return this.group;
	}-*/;

	/**
	 * Set Group this Client belongs to.
	 *
	 * @param group the Group to set
	 */
	public final native void setGroup(GroupJSO group) /*-{
		this.group = group;
	}-*/;

}