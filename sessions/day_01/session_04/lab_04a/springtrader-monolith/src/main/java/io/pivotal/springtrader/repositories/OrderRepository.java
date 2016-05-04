package io.pivotal.springtrader.repositories;


import io.pivotal.springtrader.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author David Ferreira Pinto
 *
 */
@Repository
public interface OrderRepository extends CrudRepository<Order,Integer> {

	List<Order> findByAccountId(String accountId);
}
