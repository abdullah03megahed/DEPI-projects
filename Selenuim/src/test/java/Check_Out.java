// Abdullah Mohamed Megahed (Checkout Process Testing)
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
import java.util.List;

public class Check_Out {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://www.saucedemo.com/v1/";
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By inventoryList = By.className("inventory_list"); // To confirm login
    private final By shoppingCartLink = By.id("shopping_cart_container");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private String addToCartButtonXPathByName(String itemName) {
        return String.format("//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='inventory_item_description']//button[contains(@class, 'btn_inventory')]", itemName);
    }
    private final By genericAddToCartButton = By.xpath("(//button[contains(@class, 'btn_inventory') and text()='ADD TO CART'])[1]"); // Adds the first available item
    private final By checkoutButton = By.id("checkout");
    private final By cartItem = By.className("cart_item");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelCheckoutStepOneButton = By.id("cancel"); // This ID is reused by SauceDemo
    private final By checkoutStepOneErrorMessage = By.xpath("//h3[@data-test='error']");
    private final By finishButton = By.id("finish");
    private final By inventoryItemNameInOverview = By.className("inventory_item_name");
    private final By summarySubtotalLabel = By.className("summary_subtotal_label");
    private final By summaryTaxLabel = By.className("summary_tax_label");
    private final By summaryTotalLabel = By.className("summary_total_label");
    private final By completeHeader = By.className("complete-header"); // "THANK YOU FOR YOUR ORDER"
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");


    @BeforeMethod
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL); 
        performLogin("standard_user", "secret_sauce");
        Assert.assertTrue(isElementDisplayed(inventoryList), "Login failed, inventory not visible.");
    }
    private void performLogin(String username, String password) {
        findElementWithWait(usernameField).sendKeys(username);
        findElementWithWait(passwordField).sendKeys(password);
        clickElementWithWait(loginButton);
    }

    private WebElement findElementWithWait(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private List<WebElement> findElementsWithWait(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    private void clickElementWithWait(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private boolean isElementDisplayed(By locator) {
        try {
            return findElementWithWait(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void addProductToCart(String productName) {
        By productAddToCartButton = By.xpath(addToCartButtonXPathByName(productName));
        try {
            clickElementWithWait(productAddToCartButton);
            System.out.println("Added '" + productName + "' to cart.");
        } catch (TimeoutException e) {
            System.err.println("Could not find add to cart button for: " + productName + ". Trying generic add.");
            clickElementWithWait(genericAddToCartButton); // Fallback to adding the first item
            System.out.println("Added a generic item to cart as fallback.");
        }
        Assert.assertTrue(isElementDisplayed(shoppingCartBadge), "Shopping cart badge not visible after adding item.");
    }

    private void proceedToCheckoutInformationStep() {
        clickElementWithWait(shoppingCartLink);
        Assert.assertTrue(isElementDisplayed(cartItem), "No items visible in the cart page.");
        clickElementWithWait(checkoutButton);
        Assert.assertTrue(isElementDisplayed(firstNameField), "Not on Checkout Step One page (First Name field not found).");
    }

    private void fillShippingInformation(String firstName, String lastName, String postalCode) {
        if (firstName != null && !firstName.isEmpty()) findElementWithWait(firstNameField).sendKeys(firstName);
        if (lastName != null && !lastName.isEmpty()) findElementWithWait(lastNameField).sendKeys(lastName);
        if (postalCode != null && !postalCode.isEmpty()) findElementWithWait(postalCodeField).sendKeys(postalCode);
    }

    @Test(priority = 1, description = "Test successful checkout process for a single item.")
    public void successfulCheckout_SingleItem() {
        String itemToCheckout = "Sauce Labs Backpack";
        addProductToCart(itemToCheckout);
        proceedToCheckoutInformationStep();

        fillShippingInformation("Kareem", "Shawki", "12345");
        clickElementWithWait(continueButton);
        Assert.assertTrue(isElementDisplayed(finishButton), "Not on Checkout Step Two page (Finish button not found).");
        WebElement itemNameInOverview = findElementWithWait(By.xpath(String.format("//div[@class='inventory_item_name' and text()='%s']", itemToCheckout)));
        Assert.assertTrue(itemNameInOverview.isDisplayed(), itemToCheckout + " not visible in order overview.");
        System.out.println("Subtotal: " + findElementWithWait(summarySubtotalLabel).getText());
        System.out.println("Tax: " + findElementWithWait(summaryTaxLabel).getText());
        System.out.println("Total: " + findElementWithWait(summaryTotalLabel).getText());

        clickElementWithWait(finishButton);
        Assert.assertTrue(isElementDisplayed(completeHeader), "Not on Checkout Complete page (Complete header not found).");
        Assert.assertEquals(findElementWithWait(completeHeader).getText(), "THANK YOU FOR YOUR ORDER", "Checkout completion message is incorrect.");
        System.out.println("Order completed successfully: " + findElementWithWait(completeText).getText());

        clickElementWithWait(backHomeButton);
        Assert.assertTrue(isElementDisplayed(inventoryList), "Not returned to inventory page after completing order.");
        List<WebElement> itemsInCartAfterCheckout = driver.findElements(cartItem); // Use findElements to check for absence
        Assert.assertTrue(itemsInCartAfterCheckout.isEmpty(), "Cart was not empty after successful checkout.");
        System.out.println("Checkout and return to empty cart confirmed.");
    }

    @DataProvider(name = "shippingInfoErrors")
    public Object[][] shippingInfoErrorsData() {
        return new Object[][]{
                {"", "User", "12345", "Error: First Name is required"},
                {"Test", "", "12345", "Error: Last Name is required"},
                {"Test", "User", "", "Error: Postal Code is required"}
        };
    }

    @Test(priority = 2, dataProvider = "shippingInfoErrors", description = "Test errors for missing shipping information.")
    public void checkout_MissingShippingInformation(String firstName, String lastName, String postalCode, String expectedError) {
        addProductToCart("Sauce Labs Bike Light"); 
        proceedToCheckoutInformationStep();

        fillShippingInformation(firstName, lastName, postalCode);
        clickElementWithWait(continueButton); 
        WebElement errorMessageElement = findElementWithWait(checkoutStepOneErrorMessage);
        Assert.assertTrue(errorMessageElement.isDisplayed(), "Error message not displayed for missing shipping info.");
        Assert.assertTrue(errorMessageElement.getText().contains(expectedError),
                "Error message text mismatch. Expected to contain: '" + expectedError + "', Actual: '" + errorMessageElement.getText() + "'");
        System.out.println("Handled missing shipping info correctly: " + errorMessageElement.getText());
        Assert.assertTrue(isElementDisplayed(firstNameField), "Not on Checkout Step One page after error.");
    }

    @Test(priority = 3, description = "Test canceling checkout from Step One (Information page).")
    public void cancelCheckout_FromStepOne() {
        addProductToCart("Sauce Labs Bolt T-Shirt");
        proceedToCheckoutInformationStep();
        Assert.assertTrue(isElementDisplayed(firstNameField), "Not on correct page to cancel from Step One.");
        clickElementWithWait(cancelCheckoutStepOneButton);
        Assert.assertTrue(isElementDisplayed(checkoutButton), "Not returned to Cart page after canceling from Step One.");
        System.out.println("Canceled checkout from Step One and returned to Cart page.");
    }

     @Test(priority = 4, description = "Test canceling checkout from Step Two (Overview page).")
    public void cancelCheckout_FromStepTwo() {
        addProductToCart("Sauce Labs Fleece Jacket");
        proceedToCheckoutInformationStep();

        fillShippingInformation("Cancel", "Test", "54321");
        clickElementWithWait(continueButton);
        Assert.assertTrue(isElementDisplayed(finishButton), "Not on Checkout Step Two page (Finish button not found).");
        By cancelCheckoutStepTwoButton = By.xpath("//button[@id='cancel' and contains(@class,'cart_cancel_link')]"); // More specific for step two
        if (!isElementDisplayed(cancelCheckoutStepTwoButton)) { // Fallback if the specific one isn't found
            cancelCheckoutStepTwoButton = By.id("cancel");
        }

        clickElementWithWait(cancelCheckoutStepTwoButton);
        Assert.assertTrue(isElementDisplayed(inventoryList), "Not returned to Products/Inventory page after canceling from Step Two.");
        System.out.println("Canceled checkout from Step Two and returned to Products/Inventory page.");
    }


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
