// Yossif Mohamed Abbas
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Sort {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://www.saucedemo.com/v1/index.html";

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By sortDropdown = By.className("product_sort_container");
    private final By priceElements = By.className("inventory_item_price");
    private final By nameElements = By.className("inventory_item_name");

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    private void selectSortOption(String value) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        dropdown.findElement(By.cssSelector("option[value='" + value + "']")).click();
    }

    private List<Double> getPrices() {
        List<WebElement> prices = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(priceElements));
        return prices.stream()
                .map(e -> e.getText().replace("$", "").trim())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    private List<String> getNames() {
        List<WebElement> names = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(nameElements));
        return names.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Test(priority = 1, description = "Sort by Price: Low to High for Standard User")
    public void sortByPriceLowToHighStandardUser() {
        login("standard_user", "secret_sauce");
        selectSortOption("lohi");
        List<Double> prices = getPrices();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        Assert.assertEquals(prices, sorted, "Prices are not sorted Low to High correctly.");
        System.out.println("✅ Prices sorted Low to High for Standard User.");
    }


    @Test(priority = 3, description = "Sort by Name: A to Z for Problem User")
    public void sortByNameAToZProblemUser() {
        login("problem_user", "secret_sauce");
        selectSortOption("az");
        List<String> names = getNames();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);
        Assert.assertEquals(names, sorted, "Names are not sorted A to Z correctly.");
        System.out.println("✅ Names sorted A to Z for Problem User.");
    }

    @Test(priority = 4, description = "Sort by Name: Z to A for Performance Glitch User")
    public void sortByNameZToAPerformanceGlitchUser() {
        login("performance_glitch_user", "secret_sauce");
        selectSortOption("za");
        List<String> names = getNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals(names, sorted, "Names are not sorted Z to A correctly.");
        System.out.println("✅ Names sorted Z to A for Performance Glitch User.");
    }
}
