# Library-Management-System
A terminal based java application that helps to manage books in a library.

A Library Management System created using Object-Oriented Analysis and Design (OOAD) principles. This project demonstrates the use of abstraction, encapsulation, inheritance, and polymorphism to create a modular, maintainable, and efficient system.
The system is designed to handle various tasks such as managing books, members, and administrative operations, with a console-based interface.

The project is implemented in Java with a MySQL database for data storage. It was developed as part of the course "Object Oriented Analysis and Design CS309".

Class Diagram:
![image](https://github.com/user-attachments/assets/bb7c7c69-8c12-40ff-9688-08212af0e3fb)

Interface
Actors
The actors in the system include:

Librarian
Checkout Clerk
Borrower
Administrator
Use Cases
Below are the primary use cases for each actor:

Borrower:
Search for items by title, author, or subject.
Place a book on hold if it is currently on loan.
View personal information and list of books currently borrowed.
Checkout Clerk:
Perform all borrower use cases.
Check out an item for a borrower.
Check in a returned item.
Renew an item.
Record fines paid by borrowers.
Add new borrowers.
Update borrower details like address or phone number.
Librarian:
Perform all borrower and checkout clerk use cases.
Add new items to the library collection.
Delete items from the collection.
Update the details of library items.
Administrator:
Add new clerks and librarians.
View issued books history.
View all books in the library.

How to Run

1.Prerequisites

Install the following tools:

  Java SE Development Kit (JDK) 17 or above

  IntelliJ IDEA or other IDE

  MySQL Server and MySQL Workbench


2. Setting Up the Database

  Create a Database in MySQL Workbench:


    Open MySQL Workbench and create a database named library_db.

    Run the Database Schema:


Navigate to the database folder in the project and run the provided schema.sql file in MySQL Workbench.

This will create the required tables (books, members, transactions, etc.).

Update Database Credentials:


Open the DatabaseConnection.java file in the library.utils package.

Update the database connection credentials (username, password, and URL) as per your setup:
    
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";  
    private static final String USERNAME = "your_mysql_username";  
    private static final String PASSWORD = "your_mysql_password";  

Running the Project


Open the Project in IntelliJ IDEA:


Click on File -> Open and select the project folder.

Build and Run the Project:


Ensure the database is running and connected.

Run the Main.java file located in the root directory of the project.


If you like this repository, please support it by giving a star ⭐ and sharing it with your network!
