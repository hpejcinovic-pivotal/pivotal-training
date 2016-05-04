package io.pivotal.springtrader.repositories;


import io.pivotal.springtrader.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,Integer> {

	Account findByUseridAndPasswd(String userId, String passwd);

	Account findByUserid(String userId);

	Account findByAuthtoken(String authtoken);
}
