package io.pivotal.springtrader.services;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pivotal.springtrader.domain.Holding;
import io.pivotal.springtrader.domain.Order;
import io.pivotal.springtrader.domain.OrderType;
import io.pivotal.springtrader.domain.Portfolio;
import io.pivotal.springtrader.domain.Quote;
import io.pivotal.springtrader.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.repositories.OrderRepository;

/**
 * Manages a portfolio of holdings of stock/shares.
 * 
 * @author David Ferreira Pinto
 * @author cq
 */
@Service
public class PortfolioService {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioService.class);

	/**
	 * The order repository to store Order objects.
	 */
	@Autowired
	OrderRepository repository;

    @Autowired
    AccountService accountService;

    @Autowired
    QuoteService quoteService;

	/**
	 * Retrieves the portfolio for the given accountId. 
	 * 
	 * @param accountId The account id to retrieve for.
	 * @return The portfolio.
	 */
	public Portfolio getPortfolio(String accountId) {
		/*
		 * Retrieve all orders for accountRepository id and build portfolio.
		 * - for each order create holding.
		 * - for each holding find current price.
		 */
		logger.debug("Getting portfolio for accountId: " + accountId);
		List<Order> orders = repository.findByAccountId(accountId);
		return createPortfolio(new Portfolio(), orders);
	}

	/**
	 * Builds a portfolio object with the list of orders.
	 * 
	 * @param portfolio the portfolio object to build.
	 * @param orders the list of orders.
	 * @return the portfolio object
	 */
	private Portfolio createPortfolio(Portfolio portfolio, List<Order> orders) {
		//TODO: change to forEach() and maybe in parallel?
		for (Order order: orders) {
			Holding holding = portfolio.getHolding(order.getSymbol());
			if ( holding == null) {
				holding = new Holding();
				holding.setSymbol(order.getSymbol());
				portfolio.addHolding(holding);
			}
			holding.addOrder(order);
		}
		
		// getLatestQuotes in parallel
		portfolio.getHoldings().values().parallelStream().forEach(holding -> refreshHolding(holding));
		portfolio.refreshTotalValue();
		logger.debug("Portfolio: " + portfolio);
		return portfolio;
	}
	
	/**
	 * Calculates the current value of th holding.
	 * 
	 * @param holding the holding to refresh.
	 */
	private void refreshHolding(Holding holding) {
        Quote quote = null;
        try {
            quote = getQuote(holding.getSymbol());
        } catch (SymbolNotFoundException e) {
            logger.error(e.getMessage(),e);
        }
        holding.setCurrentValue(new BigDecimal(quote.getLastPrice()));
	}
	
	/**
	 * Retrieve up to date quotes.
	 * 
	 * @param symbol the symbol of the quote to fetch.
	 * @return
	 */
	private Quote getQuote(String symbol) throws SymbolNotFoundException{
		logger.debug("Fetching quote: " + symbol);
		return quoteService.getQuote(symbol);
	}
	
	/**
	 * Add an order to the repository and modify account balance.
	 * 
	 * @param order the order to add.
	 * @return the saved order.
	 */
	@Transactional
	public Order addOrder(Order order) {
		logger.debug("Adding order: " + order);

		if (order.getOrderFee() == null) {
			order.setOrderFee(new BigDecimal(10.50));
			logger.debug("Adding Fee to order: " + order);
		}

		if (order.getOrderType() == OrderType.BUY) {

			double amount = order.getQuantity()*order.getPrice().doubleValue()+order.getOrderFee().doubleValue();
            double currentBalance = accountService.decreaseBalance(amount,order.getAccountId());
			return saveOrder(order, currentBalance);

		} else {

			double amount = order.getQuantity()*order.getPrice().doubleValue()-order.getOrderFee().doubleValue();
            double currentBalance = accountService.increaseBalance(amount,order.getAccountId());
			return saveOrder(order,currentBalance);

		}
		
			
	}

	private Order saveOrder(Order order, double currentBalance) {
		if (currentBalance > 0.0) {
            logger.info(String.format("Account funds updated successfully for account: %s and new funds are: %s", order.getAccountId(), currentBalance));
            return repository.save(order);
        } else {
            //TODO: throw exception - not enough funds!
            logger.warn("PortfolioService:addOrder - decresing balance HTTP not ok: ");
            return null;
        }
	}
}
