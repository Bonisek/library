package cz.muni.fi.pv168.library;

import java.util.Date;
import java.util.Objects;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public class Book {

    private Long id;
    private String name;
    private String author;
    private String genre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return id != null && id.equals(book.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Book: " +
                "id=" + id +
                ", "+ name +
                ", " + author +
                ", " + genre;
    }

    public static void main(String[] args) {
        Book book = new Book();
        book.setName("pride and prejudice");
        book.setAuthor("Jane austen");
        book.setGenre("romance");

        System.out.println(book);

        Customer customer = new Customer();
        customer.setName("Lenka");
        customer.setSurname("Janeckova");
        System.out.println(customer);

        Lease lease = new Lease();
        lease.setBook(book);
        lease.setCustomer(customer);
        lease.setStartTime(new Date());
        lease.setEndTime(new Date());
        System.out.println(lease);
    }
}
