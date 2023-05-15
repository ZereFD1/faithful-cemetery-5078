package com.masai.dao;

import java.util.List;

import javax.security.auth.login.CredentialException;

import com.masai.dto.Customer;
import com.masai.dto.Stocks;
import com.masai.exception.NoRecordFoundException;
import com.masai.exception.SomeThingWrongException;

interface BrokerDao {

	void registerCustomer(Customer cust) throws SomeThingWrongException, CredentialException;

	List<Customer> viewCustomers() throws SomeThingWrongException, NoRecordFoundException;

	void addStock(Stocks stk) throws SomeThingWrongException;

	void StockReport(int stockID) throws SomeThingWrongException, NoRecordFoundException;

}
