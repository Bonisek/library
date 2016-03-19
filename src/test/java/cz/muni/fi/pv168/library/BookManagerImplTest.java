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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Lenka (433591)
 * @version 11.03.2016
 */
public class BookManagerImplTest {

    private BookManager bookManager;
    private DataSource dataSource;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        //dataSource = prepareDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE BOOK ("
                    + "id bigint primary key generated always as identity,"
                    + "bookname VARCHAR(30) ,"
                    + "author VARCHAR(40) ,"
                    + "genre VARCHAR(20))").executeUpdate();
        }
        bookManager = new BookManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE BOOK").executeUpdate();
        }
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
        assertThat("genre value was changed when changing author", book.getGenre(), is(equalTo("sci-fi")));
        assertThat("name value was changed when changing author", book.getName(), is(equalTo("Pride and Prejudice")));
        assertThat("author value wasn't changed correctly", book.getAuthor(), is(equalTo("J. K. Rowling")));

        book.setName("Harry Potter");
        bookManager.updateBook(book);
        book = bookManager.findBookById(bookID);

        assertNotNull(book);
        assertThat("genre value was changed when changing name", book.getGenre(), is(equalTo("sci-fi")));
        assertThat("name value wasn't changed correctly", book.getName(), is(equalTo("Harry Potter")));
        assertThat("author value was changed when changing name", book.getAuthor(), is(equalTo("J. K. Rowling")));

        assertDeepEquals(book1, bookManager.findBookById(book1.getId()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testUpdateBookWithNull() {

       bookManager.updateBook(null);
    }

    @Test
    public void testUpdateBookWithNonexistingId() throws Exception {
        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        bookManager.createBook(book);
        book.setId(book.getId() + 1);
        expectedException.expect(EntityNotFoundException.class);
        bookManager.updateBook(book);
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
    public void testDeleteBookWithNull() {

        bookManager.deleteBook(null);
    }

    @Test
    public void testDeleteBookWithNonexistingId() throws Exception {
        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        bookManager.createBook(book);
        book.setId(book.getId() + 1);
        expectedException.expect(EntityNotFoundException.class);
        bookManager.deleteBook(book);
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
    public void testFindBookByNullId() {

        bookManager.findBookById(null);
    }


    @Test
    public void testFindBooksByAuthor() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Confessions of a Shopaholic", "Jane Austen", "comedy");
        Book book2 = newBook("Best Recipes", "Rick Rubin", "food");

        bookManager.createBook(book);
        bookManager.createBook(book1);
        bookManager.createBook(book2);

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
    public void testFindBooksByNullAuthor() {

        bookManager.findBooksByAuthor(null);
    }

    @Test
    public void testFindBooksByGenre() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Confessions of a Shopaholic", "Sophie Kinsella", "romance");
        Book book2 = newBook("Best Recipes", "Rick Rubin", "food");

        bookManager.createBook(book);
        bookManager.createBook(book1);
        bookManager.createBook(book2);

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
    public void testFindBooksByNullGenre() {

        bookManager.findBooksByGenre(null);
    }

    @Test
    public void testFindBooksByName() throws Exception {

        Book book = newBook("Pride and Prejudice", "Jane Austen", "romance");
        Book book1 = newBook("Pride and Prejudice", "Sophie Kinsella", "sci-fi");
        Book book2 = newBook("Best Recipes", "Rick Rubin", "food");

        bookManager.createBook(book);
        bookManager.createBook(book1);
        bookManager.createBook(book2);

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
    public void testFindBooksByNullName() {

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

    //private static DataSource prepareDataSource() throws SQLException {
//        EmbeddedDataSource ds = new EmbeddedDataSource();
//        //we will use in memory database
//        ds.setDatabaseName("memory:bookmgr-test");
//        ds.setCreateDatabase("create");
//        return ds;
//    }
}