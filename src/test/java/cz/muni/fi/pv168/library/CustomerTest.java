package cz.muni.fi.pv168.library;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author  Lenka (433591)
 * @version 11.03.2016
 */
public class CustomerTest {

    private Customer customer;

    @Before
    public void setUp() {
        customer = new Customer();
    }

    @Test
    public void testGetName() throws Exception {

        customer.setName("Ján");
        String name = customer.getName();
        assertNotNull("customer has null name", name);
        assertEquals("Ján", name);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetNullName() throws Exception {
        customer.setName(null);
    }


    @Test
    public void testGetSurname() throws Exception {
        customer.setSurname("Novák");
        String surname = customer.getSurname();
        assertNotNull("customer has null surname", surname);
        assertEquals("Novák", surname);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullSurname() throws Exception {
        customer.setSurname(null);
    }

    @Test
    public void testGetPhoneNumber() throws Exception {
        customer.setPhoneNumber("sci-fi");
        String phoneNumber = customer.getPhoneNumber();
        assertNotNull("customer has null phoneNumber", phoneNumber);
        assertEquals("sci-fi", phoneNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullPhoneNumber() throws Exception {
        customer.setPhoneNumber(null);
    }

    @Test
    public void testGetAddress() throws Exception {
        customer.setAddress("sci-fi");
        String address = customer.getAddress();
        assertNotNull("customer has null address", address);
        assertEquals("sci-fi", address);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullAddress() throws Exception {
        customer.setAddress(null);
    }


}