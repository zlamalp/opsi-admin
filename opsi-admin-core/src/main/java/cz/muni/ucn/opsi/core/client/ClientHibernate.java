/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

import cz.muni.ucn.opsi.core.group.GroupHibernate;
import cz.u2.eis.valueObjects.hibernate.HibernateValueObject;

/**
 * Hibernate representation of Client object.
 *
 * @see cz.muni.ucn.opsi.api.client.Client
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Entity(name="Client")
@Table(name="CLIENTS")
public class ClientHibernate extends HibernateValueObject {

	private static final long serialVersionUID = 1472266922881562059L;

	private String name;
	private String description;
	private String notes;
	private String ipAddress;
	private String macAddress;
	private GroupHibernate group;

	/**
	 * Create new instance
	 */
	public ClientHibernate() {
		super();
	}

	/**
	 * Create new instance
	 *
	 * @param uuid UUID of Client
	 */
	public ClientHibernate(UUID uuid) {
		super(uuid);
	}

	/**
	 * Get Client name (usually hostname)
	 *
	 * @return the name (usually hostname)
	 */
	@NaturalId
	@Length(min=1, max=50)
	@NotNull
	@Column(length=50)
	public String getName() {
		return name;
	}

	/**
	 * Set Client name (usually hostname)
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get Client description
	 *
	 * @return the description
	 */
	@Length(max=250)
	@Column(length=250)
	public String getDescription() {
		return description;
	}

	/**
	 * Set Client description
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get Client notes
	 *
	 * @return the notes
	 */
	@Length(max=250)
	@Column(length=250)
	public String getNotes() {
		return notes;
	}

	/**
	 * Set Client notes
	 *
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Get Client IP address
	 *
	 * @return the IP address
	 */
	@Length(max=15)
	@Pattern(regexp="[0-2]?[0-9]{1,2}\\.[0-2]?[0-9]{1,2}\\.[0-2]?[0-9]{1,2}\\.[0-2]?[0-9]{1,2}")
	@Column(length=15)
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Set Client IP address
	 *
	 * @param ipAddress the IP address to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Get Client MAC address
	 *
	 * @return the MAC address
	 */
	@Length(max=17)
	@Pattern(regexp="[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}")
	@Column(length=17)
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Set Client MAC address
	 *
	 * @param macAddress the MAC address to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * Get Group this Client belongs to.
	 *
	 * @return the Group
	 */
	@ManyToOne
	@NotNull
	public GroupHibernate getGroup() {
		return group;
	}

	/**
	 * Set Group this Client belongs to.
	 *
	 * @param group the Group to set
	 */
	public void setGroup(GroupHibernate group) {
		this.group = group;
	}

}