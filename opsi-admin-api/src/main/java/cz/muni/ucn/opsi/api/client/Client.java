package cz.muni.ucn.opsi.api.client;

import cz.muni.ucn.opsi.api.group.Group;
import cz.u2.eis.valueObjects.ValueObject;

import java.util.UUID;

/**
 * Object representing Client (machine to install SW on) in OPSI. It's stored both locally and in OPSI.
 * Access rights of users to specific Clients are managed by Groups.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 * @see cz.muni.ucn.opsi.api.group.Group for details about access rights
 */
public class Client extends ValueObject {

	private static final long serialVersionUID = -6575924560081692249L;

	private String name;
	private String description;
	private String notes;
	private String ipAddress;
	private String macAddress;
	private Group group;

	/**
	 * Create new instance
	 */
	public Client() {
		super();
	}

	/**
	 * Create new instance
	 *
	 * @param uuid UUID associated with Client
	 */
	public Client(UUID uuid) {
		super(uuid);
	}

	/**
	 * Get Client name (usually hostname)
	 *
	 * @return the name (usually hostname)
	 */
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
	 * Get Client IP address
	 *
	 * @return the IP address
	 */
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
	public Group getGroup() {
		return group;
	}

	/**
	 * Set Group this Client belongs to.
	 *
	 * @param group the Group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * Get Client notes
	 *
	 * @return the notes
	 */
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

}