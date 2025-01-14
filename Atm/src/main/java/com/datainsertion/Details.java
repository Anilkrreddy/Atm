package com.datainsertion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Details {
    private static String url = "jdbc:postgresql://localhost:5432/atmmachine";
    private static String user = "postgres";
    private static String password = "123";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement st1 = conn.createStatement();

            // Add user insert statements to batch
          /*  st1.addBatch("INSERT INTO users (username, password, card_number, pin, mobile_number) VALUES "
                    + "('ANIL_Reddy', '123@', '1234-5678-9012-3456', '1234', '9876545610');");
            st1.addBatch("INSERT INTO users (username, password, card_number, pin, mobile_number) VALUES "
                    + "('mohanty', '456@', '4545-5678-9012-3456', '5678', '9876543210');");
            st1.addBatch("INSERT INTO users (username, password, card_number, pin, mobile_number) VALUES "
                    + "('krishna', '789@', '1234-8545-9012-3456', '9874', '9874123210');");
            st1.addBatch("INSERT INTO users (username, password, card_number, pin, mobile_number) VALUES "
                    + "('prasanna_kumar', '987@', '1234-9464-9012-3456', '6451', '9876849210');");

            // Add account insert statements to batch
            st1.addBatch("INSERT INTO accounts (user_id, balance) VALUES (1, 1000000);");
            st1.addBatch("INSERT INTO accounts (user_id, balance) VALUES (2, 10000);");
            st1.addBatch("INSERT INTO accounts (user_id, balance) VALUES (3, 156470);");
            st1.addBatch("INSERT INTO accounts (user_id, balance) VALUES (4, 10840);");

            // Add transaction insert statements to batch
            st1.addBatch("INSERT INTO transactions (account_id, amount, transaction_type) VALUES (1, 500000, 'deposit');");
            st1.addBatch("INSERT INTO transactions (account_id, amount, transaction_type) VALUES (1, 200000, 'withdrawal');");
            st1.addBatch("INSERT INTO transactions (account_id, amount, transaction_type) VALUES (2, 25000, 'deposit');");
            st1.addBatch("INSERT INTO transactions (account_id, amount, transaction_type) VALUES (3, 47450, 'withdrawal');");
            st1.addBatch("INSERT INTO transactions (account_id, amount, transaction_type) VALUES (4, 5000, 'deposit');");
*/st1.addBatch("ALTER TABLE transactions"
		+ "    DROP CONSTRAINT transactions_transaction_type_check,\r\n"
		+ "    ADD CONSTRAINT transactions_transaction_type_check\r\n"
		+ "        CHECK (transaction_type IN ('deposit', 'withdrawal', 'cardless', 'cheque'));\r\n"
		+ "");
            // Execute the batch
            st1.executeBatch();

            System.out.println("Data inserted successfully using batch execution.");

            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
