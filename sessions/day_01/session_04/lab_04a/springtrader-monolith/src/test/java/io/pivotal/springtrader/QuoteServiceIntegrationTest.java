package io.pivotal.springtrader;



import io.pivotal.springtrader.domain.CompanyInfo;
import io.pivotal.springtrader.domain.Quote;
import io.pivotal.springtrader.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.services.QuoteService;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Tests the QuoteService.
 * @author David Ferreira Pinto
 *
 */

public class QuoteServiceIntegrationTest {



	QuoteService quoteService = new QuoteService();
	/**
	 * Tests retrieving a quote from the external quoteService.
	 * @throws Exception
	 */
	@Test
	public void getQuote() throws Exception {
		Quote quote = quoteService.getQuote(TestData.QUOTE_SYMBOL);
		assertEquals(TestData.QUOTE_SYMBOL, quote.getSymbol());
		assertEquals(TestData.QUOTE_NAME, quote.getName());
	}
	/**
	 * Tests retrieving a quote with an unknown/null symbol from the external quoteService.
	 * @throws Exception
	 */
	@Test(expected=SymbolNotFoundException.class)
	public void getNullQuote() throws Exception{
		quoteService.getQuote(TestData.NULL_QUOTE_SYMBOL);
	}
	
	/**
	 * tests retrieving company information from external quoteService.
	 * @throws Exception
	 */
	@Test
	public void getCompanyInfo() throws Exception {
		List<CompanyInfo> comps = quoteService.getCompanyInfo(TestData.QUOTE_SYMBOL);
		assertFalse(comps.isEmpty());
		boolean pass = false;
		for (CompanyInfo info : comps) {
			if (info.getSymbol().equals(TestData.QUOTE_SYMBOL)) {
				pass = true;
			}
		}
		assertTrue(pass);
	}
	/**
	 * tests retrieving company information from external quoteService with unknown company.
	 * @throws Exception
	 */
	@Test
	public void getNullCompanyInfo() throws Exception {
		List<CompanyInfo> comps = quoteService.getCompanyInfo(TestData.NULL_QUOTE_SYMBOL);
		assertTrue(comps.isEmpty());
	}

	@Test
	public void searchForCompanies() throws Exception{
		List<CompanyInfo> comps = quoteService.getCompanyInfo("alphabet");
        comps.stream().forEach(System.out::println);
        assertThat(comps.size(),greaterThan(1));
        assertThat(comps.stream().anyMatch(c-> c.getSymbol().equalsIgnoreCase("GOOGL")), equalTo(true));
        assertThat(comps.stream().anyMatch(c-> c.getSymbol().equalsIgnoreCase("GOOG")), equalTo(true));


	}
	
}
