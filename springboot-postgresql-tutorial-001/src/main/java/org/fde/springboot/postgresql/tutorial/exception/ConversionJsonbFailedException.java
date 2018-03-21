/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.exception;

import org.springframework.core.convert.ConversionException;

/**
 * @author fdelom
 *
 */
public class ConversionJsonbFailedException extends ConversionException {

	private static final long serialVersionUID = -6691394138943685932L;

	public ConversionJsonbFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
