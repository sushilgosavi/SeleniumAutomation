package pageObjects;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
//import iReporter.iReporter;


public class YourOrdersPage extends BasePage {

	public WebDriver driver;

	public YourOrdersPage(WebDriver driver) {
		this.driver = driver;
		// super(driver);
	}

	public WebElement goBacktoShop() {
		return driver.findElement(By.xpath("//button[@routerlink='/dashboard']"));
	}

	public WebElement Home() {
		return driver.findElement(By.xpath("//button[@routerlink='/dashboard/']"));
	}

	public WebElement orderTable() {
		return driver.findElement(By.xpath("//table"));
	}

	@Step("Step verifyOrders with screenshot. Ordernum = {0}, item = {1}, Cost = {2}, Date = {3}")
	//public String verifyOrders(String Ordernum, String item, String Cost, String strDate) throws Throwable {
	public String verifyOrders(String Ordernum, String item, String Cost) throws Throwable {
		boolean bFound = false;
		String OrderId = "", Name = "", Price = "", BuyDate = "";
		List<WebElement> arrRowsData = orderTable().findElements(By.xpath("tbody/tr"));
		for (WebElement eachRowData : arrRowsData) {
			highlight(eachRowData);
			OrderId = eachRowData.findElement(By.xpath("th")).getText().trim();
			Name = eachRowData.findElement(By.xpath("td[2]")).getText().trim();
			Price = eachRowData.findElement(By.xpath("td[3]")).getText().trim();
			BuyDate = eachRowData.findElement(By.xpath("td[4]")).getText().trim();
			
			
			if (OrderId.equalsIgnoreCase(Ordernum) && (Name.equalsIgnoreCase(item)) && (Price.equalsIgnoreCase(Cost))){
				bFound = true;
				break;
			}

		}
		if (bFound) {
			Allure.step("Passed. Order verified successfully. All details verified successfully. OrderId = " + OrderId
					+ ", Name = " + Name + ", Price = " + Price , Status.PASSED);
			return (Price);
		} else {
			
			Allure.step("Failed. Order NOT verified. Below details are not present in table. OrderId = " + OrderId
					+ ", Name = " + Name + ". Price = " + Price , Status.FAILED);
			return ("");
		}
	}
}
