package cz.muni.fi.pv168.library;

import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
//import org.apache.derby.jdbc.EmbeddedDataSource;

import static org.junit.Assert.*;

/**
 * @author Lenka (433591)
 * @version 18.03.2016
 */
public class CustomerManagerImplTest {

    CustomerManager customerManager;

    @Before
    public void setUp() throws Exception {
        //customerManager = new CustomerManagerImpl();
    }

    @Test
    public void testCreateCustomer() throws Exception {
        
    }

    @Test
    public void testUpdateCustomer() throws Exception {

    }

    @Test
    public void testDeleteCustomer() throws Exception {

    }

    @Test
    public void testFindCustomerById() throws Exception {

    }

    @Test
    public void testFindCustomersByName() throws Exception {

    }

    @Test
    public void testFindAllCustomers() throws Exception {

    }

//    private static DataSource prepareDataSource() throws SQLException {
//        EmbeddedDataSource ds = new EmbeddedDataSource();
//        //we will use in memory database
//        ds.setDatabaseName("memory:gravemgr-test");
//        ds.setCreateDatabase("create");
//        return ds;
//    }
}