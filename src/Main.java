import java.sql.SQLException;
import java.sql.Date;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BookControl bookControl = new BookControl();
    private static MemberControl memberControl = new MemberControl();
    private static LoanControl loanControl = new LoanControl(); // Ensure LoanDAO is updated as shown above

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add a New Book");
            System.out.println("2. Update Book Details");
            System.out.println("3. Delete a Book");
            System.out.println("4. Search for a Book by ID");
            System.out.println("5. Add a New Member");
            System.out.println("6. Delete a Member");
            System.out.println("7. Loan a Book");
            System.out.println("8. Return a Book");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addNewBook();
                    break;
                case 2:
                    updateBookDetails();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    searchBookById();
                    break;
                case 5:
                    addNewMember();
                    break;
                case 6:
                    deleteMember();
                    break;
                case 7:
                    loanBook();
                    break;
                case 8:
                    returnBook();
                    break;
                case 0:
                    System.out.println("Exit and Thank You ..");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        // Close scanner and database connection
        scanner.close();
        try {
            DatabaseConnector.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addNewBook() {
        System.out.println("\n=== Add a New Book ===");
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter year published: ");
        int yearPublished = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.setPublisher(publisher);
        newBook.setYearPublished(yearPublished);

        try {
            bookControl.addBook(newBook);
            System.out.println("Book added successfully with ID: " + newBook.getBookId());
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    private static void updateBookDetails() {
        System.out.println("\n=== Update Book Details ===");
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        Book bookToUpdate = new Book();
        bookToUpdate.setBookId(bookId);

        System.out.print("Enter new title (press Enter to skip): ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) {
            bookToUpdate.setTitle(newTitle);
        }
        System.out.print("Enter new author (press Enter to skip): ");
        String newAuthor = scanner.nextLine();
        if (!newAuthor.isEmpty()) {
            bookToUpdate.setAuthor(newAuthor);
        }
        System.out.print("Enter new publisher (press Enter to skip): ");
        String newPublisher = scanner.nextLine();
        if (!newPublisher.isEmpty()) {
            bookToUpdate.setPublisher(newPublisher);
        }
        System.out.print("Enter new year published (press Enter to skip): ");
        String newYearPublishedStr = scanner.nextLine();
        if (!newYearPublishedStr.isEmpty()) {
            int newYearPublished = Integer.parseInt(newYearPublishedStr);
            bookToUpdate.setYearPublished(newYearPublished);
        }

        try {
            bookControl.updateBook(bookToUpdate);
            System.out.println("Book details updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
        }
    }

    private static void deleteBook() {
        System.out.println("\n=== Delete a Book ===");
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            bookControl.deleteBook(bookId);
            System.out.println("Book deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    private static void searchBookById() {
        System.out.println("\n=== Search for a Book by ID ===");
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            Book book = bookControl.searchBookById(bookId);
            if (book != null) {
                System.out.println("Book found:\n" + book);
            } else {
                System.out.println("No book found with ID " + bookId);
            }
        } catch (SQLException e) {
            System.out.println("Error searching for book: " + e.getMessage());
        }
    }

    private static void addNewMember() {
        System.out.println("\n=== Add a New Member ===");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        Member newMember = new Member();
        newMember.setName(name);
        newMember.setEmail(email);
        newMember.setPhone(phone);

        newMember = memberControl.addMember(newMember);
        System.out.println("Member added successfully with ID: " + newMember.getMemberId());
    }

    private static void deleteMember() {
        System.out.println("\n=== Delete a Member ===");
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            memberControl.deleteMember(memberId);
            System.out.println("Member deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
        }
    }

    private static void loanBook() {
        System.out.println("\n=== Loan a Book ===");
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        System.out.print("Enter loan date (YYYY-MM-DD): ");
        String loanDateStr = scanner.next();
        scanner.nextLine(); // Consume newline character

        try {
            Date loanDate = java.sql.Date.valueOf(loanDateStr);
            loanControl.loanBook(bookId, memberId, loanDate);
            System.out.println("Book loaned successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    private static void returnBook() {
        System.out.println("\n=== Return a Book ===");
        System.out.print("Enter loan ID: ");
        int loanId = scanner.nextInt();
        System.out.print("Enter return date (YYYY-MM-DD): ");
        String returnDateStr = scanner.next();
        scanner.nextLine(); // Consume newline character

        try {
            Date returnDate = java.sql.Date.valueOf(returnDateStr);
            loanControl.returnBook(loanId, returnDate);
            System.out.println("Book returned successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }
}
