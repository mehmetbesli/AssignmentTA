# Amazon Turkey Web Automation Project

This is a Selenium-based test automation project developed with Java and TestNG to automate end-to-end shopping flows on Amazon Turkey (`amazon.com.tr`).

The project follows the **Page Object Model (POM)** design pattern for better maintainability and readability.

## Features & Test Flow
- **Authentication**: Log in with credentials loaded from `config.properties`.
- **Search & Filter**: Search for "samsung", filter by "Samsung" brand, and filter by stock availability.
- **Navigation**: Go to page 2 of search results.
- **Cart Operations**: Attempt to add the 5th product to the cart (retries with subsequent products if adding fails), verify it is in the cart, remove it, and verify the cart is empty.
- **Reporting**:
  - **ExtentReports**: Generates detailed HTML reports and captures screenshots automatically on failure.
  - **Excel Logging**: Writes test run summary (email, case name, status) to a timestamped Excel file.

## Tech Stack
- Java 17 / JDK 24
- Selenium WebDriver (4.18.1)
- TestNG (7.9.0)
- Apache POI (for Excel reporting)
- ExtentReports 5 (for HTML reporting)
- Maven

## Project Structure
```text
 ├── src/
 │    ├── main/
 │    │    ├── java/com/mehmet/AssignmentTA/
 │    │    │    ├── constants/        # Assertion messages and constants
 │    │    │    ├── pages/            # Page Object classes (BasePage, Home, Login, etc.)
 │    │    │    └── utils/            # Utilities (Driver, ExcelUtil, ExtentReportManager, TestListener)
 │    │    └── resources/
 │    │         └── config.properties # System configurations (credentials, browser, timeout)
 │    └── test/
 │         └── java/com/mehmet/AssignmentTA/
 │              └── tests/            # TestNG tests (BaseTest, AmazonTest)
 └── test-output/                     # Generated test reports and execution outputs
      ├── ExcelReport/                # Excel test results (timestamped excel files)
      └── ExtentReport/
           ├── html/                  # Extent HTML test reports (timestamped html files)
           └── screenshots/           # Captured screenshots (attached on test failures)
```

## Setup & Configuration
1. Clone the repository.
2. The working credentials are already configured in `src/main/resources/config.properties`. You can update them if you wish to run the tests with different credentials.

## Running the Tests
You can run the tests using Maven or TestNG suite file:

### Using Maven
Run the following command in the project root directory:
```bash
mvn clean test
```

### Using TestNG Suite
Run `testng.xml` directly from your IDE.

## Reports and Outputs
After running the tests, the reports are generated in the `test-output` directory:
- **Extent HTML Report**: `test-output/ExtentReport/html/[timestamp].html`
- **Screenshots (on failure)**: `test-output/ExtentReport/screenshots/`
- **Excel Results**: `test-output/ExcelReport/excel_[timestamp].xlsx`
