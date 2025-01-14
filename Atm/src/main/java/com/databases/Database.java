package com.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Database {
    private static String url = "jdbc:postgresql://localhost:5432/atmmachine";
    private static String user = "postgres";
    private static String password = "123";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("connected");

         /*   String createUsersTableSQL = "CREATE TABLE users ("
                    + "user_id SERIAL PRIMARY KEY,"
                    + "username VARCHAR(50) NOT NULL,"
                    + "password VARCHAR(50) NOT NULL,"
                    + "card_number VARCHAR(50) NOT NULL,"
                    + "pin VARCHAR(4) NOT NULL,"
                    + "mobile_number VARCHAR(10) NOT NULL)";

            String createAccountsTableSQL = "CREATE TABLE accounts ("
                    + "account_id SERIAL PRIMARY KEY,"
                    + "user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,"
                    + "balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,"
                    + "international_transactions_enabled BOOLEAN DEFAULT FALSE,"
                    + "CHECK(balance >= 0))";

            String createTransactionsTableSQL = "CREATE TABLE transactions ("
                    + "transaction_id SERIAL PRIMARY KEY,"
                    + "account_id INTEGER REFERENCES accounts(account_id),"
                    + "transaction_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,"
                    + "amount DECIMAL(15,2) NOT NULL,"
                    + "transaction_type VARCHAR(10) CHECK (transaction_type IN ('deposit', 'withdrawal', 'cheque', 'cardless'))"
                    + ");";

           Statement st = conn.createStatement();
            st.executeUpdate(createUsersTableSQL);
            System.out.println("Users table created successfully.");
            st.executeUpdate(createAccountsTableSQL);
            System.out.println("Accounts table created successfully.");
            st.executeUpdate(createTransactionsTableSQL);
            System.out.println("Transactions table created successfully.");*/
            Statement st1 = conn.createStatement();
            // Delete related records first to avoid foreign key constraint violation
            String deleteAccountsSQL = "DELETE FROM accounts WHERE user_id = 1";
            st1.executeUpdate(deleteAccountsSQL);

            String deleteUsersSQL = "DELETE FROM users WHERE username = 'ANIL_Reddy'";
            st1.executeUpdate(deleteUsersSQL);
            System.out.println("User 'ANIL_Reddy' deleted successfully.");

            String insertSQL = "INSERT INTO users (username, password, card_number, pin, mobile_number) VALUES"
                    + "('Anil_reddy', '123@', '1234-3214-3232-6798', '3242', '7897897895')";
            st1.executeUpdate(insertSQL);
            System.out.println("User 'Anil_reddy' inserted successfully.");

            conn.close();
            System.out.println("disconnected!!!");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
