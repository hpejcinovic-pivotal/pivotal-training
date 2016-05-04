package io.pivotal.springtrader.accounts.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.pivotal.springtrader.accounts.domain.Account;
import io.pivotal.springtrader.accounts.services.AccountService;

@RestController
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> find(@PathVariable("id") final int id) {

        logger.info("AccountController.find: id=" + id);

        Account accountResponse = accountService.getAccount(id);
        return new ResponseEntity<>(accountResponse,getNoCacheHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ResponseEntity<Account> findAccountByUserId(@RequestParam(value="userid") final String userId) {

        logger.info("AccountController.getAccountByUserId: userId=" + userId);

        Account accountResponse = accountService.getAccountByUserId(userId);
        return new ResponseEntity<>(accountResponse,getNoCacheHeaders(), HttpStatus.OK);

    }


    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ResponseEntity<String> save(@RequestBody Account accountRequest, UriComponentsBuilder builder) {

        logger.debug("AccountController.save: userId=" + accountRequest.getUserid());

        Integer accountProfileId = accountService.saveAccount(accountRequest);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(builder.path("/account/{id}").buildAndExpand(accountProfileId).toUri());

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/accounts/{userId}/decreaseBalance/{amount:.+}", method = RequestMethod.GET)
    public ResponseEntity<Double> decreaseBalance(@PathVariable("userId") final String userId, @PathVariable("amount") final double amount) {

        logger.debug("AccountController.decreaseBalance: id='" + userId + "', amount='"+amount+"'");

        if (amount > 0.0) {

            double currentBalance = accountService.getAccountByUserId(userId).getBalance().doubleValue();
            double newBalance = accountService.decreaseBalance(amount,userId);

            if(currentBalance != newBalance) return new ResponseEntity<>(newBalance,getNoCacheHeaders(), HttpStatus.OK);

            else return new ResponseEntity<>(currentBalance,getNoCacheHeaders(), HttpStatus.EXPECTATION_FAILED);

        } else {

            return new ResponseEntity<>(getNoCacheHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @RequestMapping(value = "/accounts/{userId}/increaseBalance/{amount:.+}", method = RequestMethod.GET)
    public ResponseEntity<Double> increaseBalance(@PathVariable("userId") final String userId, @PathVariable("amount") final double amount) {

        logger.debug("AccountController.increaseBalance: id='" + userId + "', amount='"+amount+"'");

        if (amount > 0.0) {

            double currentBalance = accountService.increaseBalance(amount, userId);
            logger.debug("AccountController.increaseBalance: current balance='" + currentBalance + "'.");
            return new ResponseEntity<>(currentBalance, getNoCacheHeaders(),HttpStatus.OK);

        } else {

            //amount can not be negative for increaseBalance, please use decreaseBalance
            return new ResponseEntity<>(accountService.getAccountByUserId(userId).getBalance().doubleValue(), getNoCacheHeaders(),
                    HttpStatus.EXPECTATION_FAILED);
        }

    }

    private HttpHeaders getNoCacheHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-cache");
        return responseHeaders;
    }
}
