package cz.muni.fi.pv168.library;

import java.util.List;

/**
 * @author Lenka (433591)
 * @version 03.03.2016
 */
public interface CustomerManager {

    void createCustomer(Customer customer) throws ServiceFailureException;

    void updateCustomer(Customer customer);

    void deleteCustomer(Customer customer);

    Customer findCustomerById(Long id);

    List<Customer> findCustomersByName(String name);
}
