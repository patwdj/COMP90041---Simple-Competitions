/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;



/**
 * AutoNumbersEntry for LuckyNumbersCompetition entries where numbers are generated automatically.
 * @author patriciawidjojo
 *
 */
public class AutoNumbersEntry extends NumbersEntry implements Serializable {

	
	
	
	
	/**
	 * Empty constructor for AutoNumbersEntry to be used when drawing winners in normal mode.
	 */
	public AutoNumbersEntry() {
		createNumbers();
	}

	
	
	
	
	/**
	 * Constructor for AutoNumbersEntry to be used when drawing winners in testing mode
	 * @param seed is competition identifier.
	 */
	public AutoNumbersEntry(int seed) {
		createNumbers(seed);
	}

	
	
	
	
	/**
	 * Constructor to create an AutoNumbersEntry object for LuckyNumbersCompetition.
	 * @param currentID, entryID to assign this entry.
	 * @param billID relating to this entry.
	 * @param memberID relating to this entry.
	 * @param database, DataProvider that holds member and bill information
	 * @param seed is the number of entries in active competition.
	 */
	public AutoNumbersEntry(int currentID, String billID, String memberID, DataProvider database,
			int seed) {
		super(currentID, billID, memberID, database);
		setIsManual(false);
		if (SimpleCompetitions.testMode == 'N') {
			createNumbers();
		} else {
			createNumbers(seed);
		}
	}

	
	
	
	
	/**
	 * Creates and updates numbers when using AutoNumbersEntry in testing mode.
	 * @param seed to draw numbers in testing mode.
	 */
	public void createNumbers(int seed) {
		ArrayList<Integer> validList = new ArrayList<Integer>();
		int[] tempNumbers = new int[NUMBERS_LENGTH];
		for (int i = 1; i <= NUMBERS_MAX; i++) {
			validList.add(i);
		}
		Collections.shuffle(validList, new Random(seed));
		for (int i = 0; i < NUMBERS_LENGTH; i++) {
			tempNumbers[i] = validList.get(i);
		}
		Arrays.sort(tempNumbers);
		setNumbers(tempNumbers);
	}

	
	
	
	
	/**
	 * Overrides the createNumbers method of NumbersEntry to create automatic random numbers. 
	 * Used in normal mode.
	 */
	public void createNumbers() {
		ArrayList<Integer> validList = new ArrayList<Integer>();
		int[] tempNumbers = new int[NUMBERS_LENGTH];
		for (int i = 1; i <= NUMBERS_MAX; i++) {
			validList.add(i);
		}
		Collections.shuffle(validList, new Random());
		for (int i = 0; i < NUMBERS_LENGTH; i++) {
			tempNumbers[i] = validList.get(i);
		}
		Arrays.sort(tempNumbers);
		setNumbers(tempNumbers);
	}
	
	
	
	
	
}
