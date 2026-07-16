package com.mehmet.AssignmentTA.pages;

import com.mehmet.AssignmentTA.utils.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(id = "nav-cart")
    private WebElement cartMenuButton;

    @FindBy(xpath = "//span[@id='attach-sidesheet-view-cart-button']//a | //a[@id='attach-member-view-cart-button-announce'] | //span[@id='attach-sidesheet-view-cart-button']//input | //a[contains(@href, '/cart') and contains(@class, 'a-button')]")
    private WebElement attachViewCartButton;

    @FindBy(xpath = "//input[@value='Sil' or @value='Kaldır' or @data-action='delete' or contains(@name, 'submit.delete') or contains(@aria-label, 'Sil') or contains(@aria-label, 'Kaldır')]")
    private WebElement deleteButton;

    @FindBy(id = "nav-cart-count")
    private WebElement cartCount;



    @FindBy(xpath = "//span[@class='a-truncate-cut']")
    private List<WebElement> cartProductTitles;

    @FindBy(xpath = "//input[@value='Sil' or @value='Kaldır' or @data-action='delete' or contains(@name, 'submit.delete')]")
    private List<WebElement> allDeleteButtons;

    public void navigateToCart() {
        if (isDisplayed(attachViewCartButton)) {
            click(attachViewCartButton);
        } else {
            click(cartMenuButton);
        }
    }

    private String cleanTitleForMatching(String title) {
        if (title == null) return "";
        String clean = title.toLowerCase()
                            .replaceAll("[^a-z0-9 ğüşöçıİı]", " ")
                            .replaceAll("\\s+", " ")
                            .trim();
        if (clean.length() > 15) {
            return clean.substring(0, 15).trim();
        }
        return clean;
    }

    public boolean isProductInCart(String productTitle) {
        String targetTitle = cleanTitleForMatching(productTitle);
        int attempts = 0;
        while (attempts < 3) {
            try {
                for (WebElement titleElement : cartProductTitles) {
                    String cartTitle = cleanTitleForMatching(getText(titleElement));
                    if (cartTitle.contains(targetTitle) || targetTitle.contains(cartTitle)) {
                        return true;
                    }
                }
                return false;
            } catch (StaleElementReferenceException e) {
                attempts++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        return false;
    }

    public void removeProductFromCart(String productTitle) {
        String shortTitle = cleanTitleForMatching(productTitle);
        int targetIndex = -1;
        int attempts = 0;
        
        while (attempts < 3) {
            try {
                for (int i = 0; i < cartProductTitles.size(); i++) {
                    if (cleanTitleForMatching(getText(cartProductTitles.get(i))).contains(shortTitle)) {
                        targetIndex = i;
                        break;
                    }
                }
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        
        WebElement targetDeleteButton = deleteButton;
        if (targetIndex != -1) {
            try {
                if (targetIndex < allDeleteButtons.size()) {
                    targetDeleteButton = allDeleteButtons.get(targetIndex);
                }
            } catch (Exception ignored) {}
        }
        
        click(targetDeleteButton);
        
        try {
            wait.until(ExpectedConditions.stalenessOf(targetDeleteButton));
        } catch (Exception e) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
        }
    }


}
