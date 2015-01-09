package cz.muni.ucn.opsi.api.instalation;

/**
 * Object representing Installation, aka NetBootProduct in OPSI.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Installation {

	private String id;
	private String name;

	/**
	 * Get ID of installation
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set ID of installation
	 *
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get name of Installation
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of Installation
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Installation other = (Installation) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}