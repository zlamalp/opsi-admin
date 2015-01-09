package cz.muni.ucn.opsi.core.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;
import cz.u2.eis.api.events.data.LifecycleEvent;

/**
 * Application listener which checks for changes on Client objects in order to provide
 * up-to-date data to web interface.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Component
public class OpsiSynchronizer implements ApplicationListener<LifecycleEvent> {

	private OpsiClientService clientService;

	/**
	 * Setter for clientService
	 *
	 * @param clientService the clientService to set
	 */
	@Autowired
	public void setClientService(OpsiClientService clientService) {
		this.clientService = clientService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(LifecycleEvent event) {
		if (!(event.getBean() instanceof Client)) {
			return;
		}

		Client client = (Client) event.getBean();
		if (LifecycleEvent.CREATED == event.getEventType()) {
			createClient(client);
		} else if (LifecycleEvent.DELETED == event.getEventType()) {
			deleteClient(client);
		} else if (LifecycleEvent.MODIFIED == event.getEventType()) {
			updateClient(client);
		}

	}

	/**
	 * Create Client
	 *
	 * @param client Client to create
	 */
	protected void createClient(Client client) {
		clientService.createClient(client);
	}

	/**
	 * Delete Client
	 *
	 * @param client Client to delete
	 */
	private void deleteClient(Client client) {
		clientService.deleteClient(client);
	}

	/**
	 * Update Client
	 *
	 * @param client Client to update
	 */
	private void updateClient(Client client) {
		clientService.updateClient(client);
	}

}