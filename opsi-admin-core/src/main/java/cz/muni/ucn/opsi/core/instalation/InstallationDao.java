package cz.muni.ucn.opsi.core.instalation;

import java.util.List;

import cz.muni.ucn.opsi.api.instalation.Installation;

/**
 * Interface class for storing and listing Installations to DB.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface InstallationDao {

	/**
	 * List Installations stored in DB
	 *
	 * @return Installations stored in DB
	 */
	List<Installation> listInstallations();

	/**
	 * Save Installations to DB
	 *
	 * @param installations Installations to save
	 */
	void saveInstallations(List<Installation> installations);

}