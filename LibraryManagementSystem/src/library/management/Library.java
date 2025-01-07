package library.management;
import java.time.LocalDate;
import java.sql.Date;
import library.utils.DatabaseConnection;
import library.utils.LibraryException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Library {
    private static final ArrayList<String> booksAdded = new ArrayList<>();
    private static final ArrayList<String> booksRemoved = new ArrayList<>();
    private static final ArrayList<String> booksIssued = new ArrayList<>();

    public static void addBookToDatabase(Book book) throws LibraryException {
        String query = "INSERT INTO book_inventory (title, author, publication_date, available_copies) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublicationDate());
            preparedStatement.setInt(4, book.getCopiesAvailable());
            preparedStatement.executeUpdate();

            // Track added book in session details
            booksAdded.add("Title: " + book.getTitle() + " ,Author:" + book.getAuthor());

        } catch (SQLException e) {
            throw new LibraryException("Error adding book to database: " + e.getMessage());
        }
    }

    public static void removeBookFromDatabase(String bookId) throws LibraryException {
        String query = "DELETE FROM book_inventory WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookId);

            // Track removed book details before deletion
            ResultSet rs = preparedStatement.executeQuery("SELECT title FROM book_inventory WHERE book_id = " + bookId);
            if (rs.next()) {
                String bookTitle = rs.getString("title");
                booksRemoved.add("ID: " + bookId + ", Title: " + bookTitle);
            }
            System.out.println("Book id:"+bookId+" Book Name:"+rs.getString("title"));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new LibraryException("Error removing book from database: " + e.getMessage());
        }
    }

    public static void issueBook(String bookId, String memberId) throws LibraryException {
        if (!isMemberValid(memberId)) {
            throw new LibraryException("Member ID not found in database.");
        }

        String selectBookQuery = "SELECT title FROM book_inventory WHERE book_id = ?";
        String issueQuery = "INSERT INTO issued_books (book_id, member_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
        String updateBookCopiesQuery = "UPDATE book_inventory SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(7);
        String bookTitle = null; // To store the title of the book

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            // First, retrieve the book title
            try (PreparedStatement selectStmt = connection.prepareStatement(selectBookQuery)) {
                selectStmt.setString(1, bookId);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    bookTitle = rs.getString("title");
                } else {
                    throw new LibraryException("Book ID not found in the database.");
                }
            }

            // Issue the book
            try (PreparedStatement issueStmt = connection.prepareStatement(issueQuery)) {
                issueStmt.setString(1, bookId);
                issueStmt.setString(2, memberId);
                issueStmt.setDate(3, Date.valueOf(issueDate));
                issueStmt.setDate(4, Date.valueOf(dueDate));
                int rowsInserted = issueStmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new LibraryException("Failed to issue the book.");
                }

                // Track issued book in session details
                booksIssued.add("Book ID: " + bookId + " (Title: " + bookTitle + ") issued to Member ID: " + memberId);
            }

            // Update book copies
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBookCopiesQuery)) {
                updateStmt.setString(1, bookId);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    throw new LibraryException("Book is currently unavailable for issuing.");
                }
            }

            connection.commit();
            System.out.println("Book \"" + bookTitle + "\" issued successfully to member with ID: " + memberId + " with a due date of " + dueDate);

        } catch (SQLException e) {
            throw new LibraryException("Error issuing book: " + e.getMessage());
        }
    }


    // Display session details
    public static void displaySessionDetails() {
        System.out.println("\n--- Current Session Details ---");

        System.out.println("Books Added:");
        if (booksAdded.isEmpty()) {
            System.out.println("No books were added in this session.");
        } else {
            for(String addbook : booksAdded)
                System.out.println(addbook);
        }

        System.out.println("\nBooks Removed:");
        if (booksRemoved.isEmpty()) {
            System.out.println("No books were removed in this session.");
        } else {
            for(String rmbook : booksRemoved)
                System.out.println(rmbook);
        }

        System.out.println("\nBooks Issued:");
        if (booksIssued.isEmpty()) {
            System.out.println("No books were issued in this session.");
        } else {
            for(String issuebook : booksIssued)
                System.out.println(issuebook);
        }
    }
    private static boolean isMemberValid(String memberId) throws LibraryException {
        String query = "SELECT * FROM membership_details WHERE member_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // True if member exists

        } catch (SQLException e) {
            throw new LibraryException("Error verifying member: " + e.getMessage());
        }
    }

    public static void returnBook(String issueId) throws LibraryException {
        String query = "SELECT book_id, issue_date, due_date FROM issued_books WHERE issue_id = ?";
        String bookId = null;
        LocalDate dueDate = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, issueId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bookId = resultSet.getString("book_id");
                LocalDate issueDate = resultSet.getDate("issue_date").toLocalDate();
                dueDate = resultSet.getDate("due_date").toLocalDate();

                // Calculate fine
                LocalDate returnDate = LocalDate.now();
                long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
                int fine = 0;
                if (daysLate > 0) {
                    fine = (int) daysLate * 50; // 50 rupees fine for each late day
                    System.out.println("The book is returned late. Fine: " + fine + " Rupees.");
                }
            } else {
                throw new LibraryException("Issue ID not found.");
            }
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving issue details: " + e.getMessage());
        }

        // Remove entry from issued_books
        removeIssuedBook(issueId);

        // Increment available copies in book_inventory
        incrementAvailableCopies(bookId);
    }

    private static void removeIssuedBook(String issueId) throws LibraryException {
        String query = "DELETE FROM issued_books WHERE issue_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, issueId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new LibraryException("Error removing issued book entry: " + e.getMessage());
        }
    }

    private static void incrementAvailableCopies(String bookId) throws LibraryException {
        String query = "UPDATE book_inventory SET available_copies = available_copies + 1 WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new LibraryException("Error updating available copies: " + e.getMessage());
        }
    }
    public static void searchMemberById(int memberId) throws LibraryException {
        String query = "SELECT * FROM membership_details WHERE member_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, memberId); // Direct match for member_id

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // Check if there is a result
                String name = resultSet.getString("name");
                String mail = resultSet.getString("mail");
                String phone = resultSet.getString("phone");

                // Print member details
                System.out.printf("ID: %d, Name: %s, Email: %s, Phone: %s%n",
                        memberId, name, mail, phone);
            } else {
                System.out.println("No member found with ID: " + memberId);
            }
        } catch (SQLException e) {
            throw new LibraryException("Error Searching member by Id in database: " + e.getMessage());
        }
    }
    public static void searchBookById(int bookId) throws LibraryException {
        String query = "SELECT * FROM book_inventory WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId); // Direct match for member_id

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // Check if there is a result
                String name = resultSet.getString("title");
                String author = resultSet.getString("author");
                String pub_date = resultSet.getString("publication_date");

                // Print book details
                System.out.printf("ID: %d, Title: %s, Author: %s, Publication Date: %s%n",
                        bookId, name,author , pub_date);
            } else {
                System.out.println("No book found with ID: " + bookId);
            }
        } catch (SQLException e) {
            throw new LibraryException("Error Searching book by Id in database: " + e.getMessage());
        }
    }
    public static void searchBookByTitle(String Title) throws LibraryException {
        String query = "SELECT * FROM book_inventory WHERE title = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Title); // Direct match for member_id

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // Check if there is a result
                int bookId = resultSet.getInt("book_id");
                String author = resultSet.getString("author");
                String pub_date = resultSet.getString("publication_date");

                // Print book details
                System.out.printf("ID: %d, Title: %s, Author: %s, Publication Date: %s%n",
                        bookId, Title,author , pub_date);
            } else {
                System.out.println("No Book found with Name: " + Title);
            }
        } catch (SQLException e) {
            throw new LibraryException("Error Searching book by title in database: " + e.getMessage());
        }
    }

    public static void addMemberToDatabase(Member member) throws LibraryException {
        String query = "INSERT INTO membership_details (member_id, name, mail, phone) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setString(2, member.getName());
            preparedStatement.setString(3, member.getMail());
            preparedStatement.setString(4, member.getPhone());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new LibraryException("Error adding member to database: " + e.getMessage());
        }
    }

    public static boolean isMemberIdExists(String memberId) throws LibraryException {
        String query = "SELECT * FROM membership_details WHERE member_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Returns true if exists
        } catch (SQLException e) {
            throw new LibraryException("Error checking member ID: " + e.getMessage());
        }
    }

    public static void removeMemberFromDatabase(String memberId) throws LibraryException {
        String query = "DELETE FROM membership_details WHERE member_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, memberId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new LibraryException("Error removing member from database: " + e.getMessage());
        }
    }

    public static void displayBookInventory() throws LibraryException {
        String query = "SELECT * FROM book_inventory";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\n--- Book Inventory ---");
            while (resultSet.next()) {
                String bookId = resultSet.getString("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publicationDate = resultSet.getString("publication_date");
                int availableCopies = resultSet.getInt("available_copies");

                System.out.printf("ID: %s, Title: %s, Author: %s, Publication Date: %s, Available Copies: %d%n",
                        bookId, title, author, publicationDate, availableCopies);
            }
        } catch (SQLException e) {
            throw new LibraryException("Error displaying book inventory: " + e.getMessage());
        }
    }

}

