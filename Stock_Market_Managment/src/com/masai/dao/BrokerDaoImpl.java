package com.masai.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.CredentialException;

import com.masai.dbUtils.DbUtils;
import com.masai.dto.Customer;
import com.masai.dto.Stocks;
import com.masai.exception.NoRecordFoundException;
import com.masai.exception.SomeThingWrongException;
import com.masai.security.EmailValidation;
import com.masai.security.HashingPassword;

/**
 * @author Harsh
 *
 *This Code registers the customer
 *Exception Handled
 */

/**
 * @author Harsh
 *
 */
public class BrokerDaoImpl implements BrokerDao {

	EmailValidation emailValidation = new EmailValidation();
	HashingPassword hashingPassword = new HashingPassword();

	@Override
	public void registerCustomer(Customer cust) throws SomeThingWrongException, CredentialException {
		Connection connection = null;
		cust.setPassword(hashingPassword.hashingAlgorithem(cust.getPassword()));
		if (emailValidation.emailValidation(cust.getUsername()) == "Invalid") {
			throw new CredentialException("Wrong email address plese provide right syntax of email.");
		}
		try {
			// connect to database
			connection = DbUtils.connectToDatabase();
			// prepare the query
			String INSERT_QUERY = "INSERT INTO customer (cid, username, password, status, wallet) VALUES (?, ?, ?, ?, ?)";

			// get the prepared statement object
			PreparedStatement ps = connection.prepareStatement(INSERT_QUERY);

			// stuff the data in the query
			ps.setInt(1, cust.getId());
			ps.setString(2, cust.getUsername());
			ps.setString(3, cust.getPassword());
			ps.setBoolean(4, cust.isStatus());
			ps.setInt(5, cust.getWallet());

			// execute query
			ps.executeUpdate();
		} catch (SQLException sqlEx) {
			// code to log the error in the file
			throw new SomeThingWrongException();
		} finally {
			try {
				// close the exception
				DbUtils.closeConnection(connection);
			} catch (SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}

	}

	/**
	 * @author Harsh
	 *
	 *         Connection provided to database Code for view the list of customers
	 */

	@Override
	public List<Customer> viewCustomers() throws SomeThingWrongException, NoRecordFoundException {
		Connection connection = null;
		List<Customer> list = new ArrayList<>();
		try {
			// connect to database
			connection = DbUtils.connectToDatabase();
			// prepare the query
			String SELECT_QUERY = "SELECT * FROM customer";

			// get the prepared statement object
			PreparedStatement ps = connection.prepareStatement(SELECT_QUERY);

			// execute query
			ResultSet resultSet = ps.executeQuery();

			// check if result set is empty
			if (DbUtils.isResultSetEmpty(resultSet)) {
				throw new NoRecordFoundException("No Customer Found");
			}

			while (resultSet.next()) {
				Customer cust = new Customer();

				cust.setId(resultSet.getInt(1));
				cust.setUsername(resultSet.getString(2));
				cust.setPassword(resultSet.getString(3));
				cust.setStatus(resultSet.getBoolean(4));
				cust.setWallet(resultSet.getInt(5));
				list.add(cust);
			}

		} catch (SQLException sqlEx) {
			// code to log the error in the file
			throw new SomeThingWrongException();
		} finally {
			try {
				// close the exception
				DbUtils.closeConnection(connection);
			} catch (SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}
		return list;
	}

	/**
	 * @author Harsh Add the details and data of stocks
	 */

	@Override
	public void addStock(Stocks stk) throws SomeThingWrongException {
		Connection connection = null;
		try {
			// connect to database
			connection = DbUtils.connectToDatabase();
			// prepare the query
			String INSERT_QUERY = "INSERT INTO stocks (sid, stock_name, quantity, price_per_stock, total_initial_quantity) VALUES (?, ?, ?, ?, ?)";

			// get the prepared statement object
			PreparedStatement ps = connection.prepareStatement(INSERT_QUERY);

			// stuff the data in the query
			ps.setInt(1, stk.getId());
			ps.setString(2, stk.getName());
			ps.setInt(3, stk.getQuantity());
			ps.setInt(4, stk.getPrice());
			ps.setInt(5, stk.getTotalQuantity());

			// execute query
			ps.executeUpdate();
		} catch (SQLException sqlEx) {
			// code to log the error in the file
			throw new SomeThingWrongException();
		} finally {
			try {
				// close the exception
				DbUtils.closeConnection(connection);
			} catch (SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}

	}

	@Override
	public void StockReport(int stockID) throws SomeThingWrongException, NoRecordFoundException {
		Connection connection = null;
		try {
			// connect to database
			connection = DbUtils.connectToDatabase();
			// prepare the query
			String SELECT_QUERY = "SELECT * FROM stocks where sid = '" + stockID + "'";

			// get the prepared statement object
			PreparedStatement ps = connection.prepareStatement(SELECT_QUERY);

			// execute query
			ResultSet resultSet = ps.executeQuery();

			// check if result set is empty
			if (DbUtils.isResultSetEmpty(resultSet)) {
				throw new NoRecordFoundException("No Stock Found");
			}

			while (resultSet.next()) {

				String stkName = resultSet.getString(2);
				int quanityLeft = resultSet.getInt(3);
				int totalQuanity = resultSet.getInt(5);

				System.out.println("Stock name = " + stkName + " Stock sold = " + (totalQuanity - quanityLeft)
						+ " Stock available = " + quanityLeft);

			}

		} catch (SQLException sqlEx) {
			// code to log the error in the file
			throw new SomeThingWrongException();
		} finally {
			try {
				// close the exception
				DbUtils.closeConnection(connection);
			} catch (SQLException sqlEX) {
				throw new SomeThingWrongException();
			}
		}

	}

}
