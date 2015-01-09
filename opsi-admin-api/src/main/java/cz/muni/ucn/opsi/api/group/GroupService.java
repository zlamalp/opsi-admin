package cz.muni.ucn.opsi.api.group;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.annotation.Secured;

/**
 * Service class used to manage Groups. Access right of users to specific groups are stored in AD (LDAP).
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface GroupService {

	/**
	 * Create new Group for Clients. It's not stored in DB.
	 * To save it use #saveGroup()
	 *
	 * @see #saveGroup
	 *
	 * @return Created Group with UUID set
	 */
	@Secured("ROLE_ADMIN")
	Group createGroup();

	/**
	 * Get Group for editing purpose by it's UUID.
	 * To save it use #saveGroup()
	 *
	 * @see #saveGroup
	 *
	 * @param uuid UUID of Group to edit
	 * @return Group to edit
	 */
	@Secured("ROLE_ADMIN")
	Group editGroup(UUID uuid);

	/**
	 * Save Group to DB. If new, Group must be created by #createGroup().
	 *
	 * @see #createGroup
	 *
	 * @param group Group to save
	 */
	@Secured("ROLE_ADMIN")
	void saveGroup(Group group);

	/**
	 * Delete Group from DB.
	 *
	 * @param group Group to delete
	 */
	@Secured("ROLE_ADMIN")
	void deleteGroup(Group group);

	/**
	 * List Groups user has access to. Access rights are managed by AD (LDAP).
	 *
	 * @return List of Groups
	 */
	@Secured("ROLE_USER")
	List<Group> listGroups();

	/**
	 * Get Group by it's UUID.
	 *
	 * @param uuid UUID of group to get
	 *
	 * @return Group by UUID
	 */
	@Secured("ROLE_USER")
	Group getGroup(UUID uuid);

}