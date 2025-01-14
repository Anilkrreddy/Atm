import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Atmmachine {
    // Define your database connection properties
    private static final String URL = "jdbc:postgresql://localhost:5432/atmmachine";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public static void main(String[] args) {
        Atmmachine atm = new Atmmachine();
        atm.run();
    }

    public void run() {
        System.out.println("Welcome to ANILREDDY ATM");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Card Number:");
        String cardNumber = sc.nextLine(); // Read card number as a string
        System.out.println("Enter PIN:");
        int pin = sc.nextInt();
        int userId = authenticateUser(cardNumber, pin);
        if (userId != -1) {
            handleMenu(userId);
        } else {
            System.out.println("Authentication failed. Please try again.");
        }
    }

    private int authenticateUser(String cardNumber, int pin) {
        try {
            // Explicitly register MySQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT user_id, pin FROM users WHERE card_number = ?";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    st.setString(1, cardNumber);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        String storedPin = rs.getString("pin");
                        if (String.valueOf(pin).equals(storedPin)) {
                            return rs.getInt("user_id");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    // Rest of your code remains the same

    private void handleMenu(int userId) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Cash");
            System.out.println("4. Change PIN");
            System.out.println("5. Update Mobile Number");
            System.out.println("6. Request Cheque Book");
            System.out.println("7. Get Mini Statement");
            System.out.println("8. Cardless Cash Withdrawal");
            System.out.println("9. Enable International Transactions");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    displayBalance(userId);
                    notifyMinimumBalance(userId);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawalAmount = sc.nextDouble();
                    makeWithdrawal(userId, withdrawalAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = sc.nextDouble();
                    makeDeposit(userId, depositAmount);
                    break;
                case 4:
                    changePin(userId);
                    break;
                case 5:
                    updateMobileNumber(userId);
                    break;
                case 6:
                    requestChequeBook(userId);
                    break;
                case 7:
                    miniStatement(userId);
                    break;
                
                case 8:
                    cardlessCashWithdrawal(userId);
                    break;
                case 9:
                    enableInternationalTransactions(userId);
                    break;
                case 10:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Implement other methods like displayBalance, notifyMinimumBalance, makeWithdrawal, makeDeposit, etc.



 

    // Method stubs for displayBalance and makeWithdrawal
    private void displayBalance(int userId) {
        // Implementation to display balance here
    	try {
    		
    		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
    		String query="select balance from accounts where user_id=?";
    		PreparedStatement psmt=connection.prepareStatement(query);
    		psmt.setInt(1, userId);
    		ResultSet r=psmt.executeQuery();
    		if(r.next()) {
    			double balance=r.getDouble("balance");
    			System.out.println("Your current balance is: $" + balance);
    		}
    		
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    }

    


    private void logTransaction(int accountId, double amount, String transactionType) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO transactions (account_id, amount, transaction_type) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, accountId);
                ps.setDouble(2, amount);
                ps.setString(3, transactionType);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void changePin(int userId) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new PIN: ");
        String newPin = sc.nextLine();

        if (newPin == null || newPin.isEmpty()) {
            System.out.println("PIN cannot be empty or null. Please enter a valid PIN.");
            return;
        }

        String hashedPin = hash1(newPin);
        if (hashedPin == null || hashedPin.isEmpty()) {
            System.out.println("Error: Hashed PIN is invalid. Please try again.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE users SET pin = ? WHERE user_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, hashedPin);
                ps.setInt(2, userId);
                ps.executeUpdate();
                System.out.println("PIN changed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String hash1(String pin) {
        if (pin == null || pin.isEmpty()) return null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(pin.getBytes());
            BigInteger number = new BigInteger(1, hashBytes);

            // Convert the hash to base 10 and take the last 4 digits
            String hashText = number.toString(10).substring(0, 4);
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String hash(String newPin) {
        return hash1(newPin);
    }


private void notifyMinimumBalance(int userId) {
double minimumBalance = 5000.0;  // Define your minimum balance threshold

try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
    String query = "SELECT balance FROM accounts WHERE user_id = ?";
    try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance < minimumBalance) {
                    System.out.println("Alert: Your balance is below the minimum threshold of $" + minimumBalance);
                }
            }
        }
    }
} catch (SQLException e) {
    System.out.println("Error: " + e.getMessage());
}
}

private void updateMobileNumber(int userId) {
System.out.print("Enter new mobile number: ");
Scanner sc = new Scanner(System.in);
String newMobileNumber = sc.nextLine();

try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
    String query = "UPDATE users SET mobile_number = ? WHERE user_id = ?";
    try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, newMobileNumber);
        ps.setInt(2, userId);
        ps.executeUpdate();
        System.out.println("Mobile number updated successfully.");
    }
} catch (SQLException e) {
    System.out.println("Error: " + e.getMessage());
}
}private void requestChequeBook(int userId) {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        int accountId = getAccountId(userId, connection);
        logTransaction(accountId, 0, "cheque");
        System.out.println("Cheque book request submitted successfully.");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void enableInternationalTransactions(int userId) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Do you want to enable international transactions? (yes/no): ");
    String response = sc.nextLine().toLowerCase();

    boolean enable = response.equals("yes");
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String query = "UPDATE accounts SET international_transactions_enabled = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, enable);
            ps.setInt(2, userId);
            ps.executeUpdate();
            System.out.println(enable ? "International transactions enabled." : "International transactions disabled.");
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void makeWithdrawal(int userId, double amount) {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        conn.setAutoCommit(false);  // Begin transaction

        String selectBalanceSQL = "SELECT balance FROM accounts WHERE user_id = ?";
        try (PreparedStatement selectSt = conn.prepareStatement(selectBalanceSQL)) {
            selectSt.setInt(1, userId);
            ResultSet rs = selectSt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    String updateSQL = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
                    try (PreparedStatement updateSt = conn.prepareStatement(updateSQL)) {
                        updateSt.setDouble(1, amount);
                        updateSt.setInt(2, userId);
                        updateSt.executeUpdate();
                    }

                    int accountId = getAccountId(userId, conn);
                    logTransaction(accountId, amount, "withdrawal");
                    conn.commit();  // Commit transaction
                    System.out.println("Withdrawal successful. Please take your cash.");
                } else {
                    System.out.println("Insufficient balance for withdrawal.");
                    conn.rollback();  // Rollback on insufficient balance
                }
            } else {
                System.out.println("Account not found.");
                conn.rollback();  // Rollback on account not found
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void makeDeposit(int userId, double amount) {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        conn.setAutoCommit(false);  // Begin transaction

        int accountId = getAccountId(userId, conn);
        String updateSQL = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
        try (PreparedStatement updateSt = conn.prepareStatement(updateSQL)) {
            updateSt.setDouble(1, amount);
            updateSt.setInt(2, userId);
            updateSt.executeUpdate();
        }
        
        logTransaction(accountId, amount, "deposit");

        conn.commit();  // Commit transaction
        System.out.println("Deposit successful.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void cardlessCashWithdrawal(int userId) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter amount for cardless withdrawal: ");
    double amount = sc.nextDouble();

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String query = "UPDATE accounts SET balance = balance - ? WHERE user_id = ? AND balance >= ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDouble(1, amount);
        ps.setInt(2, userId);
        ps.setDouble(3, amount);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            int accountId = getAccountId(userId, connection);
            logTransaction(accountId, amount, "cardless");  // Ensure transaction type fits the constraint
            System.out.println("Cardless withdrawal successful. Please visit a nearby ATM to collect your cash.");
        } else {
            System.out.println("Insufficient balance for cardless withdrawal.");
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void miniStatement(int userId) {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String query = "SELECT * FROM transactions WHERE account_id = (SELECT account_id FROM accounts WHERE user_id = ?) ORDER BY transaction_date DESC LIMIT 10";
        PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
                System.out.println("Mini Statement:");
                while (rs.next()) {
                    System.out.println("Date: " + rs.getTimestamp("transaction_date"));
                    System.out.println("Type: " + rs.getString("transaction_type"));
                    System.out.println("Amount: " + rs.getDouble("amount"));
                    System.out.println();
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

	private int getAccountId(int userId, Connection connection) throws SQLException {
        String query = "SELECT account_id FROM accounts WHERE user_id = ?";
      PreparedStatement ps = connection.prepareStatement(query) ;
      
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("account_id");
                } else {
                    throw new SQLException("Account not found for user_id: " + userId);
                }
            }
        }
	}
