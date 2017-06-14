package mt.uiautomation.web.seleniumwrapper.wrapper;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mt.uiautomation.web.seleniumwrapper.exception.DriverException;

/**
 * @author Mark
 *
 */
public abstract class SeleniumWrapper {
	protected static Logger _logger = LoggerFactory.getLogger(SeleniumWrapper.class.getSimpleName());
	public static final int DEFAULT_maxTries = 3;
	public static final int DEFAULT_RETRYTIME = 5;
	public static final int DEFAULT_TIMEOUT = 5;

	protected Actions _action;
	protected WebDriver _driver;
	protected WebDriverWait _wait;

	/**
	 * The Click(By) method clicks this element.
	 * 
	 * @param by
	 *            Locating mechanism
	 * @see click(WebElement)
	 */
	public void click(By by) {
		int tries = 0;
		while (tries < DEFAULT_maxTries) {
			try {
				click(waitGetElement(by));
				break;
			} catch (StaleElementReferenceException | NotFoundException | TimeoutException e) {
				++tries;
				_logger.info(String.format("Retry: " + tries + " Click %s", by.toString()));
				if (tries == DEFAULT_maxTries) {
					throw new DriverException(e);
				}
			}
		}
	}

	/**
	 * The Click(WebElement) method clicks this element. This method is affect
	 * by "Explicit wait" This method will wait until this element is enable and
	 * visible. And if this Method catches a StaleReferenceException this will
	 * retry to click this element.
	 * 
	 * @param element
	 *            the WebElement
	 * @exception RobotException
	 * @see {@link ClickAction}
	 */
	public void click(final WebElement link) {
		_logger.info(String.format("click %s", link.toString()));
		try {
			_wait.until(ExpectedConditions.elementToBeClickable(link));
			link.click();

		} catch (WebDriverException e) {
			try {
				// Add Handling
			} catch (WebDriverException e1) {
				throw new StaleElementReferenceException("Reference not found", e1);
			}
			throw new StaleElementReferenceException("Reference not found", e);
		}
	}

	/**
	 * Selects the value from this element. This Method retry when cause by
	 * StaeElementReferenceException and if this element value is not as is
	 * expected
	 * 
	 * @param by
	 *            locating mechanism
	 * @param value
	 *            to be selected
	 */
	public void selectOptionByValue(final By by, final String value) {
		int tries = 0;
		int RETRY_TIMEOUT = DEFAULT_TIMEOUT;
		WebDriverWait wait = _wait;

		do {
			try {

				wait.until(ExpectedConditions.presenceOfElementLocated(by));

				WebElement element = _driver.findElement(by);
				Select list = new Select(element);
				List<WebElement> listIndex = list.getAllSelectedOptions();
				wait.until(ExpectedConditions.visibilityOfAllElements(listIndex));

				final String curOpt = list.getFirstSelectedOption().getText();

				list.selectByValue(value);

				final String opt = list.getFirstSelectedOption().getText();

				wait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return !curOpt.equals(opt);
					}
				});

				break;
			} catch (StaleElementReferenceException | NotFoundException | org.openqa.selenium.TimeoutException e) {
				++tries;
				_logger.info(String.format("Retry: " + tries + " SelectOption %s", by.toString()));

				RETRY_TIMEOUT = RETRY_TIMEOUT + DEFAULT_RETRYTIME;
				wait = new WebDriverWait(_driver, RETRY_TIMEOUT);

				if (tries == DEFAULT_maxTries) {
					throw new DriverException(e);
				}
			}
		} while (tries < DEFAULT_maxTries);
	}

	/**
	 * Setups this browser and URL.
	 * 
	 * @param url
	 *            String URL of the Page
	 * @param driverPath
	 *            String WebDriver Location path
	 */
	public abstract void setUp(String url, String driverPath);

	public void tearDown() {
		try {
			_driver.quit();
		} catch (Exception e) {

		}
	}

	/**
	 * The waitGetElement(By) method wait and gets this element is visible.
	 *
	 * @param by
	 *            locating mechanism
	 * @return WebElement located
	 */
	public WebElement waitGetElement(final By by) {
		return waitGetElement(by, null);
	}

	/**
	 * The waitGetElement(By, WebElement) method wait and gets until this
	 * element is visible.
	 * 
	 * @param by
	 *            locating mechanism, Child Element
	 * @param parentElement
	 *            the WebElement. parentElement can be Null
	 * @return WebElement located
	 */
	public WebElement waitGetElement(final By identifier, final WebElement parentElement) {
		WebElement element = null;
		int tries = 0;
		int RETRY_TIMEOUT = DEFAULT_TIMEOUT;
		WebDriverWait wait = _wait;

		while (tries < DEFAULT_maxTries) {
			_logger.info(String.format("waitGetElement %s", identifier.toString()));
			try {
				if (null == parentElement) {
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(identifier));
					element = _driver.findElement(identifier);
				} else {
					wait.until(ExpectedConditions.visibilityOf(parentElement));
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(identifier));
					element = parentElement.findElement(identifier);
				}

				_wait.until(ExpectedConditions.visibilityOf(element));
				break;
			} catch (StaleElementReferenceException | NotFoundException | TimeoutException e) {
				++tries;
				_logger.info(String.format("Retry: " + tries + " Get Element %s", identifier.toString()));

				RETRY_TIMEOUT = RETRY_TIMEOUT + DEFAULT_RETRYTIME;
				wait = new WebDriverWait(_driver, RETRY_TIMEOUT);

				if (tries == DEFAULT_maxTries) {
					throw new DriverException("Cannot get Element");
				}
			} catch (WebDriverException we) {
				throw new DriverException("Something Went Wrong", we);
			}
		}
		return element;
	}

	/**
	 * The enterText(By,String) method enters character to this element.
	 * 
	 * @param by
	 *            locating mechanism
	 * @param keys
	 *            String to be entered in this element
	 */
	public void enterText(By by, String keys) {
		int tries = 0;
		while (tries < DEFAULT_maxTries) {
			try {
				WebElement element = waitGetElement(by);
				enterText(element, keys);
				break;
			} catch (StaleElementReferenceException | NotFoundException | TimeoutException e) {
				++tries;
				_logger.info(String.format("Retry: " + tries + " Enter %s", by.toString()));
				if (tries == DEFAULT_maxTries) {
					throw new DriverException(e);
				}
			}
		}

	}

	/**
	 * The enterText(WebElement,String) method enters character to this element.
	 * This method is affect by "Explicit wait" This method will wait until this
	 * element is enable and visible. This Method will check the element value
	 * if the expected text is in this element
	 * 
	 * @param element
	 *            the WebeElement
	 * @param keys
	 *            String to be entered in this element
	 */
	public void enterText(final WebElement elem, String keys) {
		_logger.info(String.format("enterText %s \"%s\"", elem.toString(), keys));
		WebDriverWait wait = _wait;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(elem));
			if (elem.getAttribute("value").length() > 0) {
				elem.clear();
			}
			elem.sendKeys(keys);
			wait.until(ExpectedConditions.textToBePresentInElementValue(elem, keys));

			waitUntilCondition(elem.getAttribute("value").equals(keys));
		} catch (WebDriverException e) {
			throw new StaleElementReferenceException("Reference not found", e);
		}
	}

	/**
	 * Waits until this condition become true.
	 * 
	 * @param condition
	 *            Boolean
	 */
	public void waitUntilCondition(final Boolean condition) {
		try {
			_wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return condition;
				}
			});
		} catch (WebDriverException e) {
			throw new DriverException(e);
		} catch (RuntimeException e1) {
			throw new DriverException(e1);
		}
	}

}
