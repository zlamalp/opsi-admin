package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;

/**
 * JSO wrapper around Hardware object from OPSI
 *
 * // FIXME and TODO - object is not supported on server
 *
 * @author Jan Dosoudil
 */
public class HardwareJSO extends JavaScriptObject implements BeanModelTag {

	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.client.HardwareJSO";

	protected HardwareJSO() {
	}

	/**
	 * Get instance of this object from JSON string
	 *
	 * @param source JSON source
	 * @return Object parsed from JSON
	 */
    public static final native HardwareJSO fromJSON(String source) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
        return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
    }-*/;

	/**
	 * Get instance of JsArray this objects from JSON string
	 *
	 * @param source JSON source
	 * @return List of objects parsed from JSON
	 */
    public static final native JsArray<HardwareJSO> fromJSONArray(String source) /*-{
        var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
        return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
    }-*/;


}
