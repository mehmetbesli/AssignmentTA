package com.mehmet.AssignmentTA.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    static {
        java.util.Locale.setDefault(java.util.Locale.US);
    }

    public static ExtentReports getReportInstance() {
        if (extent == null) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportName = timeStamp + ".html";
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport/html/" + reportName;
            
            // Ensure directory exists
            File reportFile = new File(reportPath);
            File parentDir = reportFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Amazon Test Automation Report");
            sparkReporter.config().setReportName("Amazon Web Automation Regression Suite");
            sparkReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("QA Engineer", "Mehmet");
            extent.setSystemInfo("Application", "Amazon Turkey");
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }

    public static synchronized ExtentTest createTest(String testName) {
        ExtentTest t = getReportInstance().createTest(testName);
        test.set(t);
        return t;
    }

    public static synchronized ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static String captureScreenshot(String testName) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String screenshotName = testName + "_" + timeStamp + ".png";
        String relativePath = "../screenshots/" + screenshotName;
        String absolutePath = System.getProperty("user.dir") + "/test-output/ExtentReport/screenshots/" + screenshotName;

        File srcFile = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.FILE);
        File destFile = new File(absolutePath);
        
        try {
            File parent = destFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            FileHandler.copy(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativePath; // Relative path to use in report
    }
}
