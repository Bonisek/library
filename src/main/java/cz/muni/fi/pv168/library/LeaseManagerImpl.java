package cz.muni.fi.pv168.library;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public class LeaseManagerImpl implements LeaseManager {

    private DataSource dataSource;

    public LeaseManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createLease(Lease lease) {

    }

    public void updateLease(Lease lease) {

    }

    public void deleteLease(Lease lease) {

    }

    @Override
    public Lease findLeaseById(Long id) {
        return null;
    }

    public List<Lease> findLeasesByBook(Book book) {
        return null;
    }

    public List<Lease> findAllLeases() {
        return null;
    }

    public List<Lease> findLeasesByCustomer(Customer customer) {
        return null;
    }
}
