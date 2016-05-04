package io.pivotal.springtrader.quotes.exceptions;

/**
 * Exception representing that a quote symbol cannot be found.
 *
 * @author David Ferreira Pinto
 */
public class SymbolNotFoundException extends Exception {

	private static final long serialVersionUID = -5906264030684211422L;

	public SymbolNotFoundException(String message) {
        super(message);
    }
}
