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

public class ViewPage extends BasePage {

	public WebDriver driver;

	public ViewPage(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement productName() {
		return driver.findElement(By.xpath("//div/h2"));
	}
	
	public WebElement addToCart() {
		return driver.findElement(By.xpath("//button[contains(@class,'primary')]"));
	}
	
	public WebElement continueShopping() {
		return driver.findElement(By.xpath("//a[@class='continue']"));
	}

	public WebElement itemCost() {
		return driver.findElement(By.xpath("//div[contains(@class,'col-lg-6')]/div/h3"));
	}
	//div[contains(@class,'col-lg-6')]/div/h2		itemname
		//	item cost
	
	public WebElement cart() {
		return driver.findElement(By.xpath("//button[contains(@routerlink,'cart')]"));
	}
	
	public void validateView(String productName) {
		
	}
}
