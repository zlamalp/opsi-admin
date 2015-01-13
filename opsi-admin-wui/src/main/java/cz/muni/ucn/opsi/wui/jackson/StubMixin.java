package cz.muni.ucn.opsi.wui.jackson;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.UUID;

/**
 * JSON Mixin config for Group and Clients
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@JsonAutoDetect(value = {JsonMethod.NONE})
public abstract class StubMixin {

	@JsonProperty
	public abstract UUID getUuid();

}