package mt.uiautomation.web.seleniumwrapper;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import mt.uiautomation.web.seleniumwrapper.wrapper.ChromeDriverWrapper;
import mt.uiautomation.web.seleniumwrapper.wrapper.SeleniumWrapper;

/**
 * @author Mark
 *
 */
public class Base {
	public static SeleniumWrapper _driver;

	@BeforeClass
	public static void beforeClass() throws Exception {
		String url = System.getProperty("server");
		Assert.assertNotNull("must provide server argument", url);

		String platform = System.getProperty("browser", "chrome").toLowerCase();

		String driverPath = System.getProperty("driverPath");

		if (driverPath == null) {
			String osVersion = System.getProperty("os.name");

			if (platform.equals("chrome")) {
				if (osVersion.toLowerCase().contains("window")) {
					driverPath = "chromedriver.exe";
				} else if (osVersion.toLowerCase().contains("mac")) {
					driverPath = "chromedriver";
				} else {
					// Assume your OS is linux
					driverPath = "chromedriverLinux";
				}
			} else if (platform.equals("ghost")) {
				if (osVersion.toLowerCase().contains("window")) {
					driverPath = "phantomjs.exe";
				} else if (osVersion.toLowerCase().contains("mac")) {
					driverPath = "phantomjs";
				} else {
					throw new UnsupportedOperationException(String.format("%s is not supported", osVersion));
				}
			} else if (platform.equals("firefox")) {
				if (osVersion.toLowerCase().contains("window")) {
					String path = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
					System.out.println("assume firefox binary is on " + path);
					driverPath = path;
				} else if (osVersion.toLowerCase().contains("mac")) {
					String path = "/Applications/Firefox.app/Contents/MacOS/firefox";
					System.out.println("assume firefox binary is on " + path);
					driverPath = path;
				} else {
					throw new UnsupportedOperationException(String.format("%s is not supported", osVersion));
				}
			}
		}

		if (platform.equalsIgnoreCase("chrome")) {
			_driver = new ChromeDriverWrapper();
		} // Add more condition for different driver

		_driver.setUp(url, driverPath);

		// Initialize page class here

	}

	@AfterClass
	public static void afterClass() {
		if (null != _driver) {
			_driver.tearDown();
		}
	}

}
