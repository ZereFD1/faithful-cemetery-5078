package com.masai.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.security.auth.login.CredentialException;

import com.masai.colors.ConsoleColors;
import com.masai.dao.BrokerDaoImpl;
import com.masai.dao.CustomerDaoImpl;
import com.masai.dbUtils.DbUtils;
import com.masai.dto.Customer;
import com.masai.dto.Stocks;
import com.masai.exception.NoRecordFoundException;
import com.masai.exception.SomeThingWrongException;

public class Main {
	public static void isUser(String username, String password, Scanner sc, CustomerDaoImpl cdi)
			throws SomeThingWrongException, NoRecordFoundException {
		Connection con = null;
		try {
			con = DbUtils.connectToDatabase();

			String insertQuery = "select * from customer where username = ? && password = ?";
			PreparedStatement ps = con.prepareStatement(insertQuery);

			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();

			if (DbUtils.isResultSetEmpty(rs)) {
				System.out.println(ConsoleColors.RED + "Wrong username or password" + ConsoleColors.RESET);
			} else {
				rs.next();
				int cid = rs.getInt(1);
				System.out.println(ConsoleColors.GREEN + "cid " + cid + ConsoleColors.RESET);
				System.out.println(ConsoleColors.GREEN + "Welcome " + username + ConsoleColors.RESET);
				UserMenu(sc, cdi, cid);
				System.exit(0);
			}

		} catch (SQLException e) {

			new SomeThingWrongException();
		} finally {
			try {
				DbUtils.closeConnection(con);
			} catch (SQLException e) {

				new SomeThingWrongException();
			}
		}

	}

	public static void isAdmin(String username, String password, Scanner sc, BrokerDaoImpl bdi)
			throws SomeThingWrongException, NoRecordFoundException, CredentialException {
		Connection con = null;
		try {
			con = DbUtils.connectToDatabase();

			String insertQuery = "select * from broker where b_username = ? && b_password = ?";

			PreparedStatement ps = con.prepareStatement(insertQuery);

			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (DbUtils.isResultSetEmpty(rs)) {
				System.out.println(ConsoleColors.RED + "Wrong username or password" + ConsoleColors.RESET);
			} else {
				System.out.println(ConsoleColors.GREEN + "Welcome " + username + ConsoleColors.RESET);
				adminMenu(sc, bdi);
				System.exit(0);

			}

		} catch (SQLException e) {

			new SomeThingWrongException();
		} finally {
			try {
				DbUtils.closeConnection(con);
			} catch (SQLException e) {

				new SomeThingWrongException();
			}
		}

	}

	static void adminLogin(Scanner sc, BrokerDaoImpl bdi)
			throws SomeThingWrongException, NoRecordFoundException, CredentialException {
		System.out.println(ConsoleColors.YELLOW + "Hi Broker Please enter Email or password" + ConsoleColors.RESET);

		System.out.println(ConsoleColors.GREEN + "Enter Email please" + ConsoleColors.RESET);
		String username = sc.next();
		System.out.println(ConsoleColors.GREEN + "Enter password please" + ConsoleColors.RESET);
		String password = sc.next();

		isAdmin(username, password, sc, bdi);
	}

	static void userLogin(Scanner sc, CustomerDaoImpl cdi) throws SomeThingWrongException, NoRecordFoundException {
		System.out.println(ConsoleColors.YELLOW + "Hi Customer Please enter Email or password" + ConsoleColors.RESET);

		System.out.println(ConsoleColors.GREEN + "Enter Email please" + ConsoleColors.RESET);
		String username = sc.next();
		System.out.println(ConsoleColors.GREEN + "Enter password please" + ConsoleColors.RESET);
		String password = sc.next();

		isUser(username, password, sc, cdi);
	}

	static void displayAdminMenu() {
		System.out.println("1. Register new customer.");
		System.out.println("2. View all the customers.");
		System.out.println("3. Add new stocks.");
		System.out.println("4. View all the stocks.");
		System.out.println("5. View consolidated report of a stock.");
		System.out.println("6. Delete customer");
		System.out.println("7. Delete stock");
		System.out.println("0. for Exit");
	}

	static void adminMenu(Scanner sc, BrokerDaoImpl bdi)
			throws SomeThingWrongException, NoRecordFoundException, CredentialException {
		int choice = 0;
		do {
			displayAdminMenu();
			System.out.print(ConsoleColors.GREEN + "Enter selection " + ConsoleColors.RESET);
			choice = sc.nextInt();
			switch (choice) {
			case 0:
				System.out.println(ConsoleColors.RED + "Bye Bye broker" + ConsoleColors.RESET);
				break;
			case 1:

				Customer cust = new Customer();
				System.out.println("Enter customer id");
				cust.setId(sc.nextInt());
				System.out.println("Enter username of customer");
				cust.setUsername(sc.next());
				System.out.println("Enter customer password");
				cust.setPassword(sc.next());
				cust.setStatus(true);
				cust.setWallet(500);

				bdi.registerCustomer(cust);
				break;
			case 2:
				System.out.println(bdi.viewCustomers());
				break;
			case 3:
				Stocks stk = new Stocks();

				System.out.println("Enter stock id");

				stk.setId(sc.nextInt());
				System.out.println("Enter stock name");
				stk.setName(sc.next());
				stk.setQuantity(500);
				System.out.println("Enter per stock amount");
				stk.setPrice(sc.nextInt());
				stk.setTotalQuantity(500);
				bdi.addStock(stk);
				break;
			case 4:

				break;
			case 5:
				System.out.println("Enter stock id to view its report");
				bdi.StockReport(sc.nextInt());
				break;
			case 6:

				break;
			case 7:

				break;
			default:
				System.out.println("Invalid Selection, try again");
			}
		} while (choice != 0);
	}

	static void displayUserMenu() {
		System.out.println("1. View All Stocks.");
		System.out.println("2. Buy Stock.");
		System.out.println("3. Sell stocks.");
		System.out.println("4. View Transaction History.");
		System.out.println("0. for Exit");
	}

	static void UserMenu(Scanner sc, CustomerDaoImpl cdi, int cid)
			throws SomeThingWrongException, NoRecordFoundException {
		int choice = 0;
		do {
			displayUserMenu();
			System.out.print(ConsoleColors.GREEN + "Enter selection " + ConsoleColors.RESET);
			choice = sc.nextInt();
			switch (choice) {
			case 0:
				System.out.println(ConsoleColors.RED + "Bye Bye User" + ConsoleColors.RESET);
				break;
			case 1:
				System.out.println(cdi.viewStocks());
				break;
			case 2:
				System.out.println(ConsoleColors.BLUE + "Enter stockid of stock you want to buy" + ConsoleColors.RESET);
				int stock_id = sc.nextInt();
				System.out.println(ConsoleColors.BLUE + "Enter stock quantity you want to buy" + ConsoleColors.RESET);
				int buyquantity = sc.nextInt();
				cdi.buyStock(cid, stock_id, buyquantity);
				break;
			case 3:

				System.out
						.println(ConsoleColors.BLUE + "Enter stockid of stock you want to sell" + ConsoleColors.RESET);
				int sid = sc.nextInt();
				System.out.println(ConsoleColors.BLUE + "Enter stock quantity you want to sell" + ConsoleColors.RESET);
				int sellquantity = sc.nextInt();
				cdi.sellStock(cid, sid, sellquantity);
				break;
			case 4:
				cdi.viewTransactionHistory(cid);
				break;
			default:
				System.out.println(ConsoleColors.RED + "Invalid Selection, try again" + ConsoleColors.RESET);
			}
		} while (choice != 0);
	}

	public static void main(String[] args) throws SomeThingWrongException, NoRecordFoundException, CredentialException {

		Scanner sc = new Scanner(System.in);
		BrokerDaoImpl bdi = new BrokerDaoImpl();
		CustomerDaoImpl cdi = new CustomerDaoImpl();

		int choice = 0;
		do {
			System.out
					.println(ConsoleColors.GREEN + "1. Broker Login\n2. Customer Login\n0. Exit" + ConsoleColors.RESET);
			choice = sc.nextInt();
			switch (choice) {
			case 0:
				System.out.println(ConsoleColors.GREEN + "Thank you, Visit again" + ConsoleColors.RESET);
				break;
			case 1:
				adminLogin(sc, bdi);
				adminMenu(sc, bdi);
				break;
			case 2:
				userLogin(sc, cdi);
				break;
			default:
				System.out.println(ConsoleColors.GREEN + "Invalid Selection, try again" + ConsoleColors.RESET);
			}
		} while (choice != 0);
		sc.close();

	}

}
