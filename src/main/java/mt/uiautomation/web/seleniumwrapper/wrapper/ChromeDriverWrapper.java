package mt.uiautomation.web.seleniumwrapper.wrapper;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Mark
 *
 */

public class ChromeDriverWrapper extends SeleniumWrapper {

	public void setUp(String url, String driverPath) {
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions options = new ChromeOptions();
		options.addArguments(
				Arrays.asList("--no-sandbox", "--disable-logging", "--disable-extensions", "--lang=en-US"));
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();

		_driver = new ChromeDriver(capabilities);
		_driver.get(url);

		_driver.manage().window().setSize(new Dimension(1520, 920));
		_driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		_driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);

		_wait = new WebDriverWait(_driver, DEFAULT_TIMEOUT);
	}

}
