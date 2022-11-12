package pageObjects;

//import reporter.reporter;

import testCases.E2E_SmokeSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import org.testng.asserts.SoftAssert;

import edu.emory.mathcs.backport.java.util.Collections;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
//import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.openqa.selenium.support.ui.Select;

@Listeners(ListenersPackage.ListenerTest.class)
public class BasePage {
	public static WebDriver driver;

	@Step("Logging in ....")
	@BeforeMethod()
	public WebDriver Login() throws Throwable {
		
		System.out.println(" Test is starting now");

		driver = initiailzeDriver();
		
		String appURL = getProperty("appURL");
		String UserEmail = getProperty("UserEmail");
		String UserPwd = getProperty("UserPwd");

		driver.get(appURL);
		driver.manage().window().maximize();
		WebElement userid = driver.findElement(By.id("userEmail"));
		highlight(userid);
		userid.sendKeys(UserEmail);
		
		//reporter.log();
		//System.out.print("UserEmail logged in = " + UserEmail + "\n");
		Allure.step("UserEmail logged in = " + UserEmail + "\n", Status.PASSED);
		WebElement password = driver.findElement(By.id("userPassword"));
		highlight(password);
		password.sendKeys(UserPwd);
		WebElement login = driver.findElement(By.id("login"));
		highlight(login);
		login.click();
		String pageTitle = driver.getTitle();
		//softAssertion.assertEquals(pageTitle, "lets shop", "Correct Page " + pageTitle + " Appeared.");
		//softAssertion.assertEquals(pageTitle, "let's shop", "Expected Page Didnot appear");

		if (pageTitle.equalsIgnoreCase("let's shop")) {
			Allure.step("Correct Page " + pageTitle + " Appeared.", Status.PASSED);
			//System.out.println("Correct Page " + pageTitle + " Appeared.");
			
		} else {
			Allure.step("Incorrect Page " + pageTitle + " Appeared.",  Status.FAILED);
			//System.out.println("Incorrect Page " + pageTitle + " Appeared.");
			driver.quit();
			System.exit(1);
		}
		return driver;
	}
	
	@Step("Initializing web driver")
	public WebDriver initiailzeDriver() throws Throwable {
		String browserName = getProperty("browser");
		String globalTimeOut = getProperty("TimeOut");
		if (browserName.trim().equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();

		} else if (browserName.trim().equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();

		} else if (browserName.trim().equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browserName.trim().equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		} else if (browserName.trim().equalsIgnoreCase("safari")) {
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
		} else {
			
			Allure.step("Browser = "+browserName+" is invalid.", Status.FAILED);
			//System.out.println("Fail. Browser = "+browserName+" is invalid.");
			System.exit(1);
		}
		
		Allure.step("Browser = "+browserName+" is successfully launched.", Status.PASSED);
		//System.out.println("Pass. Browser = "+browserName+" is successfully launched.");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(globalTimeOut)));
		return driver;
	}
	

	
	@AfterMethod()
	public void tearDown() {
		System.out.println(" Test is closing now... !");
		driver.quit();
	}
	
	public String getProperty(String PropName) throws IOException {

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				"D:\\Java\\Udemy_WorkSpace\\E2EProject - Copy\\src\\test\\java\\utility\\globalVariables.properties");
		prop.load(fis);

		return prop.getProperty(PropName);

	}

	@Step("Clicking on {0}")
	public void click(WebElement elem) throws Throwable {
		// WebElement element = driver.findElement(By.id("id_of_element"));
		if(objectPresent(elem)) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elem);
			wait(1);
			highlight(elem);
			
			elem.click();
			wait(1);
			Allure.step("Successfully clicked on " + elem.toString(), Status.PASSED);
		}
		//System.out.println("Successfully clicked on " + elem.toString());
		
	}

	public void wait(int iTime) throws InterruptedException {
		Thread.sleep(iTime * 1000);
	}

	@Step("Selecting an option {1} from dropdown {0}")
	public void select(WebElement dropdown, String option) throws Throwable {
		boolean bFound = false;
		highlight(dropdown);
		if (dropdown.getTagName().equalsIgnoreCase("select")) {
			Select objDropDown = new Select(dropdown);
			wait(1);
			objDropDown.selectByVisibleText(option);
			if(objDropDown.getFirstSelectedOption().equals(option)) {
				//System.out.println(option + " is successfully selected by visible option method. Pass");
				
				Allure.step(option + " is successfully selected by visible option method.", Status.PASSED);
				//takeScreenprint(option + " is successfully selected by visible option method.");
				return;
			}
		} else if (dropdown.getTagName().equalsIgnoreCase("div")) {
			WebElement objInput = dropdown.findElement(By.xpath("input"));
			objInput.sendKeys(option);
			wait(1);
			List<WebElement> objDDs = dropdown.findElements(By.xpath("section/button"));
			for (WebElement objDD : objDDs) {
				if (objDD.getText().equalsIgnoreCase(option)) {
					objDD.click();
					bFound = true;
					break;
				}
			}

		}
		if(bFound) {
			//System.out.println(option + " is successfully selected.");
			//takeScreenprint(option + " is successfully selected.");
		
			Allure.step(option + " is successfully selected.",Status.PASSED);
		}
	}

	@Step("Entering value {1} in field {0}")
	public void enterEditField(WebElement textField, String ValuetoEnter) throws Throwable {
		if (!textField.isDisplayed()) {
			//System.out.println("The text field = " + textField.getText() + " is not displayed on page.");
			//takeScreenprint("The text field = " + textField.getText() + " is not displayed on page.");
			Allure.step("The text field = " + textField.getText() + " is not displayed on page.", Status.FAILED);
			return;
		}
		if (!textField.isEnabled()) {
			//System.out.println("The text field = " + textField.getText() + " is not enabled on page.");
			//takeScreenprint("The text field = " + textField.getText() + " is not enabled on page.");
			Allure.step("The text field = " + textField.getText() + " is not enabled on page.", Status.FAILED);
			return;
		}
		clear(textField);

		textField.sendKeys(ValuetoEnter);
		if (textField.getAttribute("value").equals(ValuetoEnter)) {
			//System.out.println("Value successfully entered = " + textField.getAttribute("value"));
			//takeScreenprint("Value successfully entered = " + textField.getAttribute("value"));
			highlight(textField);
			Allure.step("Value successfully entered = " + textField.getAttribute("value") , Status.PASSED);
		}else {
			//System.out.println("Wrong value entered = " + textField.getAttribute("value"));
			//takeScreenprint("Wrong value entered = " + textField.getAttribute("value"));
			Allure.step("Wrong value entered = " + textField.getAttribute("value"), Status.FAILED);
		}
	}

	@Step("Field {0} is cleared")
	public void clear(WebElement editbox) throws Throwable {
		if (!editbox.getAttribute("value").isEmpty()) {
			editbox.clear();
			if(editbox.getAttribute("value").isEmpty()) {
				//System.out.println("The text field = " + editbox.toString()+ " is cleared.");
				//takeScreenprint("The text field = " + editbox.toString()+ " is cleared.");
				Allure.step("The text field = " + editbox.toString()+ " is cleared.",  Status.PASSED);
			}else {
				//System.out.println("The text field = " + editbox.toString() + " is NOT cleared. Fail");
				//takeScreenprint
				Allure.step("The text field = " + editbox.toString() + " is NOT cleared.",  Status.FAILED);
			}
		}
	}

	public String GetDateInTimeZone(Date idate, String strTimeZone) {
		Date date = idate;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String timeZone = Calendar.getInstance().getTimeZone().getID();
		formatter.setTimeZone(TimeZone.getTimeZone(strTimeZone));
		// date = formatter.format(date);
		String strDate = formatter.format(date);
		formatter = new SimpleDateFormat("E MMM dd");
		strDate = formatter.format(date);
		//System.out.println("strDate = " + strDate);
		return strDate;

	}
	
	public String getXPathReplaced(String xpath, String toReplace) {
		String newXPath="";
		newXPath = xpath.replace("<replace>", toReplace);
		return newXPath;
	}

	public boolean objectPresent(WebElement obj) {
		if(!obj.isDisplayed() || !obj.isEnabled()) {
			Allure.step(obj.toString() + " is not available on the screen.", Status.FAILED);
			return false;
		}
		Allure.step(obj.toString() + " is available on the screen.", Status.PASSED);
		highlight(obj);
		return true;
	}
	
	public void performAction( String ActionToDo, WebElement sourceWebEle, WebElement destiWebEle) { 

		Actions actions = new Actions(driver);
		//WebElement elementLocator = WebEle.findElement(By.id("ID"));
		//String monthString;
        switch (ActionToDo.trim().toLowerCase()) {
        case "doubleclick":
        	System.out.print("before doubleclick");
        	actions.doubleClick(sourceWebEle).perform();
        	System.out.print("after doubleclick");
        	break;
        case "rightclick":
        	actions.contextClick(sourceWebEle).perform();
        	break;
        case "click":
        	actions.click(sourceWebEle).perform();
        	break;
        case "click&hold":
        	actions.clickAndHold(sourceWebEle).perform();	
        	break;
        case "dragAndDrop":
        	System.out.print("before drag and drop.");
        	System.out.print("sourceWebEle = " + sourceWebEle.toString());
        	System.out.print("destiWebEle = " + destiWebEle.toString());
        	
        	actions.dragAndDrop(sourceWebEle, destiWebEle).build().perform();	
        	System.out.print("after drag and drop.");
        	break;
        	
        }
	}
	
	public WebElement highlight(WebElement elem) {
	    //WebElement elem = driver.findElement(by);
	    // draw a border around the found element
	    if (driver instanceof JavascriptExecutor) {
	        ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", elem);
	    }
	    return elem;
	}
}
