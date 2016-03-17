package cz.muni.fi.pv168.library;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public class CustomerManagerImpl implements CustomerManager {

    private final DataSource dataSource;

    public CustomerManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createCustomer(Customer customer) throws ServiceFailureException {

        validate(customer);
        if (customer.getId() != null) {
            throw new IllegalArgumentException("customer id is already set");
        }

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("INSERT INTO CUSTOMER (name,surname,phoneNumber,address)" +
                    " VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                st.setString(1, customer.getName());
                st.setString(2, customer.getSurname());
                st.setString(3, customer.getPhoneNumber());
                st.setString(4, customer.getAddress());
                int addedRows = st.executeUpdate();
                if (addedRows != 1) {
                    throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert customer " + customer);
                }
                ResultSet keyRS = st.getGeneratedKeys();
                customer.setId(getKey(keyRS, customer));
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting a customer", ex);
        }

    }

    public void updateCustomer(Customer customer) throws ServiceFailureException {

        validate(customer);
        if (customer.getId() == null) {
            throw new IllegalArgumentException("customer id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "UPDATE Customer SET name = ?, surname = ?, capacity = ?, note = ? WHERE id = ?")) {

            st.setString(1, customer.getName());
            st.setString(2, customer.getSurname());
            st.setString(3, customer.getPhoneNumber());
            st.setString(4, customer.getAddress());
            st.setLong(5, customer.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Customer " + customer + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating customer " + customer, ex);
        }
    }



    @Override
    public void deleteCustomer(Customer customer) throws ServiceFailureException {

        validate(customer);
        if (customer.getId() == null) {
            throw new IllegalArgumentException("customer id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "DELETE FROM customer WHERE id = ?")) {

            st.setLong(1, customer.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Customer " + customer + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating customer " + customer, ex);
        }
    }

    public Customer findCustomerById(Long id) throws ServiceFailureException {

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,name,surname,phoneNumber,address FROM customer WHERE id = ?")) {

            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Customer customer = resultSetToCustomer(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                                    + "(source id: " + id + ", found " + customer + " and " + resultSetToCustomer(rs));
                }

                return customer;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving customer with id " + id, ex);
        }
        
       
    }

    public List<Customer> findCustomersByName(String name) throws ServiceFailureException {

        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id, name, surname, phoneNumber, address FROM customer WHERE name = ?");
                ) {

            st.setString(1, name);
            ResultSet rs = st.executeQuery();

            List<Customer> result = new ArrayList<>();

            while (rs.next()) {
                result.add(resultSetToCustomer(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ServiceFailureException("Error when retrieving customers with name " + name, e);
        }

    }

    @Override
    public List<Customer> findAllCustomers() throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,name,surname,phoneNumber,address FROM customer")) {

            ResultSet rs = st.executeQuery();

            List<Customer> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToCustomer(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all customers", ex);
        }
    }

    private void validate(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer is null");
        }
    }

    private Customer resultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setName(rs.getString("name"));
        customer.setSurname(rs.getString("surname"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        customer.setAddress(rs.getString("address"));
        return customer;
    }

    private Long getKey(ResultSet keyRS, Customer customer) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert customer " + customer
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert customer " + customer
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert customer " + customer
                    + " - no key found");
        }
    }
    
}
