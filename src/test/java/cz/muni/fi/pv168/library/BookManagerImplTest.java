package cz.muni.fi.pv168.library;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Lenka (433591)
 * @version 11.03.2016
 */
public class BookManagerImplTest {

    BookManager bookManager;

    @Before
    public void setUp() throws Exception {
        bookManager = new BookManagerImpl();
    }

    @Test
    public void testCreateBook() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        bookManager.createBook(book);
        Long bookId = book.getId();
        Book result = bookManager.findBookById(bookId);

        assertNotNull("loaded book is null", result);
        assertNotNull("saved book has null ID", result.getId());
        assertThat("loaded book differs from the saved one", result, is(equalTo(book)));
        assertThat("loaded book is the same instance", result, is(not(sameInstance(book))));
        assertDeepEquals(book, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBookWithNull() throws Exception {
        bookManager.createBook(null);
    }

    @Test
    public void testUpdateBook() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Confessions of a Shopaholic", "Sophie Kinsella", "comedy");
        Long bookID = book.getId();

        bookManager.createBook(book);
        bookManager.createBook(book1);
        book.setGenre("sci-fi");
        bookManager.updateBook(book);
        book = bookManager.findBookById(bookID);

        assertNotNull(book);
        assertThat("genre value wasn't changed correctly", book.getGenre(), is(equalTo("sci-fi")));
        assertThat("name value was changed when changing genre", book.getName(), is(equalTo("Pride and Prejudice")));
        assertThat("author value was changed when changing genre", book.getAuthor(), is(equalTo("Jane Austen")));

        book.setAuthor("J. K. Rowling");
        bookManager.updateBook(book);
        book = bookManager.findBookById(bookID);

        assertNotNull(book);
        assertThat("genre value was changed when changing genre", book.getGenre(), is(equalTo("sci-fi")));
        assertThat("name value was changed when changing genre", book.getName(), is(equalTo("Pride and Prejudice")));
        assertThat("author value wasn't changed correctly", book.getAuthor(), is(equalTo("J. K. Rowling")));

        book.setName("Harry Potter");
        bookManager.updateBook(book);
        book = bookManager.findBookById(bookID);

        assertNotNull(book);
        assertThat("genre value was changed when changing genre", book.getGenre(), is(equalTo("sci-fi")));
        assertThat("name value wasn't changed correctly", book.getName(), is(equalTo("Harry Potter")));
        assertThat("author value was changed when changing genre", book.getAuthor(), is(equalTo("J. K. Rowling")));

        assertDeepEquals(book1, bookManager.findBookById(book1.getId()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void updateBookWithWrongAttributes() {

       bookManager.updateBook(null);
    }

    @Test
    public void testDeleteBook() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Confessions of a Shopaholic", "Sophie Kinsella", "comedy");

        bookManager.createBook(book);
        bookManager.createBook(book1);

        assertNotNull(bookManager.findBookById(book.getId()));
        assertNotNull(bookManager.findBookById(book1.getId()));

        bookManager.deleteBook(book);
        assertNull(bookManager.findBookById(book.getId()));
        assertNotNull(bookManager.findBookById(book1.getId()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void deleteBookWithWrongAttributes() {

        bookManager.deleteBook(null);
    }

    @Test
    public void testFindBookById() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        bookManager.createBook(book);

        Book result = bookManager.findBookById(book.getId());

        assertNotNull(result);
        assertDeepEquals(book, result);

    }

    @Test (expected = IllegalArgumentException.class)
    public void findBookByNullId() {

        bookManager.findBookById(null);
    }

    @Test
    public void testFindBooksByAuthor() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Confessions of a Shopaholic", "Jane Austen", "comedy");

        List<Book> result = bookManager.findBooksByAuthor("Jane Austen");

        assertNotNull(result);
        assertEquals("loaded list contains more or fewer values", 2, result.size());
        assertNotNull("loaded list contains null value", result.get(0));
        assertNotNull("loaded list contains null value", result.get(1));
        assertTrue("loaded list doesn't contain saved book", result.contains(book));
        assertTrue("loaded list doesn't contain saved book", result.contains(book1));

        result = bookManager.findBooksByAuthor("Jack London");
        assertNotNull(result);
        assertEquals("loaded list should be empty", 0, result.size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void findBooksByNullAuthor() {

        bookManager.findBooksByAuthor(null);
    }


    @Test
    public void testFindBooksByGenre() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Confessions of a Shopaholic", "Sophie Kinsella", "romance");

        List<Book> result = bookManager.findBooksByGenre("romance");

        assertNotNull(result);
        assertEquals("loaded list contains more or fewer values", 2, result.size());
        assertNotNull("loaded list contains null value", result.get(0));
        assertNotNull("loaded list contains null value", result.get(1));
        assertTrue("loaded list doesn't contain saved book", result.contains(book));
        assertTrue("loaded list doesn't contain saved book", result.contains(book1));

        result = bookManager.findBooksByGenre("sci-fi");
        assertNotNull(result);
        assertEquals("loaded list should be empty", 0, result.size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void findBooksByNullGenre() {

        bookManager.findBooksByGenre(null);
    }

    @Test
    public void testFindBooksByName() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Pride and Prejudice", "Sophie Kinsella", "sci-fi");

        List<Book> result = bookManager.findBooksByName("Pride and Prejudice");

        assertNotNull(result);
        assertEquals("loaded list contains more or fewer values", 2, result.size());
        assertNotNull("loaded list contains null value", result.get(0));
        assertNotNull("loaded list contains null value", result.get(1));
        assertTrue("loaded list doesn't contain saved book", result.contains(book));
        assertTrue("loaded list doesn't contain saved book", result.contains(book1));

        result = bookManager.findBooksByName("Harry Potter");
        assertNotNull(result);
        assertEquals("loaded list should be empty", 0, result.size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void findBooksByNullName() {

        bookManager.findBooksByName(null);
    }

    private static Book newBook(String name, String author, String genre) {
        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setGenre(genre);
        return book;
    }

    private void assertDeepEquals(Book expected, Book actual) {
        assertEquals("id value is not equal",expected.getId(), actual.getId());
        assertEquals("author value is not equal",expected.getAuthor(), actual.getAuthor());
        assertEquals("genre value is not equal",expected.getGenre(), actual.getGenre());
        assertEquals("name value is not equal",expected.getName(), actual.getName());
    }
}