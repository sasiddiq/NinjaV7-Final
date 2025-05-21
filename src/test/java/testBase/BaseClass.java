package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseClass {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public Properties p;
	private static final Logger logger = LogManager.getLogger(BaseClass.class);

	public static WebDriver getDriver() {
		return driver.get();
	}

	@BeforeClass(groups = { "sanity", "regression", "datadriven" })
	@Parameters({ "os", "browser" })
	public void openApp(String os, String br) {
		logger.info("Initializing application on OS: {} and Browser: {}", os, br);

		try (FileReader file = new FileReader(".//src//test//resources//config.properties")) {
			p = new Properties();
			p.load(file);
			logger.debug("Loaded configuration properties");

			WebDriver localDriver = null;
			
			if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {
				DesiredCapabilities capabilities = new DesiredCapabilities();

				// os
				if (os.equalsIgnoreCase("windows")) {
					capabilities.setPlatform(Platform.WIN11);
				} else if (os.equalsIgnoreCase("mac")) {
					capabilities.setPlatform(Platform.MAC);
				} else {
					System.out.println("No matching os");
					return;
				}

				String gridURL = "http://localhost:4444/wd/hub"; // Update if needed
				//String gridURL = "http://192.168.86.36:4444/wd/hub"; // this will also work
				

				switch (br.toLowerCase()) {
				case "chrome":
					ChromeOptions chromeOptions = new ChromeOptions();
					localDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), chromeOptions.merge(capabilities));
					break;

				case "firefox":
					FirefoxOptions firefoxOptions = new FirefoxOptions();
					localDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), firefoxOptions.merge(capabilities));
					break;

				case "edge":
					EdgeOptions edgeOptions = new EdgeOptions();
					localDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), edgeOptions.merge(capabilities));
					break;

				default:
					logger.error("No matching browser found: {}", br);
					return;
				}

			}
			
			
			
			
			
			if (p.getProperty("execution_env").equalsIgnoreCase("local")) {
			

			switch (br.toLowerCase()) {
				case "chrome":
					localDriver = new ChromeDriver();
					logger.debug("ChromeDriver initialized");
					break;
				case "edge":
					localDriver = new EdgeDriver();
					logger.debug("EdgeDriver initialized");
					break;
				case "firefox":
					localDriver = new FirefoxDriver();
					logger.debug("FirefoxDriver initialized");
					break;
				default:
					logger.error("Unsupported browser: {}", br);
					return;
			}
			}
			driver.set(localDriver);
			logger.debug("Driver assigned to thread-local storage");

			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
			getDriver().manage().window().maximize();
			getDriver().get(p.getProperty("appURL"));

			logger.info("Application launched successfully: {}", p.getProperty("appURL"));

		} catch (IOException e) {
			logger.error("Failed to load config.properties file", e);
		} catch (Exception e) {
			logger.error("Error occurred while launching the application", e);
		}
	}

	@AfterClass(groups = { "sanity", "regression", "datadriven" })
	public void closeApp() {
		try {
			getDriver().quit();
			logger.info("Browser session closed successfully");
		} catch (Exception e) {
			logger.error("Failed to close the browser session", e);
		}
	}

	public String captureScreen(String tname) {
		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";

		try {
			File sourceFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
			File targetFile = new File(targetFilePath);

			if (sourceFile.renameTo(targetFile)) {
				logger.info("Screenshot captured: {}", targetFilePath);
			} else {
				logger.warn("Screenshot could not be renamed/moved");
			}

		} catch (Exception e) {
			logger.error("Failed to capture screenshot for test: {}", tname, e);
		}

		return targetFilePath;
	}
}
