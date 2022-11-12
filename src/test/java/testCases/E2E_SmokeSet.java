package testCases;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import pageObjects.HomePage;
import pageObjects.PaymentPage;
import pageObjects.ThankYouPage;
import pageObjects.ViewPage;
import pageObjects.YourOrdersPage;
import pageObjects.BasePage;
import pageObjects.CartPage;
import org.testng.annotations.Listeners;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

@Listeners(ListenersPackage.ListenerTest.class)
public class E2E_SmokeSet extends BasePage {
	@QAFDataProvider(dataFile = "resources/DataBank.xls", sheetName = "Orders", key = "TC001")
	@Test(groups="regression")
	@Step("The test for successful order submission")
	public void TC001_SuccessfulOrder(Map<String, String> data) throws Throwable {
		String Product = data.get("Product");
		String PaymentOption = data.get("PaymentOption");
		String CCNum = data.get("CCNum");
		String Expiry = data.get("Expiry");
		String CVV = data.get("CVV");
		String NameOnCard = data.get("NameOnCard");
		String Email = data.get("Email");
		String AddressCountry = data.get("AddressCountry");
		int Total = 0;
		String order = "", arrOrderNum, orders[];

		HomePage hp = new HomePage(driver);
		CartPage cp = new CartPage(driver);
		PaymentPage pp = new PaymentPage(driver);
		ThankYouPage tp = new ThankYouPage(driver);
		YourOrdersPage yop = new YourOrdersPage(driver);


		Total = hp.AddToCart(Product);
		click(hp.cart());
		cp.ValidateCart(Product, Total);
		click(cp.checkout());
		pp.validateItemsOnPaymentPage(Product);
		pp.addPayment(PaymentOption, CCNum, Expiry, CVV, NameOnCard, Email, AddressCountry);
		arrOrderNum = tp.getOrdernum1();
		orders = arrOrderNum.split("-");
		tp.orderHistory().click();
		String iCost = "$ " + (String.valueOf(Total));

		String validateTotal = yop.verifyOrders(orders[0].trim(), Product, iCost);

		assertEquals(validateTotal, iCost, "validateTotal = " + validateTotal + " and iCost = " + iCost);
		click(yop.Home());

	}

	@QAFDataProvider(dataFile = "resources/DataBank.xls", sheetName = "Orders", key = "TC002")
	@Test	(enabled = false)
	@Step("The test to view order")
	public void TC001_ViewOrder(Map<String, String> data) throws Throwable {
		String Product = data.get("Product");
		String PaymentOption = data.get("PaymentOption");
		String CCNum = data.get("CCNum");
		String Expiry = data.get("Expiry");
		String CVV = data.get("CVV");
		String NameOnCard = data.get("NameOnCard");
		String Email = data.get("Email");
		String AddressCountry = data.get("AddressCountry");
		
		int  Total = 0, iCost = 0, iCost1=0, iTotalCost=0, iValidateTotal1=0, iValidateTotal2=0;
		String Cost = "", Cost1="", arrOrderNum, orders[], strTotalCost1="", strTotalCost2="", str_totalToVerify = "", str_validateTotal="";
		String str_validateTotal1="", str_validateTotal2="";
		
		HomePage hp = new HomePage(driver);
		ViewPage vp = new ViewPage(driver);
		CartPage cp = new CartPage(driver);
		PaymentPage pp = new PaymentPage(driver);
		ThankYouPage tp = new ThankYouPage(driver);
		YourOrdersPage yop = new YourOrdersPage(driver);
		
		click(hp.view(Product));
		
		Cost = vp.itemCost().getText();
		click(vp.addToCart());
		click(hp.cart());		
		
		iCost = Integer.valueOf(Cost.replace("$ ", "").trim());		
		cp.ValidateCart(Product, iCost);		
		click(cp.checkout());		
		pp.addPayment(PaymentOption, CCNum, Expiry, CVV, NameOnCard, Email, AddressCountry);
		arrOrderNum = tp.getOrdernum1();
		orders = arrOrderNum.split("-");		
		tp.orderHistory().click();				
		strTotalCost1 = "$ " + (String.valueOf(iCost));		
		str_validateTotal1 = yop.verifyOrders(orders[0].trim(), Product, strTotalCost1);
		iValidateTotal1 = Integer.valueOf((str_validateTotal1.replace("$", "")).trim());		
		assertEquals(iValidateTotal1, iCost, "Individual Total Added = " + strTotalCost1 + " and Total captured fromCart = " + iValidateTotal1);	
		
	}
	
	
	@QAFDataProvider(dataFile = "resources/DataBank.xls", sheetName = "Orders", key = "TC003")
	@Test(groups="regression")
	@Step("The test to verify Search")
	public void TC003_Search(Map<String, String> data) throws Throwable {
	
		HomePage hp = new HomePage(driver);		
		
		click(hp.searchCheckBox("electronics"));
		hp.DownLoadImage("IPHONE 13 PRO");
		click(hp.searchCheckBox("electronics"));
		
		click(hp.searchCheckBox("women"));
		hp.DownLoadImage("ZARA COAT 3");
		click(hp.searchCheckBox("women"));
		
		click(hp.searchCheckBox("laptops"));
		assertEquals(true, objectPresent(hp.error()), "Error Appeared Successfully");
		Allure.step("Error Appeared Successfully", Status.PASSED);
		assertEquals("No Products Found", hp.error().getText().trim(), "Error - 'No Products Found' Appeared successfully");
		Allure.step("Error - 'No Products Found' Appeared successfully", Status.PASSED);
		click(hp.searchCheckBox("laptops"));			
		
	}
}
