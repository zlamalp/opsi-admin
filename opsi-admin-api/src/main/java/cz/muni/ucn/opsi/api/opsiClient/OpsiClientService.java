package cz.muni.ucn.opsi.api.opsiClient;

import java.util.List;
import java.util.Map;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.Hardware;
import cz.muni.ucn.opsi.api.instalation.Instalation;

/**
 * @author Jan Dosoudil
 *
 */
public interface OpsiClientService {

	/**
	 * @param client
	 */
	void createClient(Client client);

	/**
	 * @return
	 */
	List<Instalation> listInstalations();

	/**
	 * @param instalationId
	 * @return
	 */
	Instalation getIntalationById(String instalationId);

	/**
	 * @param client
	 * @param i
	 */
	void clientInstall(Client client, Instalation i);

	/**
	 * @param client
	 */
	void deleteClient(Client client);

	/**
	 * @param client
	 */
	void updateClient(Client client);

	/**
	 * @return
	 */
	List<Client> listClientsForImport();

	/**
	 * @param client
	 * @return
	 */
	List<Hardware> listHardware(Client client);

	/**
	 * List all "set" product properties
	 *
	 * @param objectId
	 * @return
	 */
	List<ProductPropertyState> getProductProperties(String objectId);

	/**
	 * Set new product properties for specific object
	 *
	 * @param props
	 * @return
	 */
	void setProductProperties(List<ProductPropertyState> props);

}
