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

	public static final String CLASS_NAME = "cz.muni.ucn.opsi.api.opsiClient.ProductPropertyState";

	protected ProductPropertyJSO() {
	}

	public final native String getObjectId() /*-{
		return this.objectId;
	}-*/;

	public final native String getProductId() /*-{
		return this.productId;
	}-*/;

	public final native String getPropertyId() /*-{
		return this.propertyId;
	}-*/;

	public final native JsArrayString getValues() /*-{
		return this.values;
	}-*/;

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

	public final native void setType() /*-{
		this.type = "ProductPropertyState";
	}-*/;

	/**
	 * @param u
	 * @return
	 */
	public static final native ProductPropertyJSO fromJSON(String u) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
		return json.@com.google.gwt.json.client.JSONObject::getJavaScriptObject()();
	}-*/;

	/**
	 * @param u
	 * @return
	 */
	public static final native JsArray<ProductPropertyJSO> fromJSONArray(String u) /*-{
		var json = @com.google.gwt.json.client.JSONParser::parseStrict(Ljava/lang/String;)(u);
		return json.@com.google.gwt.json.client.JSONArray::getJavaScriptObject()();
	}-*/;

}