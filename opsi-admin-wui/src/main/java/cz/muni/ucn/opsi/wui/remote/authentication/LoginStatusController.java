package cz.muni.ucn.opsi.wui.remote.authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Server side of apps API for handling logging-in
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Controller
@RequestMapping("/loginStatus")
public class LoginStatusController {

	private AccessDecisionManager decisionManager;

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody AuthenticationStatus getLoginStatus() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null == authentication) {
			return new AuthenticationStatus(AuthenticationStatus.STATUS_NOT_LOGGED_IN);
		}

		try {
			decisionManager.decide(authentication, null, Arrays.asList((ConfigAttribute) new SecurityConfig("IS_AUTHENTICATED_REMEMBERED")));

			Collection<GrantedAuthority> authorities = authentication.getAuthorities();
			List<String> auths = new ArrayList<String>();
			for (GrantedAuthority grantedAuthority : authorities) {
				auths.add(grantedAuthority.getAuthority());
			}
			AuthenticationStatus status = new AuthenticationStatus(AuthenticationStatus.STATUS_LOGGED_IN);
			status.setDisplayName(authentication.getPrincipal().toString());
			status.setUsername(authentication.getName());
			status.setRoles(auths.toArray(new String[auths.size()]));
			return status;

		} catch (AccessDeniedException e) {
			return new AuthenticationStatus(AuthenticationStatus.STATUS_NOT_LOGGED_IN);
		}

	}

	/**
	 * Setter for accessDecisionManager
	 *
	 * @param decisionManager the decisionManager to set
	 */
	@Resource(name="accessDecisionManager")
	public void setDecisionManager(AccessDecisionManager decisionManager) {
		this.decisionManager = decisionManager;
	}

}