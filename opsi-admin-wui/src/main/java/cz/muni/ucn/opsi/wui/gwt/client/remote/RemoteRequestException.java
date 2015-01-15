package cz.muni.ucn.opsi.wui.gwt.client.remote;

/**
 * Exception wrapper for remote requests
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RemoteRequestException extends RuntimeException {

	private static final long serialVersionUID = 1368071864708518048L;

	/**
	 * Create new instance of requests
	 */
	public RemoteRequestException() {
		super();
	}

	/**
	 * Create new instance of requests
	 *
	 * @param message message to pass
	 * @param cause original exception
	 */
	public RemoteRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create new instance of requests
	 *
	 * @param message message to pass
	 */
	public RemoteRequestException(String message) {
		super(message);
	}

	/**
	 * Create new instance of requests
	 *
	 * @param cause original exception
	 */
	public RemoteRequestException(Throwable cause) {
		super(cause);
	}

}