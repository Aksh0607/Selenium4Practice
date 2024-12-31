import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Selenium4Tests {

    WebDriver driver;

    @BeforeMethod
	
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/login");
    }

    @AfterMethod
	
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testRelativeLocators() {
        // Add explicit wait for elements to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Find the "Username" field using its ID
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));

        // Use 'below' to find the "Password" field
        WebElement passwordField = driver.findElement(RelativeLocator.with(By.tagName("input")).below(usernameField));
        passwordField.sendKeys("SuperSecretPassword!");

        // Use 'below' to find the "Login" button
        WebElement loginButton = driver.findElement(RelativeLocator.with(By.tagName("button")).below(passwordField));
        System.out.println("Login Button Text: " + loginButton.getText());

        // Use 'above' to find the "Username" label
        WebElement usernameLabel = driver.findElement(RelativeLocator.with(By.tagName("label")).above(usernameField));
        System.out.println("Username Label: " + usernameLabel.getText());

        // Use 'near' to locate an element close to the "Login" button
        WebElement nearLoginButton = driver.findElement(RelativeLocator.with(By.tagName("button")).near(passwordField, 100));
        System.out.println("Element near Login Button: " + nearLoginButton.getText());
    }
    
    @Test
    public void testElementScreenshot() throws WebDriverException, IOException {
    	WebElement loginButton = driver.findElement(By.xpath("//button"));
    	FileUtils.copyFile(loginButton.getScreenshotAs(OutputType.FILE), new File("Login Button Screenshot.png"));
    }
}
