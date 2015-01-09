package cz.muni.ucn.opsi.core.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.group.Group;
import cz.muni.ucn.opsi.core.group.GroupHibernate;

/**
 * Implementation class for storing and listing Clients to DB.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Repository
public class ClientDaoImpl implements ClientDao {

	private SessionFactory sessionFactory;

	/**
	 * Setter for sessionFactory
	 *
	 * @param sessionFactory the sessionFactory to set
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Client get(UUID uuid) {
		Session session = sessionFactory.getCurrentSession();
		ClientHibernate clientH = (ClientHibernate) session.get(ClientHibernate.class, uuid);

		return transform(clientH);
	}

	@Override
	public void save(Client client) {
		Session session = sessionFactory.getCurrentSession();
		ClientHibernate loaded = (ClientHibernate) session.get(ClientHibernate.class, client.getUuid());

		if (!StringUtils.isBlank(client.getMacAddress())) {
			@SuppressWarnings("unchecked")
			List<ClientHibernate> macClients = session.createQuery("select c from Client c " +
					" where c.macAddress = :mac")
				.setParameter("mac", client.getMacAddress())
				.list();

			if (macClients.size() > 1 && loaded != null ||
					macClients.size() > 0 && loaded == null) {
				throw new DuplicateKeyException("Duplicate MacAddress: " + client.getMacAddress());
			} else if (macClients.size() == 1 && loaded != null) {
				ClientHibernate clientMac = macClients.get(0);
				if (!clientMac.getUuid().equals(client.getUuid())) {
					throw new DuplicateKeyException("Duplicate MacAddress: " + client.getMacAddress());
				}
			}
		}

		ClientHibernate toSave = transform(loaded, client);
		session.save(toSave);
		session.flush();
	}

	@Override
	public void delete(Client client) {
		Session session = sessionFactory.getCurrentSession();
		ClientHibernate clientH = (ClientHibernate) session.load(ClientHibernate.class, client.getUuid());
		session.delete(clientH);
		session.flush();
	}

	@Override
	public List<Client> list(Group group) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<ClientHibernate> list = session.createQuery("select c from Client c " +
					"where c.group.uuid = :group")
			.setParameter("group", group.getUuid())
			.list();

		return transform(list);
	}

	@Override
	public List<String> listNamesAll() {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<String> list = session.createQuery("select c.name from Client c ")
			.list();

		return list;
	}

	/**
	 * Transform Hibernate version to API version of Installation
	 *
	 * @see cz.muni.ucn.opsi.core.client.ClientHibernate
	 * @see cz.muni.ucn.opsi.api.client.Client
	 *
	 * @param hibernate Hibernate version of object
	 * @return API version of Client object
	 */
	private Client transform(ClientHibernate hibernate) {
		if (null == hibernate) {
			return null;
		}

		Client c = new Client();
		c.setUuid(hibernate.getUuid());
		c.setName(hibernate.getName());
		c.setDescription(hibernate.getDescription());
		c.setNotes(hibernate.getNotes());
		c.setIpAddress(hibernate.getIpAddress());
		c.setMacAddress(hibernate.getMacAddress());

		GroupHibernate groupH = hibernate.getGroup();
		Group g = new Group();
		g.setUuid(groupH.getUuid());
		g.setName(groupH.getName());
		g.setRole(groupH.getRole());

		c.setGroup(g);

		return c;
	}

	/**
	 * Transform list of Hibernate version to API version of Installations
	 *
	 * @see cz.muni.ucn.opsi.core.client.ClientHibernate
	 * @see cz.muni.ucn.opsi.api.client.Client
	 *
	 * @param hibernate list of Hibernate version of objects
	 * @return List of API version of Installation objects
	 */
	private List<Client> transform(List<ClientHibernate> hibernate) {
		if (null == hibernate) {
			return null;
		}
		List<Client> ret = new ArrayList<Client>(hibernate.size());
		for (ClientHibernate clientHibernate : hibernate) {
			ret.add(transform(clientHibernate));
		}
		return ret;
	}

	/**
	 * Transform API version to Hibernate version of Client while using
	 * current Hibernate version of object to update values.
	 *
	 * @see cz.muni.ucn.opsi.core.client.ClientHibernate
	 * @see cz.muni.ucn.opsi.api.client.Client
	 *
	 * @param loaded current Hibernate object from DB
	 * @param client API version of Client object
	 * @return Hibernate version of object
	 */
	private ClientHibernate transform(ClientHibernate loaded, Client client) {

		Session session = sessionFactory.getCurrentSession();

		ClientHibernate toSave = loaded;
		if (null == toSave) {
			toSave = new ClientHibernate();
			toSave.setUuid(client.getUuid());
		}
		toSave.setUuid(client.getUuid());
		toSave.setName(client.getName());
		toSave.setDescription(client.getDescription());
		toSave.setIpAddress(client.getIpAddress());
		toSave.setMacAddress(client.getMacAddress());
		toSave.setNotes(client.getNotes());

		GroupHibernate groupH = (GroupHibernate) session.load(GroupHibernate.class, client.getGroup().getUuid());
		toSave.setGroup(groupH);

		return toSave;

	}

}