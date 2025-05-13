public class Add_To_Cart {

package com.saucedemo.tests;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

    public class CartTest {
        private WebDriver driver;
        private LoginPage loginPage;
        private ProductsPage productsPage;

        @BeforeMethod
        public void setUp() {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://www.saucedemo.com/v1/index.html");
            loginPage = new LoginPage(driver);
            productsPage = new ProductsPage(driver);
        }

        @Test
        public void testAddAndRemoveFromCart() {
            // Login
            loginPage.login("standard_user", "secret_sauce");

            // Add item to cart
            productsPage.addBackpackToCart();
            Assert.assertEquals(productsPage.getCartItemCount(), "1", "Item was not added to cart");

            // Remove item from cart
            productsPage.removeBackpackFromCart();
            Assert.assertEquals(productsPage.getCartItemCount(), "0", "Item was not removed from cart");
        }

        @AfterMethod
        public void tearDown() {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
