package cz.muni.ucn.opsi.api.group;

import cz.u2.eis.valueObjects.ValueObject;

import java.util.UUID;

/**
 * Object representing Group. It's not supported by OPSI and is stored only locally in DB and AD (LDAP).
 * Access rights of users to specific Groups of Clients are managed through LDAP.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Group extends ValueObject {

	private static final long serialVersionUID = 377295439759900534L;

	private String name;
	private String role;

	/**
	 * Create new instance
	 */
	public Group() {
		super();
	}

	/**
	 * Create new instance
	 *
	 * @param uuid UUID associated with Group
	 */
	public Group(UUID uuid) {
		super(uuid);
	}

	/**
	 * Get name of Group
	 *
	 * @return the Group name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of Group
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get role associated with Group
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Set role associated with Group
	 *
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

}