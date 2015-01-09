package cz.muni.ucn.opsi.core.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cz.muni.ucn.opsi.api.client.Hardware;
import cz.muni.ucn.opsi.api.group.Group;
import cz.muni.ucn.opsi.api.group.GroupService;
import cz.muni.ucn.opsi.api.instalation.Installation;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;
import cz.u2.eis.api.events.data.LifecycleEvent;
import cz.u2.eis.api.events.data.SecuredLifecycleEvent;

/**
 * Implementing service class used to manage Clients.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@censet.cz>
 */
@Service
@Transactional(readOnly=true)
public class ClientServiceImpl implements ClientService, ApplicationEventPublisherAware {

	private GroupService groupService;
	private ClientDao clientDao;
	private ApplicationEventPublisher eventPublisher;
	private AccessDecisionManager accessDecisionManager;
	private OpsiClientService opsiClientService;
	private OpsiClientService opsiClientService2;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	/**
	 * Setter for clientDao
	 *
	 * @param clientDao the clientDao to set
	 */
	@Autowired
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	/**
	 * Setter for groupService
	 *
	 * @param groupService the groupService to set
	 */
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * Set accessDecisionManager
	 *
	 * @param accessDecisionManager the accessDecisionManager to set
	 */
	@Autowired
	public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}

	/**
	 * Set local / primary opsiClientService
	 *
	 * @param opsiClientService the opsiClientService to set
	 */
	@Autowired
	public void setOpsiClientService(OpsiClientService opsiClientService) {
		this.opsiClientService = opsiClientService;
	}

	/**
	 * Set remote / secondary opsiClientService2
	 *
	 * @param opsiClientService2 the opsiClientService to set
	 */
	@Autowired
	@Qualifier("opsi2")
	public void setOpsiClientService2(OpsiClientService opsiClientService2) {
		this.opsiClientService2 = opsiClientService2;
	}

	@Override
	@Transactional
	public Client createClient(UUID groupUuid) {
		Group group = groupService.getGroup(groupUuid);

		checkGroupRights(group);

		Client client = new Client(UUID.randomUUID());
		client.setGroup(group);
		return client;
	}

	@Override
	@Transactional
	public Client editClient(UUID uuid) {
		Client client = clientDao.get(uuid);
		return client;
	}

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

	@Override
	@Transactional(readOnly=false)
	public void deleteClient(Client client) {
		Client c = clientDao.get(client.getUuid());
		checkGroupRights(c.getGroup());

		clientDao.delete(client);

		eventPublisher.publishEvent(new SecuredLifecycleEvent(LifecycleEvent.DELETED, client, "ROLE_USER"));
	}

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

	@Override
	public void installClient(Client client, Installation i) {
		Client c = clientDao.get(client.getUuid());
		Group group = c.getGroup();

		checkGroupRights(group);

		opsiClientService.clientInstall(client, i);
	}

	@Override
	public List<Client> listClientsForImport(UUID groupUuid, String opsi) {
		Group group = groupService.getGroup(groupUuid);

		List<Client> clients;
		if ("0".equals(opsi)) {
			clients = opsiClientService.listClientsForImport();
		} else if ("1".equals(opsi)) {
			clients = opsiClientService2.listClientsForImport();
		} else {
			throw new IllegalArgumentException("unknown opsi code: " + opsi);
		}
		List<String> namesList = clientDao.listNamesAll();
		Set<String> names = new HashSet<String>();
		for (String name : namesList) {
			names.add(name.trim().toLowerCase());
		}
		List<Client> ret = new ArrayList<Client>();
		for (Client client : clients) {
			String name = client.getName().trim().toLowerCase();
			if (names.contains(name)) {
				continue;
			}
			client.setGroup(group);
			ret.add(client);
		}
		return ret;
	}

	@Override
	public List<Hardware> listHardware(UUID uuid) {
		Client client = clientDao.get(uuid);
		return opsiClientService.listHardware(client);
	}


	protected void checkGroupRights(Group group) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String securityRole = group.getRole();
		if (StringUtils.isNotBlank(securityRole)) {
			accessDecisionManager.decide(authentication, null,
					Arrays.asList(new ConfigAttribute[] {new SecurityConfig("ROLE_ADMIN"),
							new SecurityConfig(securityRole)}));
		}
	}

}