package library.management;

import library.utils.LibraryException;
import java.util.Scanner;

public class LibrarySystem {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n--- Library Management Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Member by ID");
            System.out.println("6. Add Member"); // New option for adding members
            System.out.println("7. Remove Member"); // New option for removing members
            System.out.println("8. Current Session Details");
            System.out.println("9.Display Book Inventory");
            System.out.println("10.Search Book Inventory");
            System.out.println("11. Exit");

            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            try {
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> removeBook();
                    case 3 -> issueBook();
                    case 4 -> returnBook();
                    case 5 -> searchMemberById();
                    case 6 -> addMember();
                    case 7 -> removeMember();
                    case 8 -> Library.displaySessionDetails(); // Display session details
                    case 9 -> Library.displayBookInventory();
                    case 10-> searchBookInventory();
                    case 11 -> {
                        System.out.println("Exiting system...");
                        return;
                    }
                    default -> System.out.println("Invalid choice, please try again.");
                }
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addBook() throws LibraryException {
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Publication Date (YYYY-MM-DD): ");
        String publicationDate = scanner.nextLine();
        System.out.print("Enter Number of Copies: ");
        int copies = Integer.parseInt(scanner.nextLine());

        Book book = new RegularBook(title, author, publicationDate, copies);
        Library.addBookToDatabase(book);
        System.out.println("Book added successfully.");

        book.displayInfo();
    }

    private void removeBook() throws LibraryException {
        System.out.print("Enter Book ID to remove: ");
        String bookId = scanner.nextLine();
        Library.removeBookFromDatabase(bookId);
        System.out.println("Book removed successfully.");
    }

    private void issueBook() throws LibraryException {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();

        System.out.print("Enter Book ID to issue: ");
        String bookId = scanner.nextLine();

        // Call the issueBook method from Library, passing in both memberId and bookId
        Library.issueBook(bookId, memberId);
        System.out.println("Book issued successfully.");
    }


    private void returnBook() throws LibraryException {
        System.out.print("Enter Issue ID to return: ");
        String issueId = scanner.nextLine();
        Library.returnBook(issueId);
        System.out.println("Book returned successfully.");
    }

    private void searchMemberById() throws LibraryException {
        System.out.print("Enter Member ID to search: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        Library.searchMemberById(memberId); // Call the search method
    }

    private void searchBookInventory() throws LibraryException {
        System.out.println("\n1.Search By Id");
        System.out.println("\n2.Search By title");
        System.out.print("Enter Choice:");
        int ch = Integer.parseInt(scanner.nextLine());
        if(ch==1){
            System.out.print("Enter book Id");
            int bookId = Integer.parseInt(scanner.nextLine());
            Library.searchBookById(bookId);
        }
        else if (ch==2) {
            System.out.print("Enter book Title");
            String Title = scanner.nextLine();
            Library.searchBookByTitle(Title);
        }
        // Call the search method
    }

    // New method to add a member
    private void addMember() throws LibraryException {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();
        if (Library.isMemberIdExists(memberId)) {
            System.out.println("Member ID already exists. Please use a different ID.");
            return;
        }
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();

        Member member = new Member(memberId, name, email, phone);
        Library.addMemberToDatabase(member);
        System.out.println("Member added successfully.");
    }

    private void removeMember() throws LibraryException {
        System.out.print("Enter Member ID to remove: ");
        String memberId = scanner.nextLine();
        Library.removeMemberFromDatabase(memberId);
        System.out.println("Member removed successfully.");
    }
}
