package pageObjects;

import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

public class HomePage extends BasePage {
	public static WebDriver driver;
	
	public HomePage(WebDriver driver) {
		this.driver = (driver);
	}

	String leftNavSearchCheckBox = "//section[@id='sidebar']//label[text()='<replace>']/../input[@type='checkbox']";

	public WebElement searchCheckBox(String searchCheckBox) {
		String newXPath = getXPathReplaced(leftNavSearchCheckBox, searchCheckBox);
		// System.out.println("newXPath = " + newXPath);
		return driver.findElement(By.xpath(newXPath));
	}

	public static WebElement error() {
		return driver.findElement(By.xpath("//div[@id='toast-container']"));
	}

	public static WebElement search1() {
		return driver.findElement(By.xpath("//section[@id='sidebar']//input[@name='search']"));
	}

	public WebElement cart() {
		return driver.findElement(By.xpath("//button[contains(@routerlink,'cart')]"));
	}

	public static WebElement itemHeader() {
		return driver.findElement(By.xpath("//div[@class='card-body']/h5"));
	}

	public WebElement view(String itemname) {

		// HomePage hp = new HomePage(driver);
		// List<WebElement> itemHeader;
		List<WebElement> products = (List<WebElement>) HomePage.itemHeader();
		for (WebElement product : products) {
			// System.out.println("product.getText() = " + product.getText());
			if (product.getText().trim().equalsIgnoreCase(itemname)) {
				WebElement view = product.findElement(By.xpath("../button[contains(@class,'w-40')]"));
				return view;
			}
		}
		return null;

	}

	@Step("This method will add {0} to cart.")
	public int AddToCart(String Products) throws Throwable {

		String[] arrProducts = (Products.split(";"));
		int productCost;
		int Total = 0;
		for (String productName : arrProducts) {
			int itemsCountInCartBeforeAdd, itemsCountInCartAfterAdd;

			WebElement cart = driver.findElement(By.xpath("//button[contains(@routerlink,'cart')]/label"));

			if (cart.getText().isEmpty()) {
				itemsCountInCartBeforeAdd = 0;
			} else {
				itemsCountInCartBeforeAdd = Integer.parseInt(cart.getText());
			}

			List<WebElement> allProducts = driver.findElements(By.xpath("//div[@class='card-body']"));
			for (WebElement product : allProducts) {
				String pName = product.findElement(By.xpath("h5/b")).getText();
				if (pName.equalsIgnoreCase(productName.trim())) {
					Allure.step("Product = " + pName + " found on page", Status.PASSED);
					highlight(product);
					click(product.findElement(By.xpath("button[2]")));
					wait(2);
					itemsCountInCartAfterAdd = Integer.parseInt(cart.getText());
					if (itemsCountInCartAfterAdd == (1 + itemsCountInCartBeforeAdd)) {
						Allure.step("Product = " + pName + " added in cart successfully.", Status.PASSED);
						String tempCost = product.findElement(By.xpath("div/div")).getText();
						tempCost = tempCost.replace("$", "").trim();
						productCost = (int) Float.parseFloat(tempCost);
						Total = Total + productCost;

					} else {
						Allure.step(
								"Fail. Items not added item in the Cart. Item Name = '" + productName + "'."
										+ "\nItems in Cart before adding = " + itemsCountInCartBeforeAdd + "."
										+ "\nItems in Cart after adding = " + itemsCountInCartAfterAdd + ".",
								Status.FAILED);

					}
				}
			}

		}
		// System.out.println("Total = " + Total);
		Allure.step("The total is calculated. Total = " + Total, Status.PASSED);
		// iReporter.log("The total is calculated. Total = " + Total, "Pass");

		return (Total);

	}

	public void DownLoadImage(String productName) throws InterruptedException, IOException {

		WebElement item = HomePage.itemHeader();
		highlight(item);
		if (item.getText().trim().equalsIgnoreCase(productName) && objectPresent(item)) {
			WebElement image = item.findElement(By.xpath("//div[@class='card-body']/h5/../../img"));
			highlight(image);
			String imageSource = image.getAttribute("src");
			URL imageURL = new URL(imageSource);
			BufferedImage saveImage = ImageIO.read(imageURL);
			ImageIO.write(saveImage, "jpg", new File("d://logo-forum.png"));
			Allure.step("Image for "+productName + " saved successfully at d://logo-forum.png",Status.PASSED);
		}
	}

	public void pressKeys(Keys key_Name) {
		Actions actions = new Actions(driver);
		actions.sendKeys(key_Name).perform();

	}

}
