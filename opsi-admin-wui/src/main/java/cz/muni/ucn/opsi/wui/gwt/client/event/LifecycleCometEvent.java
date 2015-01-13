package cz.muni.ucn.opsi.wui.gwt.client.event;

import de.novanic.eventservice.client.event.Event;

/**
 * LifeCycle Event object for Comet service
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal
 */
public class LifecycleCometEvent implements Event {

	private static final long serialVersionUID = 1143019399990173603L;

	private String jsonObject;

	/**
	 * Create new instance
	 */
	public LifecycleCometEvent() {}

	/**
	 * Create new instance
	 *
	 * @param jsonObject Object associated with event
	 */
	public LifecycleCometEvent(String jsonObject) {
		super();
		this.jsonObject = jsonObject;
	}

	/**
	 * Get JSON object associated with event
	 *
	 * @return the jsonObject
	 */
	public String getJsonObject() {
		return jsonObject;
	}

	/**
	 * Set JSON object associated with event
	 *
	 * @param jsonObject the jsonObject to set
	 */
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

}