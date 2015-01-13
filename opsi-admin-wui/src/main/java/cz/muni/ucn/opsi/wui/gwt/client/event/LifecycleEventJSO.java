package cz.muni.ucn.opsi.wui.gwt.client.event;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * JSO wrapper for LifeCycleEvent for Comet service
 *
 * @author Jan Dosoudil
 * @autgor Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LifecycleEventJSO extends JavaScriptObject {

	public static final int CREATED = 1;
	public static final int MODIFIED = 2;
	public static final int DELETED = 3;
	public static final int LOCKED = 10;
	public static final int UNLOCKED = 11;

	protected LifecycleEventJSO() {
	}

	/**
	 * Get type of this Event
	 *
	 * @return the eventType
	 */
	public final native int getEventType() /*-{
		return this.eventType;
	}-*/;

	/**
	 * Get bean associated with this Event
	 *
	 * @return the bean
	 */
	public final native JavaScriptObject getBean() /*-{
		return this.bean;
	}-*/;

	/**
	 * Get class of bean  associated with this event
	 *
	 * @return the bean class
	 */
	public final native String getBeanClass() /*-{
		return this.beanClass;
	}-*/;

	/**
	 * Convert JSON string to LifeCycleEventJSO object
	 *
	 * @param json JSON to parse
	 * @return LifeCycleEvent object from JSON
	 */
	public static final native LifecycleEventJSO fromJSON(String json) /*-{
		return eval('(' + json + ')');
	}-*/;

}