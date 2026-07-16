package com.mehmet.AssignmentTA.pages;

import com.mehmet.AssignmentTA.utils.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriverWait wait;

    public BasePage() {
        PageFactory.initElements(Driver.getDriver(), this);
        int timeout = Integer.parseInt(Driver.getProperty("timeout"));
        this.wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
    }

    public void click(WebElement element) {
        try {
            waitForClickability(element);
            scrollToElement(element);
            element.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Tıklama engellendi, çerez veya popup kontrolü yapılıyor...");
            try {
                WebElement acceptCookies = Driver.getDriver().findElement(By.id("sp-cc-accept"));
                if (acceptCookies.isDisplayed()) {
                    acceptCookies.click();
                    Thread.sleep(500);
                    element.click();
                    return;
                }
            } catch (Exception ignored) {}
            
            System.out.println("JavaScript ile tıklama deneniyor...");
            try {
                ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", element);
            } catch (Exception jsException) {
                throw e;
            }
        }
    }

    public void sendKeys(WebElement element, String text) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(WebElement element) {
        waitForVisibility(element);
        return element.getText().trim();
    }

    public boolean isDisplayed(WebElement element) {
        try {
            waitForVisibility(element);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForClickability(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(500);
        } catch (Exception e) {
            ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }
}
