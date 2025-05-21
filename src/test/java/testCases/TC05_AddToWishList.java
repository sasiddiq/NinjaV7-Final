package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountPage;
import pageObjects.CategoryPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.ProductPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;

public class TC05_AddToWishList extends BaseClass {

    private static final Logger logger = LogManager.getLogger(TC05_AddToWishList.class);

    @Test(groups = { "regression" }, retryAnalyzer = utilities.RetryAnalyzer.class)
    void testAddToWishList() {
        logger.info("===== Starting test: testAddToWishList =====");

        try {
            HomePage hp = new HomePage(getDriver());
            logger.debug("Initialized HomePage");

            hp.clickMyAccount();
            logger.debug("Clicked on 'My Account'");

            hp.goToLogin();
            logger.debug("Navigated to Login page");

            LoginPage lp = new LoginPage(getDriver());
            lp.setEmail("sid@cloudberry.services");
            lp.setPwd("Test123");
            logger.debug("Entered login credentials");

            lp.clickLogin();
            logger.debug("Submitted login form");

            CategoryPage cp = new CategoryPage(getDriver());
            cp.clickLaptopsAndNotebooks();
            logger.debug("Navigated to 'Laptops & Notebooks'");

            cp.clickShowAll();
            logger.debug("Clicked 'Show All'");

            Thread.sleep(500); // Should be replaced by WebDriverWait for reliability
            cp.selectHPProduct();
            logger.debug("Selected HP product");

            ProductPage pp = new ProductPage(getDriver());
            pp.addToWishlist();
            logger.debug("Clicked 'Add to Wishlist'");

            boolean success = pp.isSuccessMessageDisplayed();
            logger.debug("Success message displayed: {}", success);

            Assert.assertTrue(success, "Wishlist message not shown.");
            logger.info("Test passed: Product added to wishlist");

        } catch (AssertionError ae) {
            logger.error("Assertion failed - wishlist confirmation not displayed", ae);
            throw ae;
        } catch (Exception e) {
            logger.error("Unexpected error occurred during testAddToWishList", e);
            Assert.fail("Test failed due to unexpected exception.");
        }

        logger.info("===== Finished test: testAddToWishList =====");
    }
}
