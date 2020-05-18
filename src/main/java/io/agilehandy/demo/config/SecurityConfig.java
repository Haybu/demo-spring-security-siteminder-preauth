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
package io.agilehandy.demo.config;

import io.agilehandy.demo.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

/**
 * @author Haytham Mohamed
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.security.siteminder.principal.header}")
	String principalHeaderName;

	@Value("${spring.security.siteminder.credential.header}")
	String credentialHeaderName;

	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	private final CustomUserDetailsService userDetailsService;

	public SecurityConfig(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(){

		log.info("Configuring pre authentication provider");

		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper =
				new UserDetailsByNameServiceWrapper<>(userDetailsService);

		PreAuthenticatedAuthenticationProvider provider =
				new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(wrapper);

		return provider;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
	}

	public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {

		RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setPrincipalRequestHeader(principalHeaderName);
		filter.setCredentialsRequestHeader(credentialHeaderName);
		filter.setExceptionIfHeaderMissing(true);
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/secured/**").hasAuthority("ROLE_USER")
				.antMatchers("/permitted/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin(Customizer.withDefaults())
				.addFilter(requestHeaderAuthenticationFilter())
		;
	}

}
