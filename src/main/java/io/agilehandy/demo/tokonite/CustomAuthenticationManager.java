package io.agilehandy.demo.tokonite;

import java.util.Collection;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author Rob Winch
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
	private final CustomTokenValidator tokenValidator;
	private final CustomAuthorityLoader authorities;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String rawToken = authentication.getName();
		CustomToken token = this.tokenValidator.extract(rawToken);
		Collection<SimpleGrantedAuthority> username = this.authorities.load(token.getUserName());
		CustomUser user = CustomUser.builder()
				.firstName(token.getFirstName())
				.lastName(token.getLastName())
				.username(token.getUserName())
				.build();
		Collection<SimpleGrantedAuthority> authorities = this.authorities.load(user.getUsername());
		return new PreAuthenticatedAuthenticationToken(user, token, authorities);
	}
}
