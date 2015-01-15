package cz.muni.ucn.opsi.wui.remote.instalation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.muni.ucn.opsi.api.instalation.Installation;
import cz.muni.ucn.opsi.api.instalation.InstallationService;

/**
 * Server side API (controller) for handling requests on Installations.
 *
 * @see cz.muni.ucn.opsi.wui.gwt.client.group.GroupService for client side of this API.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Controller
public class InstallationController {

	private InstallationService installationService;

	/**
	 * Setter for installation service
	 *
	 * @param installationService the installationService to set
	 */
	@Autowired
	public void setInstallationService(InstallationService installationService) {
		this.installationService = installationService;
	}

	@RequestMapping(value = "/installation/list", method = RequestMethod.GET)
	public @ResponseBody List<Installation> listInstallations() {
		return installationService.listInstallations();
	}

	@RequestMapping(value = "/installation/listAll", method = RequestMethod.GET)
	public @ResponseBody List<Installation> listInstallationsAll() {
		return installationService.listInstallationsAll();
	}

	@RequestMapping(value = "/installation/save", method = RequestMethod.POST)
	public @ResponseBody void saveInstallations(@RequestBody InstallationList installations) {
		installationService.saveInstallations(installations);
	}

	@SuppressWarnings("serial")
	public static class InstallationList extends ArrayList<Installation> { }

}