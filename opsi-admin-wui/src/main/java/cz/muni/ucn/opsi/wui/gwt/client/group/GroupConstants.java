package cz.muni.ucn.opsi.wui.gwt.client.group;

import com.google.gwt.i18n.client.Constants;

/**
 * Interface for fixed strings (possible translation)
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface GroupConstants extends Constants {

	@Key("group.name")
	String getName();
	@Key("group.role")
	String getRole();
	@Key("group.groupNew")
	String getGroupNew();
	@Key("group.groupEdit")
	String getGroupEdit();
	@Key("group.groupDelete")
	String getGroupDelete();

}