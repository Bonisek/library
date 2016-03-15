package cz.muni.fi.pv168.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kristian Mateka
 */
public class LeaseManagerImplTest {
    private LeaseManagerImpl manager;

    @Before
    public void setUp() {
        manager = new LeaseManagerImpl();
    }

    /**
     * Test of createLease method, of class LeaseManagerImpl.
     */
    @Test
    public void testCreateLease() {
        Customer c = new Customer();
        Book b = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2010-12-31");
            Lease l = newLease(b, c, start, end);
            manager.createLease(l);

            assertNotNull("ayy", manager.findLeaseById(l.getId()).getBook());
            assertNotNull("ayy", manager.findLeaseById(l.getId()).getCustomer());
            assertNotNull("ayy", manager.findLeaseById(l.getId()).getEndTime());
            assertNotNull("ayy", manager.findLeaseById(l.getId()).getStartTime());

            assertEquals("hue", manager.findLeaseById(l.getId()).getBook().getId(), b.getId());
            assertEquals("hue", manager.findLeaseById(l.getId()).getCustomer().getId(), c.getId());
            assertEquals("hue", manager.findLeaseById(l.getId()).getEndTime(), end);
            assertEquals("hue", manager.findLeaseById(l.getId()).getStartTime(), start);

        }catch(ParseException ex){

        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCreateLeaseWithNull() {
        manager.createLease(null);
    }

    /**
     * Test of updateLease method, of class LeaseManagerImpl.
     */
    @Test
    public void testUpdateLease() {
    }

    @Test (expected = IllegalArgumentException.class)
    public void testUpdateLeaseWithNull() {
        manager.updateLease(null);
    }

    /**
     * Test of deleteLease method, of class LeaseManagerImpl.
     */
    @Test
    public void testDeleteLease() {
        Customer c = new Customer();
        Customer c2 = new Customer();
        Book b = new Book();
        Book b2 = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease l = newLease(b, c, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease l2 = newLease(b2, c2, start, end);

            manager.createLease(l);
            manager.createLease(l2);

            assertEquals("ayyyy", manager.findAllLeases().size(), 2);

            manager.deleteLease(l);

            assertEquals("ayyyy", manager.findAllLeases().size(), 1);

        }catch(ParseException ex){

        }

    }

    @Test (expected = IllegalArgumentException.class)
    public void testDeleteLeaseWithNull() {
        manager.deleteLease(null);
    }

    /**
     * Test of findLeasesByBook method, of class LeaseManagerImpl.
     */
    @Test
    public void testFindLeasesByBook() {
        Customer c = new Customer();
        Customer c2 = new Customer();
        Book b = new Book();
        Book b2 = new Book();
        Book b3 = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease l = newLease(b, c, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease l2 = newLease(b2, c2, start, end);

            manager.createLease(l);
            manager.createLease(l2);

            assertEquals("ayyyy", manager.findLeasesByBook(b2).size(), 1);
            assertEquals("ayyyy", manager.findLeasesByBook(b3).size(), 0);

        }catch(ParseException ex){

        }
    }

    /**
     * Test of findAllLeases method, of class LeaseManagerImpl.
     */
    @Test
    public void testFindAllLeases() {
        Customer c = new Customer();
        Customer c2 = new Customer();
        Book b = new Book();
        Book b2 = new Book();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date start = sdf.parse("2009-12-31");
            Date end = sdf1.parse("2009-12-31");
            Lease l = newLease(b, c, start, end);
            start = sdf.parse("2010-12-31");
            end = sdf1.parse("2011-12-31");
            Lease l2 = newLease(b2, c2, start, end);

            manager.createLease(l);
            manager.createLease(l2);

            assertEquals("ayyyy", manager.findAllLeases().size(), 2);

        }catch(ParseException ex){

        }
    }

    /**
     * Test of findLeasesByCustomer method, of class LeaseManagerImpl.
     */
    @Test
    public void testFindLeasesByCustomer() {
        System.out.println("findLeasesByCustomer");
        Customer customer = null;
        LeaseManagerImpl instance = new LeaseManagerImpl();
        List<Lease> expResult = null;
        List<Lease> result = instance.findLeasesByCustomer(customer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private static Lease newLease(Book b, Customer c, Date start, Date end){
        Lease l = new Lease();
        l.setBook(b);
        l.setCustomer(c);
        l.setEndTime(end);
        l.setStartTime(start);

        return l;
    }
}
