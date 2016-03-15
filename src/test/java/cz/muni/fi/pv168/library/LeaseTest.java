package cz.muni.fi.pv168.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
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
        Book b = new Book();
        b.setId(1L);
        lease.setBook(b);

        assertNotNull("huehue", lease.getBook());
        assertEquals("ayyy", lease.getBook(), b);
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
        Customer c = new Customer();
        c.setId(1L);

        assertNotNull("huehue", lease.getCustomer());
        assertEquals("ayyy", lease.getCustomer(), c);
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

            assertNotNull("huehue", lease.getStartTime());
            assertEquals("ayyy", lease.getStartTime(), date);
        }catch(ParseException ex){
            //
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

            assertNotNull("huehue", lease.getEndTime());
            assertEquals("ayyy", lease.getEndTime(), date);
        }catch(ParseException ex){
            //
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullEndTime() throws Exception {
        lease.setEndTime(null);
    }

    private static final Comparator<Customer> idComparator = new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };
}
