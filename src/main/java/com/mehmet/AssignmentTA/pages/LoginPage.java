package com.mehmet.AssignmentTA.pages;

import com.mehmet.AssignmentTA.utils.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@id='ap_email' or @name='email' or @type='email']")
    private WebElement emailField;

    @FindBy(xpath = "//input[@id='continue' or @type='submit' or @aria-labelledby='continue-announce']")
    private WebElement continueButton;

    @FindBy(xpath = "//input[@id='ap_password' or @name='password' or @type='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@id='signInSubmit' or @type='submit']")
    private WebElement signInButton;

    @FindBy(id = "auth-captcha-image-container")
    private List<WebElement> captchaContainers;

    @FindBy(name = "cvf_captcha_input")
    private List<WebElement> captchaInputs;

    @FindBy(id = "auth-mfa-otpcode")
    private List<WebElement> otpCodes;

    @FindBy(id = "nav-link-accountList-nav-line-1")
    private WebElement navLine1;

    public void login(String email, String password) {
        sendKeys(emailField, email);
        click(continueButton);

        handleCaptchaOrVerification();

        try {
            waitForVisibility(passwordField);
        } catch (Exception e) {
            handleCaptchaOrVerification();
        }

        sendKeys(passwordField, password);
        click(signInButton);

        handleCaptchaOrVerification();
    }

    public boolean isLoginSuccessful() {
        try {
            waitForVisibility(navLine1);
            String text = getText(navLine1);
            return !text.contains("Giriş yapın") && !text.contains("Giriş yap");
        } catch (Exception e) {
            return false;
        }
    }

    private void handleCaptchaOrVerification() {
        boolean verificationNeeded = false;
        Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        try {
            if (captchaContainers.size() > 0 ||
                captchaInputs.size() > 0 ||
                otpCodes.size() > 0 ||
                Driver.getDriver().getTitle().contains("Doğrulama") ||
                Driver.getDriver().getTitle().contains("Verification") ||
                Driver.getDriver().getTitle().contains("Robot") ||
                Driver.getDriver().getCurrentUrl().contains("cvf/approval")) {
                verificationNeeded = true;
            }
        } catch (Exception ignored) {
        } finally {
            int timeout = Integer.parseInt(Driver.getProperty("timeout"));
            Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        }

        if (verificationNeeded) {
            
            new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(60))
                .until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.id("twotabsearchtextbox")),
                    ExpectedConditions.presenceOfElementLocated(By.id("ap_password")),
                    ExpectedConditions.presenceOfElementLocated(By.id("nav-link-accountList-nav-line-1"))
                ));
        }
    }
}
