package io.agilehandy.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Winch
 */
public class HomePage {

	public static HomePage to(WebDriver driver, int port) {
		driver.get("http://localhost:" + port +"/secured/home");
		return PageFactory.initElements(driver, HomePage.class);
	}

	private final WebDriver webDriver;

	@FindBy(id = "username")
	private WebElement username;

	@FindBy(css = "input[type=submit]")
	private WebElement logoutButton;

	public HomePage(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public HomePage assertAt() {
		assertThat(this.webDriver.getTitle()).isEqualTo("Spring Security - Demo");
		return this;
	}

	public HomePage assertUsername(String username) {
		assertThat(this.username.getText()).isEqualTo(username);
		return this;
	}
}
