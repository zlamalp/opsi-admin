package cz.muni.ucn.opsi.api.opsiClient;

import java.util.List;

/**
 * Represents property values for a product on a host in OPSI.
 *
 * @author Martin Kuba makub@ics.muni.cz
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ProductPropertyState {

	String objectId; //host e.g. test.host.cz
	String productId; // product  e.g. win2003
	String propertyId; // property e.g. data_partition_create, data_partition_preserve, windows_partition_size
	List<String> values;

	/**
	 * New instance of ProductPropertyState
	 */
	public ProductPropertyState() {
	}

	/**
	 * New instance of ProductPropertyState
	 *
	 * @param objectId   Name of associated Client (usually hostname)
	 * @param productId  ID of associated Installation (NetBootProduct in OPSI)
	 * @param propertyId ID (name) of ProductProperty
	 * @param values     List of values to set
	 */
	public ProductPropertyState(String objectId, String productId, String propertyId, List<String> values) {
		this.objectId = objectId;
		this.productId = productId;
		this.propertyId = propertyId;
		this.values = values;
	}

	/**
	 * Getter for serialization.
	 *
	 * @return constant "ProductPropertyState"
	 */
	public String getType() {
		return "ProductPropertyState";
	}

	/**
	 * Get Clients name associated with this ProductProperty (usually hostname)
	 *
	 * @return Name of Client associated with this ProductProperty
	 * @see cz.muni.ucn.opsi.api.client.Client
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * Set Clients name associated with this ProductProperty (usually hostname)
	 *
	 * @param objectId Name of Client associated with this ProductProperty
	 * @see cz.muni.ucn.opsi.api.client.Client
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * Get ID property of Installation object associated with this ProductProperty
	 *
	 * @return ID property of Installation associated with this ProductProperty
	 * @see cz.muni.ucn.opsi.api.instalation.Installation
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * Set ID property of Installation object associated with this ProductProperty
	 *
	 * @param productId ID property of Installation associated with this ProductProperty
	 * @see cz.muni.ucn.opsi.api.instalation.Installation
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * Get ID (name) of this ProductProperty
	 *
	 * @return ID (name) of this ProductProperty
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * Set ID (name) of this ProductProperty
	 *
	 * @param propertyId ID (name) of this ProductProperty
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Get values set for this ProductProperty
	 *
	 * @return List of values set for this ProductProperty
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * Set values set for this ProductProperty
	 *
	 * @param values Values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "ProductPropertyState{" +
				"objectId='" + objectId + '\'' +
				", productId='" + productId + '\'' +
				", propertyId='" + propertyId + '\'' +
				", values=" + values +
				'}';
	}

}