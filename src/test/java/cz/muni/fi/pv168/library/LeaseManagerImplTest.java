package cz.muni.fi.pv168.library;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;

import static org.junit.Assert.*;

/**
 *
 * @author Kristian Mateka
 */
public class LeaseManagerImplTest {

    private LeaseManager leaseManager;
    private DataSource dataSource;
    
    @Rule
    ExpectedException expectedException = ExpectedException.none();
    

    @Before
    public void setUp() throws Exception {
        //TODO references
        //dataSource = prepareDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE LEASE ("
                    + "id bigint primary key generated always as identity,"
                    + "bookId REFERENCES ,"
                    + "author VARCHAR(40) ,"
                    + "genre VARCHAR(20))").executeUpdate();
        }
        leaseManager = new LeaseManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE LEASE").executeUpdate();
        }
    }

    /**
     * Test of createLease method, of class LeaseManagerImpl.
     */
    @Test
    public void testCreateLease() {
        Customer customer = new Customer();
        Book book = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2010-12-31");
            Lease lease = newLease(book, customer, start, end);
            leaseManager.createLease(lease);

            assertNotNull(lease.getId());

            Lease lease1 = leaseManager.findLeaseById(lease.getId());
            assertNotNull("id is null", lease1.getId());
            assertNotNull("returned book is null", lease1.getBook());
            assertNotNull("returned customer is null", lease1.getCustomer());
            assertNotNull("returned end time is null", lease1.getEndTime());
            assertNotNull("returned start time is null", lease1.getStartTime());

            assertEquals(lease1.getBook(), book);
            assertEquals(lease1.getCustomer(), customer);
            assertEquals(lease1.getEndTime(), end);
            assertEquals(lease1.getStartTime(), start);

        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCreateLeaseWithNull() {
        leaseManager.createLease(null);
    }

    /**
     * Test of updateLease method, of class LeaseManagerImpl.
     */
    @Test
    public void testUpdateLease() {
        Customer customer = new Customer();
        customer.setName("Lenka");
        Book book = new Book();
        book.setName("Food");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2010-12-31");
            Lease lease = newLease(book, customer, start, end);
            leaseManager.createLease(lease);

            book.setName("Universe");
            customer.setAddress("Slovakia");
            start = sdf.parse("2012-05-19");
            end = sdf.parse("2012-06-20");
            lease.setCustomer(customer);
            lease.setBook(book);
            lease.setEndTime(end);
            lease.setStartTime(start);

            leaseManager.updateLease(lease);

            assertNotNull(lease.getId());

            Lease lease1 = leaseManager.findLeaseById(lease.getId());
            assertNotNull("id is null", lease1.getId());
            assertNotNull("returned book is null", lease1.getBook());
            assertNotNull("returned customer is null", lease1.getCustomer());
            assertNotNull("returned end time is null", lease1.getEndTime());
            assertNotNull("returned start time is null", lease1.getStartTime());

            assertEquals(lease1.getBook(), book);
            assertEquals(lease1.getCustomer(), customer);
            assertEquals(lease1.getEndTime(), end);
            assertEquals(lease1.getStartTime(), start);

        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testUpdateLeaseWithNull() {
        leaseManager.updateLease(null);
    }

    @Test
    public void testUpdateLeaseWithNonexistingId() throws Exception {
        Customer customer = new Customer();
        Book book = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2010-12-31");
            Lease lease = newLease(book, customer, start, end);
            leaseManager.createLease(lease);
            lease.setId(lease.getId() + 1);
            expectedException.expect(EntityNotFoundException.class);
            leaseManager.updateLease(lease);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test of deleteLease method, of class LeaseManagerImpl.
     */
    @Test
    public void testDeleteLease() {
        Customer customer = new Customer();
        Customer customer1 = new Customer();
        Book book = new Book();
        Book book1 = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease lease = newLease(book, customer, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease lease1 = newLease(book1, customer1, start, end);

            leaseManager.createLease(lease);
            leaseManager.createLease(lease1);

            List<Lease> leases = leaseManager.findAllLeases();
            assertNotNull(leases);
            assertEquals(leases.size(), 2);

            leaseManager.deleteLease(lease);

            leases = leaseManager.findAllLeases();
            assertNotNull(leases);
            assertEquals(leases.size(), 1);
            assertFalse(leases.contains(lease));
            assertTrue(leases.contains(lease1));

        }catch(ParseException ex){
            ex.printStackTrace();
        }

    }

    @Test (expected = IllegalArgumentException.class)
    public void testDeleteLeaseWithNull() {
        leaseManager.deleteLease(null);
    }

    public void testDeleteLeaseWithNonexistingId() throws Exception {
        Customer customer = new Customer();
        Book book = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2010-12-31");
            Lease lease = newLease(book, customer, start, end);
            leaseManager.createLease(lease);
            lease.setId(lease.getId() + 1);
            expectedException.expect(EntityNotFoundException.class);
            leaseManager.deleteLease(lease);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test of findLeasesByBook method, of class LeaseManagerImpl.
     */
    @Test
    public void testFindLeasesByBook() {
        Customer customer = new Customer();
        Customer customer1 = new Customer();
        Book book = new Book();
        Book book1 = new Book();
        Book book2 = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease lease = newLease(book, customer, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease lease1 = newLease(book1, customer1, start, end);

            leaseManager.createLease(lease);
            leaseManager.createLease(lease1);

            List<Lease> leases = leaseManager.findLeasesByBook(book1);
            assertNotNull(leases);
            assertEquals(leases.size(), 1);
            leases = leaseManager.findLeasesByBook(book2);
            assertNotNull(leases);
            assertEquals(leases.size(), 0);

        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindLeasesByNullBook() {
        leaseManager.findLeasesByBook(null);
    }

    /**
     * Test of findAllLeases method, of class LeaseManagerImpl.
     */
    @Test
    public void testFindAllLeases() {
        Customer customer = new Customer();
        Customer customer1 = new Customer();
        Book book = new Book();
        Book book1 = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease lease = newLease(book, customer, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease lease1 = newLease(book1, customer1, start, end);

            leaseManager.createLease(lease);
            leaseManager.createLease(lease1);

            List<Lease> leases = leaseManager.findAllLeases();
            assertNotNull(leases);
            assertEquals(leases.size(), 2);
            assertTrue(leases.contains(lease));
            assertTrue(leases.contains(lease1));

        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Test of findLeasesByCustomer method, of class LeaseManagerImpl.
     */
    @Test
    public void testFindLeasesByCustomer() {
        Customer customer = new Customer();
        Customer customer1 = new Customer();
        Book book = new Book();
        Book book1 = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease lease = newLease(book, customer, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease lease1 = newLease(book1, customer, start, end);

            leaseManager.createLease(lease);
            leaseManager.createLease(lease1);

            List<Lease> leases = leaseManager.findLeasesByCustomer(customer);
            assertNotNull(leases);
            assertEquals(leases.size(), 2);
            leases = leaseManager.findLeasesByCustomer(customer1);
            assertNotNull(leases);
            assertEquals(leases.size(), 0);

        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindLeasesByNullCustomer() {
        leaseManager.findLeasesByCustomer(null);
    }

    @Test
    public void testFindLeaseById() {
        Customer customer = new Customer();
        Book book = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease lease = newLease(book, customer, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease lease1 = newLease(book, customer, start, end);

            leaseManager.createLease(lease);
            leaseManager.createLease(lease1);

            assertNotNull(lease.getId());
            Lease lease2 = leaseManager.findLeaseById(lease.getId());
            assertNotNull(lease2);
            assertEquals(lease2, lease);

        }catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindLeaseByNullId() {
        leaseManager.findLeaseById(null);
    }

    private static Lease newLease(Book book, Customer customer, Date start, Date end){
        Lease lease = new Lease();
        lease.setBook(book);
        lease.setCustomer(customer);
        lease.setEndTime(end);
        lease.setStartTime(start);

        return lease;
    }

    //private static DataSource prepareDataSource() throws SQLException {
//        EmbeddedDataSource ds = new EmbeddedDataSource();
//        //we will use in memory database
//        ds.setDatabaseName("memory:gravemgr-test");
//        ds.setCreateDatabase("create");
//        return ds;
//    }
}
