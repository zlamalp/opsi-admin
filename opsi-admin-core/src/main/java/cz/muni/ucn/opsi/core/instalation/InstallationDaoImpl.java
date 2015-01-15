package cz.muni.ucn.opsi.core.instalation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.muni.ucn.opsi.api.instalation.Installation;

/**
 * Implementation class for storing and listing Installations to DB.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Repository
public class InstallationDaoImpl implements InstallationDao {

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
	public List<Installation> listInstallations() {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<InstallationHibernate> instalations = session.createQuery(
					"select i from Instalation i order by i.name")
				.list();

		return transform(instalations);
	}

	@Override
	public void saveInstallations(List<Installation> installations) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<InstallationHibernate> loadedHib = session.createQuery(
					"select i from Instalation i order by i.name")
				.list();
		Map<String, InstallationHibernate> mapHib = new HashMap<String, InstallationHibernate>();
		for (InstallationHibernate ih : loadedHib) {
			mapHib.put(ih.getId(), ih);
		}

		Set<Installation> loaded = new HashSet<Installation>(transform(loadedHib));

		List<Installation> existing = new ArrayList<Installation>();
		for (Installation i : installations) {
			if (!loaded.contains(i)) {
				continue;
			}
			existing.add(i);
		}
		installations.removeAll(existing);

		List<Installation> toRemove = new ArrayList<Installation>();
		for (Installation i : loaded) {
			if (!installations.contains(i)) {
				toRemove.add(i);
			}
		}

		for (Installation installation : toRemove) {
			session.delete(mapHib.get(installation.getId()));
		}

		for (Installation i : installations) {
			session.save(transform(i));

		}

	}

	/**
	 * Transform API version to Hibernate version of Installation
	 *
	 * @see InstallationHibernate
	 * @see cz.muni.ucn.opsi.api.instalation.Installation
	 *
	 * @param installation API version of Installation object
	 * @return Hibernate version of object
	 */
	private InstallationHibernate transform(Installation installation) {
		if (null == installation) {
			return null;
		}
		InstallationHibernate ih = new InstallationHibernate();
		ih.setId(installation.getId());
		ih.setName(installation.getName());
		return ih;
	}

	/**
	 * Transform list of Hibernate version to API version of Installations
	 *
	 * @see InstallationHibernate
	 * @see cz.muni.ucn.opsi.api.instalation.Installation
	 *
	 * @param hibernate list of Hibernate version of objects
	 * @return List of API version of Installation objects
	 */
	private List<Installation> transform(List<InstallationHibernate> hibernate) {
		if (null == hibernate) {
			return null;
		}
		List<Installation> ret = new ArrayList<Installation>(hibernate.size());
		for (InstallationHibernate ih : hibernate) {
			ret.add(transform(ih));
		}
		return ret;
	}

	/**
	 * Transform Hibernate version to API version of Installation
	 *
	 * @see InstallationHibernate
	 * @see cz.muni.ucn.opsi.api.instalation.Installation
	 *
	 * @param hibernate Hibernate version of object
	 * @return API version of Installation object
	 */
	private Installation transform(InstallationHibernate hibernate) {
		if (null == hibernate) {
			return null;
		}
		Installation i = new Installation();
		i.setId(hibernate.getId());
		i.setName(hibernate.getName());
		return i;
	}

}