import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class LoanControl {

    public void loanBook(int bookId, int memberId, Date loanDate) {
        if (!bookExists(bookId)) {
            System.out.println("Error: Book ID " + bookId + " does not exist.");
            return;
        }

        if (!memberExists(memberId)) {
            System.out.println("Error: Member ID " + memberId + " does not exist.");
            return;
        }

        String sql = "INSERT INTO loans (book_id, member_id, loan_date) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.setInt(2, memberId);
            statement.setDate(3, new java.sql.Date(loanDate.getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int loanId, Date returnDate) {
        String sql = "UPDATE loans SET return_date = ? WHERE loan_id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, new java.sql.Date(returnDate.getTime()));
            statement.setInt(2, loanId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean bookExists(int bookId) {
        String sql = "SELECT COUNT(*) FROM books WHERE book_id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean memberExists(int memberId) {
        String sql = "SELECT COUNT(*) FROM members WHERE member_id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
