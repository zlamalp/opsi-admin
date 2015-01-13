package cz.muni.ucn.opsi.core.group;

import cz.muni.ucn.opsi.api.group.Group;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation class for storing and listing Groups to DB.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Repository
public class GroupDaoImpl implements GroupDao {

	private SessionFactory sessionFactory;

	/**
	 * Setter for sessionFactory
	 *
	 * @param sessionFactory the sessionFactory to set
	 */
	@Autowired
	@Qualifier("opsi")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Group get(UUID uuid) {
		Session session = sessionFactory.getCurrentSession();
		GroupHibernate groupH = (GroupHibernate) session.get(GroupHibernate.class, uuid);
		return transform(groupH);
	}

	@Override
	public void save(Group group) {
		Session session = sessionFactory.getCurrentSession();

		GroupHibernate saved = (GroupHibernate) session.get(GroupHibernate.class, group.getUuid());
		GroupHibernate toSave = transform(saved, group);

		session.save(toSave);
		session.flush();
	}

	@Override
	public void delete(Group group) {
		Session session = sessionFactory.getCurrentSession();
		GroupHibernate groupH = (GroupHibernate) session.load(GroupHibernate.class, group.getUuid());
		session.delete(groupH);
		session.flush();
	}

	@Override
	public List<Group> list() {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<GroupHibernate> list = session.createQuery("select g from Group g order by name").list();
		return transform(list);
	}

	/**
	 * Transform Hibernate version to API version of Group
	 *
	 * @param hibernate Hibernate version of object
	 * @return API version of Group object
	 * @see cz.muni.ucn.opsi.core.group.GroupHibernate
	 * @see cz.muni.ucn.opsi.api.group.Group
	 */
	private Group transform(GroupHibernate hibernate) {
		if (null == hibernate) {
			return null;
		}
		Group group = new Group();
		group.setUuid(hibernate.getUuid());
		group.setName(hibernate.getName());
		group.setRole(hibernate.getRole());
		return group;
	}

	/**
	 * Transform list of Hibernate version to API version of Groups
	 *
	 * @param hibernate list of Hibernate version of objects
	 * @return list of API version of Group objects
	 * @see cz.muni.ucn.opsi.core.group.GroupHibernate
	 * @see cz.muni.ucn.opsi.api.group.Group
	 */
	private List<Group> transform(List<GroupHibernate> hibernate) {
		if (null == hibernate) {
			return null;
		}
		List<Group> groups = new ArrayList<Group>(hibernate.size());
		for (GroupHibernate groupHibernate : hibernate) {
			groups.add(transform(groupHibernate));
		}
		return groups;
	}

	/**
	 * Transform API version to Hibernate version of Group while using
	 * current Hibernate version of object to update values.
	 *
	 * @param saved current Hibernate object from DB
	 * @param group API version of Group object
	 * @return Hibernate version of object
	 * @see cz.muni.ucn.opsi.core.group.GroupHibernate
	 * @see cz.muni.ucn.opsi.api.group.Group
	 */
	private GroupHibernate transform(final GroupHibernate saved, final Group group) {
		GroupHibernate toSave = saved;
		if (toSave == null) {
			toSave = new GroupHibernate();
			toSave.setUuid(group.getUuid());
		}
		toSave.setName(group.getName());
		toSave.setRole(group.getRole());
		return toSave;
	}

}