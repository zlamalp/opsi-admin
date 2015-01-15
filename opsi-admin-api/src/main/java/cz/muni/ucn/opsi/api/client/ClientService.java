package cz.muni.ucn.opsi.api.client;

import cz.muni.ucn.opsi.api.instalation.Installation;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.UUID;

/**
 * Service class used to handle Clients (create/edit/delete/install)
 * on opsi-admin-wui server. Calls OPSI only when necessary (e.g. install).
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@censet.cz>
 */
public interface ClientService {

	/**
	 * Create new Client object in specified Group.
	 * Client is not saved to OPSI until calling #saveClient().
	 *
	 * @return create Client object
	 * @see #saveClient
	 */
	@Secured("ROLE_USER")
	Client createClient(UUID groupUuid);

	/**
	 * Retrieve information about Client for editing purpose.
	 * In order to save changes made to client call #saveClient();
	 *
	 * @param uuid UUID of Client to edit
	 * @return Client retrieved by UUID
	 * @see #saveClient
	 */
	@Secured("ROLE_USER")
	Client editClient(UUID uuid);

	/**
	 * Save Client object to OPSI. If new, Client must be created using #createClient();
	 *
	 * @param client Client to save
	 * @see #createClient
	 */
	@Secured("ROLE_USER")
	void saveClient(Client client);

	/**
	 * Delete Client from OPSI
	 *
	 * @param client Client to delete
	 */
	@Secured("ROLE_USER")
	void deleteClient(Client client);

	/**
	 * List all Clients in some Group
	 *
	 * @param groupUuid UUID of group to list clients for
	 * @return Client in group
	 */
	@Secured("ROLE_USER")
	List<Client> listClients(UUID groupUuid);

	/**
	 * Install specific NetBoot product (Installation) to Client.
	 *
	 * @param client       Client to install onto
	 * @param installation NetBoot product (Installation) to install
	 */
	@Secured("ROLE_USER")
	void installClient(Client client, Installation installation);

	/**
	 * List available Clients for import into Group from OPSI server (local or remote)
	 *
	 * @param opsi "0" import from local OPSI / "1" = import from remote OPSI
	 * @return List of Clients available for import.
	 */
	@Secured("ROLE_ADMIN")
	List<Client> listClientsForImport(UUID groupUuid, String opsi);

	/**
	 * List hardware information about the Client
	 *
	 * @param uuid UUID of Client to get hardware info about
	 * @return List of Hardware information
	 */
	List<Hardware> listHardware(UUID uuid);

}