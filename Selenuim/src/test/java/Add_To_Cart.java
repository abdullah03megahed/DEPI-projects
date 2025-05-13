import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class Add_To_Cart {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://www.saucedemo.com/v1/index.html";

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By inventoryList = By.className("inventory_list");
    private final By addToCartButtonBackpack = By.id("add-to-cart-sauce-labs-backpack");
    private final By addToCartButtonBikeLight = By.id("add-to-cart-sauce-labs-bike-light");
    private final By removeButtonBackpack = By.id("remove-sauce-labs-backpack");
    private final By removeButtonBikeLight = By.id("remove-sauce-labs-bike-light");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By cartItems = By.className("cart_item");
    private final By inventoryItemName = By.className("inventory_item_name");

    @BeforeMethod
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL);
        performLogin("standard_user", "secret_sauce");
    }

    private void performLogin(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryList)).isDisplayed(),
                "Login failed: Inventory list not displayed.");
    }

    @Test(priority = 1, description = "Test adding one item to cart and verify cart count.")
    public void addOneItemToCart() {
        // Add Sauce Labs Backpack to cart
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBackpack)).click();

        // Verify cart badge shows 1
        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
        Assert.assertEquals(badge.getText(), "1", "Cart badge should show 1 item.");

        // Navigate to cart and verify item
        driver.findElement(cartLink).click();
        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItems));
        Assert.assertTrue(cartItem.isDisplayed(), "Cart item not displayed.");
        Assert.assertTrue(cartItem.findElement(inventoryItemName).getText().contains("Sauce Labs Backpack"),
                "Sauce Labs Backpack not found in cart.");
        System.out.println("Successfully added one item to cart and verified.");
    }

    @Test(priority = 2, description = "Test adding multiple items to cart and verify cart count.")
    public void addMultipleItemsToCart() {
        // Add Sauce Labs Backpack and Bike Light to cart
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBackpack)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBikeLight)).click();

        // Verify cart badge shows 2
        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
        Assert.assertEquals(badge.getText(), "2", "Cart badge should show 2 items.");

        // Navigate to cart and verify items
        driver.findElement(cartLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartItems));
        int itemCount = driver.findElements(cartItems).size();
        Assert.assertEquals(itemCount, 2, "Cart should contain 2 items.");
        System.out.println("Successfully added multiple items to cart and verified.");
    }

    @Test(priority = 3, description = "Test removing one item from cart and verify cart count.")
    public void removeOneItemFromCart() {
        // Add two items to cart
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBackpack)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBikeLight)).click();

        // Remove Sauce Labs Backpack
        wait.until(ExpectedConditions.elementToBeClickable(removeButtonBackpack)).click();

        // Verify cart badge shows 1
        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
        Assert.assertEquals(badge.getText(), "1", "Cart badge should show 1 item after removal.");

        // Navigate to cart and verify remaining item
        driver.findElement(cartLink).click();
        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItems));
        Assert.assertTrue(cartItem.findElement(inventoryItemName).getText().contains("Sauce Labs Bike Light"),
                "Sauce Labs Bike Light not found in cart after removal.");
        System.out.println("Successfully removed one item from cart and verified.");
    }

    @Test(priority = 4, description = "Test removing all items from cart and verify cart is empty.")
    public void removeAllItemsFromCart() {
        // Add two items to cart
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBackpack)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBikeLight)).click();

        // Remove both items
        wait.until(ExpectedConditions.elementToBeClickable(removeButtonBackpack)).click();
        wait.until(ExpectedConditions.elementToBeClickable(removeButtonBikeLight)).click();

        // Verify cart badge is not present
        boolean badgePresent = driver.findElements(cartBadge).size() > 0;
        Assert.assertFalse(badgePresent, "Cart badge should not be visible after removing all items.");

        // Navigate to cart and verify it's empty
        driver.findElement(cartLink).click();
        boolean cartEmpty = driver.findElements(cartItems).size() == 0;
        Assert.assertTrue(cartEmpty, "Cart should be empty after removing all items.");
        System.out.println("Successfully removed all items from cart and verified.");
    }

    @Test(priority = 5, description = "Test adding item with problem user (should work despite issues).")
    public void addItemWithProblemUser() {
        // Log out and log in as problem_user
        driver.get(BASE_URL);
        performLogin("problem_user", "secret_sauce");

        // Add Sauce Labs Backpack to cart
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBackpack)).click();

        // Verify cart badge shows 1 (problem user may have UI issues, but cart should work)
        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
        Assert.assertEquals(badge.getText(), "1", "Cart badge should show 1 item for problem user.");
        System.out.println("Successfully added item to cart with problem user.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}