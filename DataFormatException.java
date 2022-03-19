/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;



/**
 * DataFormatException to be thrown where there input file has row(s)/data in the wrong format.
 * @author patriciawidjojo
 *
 */
public class DataFormatException extends Exception implements Serializable {
	
	
	
	
	
	/**
	 * Empty DataFormatException constructor with default message
	 */
	public DataFormatException() {
		super("Incorrect input data format!");
	}
	
	
	
	
	
	/**
	 * DataFormatException constructor with custom message
	 * @param message to include as part of exception
	 */
	public DataFormatException(String message) {
		super(message);
	}
	
	
	
	

}
