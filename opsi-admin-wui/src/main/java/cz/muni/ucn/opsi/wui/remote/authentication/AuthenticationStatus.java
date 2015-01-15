package cz.muni.ucn.opsi.wui.remote.authentication;

import java.io.Serializable;

/**
 * Object representing authentication status (if user is logged in or not).
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AuthenticationStatus implements Serializable {

	private static final long serialVersionUID = -2728070266616596504L;

	public static final String STATUS_LOGGED_IN = "OK";
	public static final String STATUS_LOGIN_FAILED = "FAILED";
	public static final String STATUS_NOT_LOGGED_IN = "NOT_LOGGED_IN";

	private String status;
	private String message;
	private String[] roles;
	private String username;
	private String displayName;

	/**
	 * Create new instance
	 */
	public AuthenticationStatus() {
	}

	/**
	 * Create new authentication status
	 *
	 * @param status status to set
	 */
	public AuthenticationStatus(String status) {
		this.status = status;
	}

	/**
	 * Get authentication status
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Set authentication status
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get message
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set message
	 *
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get user roles
	 *
	 * @return the roles
	 */
	public String[] getRoles() {
		return roles;
	}

	/**
	 * Set user roles
	 *
	 * @param roles the roles to set
	 */
	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	/**
	 * Get username
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set username
	 *
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get display name
	 *
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set display name
	 *
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}