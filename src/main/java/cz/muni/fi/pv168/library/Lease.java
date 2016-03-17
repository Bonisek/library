package cz.muni.fi.pv168.library;

import java.util.Date;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public class Lease {

    private Long id;
    private Book book;
    private Customer customer;
    private Date startTime;
    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lease lease = (Lease) o;

        return id != null && id.equals(lease.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Lease: " +
                "id=" + id +
                ", " + book +
                ", " + customer;// +
                //", start time: " + startTime +
                //", end time: " + endTime;
    }

}
