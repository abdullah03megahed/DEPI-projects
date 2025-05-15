# DEPI-project
Software Testing Project – Swag Labs ([https://www.saucedemo.com/v1/](https://www.saucedemo.com/v1/))

📘 Test Introduction – DEPI Project
This project is part of a Software Testing course assignment and focuses on automated end-to-end UI testing for the Swag Labs e-commerce website. The goal of this test suite is to ensure that the platform's core functionalities perform reliably and meet expected behavior under various conditions.
The tests are written in Java and leverage Selenium WebDriver for browser automation, with TestNG (or JUnit) as the test framework. These tests cover common user actions such as logging in, navigating products, sorting, manipulating the cart, and completing the checkout process.
This automation suite simulates real user interactions, verifies functional correctness, and helps detect regressions in essential parts of the system.

👥 Team Members
This project was developed by the following team members as part of the Software Testing DEPI course:
1-Abdullah Mohamed Megahed Abdelnabi
2-Kareem Mohamed Shawki Mohamed
3-Abdelrahman Mohamed Hussein Ali
4-Shady Emad Kolta Henin
5-Yossif Mohamed Abbas Helmi

📌 Project Scope
The objective of this project is to design and implement automated functional tests for the core features of the Swag Labs e-commerce website using Java, Selenium WebDriver, and TestNG. The scope includes:
✅ Validating User Authentication: Testing both valid and invalid login credentials and ensuring correct logout functionality.
✅ Testing Product Display: Ensuring product titles, images, prices, and descriptions are correctly rendered.
✅ Sorting Verification: Confirming that sorting by name or price (ascending/descending) behaves as expected.
✅ Cart Functionality: Checking if products can be added to and removed from the shopping cart with quantity updates reflected accurately.
✅ Checkout Workflow: Validating the complete purchase flow from adding items to confirming the order.
✅ Negative Scenarios: Handling incorrect or edge-case inputs, such as empty fields or unauthorized access.
✅ Bug Detection: Identifying unexpected behavior such as accessing inventory without login or missing UI elements.

❌ Out of Scope:
-Load or performance testing.
-Cross-browser compatibility testing.
-Mobile responsiveness testing.

🗂️ Automation Project Structure
DEPI-project/
│
├── src/
│   └── test/
│       └── java/
│           ├── pages/
│           │   ├── LoginPage.java           # Page Object for Login screen
│           │   ├── InventoryPage.java       # Page Object for product listings
│           │   ├── CartPage.java            # Page Object for Cart operations
│           │   ├── CheckoutPage.java        # Page Object for Checkout process
│           │   └── CompletePage.java        # Page Object for order completion
├── testng.xml                                # TestNG configuration file
├── pom.xml                                   # Maven project descriptor
├── README.md                                 # Project documentation
🔁 Design Patterns Used
-Singleton Pattern: Test and methods in pages in one class as it's effictive method for small projects.
-TestNG Framework: For organizing and running test cases with assertions and reporting.

This structure enhances:
-Maintainability.
-Scalability for adding new test suites.
-Performance by managing WebDriver properly using Singleton.

 Features Tested:-
1- Login & Logout** – Verifies correct and incorrect login scenarios and ensures successful logout functionality.
2- Product Page** – Confirms the visibility and correctness of product listings and UI elements.
3- Sorting Functionality** – Tests the product sort feature (e.g., by price or name) for correct behavior.
4- Add to / Remove from Cart** – Checks the ability to add and remove items from the shopping cart, including quantity updates.
5- Checkout Process** – Validates the full checkout workflow, from cart review to completing a purchase.

 Technologies Used:-
1- Java JDK(21.0.6).
2- Selenium WebDriver (or your test automation tool if different).
3- TestNG.

✅ Prerequisites
Before running the project, make sure you have the following installed and configured:
💻 System Requirements
-Operating System: Windows 10/11.
-Browser: Microsoft Edge.
🔧 Tools & Dependencies.
-Java JDK – Version 21 or above.
-Maven – For managing project dependencies and running tests.
-IDE – IntelliJ IDEA (Java configured).
📦 Libraries (Handled via Maven pom.xml)
-Selenium WebDriver.
-TestNG.
-WebDriverManager – for managing browser drivers automatically.
🔐 Environment Setup
-Ensure environment variables for JAVA_HOME and MAVEN_HOME are properly set.
-Internet access is required for downloading dependencies on first build.

📝 Jira Board
You can track all tasks, bugs, and progress related to this project via the official Jira board:
🔗 🔗 Click here to access the DEPI Jira Project
Note: You may need to log in with the authorized email address to access the board.

📄 Test Summary Report
This report documents the execution results of automated test cases, including:
-Pass/Fail status.
-Priority and severity.
-Test environment and configuration.
-Techniques used.
🔗 View Test Summary Report

🐞 Bug Report
The detailed bug tracking and issue report for this project can be accessed here:
🔗 Bug Report Document

📽️ Project Presentation
For a comprehensive overview and walkthrough of the project, please refer to the presentation slides available here:
🔗 View Project Presentation
