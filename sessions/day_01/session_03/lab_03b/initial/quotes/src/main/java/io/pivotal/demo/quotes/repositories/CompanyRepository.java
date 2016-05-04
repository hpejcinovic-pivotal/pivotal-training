package io.pivotal.demo.quotes.repositories;

import io.pivotal.demo.quotes.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by cq on 26/11/15.
 */
@RepositoryRestResource(collectionResourceRel = "companies", path = "companies")
public interface CompanyRepository extends PagingAndSortingRepository<Company,Long> {

    @RestResource(path = "name", rel = "name")
    Page<Company> findByNameIgnoreCase(@Param("q") String name, Pageable pageable);

    @RestResource(path = "nameContains", rel = "nameContains")
    Page<Company> findByNameContainsIgnoreCase(@Param("q") String name, Pageable pageable);

    @RestResource(path = "symbol", rel = "symbol")
    Page<Company> findBySymbolIgnoreCase(@Param("q") String symbol, Pageable pageable);

    @RestResource(path = "exchange", rel = "exchange")
    Page<Company> findByExchangeIgnoreCase(@Param("q") String exchange, Pageable pageable);
}

