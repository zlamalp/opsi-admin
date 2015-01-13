package cz.muni.ucn.opsi.core.group;

import cz.muni.ucn.opsi.core.client.ClientHibernate;
import cz.u2.eis.valueObjects.hibernate.HibernateValueObject;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Hibernate representation of Group object.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 * @see cz.muni.ucn.opsi.api.group.Group
 */
@Entity(name = "Group")
@Table(name = "GROUPS", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "u_name")})
public class GroupHibernate extends HibernateValueObject {

	private static final long serialVersionUID = 3151715438105784467L;

	private String name;
	private String role;

	private List<ClientHibernate> clients;

	/**
	 * Create new instance
	 */
	public GroupHibernate() {
		super();
	}

	/**
	 * Create new instance
	 *
	 * @param uuid UUID of Group
	 */
	public GroupHibernate(UUID uuid) {
		super(uuid);
	}

	/**
	 * Get name of Group
	 *
	 * @return the Group name
	 */
	@Length(min = 1, max = 50)
	@Column(length = 50)
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
	@Length(max = 50)
	@Column(length = 50)
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

	/**
	 * Get list of Clients in this Group
	 *
	 * @return List of clients in Group
	 */
	@OneToMany(mappedBy = "group")
	public List<ClientHibernate> getClients() {
		return clients;
	}

	/**
	 * Set Clients to Group
	 *
	 * @param clients the clients to set
	 */
	public void setClients(List<ClientHibernate> clients) {
		this.clients = clients;
	}

}