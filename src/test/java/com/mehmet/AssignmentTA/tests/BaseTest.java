package com.mehmet.AssignmentTA.tests;

import com.mehmet.AssignmentTA.utils.Driver;
import com.mehmet.AssignmentTA.utils.ExtentReportManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import java.util.Locale;

@Listeners(com.mehmet.AssignmentTA.utils.TestListener.class)
public class BaseTest {

    static {
        Locale.setDefault(Locale.US);
    }

    @BeforeMethod
    public void setUp() {
        Driver.getDriver();
    }

    @AfterMethod
    public void tearDown(org.testng.ITestResult result) {
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            try {
                String screenshotPath = ExtentReportManager.captureScreenshot(result.getName());
                ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                System.err.println("Could not attach screenshot to Extent Report: " + e.getMessage());
            }
        }
        Driver.closeDriver();
    }
}
