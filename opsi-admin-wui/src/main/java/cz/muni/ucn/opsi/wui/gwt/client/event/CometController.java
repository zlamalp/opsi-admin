package cz.muni.ucn.opsi.wui.gwt.client.event;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;

import cz.muni.ucn.opsi.wui.gwt.client.DesktopController;
import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.DefaultDomain;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * Controller for handling app events associated with Comet service.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class CometController extends Controller implements RemoteEventListener {

	public static final EventType LIFECYCLE_EVENT_TYPE = new EventType();
	private RemoteEventService eventService;
	private Domain lifecycleDomain = new DefaultDomain("lifecycleEvent");

	/**
	 * Create new instance
	 */
	public CometController() {
		registerEventTypes(DesktopController.INIT);
		//registerEventTypes(LoginController.LOGGED_OUT);
		registerEventTypes(CometController.LIFECYCLE_EVENT_TYPE);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (DesktopController.INIT == type) {
			startComet();
		//} else if (LoginController.LOGGED_OUT == type) {
			//stopCommet();
		}
	}

	@Override
	protected void initialize() {
		super.initialize();

		RemoteEventServiceFactory serviceFactory = RemoteEventServiceFactory.getInstance();
		eventService = serviceFactory.getRemoteEventService();
	}

	/**
	 * Start comet service for listening server events
	 */
	protected void startComet() {
		GWT.log("startComet");
		eventService.addListener(lifecycleDomain, this);
	}

	/**
	 * Stop comet service for listening server events
	 */
	protected void stopComet() {
		GWT.log("stopComet");
		eventService.removeListeners();
	}

	@Override
	public void apply(Event anEvent) {
		LifecycleCometEvent lce = (LifecycleCometEvent) anEvent;
		GWT.log("message: " + lce.getJsonObject());

		LifecycleEventJSO event = LifecycleEventJSO.fromJSON(lce.getJsonObject());
		Dispatcher.forwardEvent(CometController.LIFECYCLE_EVENT_TYPE, event);
	}

}