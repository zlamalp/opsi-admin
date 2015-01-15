package cz.muni.ucn.opsi.core.group;

import cz.muni.ucn.opsi.api.group.Group;

import java.util.List;
import java.util.UUID;

/**
 * Interface class for storing and listing Groups to DB.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface GroupDao {

	/**
	 * Get Group by UUID from DB
	 *
	 * @param uuid UUID to get Group by
	 * @return Group by UUID
	 */
	Group get(UUID uuid);

	/**
	 * Save Group to DB
	 *
	 * @param group Group to save
	 */
	void save(Group group);

	/**
	 * Delete Group from DB
	 *
	 * @param group Group to delete
	 */
	void delete(Group group);

	/**
	 * List all Groups from DB
	 *
	 * @return List all groups from DB
	 */
	List<Group> list();

}