package io.pivotal.springtrader.controllers;


import io.pivotal.springtrader.domain.Account;
import io.pivotal.springtrader.domain.AuthenticationRequest;
import io.pivotal.springtrader.services.AccountService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static io.pivotal.springtrader.utils.Helper.generateError;

@Controller
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private AccountService accountService;
		
	@Autowired
	private QuotesFetchingService quotesFetchingService;

    @Autowired
    private PortfolioService portfolioService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHome(Model model) {
		if (!model.containsAttribute("login")) {
			model.addAttribute("login", new AuthenticationRequest());
		}
		model.addAttribute("marketSummary", quotesFetchingService.getMarketSummary());

		//check if user is logged in!
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			logger.debug("User logged in: " + currentUserName);

			try {
				model.addAttribute("portfolio",portfolioService.getPortfolio(currentUserName));
			} catch (HttpServerErrorException e) {
				model.addAttribute("portfolioRetrievalError",e.getMessage());
			}
			model.addAttribute("account", accountService.getAccount(currentUserName));
		}

		return "index";
	}

	@RequestMapping(value="/logout", method = RequestMethod.POST)
	public String postLogout(Model model, @ModelAttribute(value="login") AuthenticationRequest login) {
		logger.info("Logout, user: " + login.getUsername());
		logger.info(model.asMap().toString());
		return "index";
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("account", new Account());
		return "registration";
	}
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String register(Model model, @ModelAttribute(value="account") Account account) {
		logger.info("register: user:" + account.getUserid());
		
		//need to set some stuff on account...
		
		account.setOpenbalance(account.getBalance());
		account.setCreationdate(new Date());
		
		AuthenticationRequest login = new AuthenticationRequest();
		login.setUsername(account.getUserid());
		model.addAttribute("login", login);
		model.addAttribute("marketSummary", quotesFetchingService.getMarketSummary());
		accountService.saveAccount(account) ;
		return "index";
	}
	@ExceptionHandler({ Exception.class })
	public ModelAndView error(HttpServletRequest req, Exception exception) {
		logger.debug("Handling error: " + exception);
		return generateError(exception);
	}
}
