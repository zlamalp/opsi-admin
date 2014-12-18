package cz.muni.ucn.opsi.api.opsiClient;

import java.util.List;

/**
 * Represents property values for a product on a host in OPSI.
 *
 * @author Martin Kuba makub@ics.muni.cz
 */
public class ProductPropertyState {

    String objectId; //host e.g. test.host.cz
    String productId; // product  e.g. win2003
    String propertyId; // property e.g. data_partition_create, data_partition_preserve, windows_partition_size
    List<String> values;

    public ProductPropertyState() {
    }

    public ProductPropertyState(String objectId, String productId, String propertyId, List<String> values) {
        this.objectId = objectId;
        this.productId = productId;
        this.propertyId = propertyId;
        this.values = values;
    }

    /**
     * Getter for serialization.
     * @return  constant "ProductPropertyState"
     */
    public String getType() {
        return "ProductPropertyState";
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public List<String> getValues() {
        return values;
    }

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
