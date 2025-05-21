package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.CategoryPage;
import pageObjects.CheckoutPage;
import pageObjects.ConfirmationPage;
import pageObjects.LoginPage;
import pageObjects.ProductPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;

public class TC04_CompletePurchase extends BaseClass {

    private static final Logger logger = LogManager.getLogger(TC04_CompletePurchase.class);

    @Test(groups = {"sanity", "regression"}, retryAnalyzer = utilities.RetryAnalyzer.class)
    public void testCompletePurchase() {
        logger.info("===== Starting test: testCompletePurchase =====");

        try {
            CategoryPage cp = new CategoryPage(getDriver());
            logger.debug("Initialized CategoryPage");

            cp.clickLaptopsAndNotebooks();
            logger.debug("Clicked 'Laptops & Notebooks' category");

            cp.clickShowAll();
            logger.debug("Clicked 'Show All'");

            Thread.sleep(500);  // Consider replacing with WebDriverWait for better stability
            cp.selectHPProduct();
            logger.debug("Selected HP Laptop Product");

            ProductPage pp = new ProductPage(getDriver());
            logger.debug("Initialized ProductPage");

            pp.setDeliveryDate();
            logger.debug("Set delivery date");

            pp.clickAddToCart();
            logger.debug("Clicked 'Add to Cart'");

            pp.clickCheckout();
            logger.debug("Clicked 'Checkout'");

            CheckoutPage cop = new CheckoutPage(getDriver());
            logger.debug("Initialized CheckoutPage");

            cop.clickLogin();
            logger.debug("Clicked 'Login' on CheckoutPage");

            LoginPage lp = new LoginPage(getDriver());
            logger.debug("Initialized LoginPage");

            lp.setEmail("sid@cloudberry.services");
            lp.setPwd("Test123");
            logger.debug("Entered login credentials");

            lp.clickLogin();
            logger.debug("Submitted login form");

            cop.completeCheckout();
            logger.debug("Completed checkout steps");

            ConfirmationPage confirmationPage = new ConfirmationPage(getDriver());
            boolean orderPlaced = confirmationPage.isOrderPlaced();
            logger.debug("Order placed status: {}", orderPlaced);

            Assert.assertTrue(orderPlaced, "Order placement failed!");
            logger.info("Test passed: Order placed successfully");

        } catch (AssertionError ae) {
            logger.error("Assertion failed - Order was not placed", ae);
            throw ae; // Needed to trigger RetryAnalyzer
        } catch (Exception e) {
            logger.error("Unexpected exception occurred during purchase flow", e);
            Assert.fail("Test failed due to unexpected exception.");
        }

        logger.info("===== Finished test: testCompletePurchase =====");
    }
}
