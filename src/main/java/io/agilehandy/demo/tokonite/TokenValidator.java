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

@Service
public class TokenValidator {

	// TODO: use your token validation implementation
	public IssoToken validateToken(String token) {
		IssoToken issoToken = map(token);

		// dummy validation, change as appropriate
		if (issoToken == null) {
			throw new IssoTokenValidationException("token does not conform to ISSO standard!");
		} else if (!issoToken.getIsActive()) {
			throw new IssoTokenValidationException("token is not active!");
		}

		return issoToken;
	}

	private IssoToken map(String json) {
		ObjectMapper objectMapper =new ObjectMapper();
		IssoToken issoToken = null;
		try {
			issoToken = objectMapper.readValue(json, IssoToken.class);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return issoToken;
	}

}
