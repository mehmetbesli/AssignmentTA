package com.mehmet.AssignmentTA.pages;

import com.mehmet.AssignmentTA.constants.AssertionMessages;
import com.mehmet.AssignmentTA.utils.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    @FindBy(id = "sp-cc-accept")
    private WebElement acceptCookiesButton;

    @FindBy(id = "nav-logo-sprites")
    private WebElement amazonLogo;

    @FindBy(id = "nav-link-accountList")
    private WebElement accountListMenu;

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(id = "nav-search-submit-button")
    private WebElement searchButton;

    public void navigateToHomePage() {
        Driver.getDriver().get(Driver.getProperty("url"));
        acceptCookiesIfVisible();
    }

    public boolean isHomePageOpen() {
        return isDisplayed(amazonLogo) && Driver.getDriver().getTitle().contains(AssertionMessages.AMAZON_URL);
    }

    public void acceptCookiesIfVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(2));
            shortWait.until(ExpectedConditions.visibilityOf(acceptCookiesButton));
            click(acceptCookiesButton);
        } catch (Exception e) {
        }
    }

    public void clickSignIn() {
        click(accountListMenu);
    }

    public void searchFor(String query) {
        sendKeys(searchBox, query);
        click(searchButton);
    }
}
