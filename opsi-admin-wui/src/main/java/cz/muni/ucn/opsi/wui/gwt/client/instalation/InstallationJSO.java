package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;

/**
 * Object representing Installation, aka NetBootProduct in OPSI.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class InstallationJSO extends JavaScriptObject implements BeanModelTag {

	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.instalation.InstallationJSO";

	/*
	private String id;
	private String name;
	*/

	protected InstallationJSO() {
	}

	/**
	 * Get ID of installation
	 *
	 * @return the id
	 */
	public final native String getId() /*-{
		return this.id;
	}-*/;

	/**
	 * Set ID of installation
	 *
	 * @param id the id to set
	 */
	public final native void setId(String id) /*-{
		this.id = id;
	}-*/;

	/**
	 * Get name of Installation
	 *
	 * @return the name
	 */
	public final native String getName() /*-{
		return this.name;
	}-*/;

	/**
	 * Set name of Installation
	 *
	 * @param name the name to set
	 */
	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	/**
	 * Get instance of this object from JSON string
	 *
	 * @param source JSON source
	 * @return Object parsed from JSON
	 */
    public static final native InstallationJSO fromJSON(String source) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
        return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
    }-*/;

	/**
	 * Get instance of JsArray this objects from JSON string
	 *
	 * @param source JSON source
	 * @return List of objects parsed from JSON
	 */
    public static final native JsArray<InstallationJSO> fromJSONArray(String source) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
        return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
    }-*/;

}