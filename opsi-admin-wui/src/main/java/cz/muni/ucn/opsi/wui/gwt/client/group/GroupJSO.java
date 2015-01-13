package cz.muni.ucn.opsi.wui.gwt.client.group;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;

/**
 * JSO wrapper around Group object.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupJSO extends JavaScriptObject implements BeanModelTag {

	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.group.GroupJSO";

	/*
	private String uuid;
	private String name;
	private String role;
	*/

	protected GroupJSO() {
	}

	/**
	 * Get UUID of this Group.
	 *
	 * @return the uuid
	 */
	public final native String getUuid() /*-{
		return this.uuid;
	}-*/;

	/**
	 * Set UUID of this Group
	 *
	 * @param uuid the uuid to set
	 */
	public final native  void setUuid(String uuid) /*-{
		this.uuid = uuid;
	}-*/;

	/**
	 * Get name of Group
	 *
	 * @return the Group name
	 */
	public final native String getName() /*-{
		return this.name;
	}-*/;

	/**
	 * Set name of Group
	 *
	 * @param name the name to set
	 */
	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	/**
	 * Get role associated with Group
	 *
	 * @return the role
	 */
	public final native String getRole() /*-{
		return this.role;
	}-*/;

	/**
	 * Set role associated with Group
	 *
	 * @param role the role to set
	 */
	public final native  void setRole(String role) /*-{
		this.role = role;
	}-*/;

	/**
	 * Get instance of this object from JSON string
	 *
	 * @param source JSON source
	 * @return Object parsed from JSON
	 */
	public static final native GroupJSO fromJSON(String source) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
		return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
	}-*/;

	/**
	 * Get instance of JsArray of this objects from JSON string
	 *
	 * @param source JSON source
	 * @return List of objects parsed from JSON
	 */
	public static final native JsArray<GroupJSO> fromJSONArray(String source) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
		return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
	}-*/;

}