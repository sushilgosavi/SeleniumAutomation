package pageObjects;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

public class CartPage  extends BasePage{

	public WebDriver driver;
	public int Salary=0;
	public CartPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement checkout() {
		return driver.findElement(By.xpath("//li/button[contains(@class,'btn-primary')]"));
	}
	
	public WebElement cartTable() {
		return driver.findElement(By.xpath("//div[@class='cart']"));
	}
	

	public void ValidateCart(String Product, int Cost) throws Throwable {
		int itemCostinCart = 0;
		String str_itemCostinCart = "", itemNameInCart = "";
		
		List<WebElement> tableRows = cartTable().findElements(By.xpath("ul"));

		for (WebElement eachRow : tableRows) {
			itemNameInCart = eachRow.findElement(By.xpath("li/div/div/h3")).getText();
			
			if(itemNameInCart.equalsIgnoreCase(Product)) {
				str_itemCostinCart = eachRow.findElement(By.xpath("li/div/div[2]")).getText().trim();
				itemCostinCart = Integer.valueOf(str_itemCostinCart.replace("$ ", "").trim());
				if(itemCostinCart == Cost) {
					Allure.step("Product and Cost matched in cart. Product to verify = " + Product + ". Cost to verify = " + Cost 
							+ ". Item Name in Cart = " + itemNameInCart + ". Item Cost in Cart = " + itemCostinCart, Status.PASSED);
					return;
				}
			}
		}
		Allure.step("Product and Cost DO NOT matched in cart. Product to verify = " + Product + ". Cost to verify = " + Cost, Status.FAILED);
		
	}



}
