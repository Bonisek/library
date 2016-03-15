package cz.muni.fi.pv168.library;

import java.sql.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public class CustomerManagerImpl implements CustomerManager {

    final static Logger log = LoggerFactory.getLogger(CustomerManagerImpl.class);

    private final DataSource dataSource;

    public CustomerManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createCustomer(Customer customer) throws ServiceFailureException {

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
            log.error("db connection problem", ex);
            throw new ServiceFailureException("Error when retrieving all customers", ex);
        }

    }

    public void updateCustomer(Customer customer) {

    }

    public void deleteCustomer(Customer customer) {

    }

    public Customer findCustomerById(Long id) {
        return null;
    }

    public List<Customer> findCustomersByName(String name) {
        return null;
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
