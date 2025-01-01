import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v130.emulation.Emulation;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Selenium4Tests {

    ChromeDriver driver;

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
    
    @Test
    public void testNewWindow() {
    	System.out.println("The current window title : "+driver.getTitle());
    	driver.switchTo().newWindow(WindowType.TAB);
    	driver.get("https://www.saucedemo.com/v1/");
    	System.out.println("In new Tab: "+driver.getTitle());
    	driver.switchTo().newWindow(WindowType.WINDOW);
    	driver.get("https://www.google.com");
    	System.out.println("In new Window: "+driver.getTitle());
    }
    
    @Test
    public void testObjectLocation() {
    	WebElement loginButton = driver.findElement(By.xpath("//button"));
    	System.out.println("X: "+ loginButton.getRect().getX());
    	System.out.println("Y: "+ loginButton.getRect().getY());
    	System.out.println("Width: "+ loginButton.getRect().getWidth());
    	System.out.println("Height: "+ loginButton.getRect().getHeight());
    }
    
    @Test
    public void testDeviceModeSimulation() {
    	DevTools devTools = driver.getDevTools();
    	devTools.createSession();
    	Map deviceMode = new HashMap(){{
    		put("width", 600);
            put("height", 1000);
            put("mobile", true);
            put("deviceScaleFactor", 50);
    	}};
    	driver.executeCdpCommand("Emulation.setDeviceMetricsOverride", deviceMode);
    	driver.get("https://www.google.com");
    }
}
