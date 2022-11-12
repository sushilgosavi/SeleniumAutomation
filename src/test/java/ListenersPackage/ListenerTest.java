package ListenersPackage;

import java.util.Date;
import java.util.List;

import org.apache.maven.model.building.Result;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.events.WebDriverListener;
//import org.openqa.selenium.support.events.WebDriverListener;
//ListenersPackage.ListenerTest
import pageObjects.BasePage;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;
//import org.joda.time.DateTime;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.google.gson.internal.LinkedTreeMap;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

@Parameters({"takeScreenprintatAlways","takeScreenprintatFail"})
public class ListenerTest extends BasePage implements ITestListener, ISuiteListener {

	
	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		takeScreenprint(result.getMethod().getMethodName());
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		// driver.quit();

	}

	@Override
	public void onStart(ITestContext context) {	
	}

	@Override
	public void onFinish(ITestContext context) {
		
//		driver.quit();
	}
	
	public String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		for(int i=0; i<result.getThrowable().getStackTrace().length; i++) {
			System.out.println(result.getThrowable().getStackTrace()[i]);
		}
		takeScreenprint(result.getMethod().getMethodName());	
	}
	
	@Attachment(value = "Screenshot", type = "image/png")
	public static void takeScreenprint(String Descrip ) throws WebDriverException  {
		//context.getCurrentXmlTest().getParameter("deviceUDID");
		//if(takeScreenprintatAlways.equalsIgnoreCase("true")) {
			Allure.addAttachment( Descrip + "_TS:"+getTime() , new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
		//}
		//if(takeScreenprintatFail.equalsIgnoreCase("true")) {
			//Allure.addAttachment( Descrip , new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
		}
	
	
	public static String getTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		LocalDateTime now = LocalDateTime.now();
		//System.out.println(dtf.format(now));
		return (dtf.format(now));
	}
	
	@Override
	public void onStart(ISuite suite) {

	}

	@Override
	public void onFinish(ISuite suite) {
	}
}
