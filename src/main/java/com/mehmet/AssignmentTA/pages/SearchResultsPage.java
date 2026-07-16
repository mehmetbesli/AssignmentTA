package com.mehmet.AssignmentTA.pages;

import com.mehmet.AssignmentTA.utils.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class SearchResultsPage extends BasePage {

    @FindBy(xpath = "//li[contains(@id, 'Samsung') or contains(@id, 'samsung') or .//span[text()='Samsung']]//a | //a[.//span[text()='Samsung']]")
    private WebElement samsungBrandFilter;

    @FindBy(xpath = "//li[contains(@id, 'p_n_availability')]//a | //a[.//span[contains(text(), 'Stoktakiler') or contains(text(), 'Stokta Olanlar') or contains(text(), 'Stokta Var')]]")
    private WebElement inStockFilter;

    @FindBy(xpath = "//span[@class='a-color-state a-text-bold']")
    private WebElement resultsKeywordText;

    @FindBy(xpath = "//a[contains(@class, 's-pagination-item') and text()='2']")
    private WebElement page2Button;

    @FindBy(xpath = "//span[contains(@class, 's-pagination-selected') and text()='2']")
    private WebElement page2ActiveIndicator;

    @FindBy(xpath = "//div[@data-component-type='s-search-result']")
    private List<WebElement> searchResults;

    public void selectSamsungFilter() {
        click(samsungBrandFilter);
    }

    public void selectInStockFilter() {
        try {
            click(inStockFilter);
        } catch (Exception e) {
            System.out.println("Stokta Var filtresi bulunamadı veya uygulanamadı: " + e.getMessage());
        }
    }

    public boolean isResultsTextContaining(String keyword) {
        return getText(resultsKeywordText).toLowerCase().contains(keyword.toLowerCase());
    }

    public boolean hasResults() {
        try {
            wait.until(d -> searchResults.size() > 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void navigateToPage2() {
        click(page2Button);
    }

    public boolean isPage2Active() {
        return isDisplayed(page2ActiveIndicator);
    }

    public void clickProductByIndex(int index) {
        wait.until(d -> searchResults.size() > index);
        WebElement targetProduct = searchResults.get(index);
        
        WebElement productLink = targetProduct.findElement(By.xpath(".//h2/a | .//a[contains(@class, 'a-link-normal')]"));
        
        String originalWindow = Driver.getDriver().getWindowHandle();
        click(productLink);
        
        try {
            for (String handle : Driver.getDriver().getWindowHandles()) {
                if (!handle.equals(originalWindow)) {
                    Driver.getDriver().switchTo().window(handle);
                    break;
                }
            }
        } catch (Exception ignored) {}
    }
}
