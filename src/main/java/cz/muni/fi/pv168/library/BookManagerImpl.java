package cz.muni.fi.pv168.library;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public class BookManagerImpl implements BookManager {

    private DataSource dataSource;

    public BookManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createBook(Book book) {

    }

    public void updateBook(Book book) {

    }

    public void deleteBook(Book book) {

    }

    public Book findBookById(Long id) {
        return null;
    }

    public List<Book> findBooksByAuthor(String name) {
        return null;
    }

    public List<Book> findBooksByGenre(String genre) {
        return null;
    }

    public List<Book> findBooksByName(String name) {
        return null;
    }
}
