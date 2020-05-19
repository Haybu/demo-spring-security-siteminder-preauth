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
package io.agilehandy.demo.tokonite;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

/**
 * @author Haytham Mohamed
 **/

// Dummy token validator

@Service
public class CustomTokenValidator {

	// TODO: use your token validation implementation
	// dummy validation, change as appropriate
	public CustomToken extract(String token) {
		CustomToken issoToken = map(token);

		if (issoToken == null) {
			throw new CustomTokenValidationException("token does not conform to ISSO standard!");
		} else if (!issoToken.getIsActive()) {
			throw new CustomTokenValidationException("token is not active!");
		}

		return issoToken;
	}

	private CustomToken map(String json) {
		ObjectMapper objectMapper =new ObjectMapper();
		CustomToken issoToken = null;
		try {
			issoToken = objectMapper.readValue(json, CustomToken.class);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return issoToken;
	}

}
