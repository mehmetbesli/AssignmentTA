package com.mehmet.AssignmentTA.pages;

import com.mehmet.AssignmentTA.utils.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class ProductDetailPage extends BasePage {

    @FindBy(id = "productTitle")
    private WebElement productTitle;

    @FindBy(id = "add-to-cart-button")
    private List<WebElement> addToCartButtons;

    @FindBy(xpath = "//input[@name='submit.add-to-cart']")
    private List<WebElement> submitAddToCartButtons;

    @FindBy(xpath = "//a[contains(@id, 'buying-options') or contains(text(), 'Satın Alma Seçenekleri') or contains(text(), 'seçeneklerini gör')] | //span[@id='buybox-see-all-buying-options-announce'] | //a[@id='buybox-see-all-buying-options-announce']")
    private WebElement seeAllBuyingOptionsButton;

    @FindBy(xpath = "//input[@name='submit.addToCart'] | //span[contains(@id, 'atc-announce')] | //span[@id='a-autoid-0-announce']//input | //span[contains(@class, 'a-button-inner')]//input[contains(@value, 'Sepet')]")
    private WebElement sidePanelAddToCartButton;

    @FindBy(xpath = "//div[@id='attach-added-to-cart-message'] | //div[@id='sw-atc-confirmation'] | //div[@id='sw-atc-details-single'] | //h1[contains(text(), 'Sepete Eklendi') or contains(text(), 'Eklendi')] | //span[contains(text(), 'Sepete Eklendi') or contains(text(), 'Eklendi')] | //div[contains(@class, 'a-alert-success')]")
    private WebElement addedToCartSuccessMessage;

    @FindBy(id = "nav-cart-count")
    private WebElement cartCount;

    @FindBy(xpath = "//input[contains(@value, 'Sepete Ekle') or contains(@value, 'sepete ekle') or @id='add-to-cart-button']")
    private List<WebElement> fallbackAddToCartButtons;

    public String getProductTitle() {
        return getText(productTitle);
    }

    public void addToCart() {
        try {
            for (WebElement btn : addToCartButtons) {
                if (btn.isDisplayed()) {
                    click(btn);
                    return;
                }
            }
            for (WebElement btn : submitAddToCartButtons) {
                if (btn.isDisplayed()) {
                    click(btn);
                    return;
                }
            }

            if (isDisplayed(seeAllBuyingOptionsButton)) {
                click(seeAllBuyingOptionsButton);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
                click(sidePanelAddToCartButton);
                return;
            }

            for (WebElement btn : fallbackAddToCartButtons) {
                if (btn.isDisplayed()) {
                    click(btn);
                    return;
                }
            }
            if (!fallbackAddToCartButtons.isEmpty()) {
                click(fallbackAddToCartButtons.get(0));
            } else {
                throw new RuntimeException("No visible Add to Cart button found");
            }
        } catch (Exception e) {
            System.out.println("Standard Add to Cart click failed: " + e.getMessage() + ". Trying JS fallback...");
            try {
                if (!addToCartButtons.isEmpty() && addToCartButtons.get(0).isDisplayed()) {
                    ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", addToCartButtons.get(0));
                } else if (!submitAddToCartButtons.isEmpty() && submitAddToCartButtons.get(0).isDisplayed()) {
                    ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", submitAddToCartButtons.get(0));
                } else {
                    if (!fallbackAddToCartButtons.isEmpty()) {
                        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", fallbackAddToCartButtons.get(0));
                    } else {
                        throw new RuntimeException("No Add to Cart inputs found for JS click fallback");
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("Failed to add product to cart: " + ex.getMessage(), ex);
            }
        }
    }

    public boolean isAddedToCartPopupVisible() {
        try {
            if (isDisplayed(addedToCartSuccessMessage)) {
                return true;
            }
        } catch (Exception ignored) {}

        try {
            String countText = getText(cartCount);
            if (!countText.isEmpty() && !countText.equals("0")) {
                System.out.println("Cart addition confirmed via cart count: " + countText);
                return true;
            }
        } catch (Exception ignored) {}

        return false;
    }
}
