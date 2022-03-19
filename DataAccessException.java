/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;



/**
 * DataAccessException to be thrown where there are issues in opening file.
 * @author patriciawidjojo
 *
 */
public class DataAccessException extends Exception implements Serializable {

	
	
	
	
	/**
	 * Empty DataAccessException constructor with default message.
	 */
	public DataAccessException() {
		super("Error accessing file!");
	}
	
	
	
	
	
	/**
	 * DataAccessException constructor with custom message
	 * @param message to include as part of exception
	 */
	public DataAccessException(String message) {
		super(message);
	}
	
	
	
	
	

}
