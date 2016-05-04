package io.pivotal.springtrader.exceptions;

/**
 * Exception representing that a quote symbol cannot be found.
 * @author David Ferreira Pinto
 *
 */
public class SymbolNotFoundException extends Exception {

	public SymbolNotFoundException(String message) {
		super(message);
	}
}
