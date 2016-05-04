package io.pivotal.springtrader.controllers;


import io.pivotal.springtrader.domain.Order;
import io.pivotal.springtrader.services.PortfolioService;
import io.pivotal.springtrader.services.QuotesFetchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static io.pivotal.springtrader.utils.Helper.generateError;

@Controller
public class PortfolioController {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);
	
	@Autowired
	private PortfolioService portfolioService;

	@Autowired
	private QuotesFetchingService quotesFetchingService;

	@RequestMapping(value = "/portfolio", method = RequestMethod.GET)
	public String portfolio(Model model) {
		logger.debug("/portfolio");
		model.addAttribute("marketSummary", quotesFetchingService.getMarketSummary());
		
		//check if user is logged in!
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    logger.debug("portfolio: User logged in: " + currentUserName);
		    
		    //TODO: add account summary.
		    try {
		    	model.addAttribute("portfolio", portfolioService.getPortfolio(currentUserName));
		    } catch (HttpServerErrorException e) {
		    	logger.debug("error retrieving portfolfio: " + e.getMessage());
		    	model.addAttribute("portfolioRetrievalError",e.getMessage());
		    }
		    model.addAttribute("order", new Order());
		}
		
		return "portfolio";
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView error(HttpServletRequest req, Exception exception) {
		logger.debug("Handling error: " + exception);
		return generateError(exception);
	}

}
