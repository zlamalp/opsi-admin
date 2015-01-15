package cz.muni.ucn.opsi.wui.jackson;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * JSON Mixin config for ValueObject
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
//@JsonTypeInfo(include=As.PROPERTY, use=Id.CLASS)
public abstract class ValueObjectMixin {

	@JsonIgnore
	public abstract String getUuidString();

}