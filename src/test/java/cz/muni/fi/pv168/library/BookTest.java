package cz.muni.fi.pv168.library;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * @author Lenka (433591)
 * @version 11.03.2016
 */
public class BookTest {

    private Book book;

    @Before
    public void setUp() {
        book = new Book();
    }

    @Test
    public void testGetName() throws Exception {

        book.setName("Harry Potter");
        String name = book.getName();
        assertNotNull("book has null name", name);
        assertEquals("Harry Potter", name);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetNullName() throws Exception {
        book.setName(null);
    }


    @Test
    public void testGetAuthor() throws Exception {
        book.setAuthor("Jane Austen");
        String author = book.getAuthor();
        assertNotNull("book has null author", author);
        assertEquals("Jane Austen", author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullAuthor() throws Exception {
        book.setAuthor(null);
    }

    @Test
    public void testGetGenre() throws Exception {
        book.setGenre("sci-fi");
        String genre = book.getGenre();
        assertNotNull("book has null genre", genre);
        assertEquals("sci-fi", genre);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullGenre() throws Exception {
        book.setGenre(null);
    }

    @Test
    public void testEquals() throws Exception {
        Book book = new Book();
        book.setName("pride and prejudice");
        book.setAuthor("Jane austen");
        book.setGenre("romance");

        Book book1 = new Book();
        book1.setName("pride and prejudice");
        book1.setAuthor("Jane austen");
        book1.setGenre("romance");

        assertTrue(book.equals(book));
        assertFalse(book.equals(book1));
        assertFalse(book.equals(null));

        book1.setId(book.getId());
        assertTrue(book.equals(book1));
    }


}