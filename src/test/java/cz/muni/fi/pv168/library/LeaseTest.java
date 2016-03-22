package cz.muni.fi.pv168.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kristian Mateka
 */
public class LeaseTest {

    private Lease lease;

    @Before
    public void setUp() {
        lease = new Lease();
    }

    /**
     * Test of getBook method, of class Lease.
     */
    @Test
    public void testGetBook() {
        Book book = new Book();
        book.setId(1L);
        lease.setBook(book);

        assertNotNull(lease.getBook());
        assertEquals(lease.getBook(), book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullBook() throws Exception {
        lease.setBook(null);
    }

    /**
     * Test of getCustomer method, of class Lease.
     */
    @Test
    public void testGetCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        lease.setCustomer(customer);

        assertNotNull(lease.getCustomer());
        assertEquals(lease.getCustomer(), customer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullCustomer() throws Exception {
        lease.setCustomer(null);
    }

    @Test
    public void testGetStartTime() {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2009-12-31");
            lease.setStartTime(date);

            assertNotNull(lease.getStartTime());
            assertEquals(lease.getStartTime(), date);
        }
        catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullStartTime() throws Exception {
        lease.setStartTime(null);
    }

    @Test
    public void testGetEndTime() {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2009-12-31");
            lease.setEndTime(date);

            assertNotNull(lease.getEndTime());
            assertEquals(lease.getEndTime(), date);
        }
        catch(ParseException ex){
            ex.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullEndTime() throws Exception {
        lease.setEndTime(null);
    }

//    private static final Comparator<Customer> idComparator = new Comparator<Customer>() {
//        @Override
//        public int compare(Customer o1, Customer o2) {
//            return o1.getId().compareTo(o2.getId());
//        }
//    };
}
