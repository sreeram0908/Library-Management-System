
# Library Management System

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Installation](#installation)
4. [Usage](#usage)
5. [Project Structure](#project-structure)
6. [Modules](#modules)
7. [Object-Oriented Features](#object-oriented-features)
8. [Future Enhancements](#future-enhancements)
9. [License](#license)

---

## Introduction
The **Library Management System** is a terminal-based Java application designed to manage library operations, including adding, removing, issuing, and returning books. The system ensures efficient book inventory handling and member management while supporting essential object-oriented programming concepts.

## Features
- Add and remove books from the inventory.
- Issue and return books with due date tracking.
- Calculate fines for overdue book returns.
- Manage library members.
- Custom exception handling for improved error management.

## Installation
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- IntelliJ IDEA (or any preferred Java IDE)
- MySQL or any compatible database system for database storage (if applicable)

### Steps to Set Up
1. Open the project in your preferred IDE.
2. Configure database connection settings in the `DatabaseConnection` class.
3. Compile and run the project.

## Usage
1. Run the `LibrarySystem` class to start the application.
2. Use the menu options to:
   - Add or remove books.
   - Issue books to members.
   - Return books and check for fines.
   - Manage library members.
3. Follow the prompts to input data as required.

## Project Structure
```
src/
├── library/
│   ├── management/
│   │   ├── LibrarySystem.java
│   │   ├── Library.java
│   │   ├── Book.java
│   │   └── RegularBook.java
│   ├── utils/
│   │   ├── LibraryException.java
│   │   └── DatabaseConnection.java
```

## Modules
- **LibrarySystem**: The main entry point that handles menu-driven interactions for administrators.
- **Library**: Handles operations related to book and member management, database queries, and business logic.
- **Book (Abstract Class)**: Represents the core properties and methods for book objects.
- **RegularBook**: A concrete implementation of the `Book` class.
- **LibraryException**: Custom exception class for handling library-specific errors.
- **DatabaseConnection**: Manages connections to the database.

## Object-Oriented Features
- **Encapsulation**: Private fields with public getters/setters in classes.
- **Abstraction**: Abstract `Book` class defines shared properties and methods.
- **Inheritance**: The `RegularBook` class extends `Book`.
- **Polymorphism**: Potential for method overriding in different book types.

## Future Enhancements
- Add a graphical user interface (GUI) for improved user experience.
- Implement role-based access control (e.g., librarian vs. member).
- Expand database structure to include activity logs and statistics.
- Introduce user authentication and security mechanisms.
- Add notifications for due dates and overdue fines.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
