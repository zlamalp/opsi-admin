package cz.muni.ucn.opsi.wui.gwt.client.instalation;

import com.google.gwt.i18n.client.Constants;

/**
 * Interface for fixed strings (possible translation)
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface InstallationConstants extends Constants {

	@Key("installation.name")
	String getName();

	@Key("installation.save")
	String getInstallationsSave();

}