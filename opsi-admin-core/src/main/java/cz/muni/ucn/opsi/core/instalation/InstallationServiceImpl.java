package cz.muni.ucn.opsi.core.instalation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.muni.ucn.opsi.api.instalation.Installation;
import cz.muni.ucn.opsi.api.instalation.InstallationService;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;

/**
 * Implementing service class used to manage Installations available to normal users.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@censet.cz>
 */
@Service
@Transactional(readOnly=true)
public class InstallationServiceImpl implements InstallationService {

	private OpsiClientService opsiClientService;
	private InstallationDao installationDao;

	/**
	 * Setter for autowiring other services
	 *
	 * @param opsiClientService the opsiClientService to set
	 */
	@Autowired
	public void setOpsiClientService(OpsiClientService opsiClientService) {
		this.opsiClientService = opsiClientService;
	}

	/**
	 * Setter for autowiring other services
	 *
	 * @param installationDao the installationDao to set
	 */
	@Autowired
	public void setInstallationDao(InstallationDao installationDao) {
		this.installationDao = installationDao;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Installation> listInstallations() {
		return installationDao.listInstallations();
	}

	@Override
	public List<Installation> listInstallationsAll() {
		return opsiClientService.listInstallations();
	}

	@Override
	@Transactional(readOnly=true)
	public Installation getInstallationById(String installationId) {
		return opsiClientService.getInstallationById(installationId);
	}

	@Override
	@Transactional(readOnly=false)
	public void saveInstallations(List<Installation> installations) {
		installationDao.saveInstallations(installations);
	}

}