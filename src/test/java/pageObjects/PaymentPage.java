package pageObjects;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

public class PaymentPage extends BasePage {

	public WebDriver driver;

	public PaymentPage(WebDriver driver) {
		this.driver=(driver);
	}

	public WebElement creditcard() {
		return driver.findElement(By.xpath("//input[@class='input txt text-validated'][@type='text']"));
	}
	
	public WebElement ExpiryMonth() {
		return driver.findElement(By.xpath("//select[@class='input ddl'][1]"));
	}
	
	public WebElement ExpiryDate() {
		return driver.findElement(By.xpath("//select[@class='input ddl'][2]"));
	}
	
	public WebElement CVV() {
		return driver.findElement(By.xpath("//div[@class='field small']/input[@class='input txt']"));
	}
	
	public WebElement name() {
		return driver.findElement(By.xpath("//div[@class='field']/input[@class='input txt']"));
	}	
	
	public WebElement email() {
		return driver.findElement(By.xpath("//div[contains(@class,'user__name')]/input"));
	}	
	
	public WebElement country() {	
		return driver.findElement(By.xpath("//div[@class='form-group']"));
	}
	
	public WebElement placeOrder() {
		return driver.findElement(By.xpath("//a[contains(@class,'action__submit')]"));
	}
	
	@Step("This method validate if {0} is present on Payment Page or not")
	public void validateItemsOnPaymentPage(String products) throws Throwable {

		String productName = "";
		List<WebElement> allProducts = driver.findElements(By.xpath("//div[@class='item__title']"));
		for (WebElement product : allProducts) {
			highlight(product);
			productName = product.getText();
			if ((products.toLowerCase().indexOf(productName.trim())) < 0) {
				Allure.step("Product is NOT found on Payment Page. Product in the cart = " + productName
						+ ". List of product to be verified = " + products, Status.FAILED)	;
				//System.out.println("Product is NOT found on Payment Page. Product in the cart = " + productName
				//		+ ". List of product to be verified = " + products);	
				return;
			}
		}
		Allure.step("All products are found on payment page. Products = " + products, Status.PASSED);
		//System.out.println("All products are found on payment page. Products = " + products);	
	}

	@Step("Adding paymenet method = {0}")
	public void addPayment(String Payment, String CardNum, String Expiry, String CVV, String NameOnCard, String Email,
			String Country) throws Throwable {
		
		ThankYouPage tp1 = new ThankYouPage(driver);
		boolean found = false;
		List<WebElement> PaymentTypes = driver.findElements(By.xpath("//div[contains(@class,'payment__type')]/div"));
		for (WebElement PaymentType : PaymentTypes) {
			if (PaymentType.getText().equalsIgnoreCase(Payment)) {
				click(PaymentType);
				found = true;
				break;
			}
		}
		if(!found) {
			Allure.step("Payment method not present. Payment = " + Payment, Status.FAILED);
			//System.out.println("Payment method not present. Payment = " + Payment);
			return;
		}
		
		enterEditField(creditcard(), CardNum );
	
		select(ExpiryMonth(),Expiry.substring(0, 2));
		
		select(ExpiryDate(),Expiry.substring(2, 4));
		
		enterEditField(CVV(),CVV);
		
		enterEditField(name(),NameOnCard);
		
		enterEditField(email(), Email);		
		
		select(country(), Country);
		
		placeOrder().click();	
		
		//System.out.println("Successfully Placed the Order. Pass");
		Allure.step("Successfully Placed the Order.", Status.PASSED);
	
	}
}
