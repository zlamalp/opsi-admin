package cz.muni.ucn.opsi.core.opsiClient;

/**
 * Exception thrown when OPSI server returns response with error message.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class OpsiException extends RuntimeException {

	private static final long serialVersionUID = 6742229821639572698L;

	/**
	 * Create new instance of OpsiException
	 */
	public OpsiException() {
	}

	/**
	 * Create new instance of OpsiException
	 *
	 * @param message Messaged returned from OPSI server
	 */
	public OpsiException(String message) {
		super(message);
	}

	/**
	 * Create new instance of OpsiException
	 *
	 * @param cause Original exception causing this one
	 */
	public OpsiException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create new instance of OpsiException
	 *
	 * @param message Messaged returned from OPSI server
	 * @param cause Original exception causing this one
	 */
	public OpsiException(String message, Throwable cause) {
		super(message, cause);
	}

}
