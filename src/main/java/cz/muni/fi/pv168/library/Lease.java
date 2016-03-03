package cz.muni.fi.pv168.library;

import java.util.Date;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public class Lease {

    private Book book;
    private Customer customer;
    private Date startTime;
    private Date endTime;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
