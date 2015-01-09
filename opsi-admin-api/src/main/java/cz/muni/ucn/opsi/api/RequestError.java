package cz.muni.ucn.opsi.api;

import java.io.Serializable;

/**
 * RequestError object exchanged between opsi-admin-wui and opsi-admin-core.
 * It's not used when communicating with OPSI.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RequestError implements Serializable {

	private static final long serialVersionUID = 1006428434123111930L;

	private String message;

	/**
	 * Create new instance
	 *
	 * @param message  Message sent to the client web app
	 */
	public RequestError(String message) {
		this.message = message;
	}

	/**
	 * Get message sent to the client web app
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set message sent to the client web app
	 *
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}