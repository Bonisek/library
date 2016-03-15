package cz.muni.fi.pv168.library;

import java.util.List;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public interface LeaseManager {

    void createLease(Lease lease);

    void updateLease(Lease lease);

    void deleteLease(Lease lease);

    Lease findLeaseById(Long id);

    List<Lease> findLeasesByBook(Book book);

    List<Lease> findAllLeases();

    List<Lease> findLeasesByCustomer(Customer customer);
}
