/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.agilehandy.demo.core;

import java.util.Collection;

import io.agilehandy.demo.tokonite.CustomAuthorityLoader;
import io.agilehandy.demo.tokonite.CustomToken;
import io.agilehandy.demo.tokonite.CustomTokenValidator;
import io.agilehandy.demo.tokonite.CustomUserBuilder;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Haytham Mohamed
 **/

@Service
public class CoreUserDetailsService implements UserDetailsService {

	private final CustomTokenValidator tokenValidator;
	private final CustomAuthorityLoader authorityLoader;
	private final CustomUserBuilder userBuilder;

	public CoreUserDetailsService(CustomTokenValidator validator, CustomAuthorityLoader loader, CustomUserBuilder builder) {
		this.tokenValidator = validator;
		this.authorityLoader = loader;
		this.userBuilder = builder;
	}

	@Override
	public UserDetails loadUserByUsername(String userHeaderValue) throws UsernameNotFoundException {
		CustomToken token = tokenValidator.extract(userHeaderValue);
		Collection<SimpleGrantedAuthority> authorities = authorityLoader.load(userHeaderValue);
		return userBuilder.build(token, authorities);
	}
}
