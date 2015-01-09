package cz.muni.ucn.opsi.api.instalation;

import java.util.List;

import org.springframework.security.access.annotation.Secured;

/**
 * Service class used to manage Installations available to normal users.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@censet.cz>
 */
public interface InstallationService {

	/**
	 * List Installations available to users.
	 *
	 * @return List of available Installations
	 */
	@Secured("ROLE_USER")
	List<Installation> listInstallations();

	/**
	 * Get Installation by ID
	 *
	 * @param installationId
	 * @return Installation object by ID
	 */
	@Secured("ROLE_USER")
	Installation getInstallationById(String installationId);

	/**
	 * List all Installations stored in OPSI
	 *
	 * @return List of all Installations
	 */
	@Secured("ROLE_ADMIN")
	List<Installation> listInstallationsAll();

	/**
	 * Save list of Installations, which will be available to normal users.
	 *
	 * @param installations Installations to allow
	 */
	@Secured("ROLE_ADMIN")
	void saveInstallations(List<Installation> installations);

}