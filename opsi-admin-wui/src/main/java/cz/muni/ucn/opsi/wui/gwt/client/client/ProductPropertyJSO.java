package cz.muni.ucn.opsi.wui.gwt.client.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import com.google.gwt.core.client.JsArrayString;
import cz.muni.ucn.opsi.wui.gwt.client.beanModel.BeanModelTag;

/**
 * JSO wrapper around ProductProperty object.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ProductPropertyJSO extends JavaScriptObject implements BeanModelTag {

	public static final String CLASS_NAME = "cz.muni.ucn.opsi.wui.gwt.client.client.ProductPropertyState";

	protected ProductPropertyJSO() {
	}

	/**
	 * Get Clients name associated with this ProductProperty (usually hostname)
	 *
	 * @see cz.muni.ucn.opsi.api.client.Client
	 *
	 * @return Name of Client associated with this ProductProperty
	 */
	public final native String getObjectId() /*-{
		return this.objectId;
	}-*/;

	/**
	 * Get ID property of Installation object associated with this ProductProperty
	 *
	 * @see cz.muni.ucn.opsi.api.instalation.Installation
	 *
	 * @return ID property of Installation associated with this ProductProperty
	 */
	public final native String getProductId() /*-{
		return this.productId;
	}-*/;

	/**
	 * Get ID (name) of this ProductProperty
	 *
	 * @return ID (name) of this ProductProperty
	 */
	public final native String getPropertyId() /*-{
		return this.propertyId;
	}-*/;

	/**
	 * Get values set for this ProductProperty
	 *
	 * @return List of values set for this ProductProperty
	 */
	public final native JsArrayString getValues() /*-{
		return this.values;
	}-*/;

	/**
	 * Get object type
	 *
	 * @return "ProductPropertyState"
	 */
	public final native String getType() /*-{
		return this.type;
	}-*/;

	/**
	 * Set object ID which is mapped to "name" of client
	 *
	 * @param objectId name of client this property belongs to
	 */
	public final native void setObjectId(String objectId) /*-{
		this.objectId = objectId;
	}-*/;

	/**
	 * Set product ID which is mapped to "id" of product e.g. 'win2003'.
	 *
	 * @param productId name of client this property belongs to
	 */
	public final native void setProductId(String productId) /*-{
		this.productId = productId;
	}-*/;

	/**
	 * Set property ID which is mapped to "name" of property to set, e.g. 'data_partition_create', 'data_partition_preserve', 'windows_partition_size'
	 *
	 * @param propertyId name of client this property belongs to
	 */
	public final native void setPropertyId(String propertyId) /*-{
		this.propertyId = propertyId;
	}-*/;

	/**
	 * Add value to values list
	 *
	 * @param val value to add to list
	 */
	public final native void addValue(String val) /*-{
		if (!this.values) this.values = new Array();
		if (typeof this.values === 'undefined') this.values = new Array();
		if (this.values === null) this.values = new Array();
		this.values.push(val);
	}-*/;

	/**
	 * Setter used to set bean name (use it before sending object to server)
	 */
	public final native void setType() /*-{
		this.type = "ProductPropertyState";
	}-*/;

	/**
	 * Get instance of this object from JSON string
	 *
	 * @param source JSON source
	 * @return Object parsed from JSON
	 */
	public static final native ProductPropertyJSO fromJSON(String source) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
		return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
	}-*/;

	/**
	 * Get instance of JsArray this objects from JSON string
	 *
	 * @param source JSON source
	 * @return List of objects parsed from JSON
	 */
	public static final native JsArray<ProductPropertyJSO> fromJSONArray(String source) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(source);
		return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
	}-*/;

}