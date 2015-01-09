package cz.muni.ucn.opsi.core.client;

import java.util.List;
import java.util.UUID;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.group.Group;

/**
 * Interface class for storing and listing Clients to DB.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface ClientDao {

	/**
	 * Get Client by UUID from DB
	 *
	 * @param uuid
	 */
	Client get(UUID uuid);

	/**
	 * Save Client to DB
	 *
	 * @param client Client to save
	 */
	void save(Client client);

	/**
	 * Delete Client from DB
	 *
	 * @param client Client to delete
	 */
	void delete(Client client);

	/**
	 * List Clients in Group
	 *
	 * @param group Group to list clients for
	 * @return List of Clients in group
	 */
	List<Client> list(Group group);

	/**
	 * List names of all Clients in DB independent of Group
	 *
	 * @return List names of all Clients in DB
	 */
	List<String> listNamesAll();

}