package cz.muni.fi.pv168.library;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
//import org.apache.derby.jdbc.EmbeddedDataSource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Lenka (433591)
 * @version 18.03.2016
 */
public class CustomerManagerImplTest {

    private CustomerManager customerManager;
    private DataSource dataSource;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        //dataSource = prepareDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE CUSTOMER ("
                    + "id bigint primary key generated always as identity,"
                    + "customername VARCHAR(20) ,"
                    + "surname VARCHAR(30) ,"
                    + "phoneNumber VARCHAR(20) ,"
                    + "address varchar(80))").executeUpdate();
        }
        customerManager = new CustomerManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE CUSTOMER").executeUpdate();
        }
    }

    @Test
    public void testCreateCustomer() throws Exception {

        Customer customer = newCustomer("Lenka", "Janeckova", "777555666", "SR");
        customerManager.createCustomer(customer);
        Long customerId = customer.getId();
        assertNotNull(customerId);
        Customer result = customerManager.findCustomerById(customerId);

        assertNotNull("loaded customer is null", result);
        assertNotNull("saved customer has null ID", result.getId());
        assertThat("loaded customer differs from the saved one", result, is(equalTo(customer)));
        assertThat("loaded customer is the same instance", result, is(not(sameInstance(customer))));
        assertDeepEquals(customer, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCustomerWithNull() throws Exception {
        customerManager.createCustomer(null);
    }

    @Test
    public void testUpdateCustomer() throws Exception {

        Customer customer = newCustomer("Lenka", "Janeckova", "777555666", "SR");
        Customer customer1 = newCustomer("Janko", "Mrkvicka", "777555666", "SR");
        Long customerID = customer.getId();

        customerManager.createCustomer(customer);
        customerManager.createCustomer(customer1);
        customer.setName("Marienka");
        customerManager.updateCustomer(customer);
        customer = customerManager.findCustomerById(customerID);

        assertNotNull(customer);
        assertThat("name value wasn't changed correctly", customer.getName(), is(equalTo("Marienka")));
        assertThat("surname value was changed when changing name", customer.getSurname(), is(equalTo("Janeckova")));
        assertThat("phoneNumber value was changed when changing name", customer.getPhoneNumber(), is(equalTo("777555666")));
        assertThat("address value was changed when changing name", customer.getAddress(), is(equalTo("SR")));

        customer.setSurname("Novakova");
        customerManager.updateCustomer(customer);
        customer = customerManager.findCustomerById(customerID);

        assertNotNull(customer);
        assertThat("phoneNumber value was changed when changing surname", customer.getPhoneNumber(), is(equalTo("777555666")));
        assertThat("name value was changed when changing surname", customer.getName(), is(equalTo("Marienka")));
        assertThat("surname value wasn't changed correctly", customer.getSurname(), is(equalTo("Novakova")));
        assertThat("address value was changed when changing surname", customer.getAddress(), is(equalTo("SR")));
        
        customer.setPhoneNumber("444333222");
        customerManager.updateCustomer(customer);
        customer = customerManager.findCustomerById(customerID);

        assertNotNull(customer);
        assertThat("surname value was changed when changing phoneNumber", customer.getSurname(), is(equalTo("Novakova")));
        assertThat("name value was changed when changing phoneNumber", customer.getName(), is(equalTo("Marienka")));
        assertThat("phoneNumber value wasn't changed correctly", customer.getPhoneNumber(), is(equalTo("444333222")));
        assertThat("address value was changed when changing phoneNumber", customer.getAddress(), is(equalTo("SR")));

        customer.setAddress("CZ");
        customerManager.updateCustomer(customer);
        customer = customerManager.findCustomerById(customerID);
        
        assertNotNull(customer);
        assertThat("surname value was changed when changing address", customer.getSurname(), is(equalTo("Novakova")));
        assertThat("name value was changed when changing address", customer.getName(), is(equalTo("Marienka")));
        assertThat("address value wasn't changed correctly", customer.getAddress(), is(equalTo("CZ")));
        assertThat("phoneNumber value was changed when changing address", customer.getPhoneNumber(), is(equalTo("444333222")));
        
        assertDeepEquals(customer1, customerManager.findCustomerById(customer1.getId()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testUpdateCustomerWithNull() {

        customerManager.updateCustomer(null);
    }

    @Test
    public void testUpdateCustomerWithNonexistingId() throws Exception {
        Customer customer = newCustomer("Janko", "Mrkvicka", "777555666", "SR");
        customerManager.createCustomer(customer);
        customer.setId(customer.getId() + 1);
        expectedException.expect(EntityNotFoundException.class);
        customerManager.updateCustomer(customer);
    }


    @Test
    public void testDeleteCustomer() throws Exception {

        Customer customer = newCustomer("Lenka", "Janeckova", "777555666", "SR");
        Customer customer1 = newCustomer("Janko", "Mrkvicka", "777555666", "SR");

        customerManager.createCustomer(customer);
        customerManager.createCustomer(customer1);

        assertNotNull(customerManager.findCustomerById(customer.getId()));
        assertNotNull(customerManager.findCustomerById(customer1.getId()));
        assertEquals(2, customerManager.findAllCustomers().size());

        customerManager.deleteCustomer(customer);
        assertEquals(1, customerManager.findAllCustomers().size());
        assertNull(customerManager.findCustomerById(customer.getId()));
        assertNotNull(customerManager.findCustomerById(customer1.getId()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDeleteCustomerWithNull() {

        customerManager.deleteCustomer(null);
    }

    @Test
    public void testDeleteCustomerWithNonexistingId() throws Exception {
        Customer customer = newCustomer("Janko", "Mrkvicka", "777555666", "SR");
        customerManager.createCustomer(customer);
        customer.setId(customer.getId() + 1);
        expectedException.expect(EntityNotFoundException.class);
        customerManager.deleteCustomer(customer);
    }

    @Test
    public void testFindCustomerById() throws Exception {

        Customer customer = newCustomer("Janko", "Mrkvicka", "777555666", "SR");
        customerManager.createCustomer(customer);

        Customer result = customerManager.findCustomerById(customer.getId());

        assertNotNull(result);
        assertDeepEquals(customer, result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindCustomerByNullId() {

        customerManager.findCustomerById(null);
    }

    @Test
    public void testFindCustomersByName() throws Exception {

        Customer customer = newCustomer("Janko", "Mrkvicka", "777555666", "SR");
        Customer customer1 = newCustomer("Janko", "Velky", "888556666", "SR");
        Customer customer2 = newCustomer("Ferko", "Maly", "774355666", "SR");

        customerManager.createCustomer(customer);
        customerManager.createCustomer(customer1);
        customerManager.createCustomer(customer2);

        List<Customer> result = customerManager.findCustomersByName("Janko");

        assertNotNull(result);
        assertEquals("loaded list contains more or fewer values", 2, result.size());
        assertNotNull("loaded list contains null value", result.get(0));
        assertNotNull("loaded list contains null value", result.get(1));
        assertTrue("loaded list doesn't contain saved customer", result.contains(customer));
        assertTrue("loaded list doesn't contain saved customer", result.contains(customer1));

        result = customerManager.findCustomersByName("Ferko");
        assertNotNull(result);
        assertEquals("loaded list should be empty", 0, result.size());
        
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindCustomerByNullName() {

        customerManager.findCustomersByName(null);
    }

    @Test
    public void testFindAllCustomers() throws Exception {

        Customer customer = newCustomer("Janko", "Mrkvicka", "777555666", "SR");
        Customer customer1 = newCustomer("Janko", "Velky", "888556666", "SR");
        Customer customer2 = newCustomer("Ferko", "Maly", "774355666", "SR");

        customerManager.createCustomer(customer);
        customerManager.createCustomer(customer1);
        customerManager.createCustomer(customer2);

        List<Customer> result = customerManager.findAllCustomers();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(customer));
        assertTrue(result.contains(customer1));
        assertTrue(result.contains(customer2));
    }
    
    private void assertDeepEquals(Customer expected, Customer actual) {
        assertEquals("id value is not equal",expected.getId(), actual.getId());
        assertEquals("name value is not equal",expected.getName(), actual.getName());
        assertEquals("surname value is not equal",expected.getSurname(), actual.getSurname());
        assertEquals("phoneNumber value is not equal",expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals("address value is not equal",expected.getAddress(), actual.getAddress());
    }
    
    private Customer newCustomer(String name, String surname, String phoneNumber, String address) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        return customer;
    }

//    private static DataSource prepareDataSource() throws SQLException {
//        EmbeddedDataSource ds = new EmbeddedDataSource();
//        //we will use in memory database
//        ds.setDatabaseName("memory:gravemgr-test");
//        ds.setCreateDatabase("create");
//        return ds;
//    }
}