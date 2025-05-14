//kareem Mohamed Shawki
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class Login_Logout {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By inventoryList = By.className("inventory_list");
    private final By menuButton = By.cssSelector("button[style=\"position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; margin: 0px; padding: 0px; border: none; opacity: 0; font-size: 8px; cursor: pointer;\"]");
    private final By logoutLink = By.cssSelector("a[id=\"logout_sidebar_link\"]");
    private final By loginLogo = By.className("login_logo");
    private final By errorMessageContainer = By.xpath("//h3[@data-test='error']");


    @BeforeMethod
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String BASE_URL = "https://www.saucedemo.com/v1/index.html";
        driver.get(BASE_URL);
    }


    private void performLogin(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }


    private void performLogout() {
        driver.findElement(menuButton).click();
        WebElement logoutElement = wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        logoutElement.click();
    }

    // --- Test Cases ---

    @Test(priority = 1, description = "Test successful login with standard user and logout.")
    public void successfulLoginAndLogout_StandardUser() {
        performLogin("standard_user", "secret_sauce");
        // Assert that login was successful by checking for an element on the inventory page
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryList)).isDisplayed(),
                "Inventory list not displayed after standard user login.");
        System.out.println("Standard user logged in successfully.");

        performLogout();
        // Assert that logout was successful by checking for an element on the login page
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(loginLogo)).isDisplayed(),
                "Login logo not displayed after logout.");
        System.out.println("Standard user logged out successfully.");
    }

    @Test(priority = 2, description = "Test login with locked out user.")
    public void login_LockedOutUser() {
        performLogin("locked_out_user", "secret_sauce");
        // Assert that the error message is displayed
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageContainer));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed for locked out user.");
        Assert.assertTrue(errorMessage.getText().contains("Sorry, this user has been locked out."),
                "Error message text is incorrect for locked out user.");
        System.out.println("Locked out user login attempt handled correctly.");
    }

    @Test(priority = 3, description = "Test login with problem user and logout.")
    public void loginAndLogout_ProblemUser() {
        performLogin("problem_user", "secret_sauce");
        // Problem user should still be able to log in. Problems might be on the inventory page.
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryList)).isDisplayed(),
                "Inventory list not displayed after problem user login.");
        System.out.println("Problem user logged in successfully.");

        performLogout();
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(loginLogo)).isDisplayed(),
                "Login logo not displayed after logout for problem user.");
        System.out.println("Problem user logged out successfully.");
    }

    @Test(priority = 4, description = "Test login with performance glitch user and logout.")
    public void loginAndLogout_PerformanceGlitchUser() {
        WebDriverWait performanceWait = new WebDriverWait(driver, Duration.ofSeconds(20));

        performanceWait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys("performance_glitch_user");
        performanceWait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys("secret_sauce");
        performanceWait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        Assert.assertTrue(performanceWait.until(ExpectedConditions.visibilityOfElementLocated(inventoryList)).isDisplayed(),
                "Inventory list not displayed after performance glitch user login.");
        System.out.println("Performance glitch user logged in successfully.");

        performLogout();
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(loginLogo)).isDisplayed(),
                "Login logo not displayed after logout for performance glitch user.");
        System.out.println("Performance glitch user logged out successfully.");
    }


    @DataProvider(name = "invalidLoginCredentials")
    public Object[][] invalidLoginData() {
        return new Object[][]{
                {"invalid_user", "secret_sauce", "Username and password do not match"},
                {"standard_user", "wrong_password", "Username and password do not match"},
                {"", "secret_sauce", "Username is required"},
                {"standard_user", "", "Password is required"},
                {"", "", "Username is required"}
        };
    }

    @Test(priority = 5, dataProvider = "invalidLoginCredentials", description = "Test login with various invalid credentials.")
    public void login_InvalidCredentials(String username, String password, String expectedErrorMessage) {
        performLogin(username, password);
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageContainer));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed for invalid credentials.");
        Assert.assertTrue(errorMessage.getText().contains(expectedErrorMessage),
                "Error message text mismatch. Expected: '" + expectedErrorMessage + "', Actual: '" + errorMessage.getText() + "'");
        System.out.println("Login attempt with Username: '" + username + "', Password: '" + password + "' handled correctly with error: " + errorMessage.getText());
    }
    //Bug
    @Test(priority = 6, description = "Test navigating to inventory page without login (should redirect to login).")
    public void navigateToInventoryWithoutLogin() {
        // Clear cookies to simulate no login
        driver.manage().deleteAllCookies();

        // Navigate to the inventory page
        driver.get("https://www.saucedemo.com/v1/inventory.html");

        // Wait to see if the login form is visible (means redirected)
        boolean redirectedToLogin;
        try {
            wait.withTimeout(Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
            redirectedToLogin = true;
        } catch (TimeoutException e) {
            redirectedToLogin = false;
        }

        if (!redirectedToLogin) {
            try {
                driver.findElement(menuButton).click();
                driver.findElement(logoutLink).click();
            } catch (Exception ex) {
                System.out.println("Logout failed, probably already logged out.");
            }
        }

        Assert.assertFalse(redirectedToLogin, "User was not redirected to login page when accessing inventory without login.");
        System.out.println("Access to inventory page without login correctly redirected to login page.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
