package mt.uiautomation.web.seleniumwrapper.exception;

/**
 * @author Mark
 *
 */

public class DriverException extends RuntimeException  {
	private static final long serialVersionUID = 7628393484973420191L;

	public DriverException() {
	}

	public DriverException(String message) {
		super(message);
	}

	public DriverException(String message, Throwable cause) {
		super(message, cause);
	}

	public DriverException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DriverException(Throwable cause) {
		super(cause);
	}
}
