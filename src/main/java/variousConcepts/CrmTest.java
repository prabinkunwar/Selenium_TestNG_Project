package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CrmTest {

	String browser;
	String url;
	WebDriver driver;

	// ELEMENT LIST
	By USERNAME_LOCATOR = By.xpath("//input[@id='username']");
	By PASSWORD_LOCATOR = By.xpath("//input[@id='password']");
	By SIGNIN_BUTTON_LOCATOR = By.xpath("//button[@name='login' and @type='submit']");
	By DASHBOARD_HEADER_LOCATOR = By.xpath("//h2[contains(text(), 'Dashboard')]");
	By CUSTOMER_MENU_LOCATOR = By.xpath("//span[contains(text(), 'Customers')]");
	By ADD_CUSTOMER_MENU_LOCATOR = By.xpath("//a[contains(text(),'Add Customer')]");

	By ADD_CONTACT_HEADER_LOCATOR = By.xpath("//h5[contains(text(),'Add Contact')]");
	By FULL_NAME_LOCATOR = By.xpath("//input[@id='account']");
	By COMPANY_DROPDOWN_LOCATOR = By.xpath("//select[@id='cid']");
	By EMAIL_LOCATOR = By.xpath("//input[@id='email']");
	By PHONE_LOCATOR = By.xpath("//input[@id='phone']");
	By ADDRESS_LOCATOR = By.xpath("//input[@id='address']");
	By CITY_LOCATOR = By.xpath("//input[@id='city']");
	By STATE_REGION_LOCATOR = By.xpath("//input[@id='state']");
	By ZIP_POSTALCODE_LOCATOR = By.xpath("//input[@id='zip']");
	By COUNTRY_DROPDOWN_LOCATOR = By.xpath("//select[@id='country']");
	By SAVE_BUTTON_LOCATOR = By.xpath("//button[@id='submit']");

	@BeforeTest
	public void readConfig() {

		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser Used = " + browser);
			url = prop.getProperty("url");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
			driver = new FirefoxDriver();
		}
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

//	@Test(priority = 1)
	public void logintest() {
		driver.findElement(USERNAME_LOCATOR).sendKeys("demo@techfios.com");
		driver.findElement(PASSWORD_LOCATOR).sendKeys("abc123");
		driver.findElement(SIGNIN_BUTTON_LOCATOR).click();

		String dashboardHeaderText = driver.findElement(DASHBOARD_HEADER_LOCATOR).getText();
		Assert.assertEquals(dashboardHeaderText, "Dashboard", "Wrong Page !!");
	}

	@Test(priority = 2)
	public void addCustomerTest() {
		logintest();
		driver.findElement(CUSTOMER_MENU_LOCATOR).click();
		driver.findElement(ADD_CUSTOMER_MENU_LOCATOR).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ADD_CONTACT_HEADER_LOCATOR));

		Assert.assertEquals(driver.findElement(ADD_CONTACT_HEADER_LOCATOR).getText(), "Add Contact", "Wrong Page !!");

		driver.findElement(FULL_NAME_LOCATOR).sendKeys("Selenium October" + generateRandom(9999));

		selectFromDropdown(COMPANY_DROPDOWN_LOCATOR, "Techfios");

		driver.findElement(EMAIL_LOCATOR).sendKeys(generateRandom(9999) + "demo@techfios.com");
		driver.findElement(PHONE_LOCATOR).sendKeys("6784523" + generateRandom(999));
		driver.findElement(ADDRESS_LOCATOR).sendKeys("Dallas");
		driver.findElement(CITY_LOCATOR).sendKeys("Fort Worth");
		driver.findElement(STATE_REGION_LOCATOR).sendKeys("Texas");
		driver.findElement(ZIP_POSTALCODE_LOCATOR).sendKeys("76244");

		selectFromDropdown(COUNTRY_DROPDOWN_LOCATOR, "Nepal");

		driver.findElement(SAVE_BUTTON_LOCATOR).click();

	}

	public void selectFromDropdown(By locator, String visibleText) {
		Select sel = new Select(driver.findElement(locator));
		sel.selectByVisibleText(visibleText);
	}

	public int generateRandom(int boundryNum) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(boundryNum);
		return generatedNum;
	}

//	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
