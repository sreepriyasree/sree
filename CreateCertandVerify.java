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

public class CreateCertandVerify {
    
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
        
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='root']/div/div[4]/div/div/ul/li[1]")));
        createButton.click();
        
        createCertificateSchema();
    }

    private void createCertificateSchema() throws InterruptedException {
        WebElement createSchemaButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Certificate Schema")));
        createSchemaButton.click();

        WebElement schemaNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='name']")));
        schemaNameInput.sendKeys("Test name of Certificate Schema");
        WebElement schemaEmail = driver.findElement(By.xpath("//input[@name='email']"));
        schemaEmail.sendKeys("sreepriya@dhiway.com");
        WebElement certId = driver.findElement(By.xpath("//input[@name='cert_id']"));
        certId.sendKeys("certid123");

        try {
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
            submitButton.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("The 'Submit' button could not be found or clicked: " + e.getMessage());
        }

        try {
            WebElement copyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='copyId']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", copyButton);
            Thread.sleep(2000);
            copyButton.click();
        } catch (Exception e) {
            System.out.println("The 'Copy' button could not be found or clicked: " + e.getMessage());
        }

        Actions act = new Actions(driver);
        WebElement verifyBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[4]/div/div/ul/li[2]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", verifyBtn);
        Thread.sleep(3000);
        verifyBtn.click();
        
        WebElement pasteArea = driver.findElement(By.xpath("//*[@id='root']/div/div[6]/div[3]/div/div[2]/div[1]/pre"));
        pasteArea.click();
        act.keyDown(Keys.COMMAND).sendKeys("V").keyUp(Keys.COMMAND).perform();
        
        // Consider using a more reasonable wait time
        wait = new WebDriverWait(driver, Duration.ofSeconds(3000));
        Thread.sleep(2000);
        String dateTimeToday = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        takeScreenshot("certificate_create_verify_" + dateTimeToday + ".png");
        System.out.println("Verification passed!");
    }

    private void takeScreenshot(String fileName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotDirectory = System.getProperty("user.dir") + "/Screenshots/";
        new File(screenshotDirectory).mkdirs(); // Ensure directory exists
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
