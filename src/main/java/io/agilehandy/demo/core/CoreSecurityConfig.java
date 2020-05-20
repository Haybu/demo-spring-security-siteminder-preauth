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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author Haytham Mohamed
 **/

@Configuration
@EnableWebSecurity
public class CoreSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.security.siteminder.principal.header}")
	String principalHeaderName;

	@Value("${spring.security.siteminder.credential.header}")
	String credentialHeaderName;

	@Autowired
	AuthenticationManager authenticationManager;

	public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
		RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
		filter.setAuthenticationManager(this.authenticationManager);
		filter.setPrincipalRequestHeader(this.principalHeaderName);
		filter.setCredentialsRequestHeader(this.credentialHeaderName);
		filter.setExceptionIfHeaderMissing(true);
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(request -> request
				.mvcMatchers("/secured/**").hasRole("USER")
				.mvcMatchers("/permitted/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(withDefaults())
			.addFilter(requestHeaderAuthenticationFilter());
	}

}
