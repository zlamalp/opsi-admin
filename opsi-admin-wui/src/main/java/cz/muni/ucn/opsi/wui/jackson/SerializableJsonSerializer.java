package cz.muni.ucn.opsi.wui.jackson;

import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Override default JSON serializer to exclude unwanted object properties
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class SerializableJsonSerializer extends JsonSerializer<Serializable> {

	@Override
	public void serialize(Serializable value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

		provider.findTypedValueSerializer(value.getClass(), true).serialize(value, jgen, provider);

	}

}