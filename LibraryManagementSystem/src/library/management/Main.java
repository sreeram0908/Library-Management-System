package library.management;

import library.utils.DatabaseConnection;
import library.utils.LibraryException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Library Management System");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (Admin.validate(username, password)) {
            System.out.println("Login Successful!");
            LibrarySystem librarySystem = new LibrarySystem();
            librarySystem.start();
        } else {
            System.out.println("Invalid credentials. Exiting system.");
        }
    }
}
