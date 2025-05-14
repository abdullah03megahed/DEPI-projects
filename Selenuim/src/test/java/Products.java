import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class Products {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By inventoryList = By.id("inventory_container");
    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By productImages = By.className("inventory_item_img");
    private final By productDescs = By.className("inventory_item_desc");
    private final By backToProducts = By.id("back-to-products"); // Fixed locator
    private final By productDetailName = By.className("inventory_details_name");
    private final By productDetailPrice = By.className("inventory_details_price");
    private final By productDetailDesc = By.className("inventory_details_desc");
    private final By productDetailImage = By.className("inventory_details_img");

    @BeforeMethod
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("https://www.saucedemo.com/v1/index.html");

        // Perform login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Ensure the inventory page fully loads before interacting
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryList));
    }

    @Test(priority = 1, description = "Verify all products are displayed correctly.")
    public void verifyProductListingPage() {
        List<WebElement> names = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
        List<WebElement> prices = driver.findElements(productPrices);
        List<WebElement> images = driver.findElements(productImages);
        List<WebElement> descriptions = driver.findElements(productDescs);

        for (int i = 0; i < names.size(); i++) {
            Assert.assertTrue(names.get(i).isDisplayed(), "Product name not displayed");
            Assert.assertTrue(prices.get(i).isDisplayed(), "Product price not displayed");
            Assert.assertTrue(images.get(i).isDisplayed(), "Product image not displayed");
            Assert.assertTrue(descriptions.get(i).isDisplayed(), "Product description not displayed");
        }

        System.out.println("All products are displayed correctly.");
    }

    @Test(priority = 2, description = "Verify each individual product page displays correct details.")
    public void verifyEachProductDetailPage() {
        List<String> productNamesList = new ArrayList<>();
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
        
        // First, store all product names
        for (WebElement product : products) {
            productNamesList.add(product.getText());
        }

        // Then iterate through each product
        for (String productName : productNamesList) {
            try {
                // Find and click the product with a matching name
                WebElement product = driver.findElement(By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']"));
                product.click();

                // Verify product details
                Assert.assertEquals(
                    wait.until(ExpectedConditions.visibilityOfElementLocated(productDetailName)).getText(),
                    productName,
                    "Product name mismatch"
                );
                Assert.assertTrue(
                    wait.until(ExpectedConditions.visibilityOfElementLocated(productDetailDesc)).isDisplayed(),
                    "Product description missing"
                );
                Assert.assertTrue(
                    wait.until(ExpectedConditions.visibilityOfElementLocated(productDetailImage)).isDisplayed(),
                    "Product image missing"
                );
                Assert.assertTrue(
                    wait.until(ExpectedConditions.visibilityOfElementLocated(productDetailPrice)).isDisplayed(),
                    "Product price missing"
                );

                // Try to find and click the back button with retries
                WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(backToProducts));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", backButton);

                // Wait for the product list to be visible again
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
                
            } catch (Exception e) {
                System.err.println("Error processing product: " + productName);
                e.printStackTrace();
                // Try to go back to the product page if there's an error
                driver.navigate().back();
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
            }
        }

        System.out.println("All product detail pages checked successfully.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}