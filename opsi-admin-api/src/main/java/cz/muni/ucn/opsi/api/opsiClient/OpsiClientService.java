package cz.muni.ucn.opsi.api.opsiClient;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.Hardware;
import cz.muni.ucn.opsi.api.instalation.Installation;

import java.util.List;

/**
 * Service class used to exchange data with OPSI server.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface OpsiClientService {

	/**
	 * Create Client object in OPSI server
	 *
	 * @param client Client ot create
	 */
	void createClient(Client client);

	/**
	 * Install specific NetBoot product on Client
	 *
	 * @param client  Client to install
	 * @param install NetBoot product to install
	 */
	void clientInstall(Client client, Installation install);

	/**
	 * Delete Client from OPSI server
	 *
	 * @param client Client to delete
	 */
	void deleteClient(Client client);

	/**
	 * Update Client in OPSI server
	 *
	 * @param client Client to update
	 */
	void updateClient(Client client);

	/**
	 * List available NetBoot products from OPSI
	 *
	 * @return List of Installations
	 */
	List<Installation> listInstallations();

	/**
	 * Get NetBoot product from OPSI by it's ID
	 *
	 * @param installationId ID of installation to get
	 * @return Installation by ID
	 */
	Installation getInstallationById(String installationId);

	/**
	 * List Client available for import to group from OPSI
	 *
	 * @return List of Clients available for import
	 */
	List<Client> listClientsForImport();

	/**
	 * List hardware information about Client from OPSI
	 * // FIXME - current implementation returns empty list
	 *
	 * @param client Client to get Hardware info for
	 * @return List of Hardware informations
	 */
	List<Hardware> listHardware(Client client);

	/**
	 * List all non-default NetBoot (Installation) properties associated with Client identified by it's name.
	 * The method returns only such values, which were previously set by #setProductProperties(props).
	 * If Client has only default settings for each installation, no data is returned.
	 *
	 * @param objectId Name property from Client (usually hostname).
	 * @return List of non-default NetBoot (Installation) properties
	 */
	List<ProductPropertyState> getProductProperties(String objectId);

	/**
	 * Set NetBoot (Installation) properties to OPSI.
	 * <p/>
	 * Association of sent property/value to some Client and NetBoot product (Installation)
	 * is part of the object itself.
	 *
	 * @param properties NetBoot (Installation) properties to set
	 */
	void setProductProperties(List<ProductPropertyState> properties);

}