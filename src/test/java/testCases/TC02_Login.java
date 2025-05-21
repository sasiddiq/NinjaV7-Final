package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.DataProviders;
import utilities.RetryAnalyzer;

public class TC02_Login extends BaseClass {

    private static final Logger logger = LogManager.getLogger(TC02_Login.class);

    @Test(
        groups = { "sanity", "regression", "datadriven" },
        dataProvider = "LoginData",
        dataProviderClass = DataProviders.class,
        retryAnalyzer = utilities.RetryAnalyzer.class
    )
    void testLogin(String email, String pwd) {
        logger.info("===== Starting testLogin with email: {} =====", email);

        try {
            HomePage hp = new HomePage(getDriver());
            logger.debug("Initialized HomePage");

            hp.clickMyAccount();
            logger.debug("Clicked My Account");

            hp.goToLogin();
            logger.debug("Navigated to Login Page");

            LoginPage lp = new LoginPage(getDriver());
            lp.setEmail(email);
            logger.debug("Entered email: {}", email);

            lp.setPwd(pwd);
            logger.debug("Entered password");

            lp.clickLogin();
            logger.debug("Clicked Login");

            AccountPage ap = new AccountPage(getDriver());

            boolean status = ap.getMyAccountConfirmation().isDisplayed();
            logger.debug("Login success status: {}", status);

            if (status) {
                logger.info("Login successful for: {}", email);
                ap.clickMyAccountDropDown();
                ap.clickLogout();
                logger.debug("Logged out successfully");
                Assert.assertTrue(true);
            } else {
                logger.warn("Login failed for: {}", email);
                Assert.fail("Login failed - Account confirmation not displayed.");
            }

        } catch (AssertionError ae) {
            logger.error("Assertion failed during login test with email: {}", email, ae);
            throw ae;
        } catch (Exception e) {
            logger.error("Exception occurred during login test with email: {}", email, e);
            Assert.fail("Unexpected exception during login test execution.");
        }

        logger.info("===== Finished testLogin with email: {} =====", email);
    }
}
