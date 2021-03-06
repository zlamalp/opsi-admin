package cz.muni.ucn.opsi.wui.jackson;

import cz.u2.eis.api.events.data.LifecycleEvent;
import cz.u2.eis.valueObjects.Stub;
import cz.u2.eis.valueObjects.ValueObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * Mapper for objects applied to serializer and deserializer
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Component("objectMapper")
public class ObjectMapperFactory implements FactoryBean<ObjectMapper> {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public ObjectMapper getObject() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		mapper.getSerializationConfig().addMixInAnnotations(ValueObject.class, ValueObjectMixin.class);
		mapper.getDeserializationConfig().addMixInAnnotations(ValueObject.class, ValueObjectMixin.class);

		mapper.getSerializationConfig().addMixInAnnotations(LifecycleEvent.class, LifecycleEventMixin.class);
		mapper.getDeserializationConfig().addMixInAnnotations(LifecycleEvent.class, LifecycleEventMixin.class);

		mapper.getSerializationConfig().addMixInAnnotations(Stub.class, StubMixin.class);
		mapper.getDeserializationConfig().addMixInAnnotations(Stub.class, StubMixin.class);

		//mapper.enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE, As.PROPERTY);

		return mapper;

	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}