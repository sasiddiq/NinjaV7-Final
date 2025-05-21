package pageObjects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id='input-option-225']")
    WebElement input_DeliveryDate;

    @FindBy(xpath = "//button[@id='button-cart']")
    WebElement btn_AddToCart;

    @FindBy(xpath = "//div[@class='alert alert-success alert-dismissible']")
    WebElement alert_Success;
    
    @FindBy(xpath = "//a[@title='Checkout']//i[@class='fa-solid fa-share']")
    WebElement link_Checkout;
    
    
    
    @FindBy(xpath = "//div//button//i[@class='fa-solid fa-heart']")
    WebElement wishlistIcon;

    @FindBy(xpath = "//div[@class='alert alert-success alert-dismissible']")
    WebElement successMessage;

    public void addToWishlist() {
       // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", wishlistIcon);
        wishlistIcon.click();
    }

//    public boolean isSuccessMessageDisplayed() {
//        return successMessage.getText().contains("Success");
//    }

    public void setDeliveryDate() throws InterruptedException {
        LocalDate date = LocalDate.now().plusDays(5);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Thread.sleep(500);
        scrollToView(input_DeliveryDate);
        Thread.sleep(500);
        input_DeliveryDate.clear();
        input_DeliveryDate.sendKeys(formattedDate);
    }
    
    

    public void clickAddToCart() throws InterruptedException {
    	// Scroll into view
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn_AddToCart);

		// Optional: Add a small wait after scrolling to let it stabilize
		Thread.sleep(500);

        btn_AddToCart.click();
    }

    public boolean isSuccessMessageDisplayed() {
        return alert_Success.getText().contains("Success");
    }

    private void scrollToView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    public void clickCheckout() throws InterruptedException 
	{
				
    	        Thread.sleep(500);
				// Scroll into view
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link_Checkout);

				// Optional: Add a small wait after scrolling to let it stabilize
				Thread.sleep(500);

				// Now clear
				link_Checkout.click();
	}
    
}
