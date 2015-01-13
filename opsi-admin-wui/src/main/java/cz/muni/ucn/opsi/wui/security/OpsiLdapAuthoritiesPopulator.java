package cz.muni.ucn.opsi.wui.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.ldap.core.ContextSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.util.StringUtils;

/**
 * LDAP populator class is responsible for reading user roles from LDAP. It's by default based on
 * group names user is memberOf.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal
 */
public class OpsiLdapAuthoritiesPopulator extends DefaultLdapAuthoritiesPopulator {

	private String userGroup = null;
	private String userRole = "ROLE_USER";
	private String adminGroup = null;
	private String adminRole = "ROLE_ADMIN";
	private boolean convertToUpperCase = true;
    private String rolePrefix = "ROLE_";

	/**
	 * Create new instance
	 *
	 * @param contextSource Context
	 * @param groupSearchBase Search base
	 */
	public OpsiLdapAuthoritiesPopulator(ContextSource contextSource, String groupSearchBase) {
		super(contextSource, groupSearchBase);
	}

	@Override
	public Set<GrantedAuthority> getGroupMembershipRoles(String userDn, String username) {

		Set<GrantedAuthority> authorities = super.getGroupMembershipRoles(userDn, username);
		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority authority : authorities) {
			roles.add(authority.getAuthority());
		}

		addGroupRoles(authorities, roles, adminGroup, adminRole);
		addGroupRoles(authorities, roles, userGroup, userRole);

		return authorities;

	}

	/**
	 * Convert LDAP group name to user role name
	 *
	 * @param authorities authorities user have
	 * @param roles roles user have
	 * @param group group to add
	 * @param role role to add
	 */
	private void addGroupRoles(Set<GrantedAuthority> authorities, Set<String> roles, String group, String role) {
		if (StringUtils.hasText(group)) {
			String roleName = rolePrefix;
			if (convertToUpperCase) {
				roleName += group.toUpperCase();
			} else {
				roleName += group;
			}
			if (roles.contains(roleName)) {
				authorities.add(new GrantedAuthorityImpl(role));
			}
		}
	}

	@Override
	public void setConvertToUpperCase(boolean convertToUpperCase) {
		this.convertToUpperCase = convertToUpperCase;
		super.setConvertToUpperCase(convertToUpperCase);
	}

	@Override
	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
		super.setRolePrefix(rolePrefix);
	}

	/**
	 * Set LDAP group name used to identify role ADMIN
	 *
	 * @param adminGroup the adminGroup to set
	 */
	public void setAdminGroup(String adminGroup) {
		this.adminGroup = adminGroup;
	}

	/**
	 * Get group name associated with role ADMIN
	 *
	 * @return the adminGroup
	 */
	public String getAdminGroup() {
		return adminGroup;
	}

	/**
	 * Set LDAP group name used to identify role USER
	 *
	 * @param userGroup the userGroup to set
	 */
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	/**
	 * Get group name associated with role USER
	 *
	 * @return the userGroup
	 */
	public String getUserGroup() {
		return userGroup;
	}

	/**
	 * Set admin role name
	 *
	 * @param adminRole the adminRole to set
	 */
	public void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}

	/**
	 * Get admin role name
	 *
	 * @return the adminRole
	 */
	public String getAdminRole() {
		return adminRole;
	}

	/**
	 * Set user role name
	 *
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * Get user role name
	 *
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

}