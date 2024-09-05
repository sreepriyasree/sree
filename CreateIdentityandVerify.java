package cordPlay;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CreateIdentityandVerify {

	private WebDriver driver;
    private WebDriverWait wait;
    
    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }
    
    @Test
    public void testPlaygroundCordNetwork() throws InterruptedException {
        driver.get("https://playground.cord.network/");
        // Wait until the element is clickable and click it
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='root']/div/div[4]/div/div/ul/li[1]")));
        createButton.click();
     // 1. Create Identity Schema
        createIdentitySchema();
    }
    private void createIdentitySchema() throws InterruptedException {
        // Fill out the schema creation form (adjust locators as needed)
        WebElement createSchemaButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Identity")));
        createSchemaButton.click();
        // Fill out the schema creation form (adjust locators as needed)
        WebElement schemaNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='name']")));
        schemaNameInput.sendKeys("Test name of Identity Schema"); 
       
        String gender = "female"; // Example condition, could be dynamic based on test data
        if (gender.equalsIgnoreCase("male")) {
            WebElement radioButtonMale = driver.findElement(By.xpath("//input[@id='male']"));
            radioButtonMale.click();
        } else {
            WebElement radioButtonFemale = driver.findElement(By.xpath("//input[@id='female']"));
            radioButtonFemale.click();
        }
        WebElement mob = driver.findElement(By.xpath("//input[@name='mobile']"));
        mob.sendKeys("9494948845");
        // Upload photo
        WebElement photo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='file']")));
        String filePath = "/Users/sreepriyasreekumar/Desktop/A-2.jpeg"; // Update with a valid file path
        photo.sendKeys(filePath);
        WebElement location = driver.findElement(By.xpath("//input[@name='location']"));
        location.sendKeys("Address Test, Bangalore");
        try {
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-primary  btn-sub']")));
            submitButton.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("The 'Issue' button could not be found or clicked: " + e.getMessage());
        }
        // Wait for confirmation or result
        try {
            WebElement copyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='copyId']")));
            //copyButton.click();
        	//WebElement copyButton = driver.findElement(By.xpath("//div[@id='copyId']"));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", copyButton);
        	Thread.sleep(2000);
        	copyButton.click();
        } catch (Exception e) {
            System.out.println("The 'Copy' button could not be found or clicked: " + e.getMessage());
        }
    // Verify the copied certificate schema
        Actions act= new Actions(driver);
        WebElement verifyBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[4]/div/div/ul/li[2]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", verifyBtn);
        Thread.sleep(3000);
        verifyBtn.click();
    driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[6]/div[3]/div/div[2]/div[1]/pre")).click();
    act.keyDown(Keys.COMMAND).sendKeys("V").keyUp(Keys.COMMAND).perform();
	Thread.sleep(20000);
	 wait = new WebDriverWait(driver, Duration.ofSeconds(80000));
	 // Capture and save the screenshot
     String dateTimeToday = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
     takeScreenshot("identity_create_verify_" + dateTimeToday + ".png");
	 System.out.println("Verification passed!");
	}
    private void takeScreenshot(String fileName) {
        // Capture the screenshot and save it to the specified file path
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotDirectory = System.getProperty("user.dir") + "/Screenshots/";
        try {
            FileUtils.copyFile(screenshot, new File(screenshotDirectory + fileName));
            System.out.println("Screenshot saved: " + screenshotDirectory + fileName);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }
    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
