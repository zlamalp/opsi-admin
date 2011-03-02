/**
 *
 */
package cz.muni.ucn.opsi.core.client;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.ClientService;
import cz.muni.ucn.opsi.api.group.Group;
import cz.muni.ucn.opsi.api.group.GroupService;
import cz.u2.eis.api.events.data.LifecycleEvent;
import cz.u2.eis.api.events.data.SecuredLifecycleEvent;

/**
 * @author Jan Dosoudil
 *
 */
@Service
@Transactional(readOnly=true)
public class ClientServiceImpl implements ClientService, ApplicationEventPublisherAware {

	private GroupService groupService;
	private ClientDao clientDao;
	private ApplicationEventPublisher eventPublisher;
	private AccessDecisionManager accessDecisionManager;

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#createClient(java.util.UUID)
	 */
	@Override
	@Transactional
	public Client createClient(UUID groupUuid) {
		Group group = groupService.getGroup(groupUuid);

		checkGroupRights(group);

		Client client = new Client(UUID.randomUUID());
		client.setGroup(group);
		return client;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#editClient(java.util.UUID)
	 */
	@Override
	@Transactional
	public Client editClient(UUID uuid) {
		Client client = clientDao.get(uuid);
		return client;
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#saveClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	@Transactional(readOnly=false)
	public void saveClient(Client client) {
		Client loaded = clientDao.get(client.getUuid());
		boolean newClient = null == loaded;

		Group group = groupService.getGroup(client.getGroup().getUuid());
		checkGroupRights(group);

		clientDao.save(client);

        SecuredLifecycleEvent event;
        if (newClient) {
        	event = new SecuredLifecycleEvent(LifecycleEvent.CREATED,
        			client, "ROLE_USER");
        } else {
        	event = new SecuredLifecycleEvent(LifecycleEvent.MODIFIED,
        			client, "ROLE_USER");
        }

        eventPublisher.publishEvent(event);
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#deleteClient(cz.muni.ucn.opsi.api.client.Client)
	 */
	@Override
	@Transactional(readOnly=false)
	public void deleteClient(Client client) {
		clientDao.delete(client);

		eventPublisher.publishEvent(new SecuredLifecycleEvent(LifecycleEvent.DELETED, client, "ROLE_USER"));
	}

	/* (non-Javadoc)
	 * @see cz.muni.ucn.opsi.api.client.ClientService#listClients(java.util.UUID)
	 */
	@Override
	@Transactional
	public List<Client> listClients(UUID groupUuid) {
		Group group = groupService.getGroup(groupUuid);
		if (null == group) {
			return null;
		}

		checkGroupRights(group);


		return clientDao.list(group);
	}

	private void checkGroupRights(Group group) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String securityRole = group.getRole();
		if (StringUtils.isNotBlank(securityRole)) {
			accessDecisionManager.decide(authentication, null,
					Arrays.asList(new ConfigAttribute[] {new SecurityConfig("ROLE_ADMIN"),
							new SecurityConfig(securityRole)}));
		}
	}

	/**
	 * @param clientDao the clientDao to set
	 */
	@Autowired
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	/**
	 * @param groupService the groupService to set
	 */
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}
	/**
	 * @param accessDecisionManager the accessDecisionManager to set
	 */
	@Autowired
	public void setAccessDecisionManager(
			AccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}
}
