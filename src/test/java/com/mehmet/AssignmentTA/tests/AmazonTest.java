package com.mehmet.AssignmentTA.tests;

import com.mehmet.AssignmentTA.constants.AssertionMessages;
import com.mehmet.AssignmentTA.pages.*;
import com.mehmet.AssignmentTA.utils.Driver;
import com.mehmet.AssignmentTA.utils.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AmazonTest extends BaseTest {

    @Test(description = "Amazon Samsung Product Wishlist and Cart Validation Flow")
    public void testAmazonShoppingFlow(java.lang.reflect.Method method) {
        String caseName = method.getAnnotation(Test.class).description();
        if (caseName == null || caseName.isEmpty()) {
            caseName = method.getName();
        }

        String email = Driver.getProperty("email");
        String password = Driver.getProperty("password");

        String timeStamp = new java.text.SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String excelPath = "test-output/ExcelReport/excel_" + timeStamp + ".xlsx";
        ExcelUtil excel = new ExcelUtil(excelPath, "Sheet1");

        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        ProductDetailPage productDetailPage = new ProductDetailPage();
        CartPage cartPage = new CartPage();

        try {
            homePage.navigateToHomePage();
            Assert.assertTrue(homePage.isHomePageOpen(), AssertionMessages.HOMEPAGE_NOT_OPENED);

            homePage.clickSignIn();
            loginPage.login(email, getPassword(password));
            Assert.assertTrue(loginPage.isLoginSuccessful(), AssertionMessages.LOGIN_FAILED);

            homePage.searchFor("samsung");

            searchResultsPage.selectSamsungFilter();
            searchResultsPage.selectInStockFilter();

            Assert.assertTrue(searchResultsPage.isResultsTextContaining(AssertionMessages.SAMSUNG_TEXT_EXPECTED), AssertionMessages.NO_RESULTS_FOUND);
            Assert.assertTrue(searchResultsPage.hasResults(), AssertionMessages.NO_RESULTS_FOUND);

            searchResultsPage.navigateToPage2();
            Assert.assertTrue(searchResultsPage.isPage2Active(), AssertionMessages.PAGE_2_NOT_ACTIVE);

            String productTitle = "";
            int targetIndex = 4;
            boolean addedSuccessfully = false;

            while (targetIndex < 10) {
                try {
                    searchResultsPage.clickProductByIndex(targetIndex);
                    
                    productTitle = productDetailPage.getProductTitle();
                    System.out.println("Seçilen Ürün Başlığı (İndeks " + targetIndex + "): " + productTitle);
                    
                    productDetailPage.addToCart();
                    
                    Assert.assertTrue(productDetailPage.isAddedToCartPopupVisible(), AssertionMessages.ADDED_TO_CART_POPUP);
                    addedSuccessfully = true;
                    break;
                } catch (Throwable t) {
                    System.out.println("İndeks " + targetIndex + " için ürünü sepete ekleme başarısız oldu: " + t.getMessage());
                    System.out.println("Arama sonuçlarına geri dönülüyor ve bir sonraki ürün deneniyor...");
                    Driver.getDriver().navigate().back();
                    targetIndex++;
                }
            }

            Assert.assertTrue(addedSuccessfully, AssertionMessages.NO_SUITABLE_PRODUCT_FOUND);

            cartPage.navigateToCart();

            Assert.assertTrue(cartPage.isProductInCart(productTitle), AssertionMessages.PRODUCT_NOT_IN_CART);
            cartPage.removeProductFromCart(productTitle);

            Assert.assertFalse(cartPage.isProductInCart(productTitle), AssertionMessages.PRODUCT_NOT_REMOVED);

            excel.setCellData(email, 1, 0);
            excel.setCellData(caseName, 1, 1);
            excel.setCellData("PASSED", 1, 2);
            System.out.println("Test sonucu Excel'e yazıldı: " + excelPath);

        } catch (Throwable e) {
            excel.setCellData(email, 1, 0);
            excel.setCellData(caseName, 1, 1);
            excel.setCellData("FAILED", 1, 2);
            System.out.println("Test başarısızlık sonucu Excel'e yazıldı: " + excelPath);
            throw e;
        }
    }

    private static String getPassword(String password) {
        return password;
    }
}
