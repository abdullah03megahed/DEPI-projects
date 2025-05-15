# Swag Labs Automated Testing Project

## 📜 Test Introduction – DEPI Project

This repository contains the automated end-to-end UI testing suite for the Swag Labs e-commerce platform ([https://www.saucedemo.com/v1/](https://www.saucedemo.com/v1/)). This project was undertaken as an assignment for the Software Testing (DEPI) course. The primary objective is to validate the core functionalities of the Swag Labs website, ensuring reliability and adherence to expected behaviors under various scenarios.

The test suite is developed in Java, utilizing Selenium WebDriver for browser automation and TestNG as the testing framework. The automated tests cover essential user interactions, including login processes, product navigation and sorting, shopping cart manipulation, and the complete checkout workflow. This automation effort aims to simulate real user experiences, verify functional correctness, and facilitate the early detection of regressions within the system's critical features.

## 👥 Team Members

This project was collaboratively developed by the following students for the Software Testing DEPI course:

* Abdullah Mohamed Megahed Abdelnabi
* Kareem Mohamed Shawki Mohamed
* Abdelrahman Mohamed Hussein Ali
* Shady Emad Kolta Henin
* Yossif Mohamed Abbas Helmi

## 📌 Project Scope

The core objective of this project is the design and implementation of automated functional tests for the key features of the Swag Labs e-commerce website. This is achieved using Java, Selenium WebDriver, and the TestNG framework.

### ✅ In Scope:

* **User Authentication:**
    * Validation of login functionality with both valid and invalid credentials.
    * Verification of successful logout.
* **Product Display:**
    * Ensuring accurate rendering of product titles, images, prices, and descriptions.
* **Sorting Functionality:**
    * Confirmation of correct product sorting by name (ascending/descending) and price (ascending/descending).
* **Cart Functionality:**
    * Verification of adding products to the shopping cart.
    * Verification of removing products from the shopping cart.
    * Ensuring accurate reflection of quantity updates in the cart.
* **Checkout Workflow:**
    * Validation of the entire purchase process, from adding items to order confirmation.
* **Negative Scenarios:**
    * Handling of incorrect or edge-case inputs, such as empty form fields.
    * Testing for unauthorized access attempts.
* **Bug Detection:**
    * Identification of unexpected behaviors, such as accessing inventory pages without prior login.
    * Detection of missing or malfunctioning UI elements.

### ❌ Out of Scope:

* Load or performance testing.
* Cross-browser compatibility testing (beyond the primary test browser).
* Mobile responsiveness testing.

## 🗂️ Automation Project Structure

The project follows a standard Maven project structure, incorporating the Page Object Model (POM) design pattern for enhanced maintainability and readability.

![image](https://github.com/user-attachments/assets/965620ec-2ef5-4db8-b2aa-3ab2ec88df0c)


This structure promotes:

* **Maintainability:** Separation of test logic from page-specific code.
* **Scalability:** Ease of adding new test cases and page objects.
* **Reusability:** Page objects can be reused across multiple test scripts.

## 🔁 Design Patterns and Frameworks Used

* **Page Object Model (POM):** Used to create a readable and maintainable test automation framework by representing each web page as a class and web elements as variables, with user interactions implemented as methods.
* **Singleton Pattern:** Implemented for WebDriver instance management to ensure a single, shared browser instance across tests, which can be effective for managing resources in smaller to medium-sized projects.
* **TestNG Framework:** Utilized for structuring and executing test cases, managing test suites, generating reports, and providing assertion capabilities.

## ✨ Features Tested

The automated test suite covers the following core functionalities:

1.  **Login & Logout:** Verifies authentication with valid and invalid credentials and ensures proper logout.
2.  **Product Page Navigation & Display:** Confirms the correct display of product listings, including details and UI elements.
3.  **Sorting Functionality:** Validates the product sorting feature (by price and name, ascending/descending).
4.  **Add to / Remove from Cart:** Tests the ability to add items to and remove items from the shopping cart, ensuring quantity updates are accurate.
5.  **Checkout Process:** Validates the end-to-end checkout workflow, from cart review to successful order completion.

## 🛠️ Technologies Used

* **Programming Language:** Java (JDK 21.0.6 or later)
* **Test Automation Tool:** Selenium WebDriver
* **Testing Framework:** TestNG
* **Build Automation Tool & Dependency Management:** Apache Maven
* **Browser Driver Management:** WebDriverManager

## ✅ Prerequisites

Ensure the following software is installed and configured on your system before running the tests:

### 💻 System Requirements:

* **Operating System:** Windows 10/11 (primarily tested on this OS)
* **Browser:** Microsoft Edge (ensure the corresponding Edge WebDriver is compatible or managed by WebDriverManager)

### 🔧 Tools & Dependencies:

* **Java Development Kit (JDK):** Version 21 or higher.
* **Apache Maven:** For project build and dependency management.
* **Integrated Development Environment (IDE):** IntelliJ IDEA (recommended, with Java configured) or any other Java-compatible IDE.

### 📦 Libraries (Managed via `pom.xml`):

* Selenium WebDriver
* TestNG
* WebDriverManager (automates the download and setup of browser drivers)

### 🔐 Environment Setup:

* **Environment Variables:**
    * `JAVA_HOME`: Should point to your JDK installation directory.
    * `MAVEN_HOME` (Optional, if `mvn` is not in PATH): Should point to your Maven installation directory. Ensure Maven's `bin` directory is added to your system's PATH.
* **Internet Connection:** Required for the initial build to download Maven dependencies.
