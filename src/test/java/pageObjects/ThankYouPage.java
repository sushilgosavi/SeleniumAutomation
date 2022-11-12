package pageObjects;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

public class ThankYouPage extends BasePage {

	public WebDriver driver;

	public ThankYouPage(WebDriver driver) {
		this.driver = (driver);
	}

	public WebElement thankyouText() {
		return driver.findElement(By.xpath("//h1"));
	}

	public WebElement ordernum() {
		return driver.findElement(By.xpath("//tr[@class='ng-star-inserted']/td"));
	}

	public WebElement orderHistory() {
		return driver.findElement(By.xpath("//label[contains(@routerlink,'myorders')]"));
	}

	@Step("Retrieving Order number from Thank You page after successful submission")
	public String getOrdernum() throws Throwable {
		if (thankyouText().getText().contains("THANKYOU")) {
			String ordernum = ordernum().getText().replace("|", "").trim();
			// System.out.println("The order number is found successfully. Order Num = " +
			// ordernum + ". Pass" );
			highlight(thankyouText());
			Allure.step("The order number is found successfully. Order Num = " + ordernum, Status.PASSED);
			return (ordernum);
		} else {
			// System.out.println("The order number is NOT found. Fail" );
			Allure.step("The order number is NOT found.", Status.FAILED);
			return "";
		}
	}

	public String getOrdernum1() {
		String OrderNums = "", OrderNumsToReturn = "", arrOrderNum[], tempOrder = "";
		int orderCount = 0;
		if (thankyouText().getText().contains("THANKYOU")) {
			OrderNums = ordernum().getText();
			arrOrderNum = OrderNums.split(" ");
			orderCount = arrOrderNum.length;
			for (int i = 0; i < orderCount; i++) {
				tempOrder = arrOrderNum[i].trim();
				if (!tempOrder.equalsIgnoreCase("|")) {
					OrderNumsToReturn = OrderNumsToReturn + arrOrderNum[i] + "-";
					// System.out.println("OrderNums = " + OrderNums1 );
				}
			}
			Allure.step("The order number found. Order Number = " + OrderNums, Status.PASSED);
			return (OrderNumsToReturn);
		} else {
			Allure.step("The order number is NOT found.", Status.FAILED);
			return "";
		}

	}
}
