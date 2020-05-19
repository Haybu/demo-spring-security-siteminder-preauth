package io.agilehandy.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Test
	void headerAuthenticates(@Autowired WebDriver webDriver) throws Exception {
		HomePage homePage = HomePage.to(webDriver, 8080);
		homePage.assertAt();
		homePage.assertUsername("jsmith");
	}

	@TestConfiguration
	static class CustomizeMockMvcForSiteMinderConfig {
		@Bean
		MockMvcBuilderCustomizer customizer() {
			return builder -> {
				MockHttpServletRequestBuilder authenticatedRequest = get("/")
						.header("SM_USER", "{\"userName\":\"jsmith\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"isActive\":true}");
				builder.defaultRequest(authenticatedRequest);
			};
		}
	}
}
