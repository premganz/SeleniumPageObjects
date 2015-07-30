package org.spo.fw.exception;
/**
 * 
 * @author Prem * 
 * it makes sense to wrap webdriver exceptions as either recoverable or nonrecoverable
 * recoverable ones are like unexpected hangs on the browser, which could be handled at the test runner level, wherein the 
 * whole test can be retried.
 * irrecoverable ones are also mostly handled at the test runner level, but capable of being caught at the test script level *
 *
 */

public class UnexpectedWebDriverException extends SPOException {

	
}
