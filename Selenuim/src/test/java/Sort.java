// Yossif Mohamed Abbas
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
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

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By sortDropdown = By.className("product_sort_container");
    private final By priceElements = By.className("inventory_item_price");
    private final By nameElements = By.className("inventory_item_name");

    @BeforeMethod
    public void setup() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String BASE_URL = "https://www.saucedemo.com/v1/index.html";
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

    @DataProvider(name = "usersAndSortOptions")
    public Object[][] usersAndSortOptions() {
        return new Object[][]{
                {"standard_user", "secret_sauce", "az"},
                {"standard_user", "secret_sauce", "za"},
                {"standard_user", "secret_sauce", "lohi"},
                {"standard_user", "secret_sauce", "hilo"},

                {"problem_user", "secret_sauce", "az"},
                {"problem_user", "secret_sauce", "za"},
                {"problem_user", "secret_sauce", "lohi"},
                {"problem_user", "secret_sauce", "hilo"},

                {"performance_glitch_user", "secret_sauce", "az"},
                {"performance_glitch_user", "secret_sauce", "za"},
                {"performance_glitch_user", "secret_sauce", "lohi"},
                {"performance_glitch_user", "secret_sauce", "hilo"}
        };
    }

    @Test(dataProvider = "usersAndSortOptions")
    public void testSorting(String username, String password, String sortType) {
        login(username, password);
        selectSortOption(sortType);

        if (sortType.equals("lohi") || sortType.equals("hilo")) {
            List<Double> prices = getPrices();
            List<Double> expected = new ArrayList<>(prices);
            if (sortType.equals("lohi")) {
                Collections.sort(expected);
            } else {
                expected.sort(Collections.reverseOrder());
            }
            Assert.assertEquals(prices, expected, "Prices not sorted correctly for: " + username + " - " + sortType);
            System.out.println("✅ Prices sorted correctly for " + username + " - " + sortType);
        } else {
            List<String> names = getNames();
            List<String> expected = new ArrayList<>(names);
            if (sortType.equals("az")) {
                Collections.sort(expected);
            } else {
                expected.sort(Collections.reverseOrder());
            }
            Assert.assertEquals(names, expected, "Names not sorted correctly for: " + username + " - " + sortType);
            System.out.println("✅ Names sorted correctly for " + username + " - " + sortType);
        }
    }
}