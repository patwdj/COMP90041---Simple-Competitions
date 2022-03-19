/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;
import java.util.Arrays;



/**
 * NumbersEntry object represents an entry in the LuckyNumbersCompetition. 
 * @author patriciawidjojo
 *
 */
public class NumbersEntry extends Entry implements Serializable {

	public static final int NUMBERS_LENGTH = 7;
	public static final int NUMBERS_MIN = 1;
	public static final int NUMBERS_MAX = 35;
	private int[] numbers;
	private int numberOfMatch;

	
	
	
	
	/**
	 * Empty constructor to create NumbersEntry object/Subclasses
	 */
	public NumbersEntry() {}

	
	
	
	
	/**
	 * Constructor to create NumbersEntry object 
	 * @param currentID, entryID to assign this entry.
	 * @param billID, billID relating to this entry.
	 * @param memberID, memberID relating to this entry.
	 * @param isManual, boolean where true means that this entry is a manual entry, auto otherwise
	 * @param database, DataProvider that holds member and bill information
	 */
	public NumbersEntry(int currentID, String billID, String memberID, boolean isManual,
			DataProvider database) {
		super(currentID, billID, memberID, isManual, database);
		createNumbers();
	}
	
	
	
	
	
	/**
	 * Constructor to be called when creating AutoNumbersEntry
	 * @param currentID, entryID to assign this entry.
	 * @param billID, billID relating to this entry.
	 * @param memberID, memberID relating to this entry.
	 * @param database, DataProvider that holds member and bill information
	 */
	public NumbersEntry(int currentID, String billID, String memberID, DataProvider database) {
		super(currentID, billID, memberID, database);
	}
	
	
	
	
	
	//setters and getters.
	public int[] getNumbers() {
		return numbers;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}
	
	public int getNumberOfMatch() {
		return numberOfMatch;
	}

	public void setNumberOfMatch(int numberOfMatch) {
		this.numberOfMatch = numberOfMatch;
	}





	/**
	 * gets valid entry numbers through user input (if manual) or auto entry (if auto)
	 */
	public void createNumbers() {
		
		boolean isValidEntry = false;
		int digitsCount = 0;
		boolean validValues = false;
		
		do {
			while (true) {
				try {
					validValues = true;
					System.out.println("Please enter " + NUMBERS_LENGTH
							+ " different numbers (from "
							+ "the range 1 to 35) separated by whitespace.");
					String inputEntry = SimpleCompetitions.keyboard.nextLine();
					String[] splitInputEntry = (inputEntry.split(" "));
					digitsCount = splitInputEntry.length;
					numbers = new int[digitsCount];

					// first, check that manual input is 7 numbers (referred to as digits)
					// and all digits values between 1-35
					for (int digit = 0; digit < digitsCount; digit++) {
						numbers[digit] = Integer.parseInt(splitInputEntry[digit]);
						if (numbers[digit] < NUMBERS_MIN || numbers[digit] > NUMBERS_MAX) {
							validValues = false;
							break;
						}
					}
					break;
				} catch (NumberFormatException e) {
					System.out.println("Invalid input! Numbers are expected. Please try again!");
				} catch (Exception e) {
					System.out.println("Invalid input! Please try again!");
				}
			}
			if (digitsCount < NUMBERS_LENGTH) {
				System.out.println("Invalid input! Fewer than " + NUMBERS_LENGTH + " numbers "
						+ "are provided. Please try again!");
			} else if (digitsCount > NUMBERS_LENGTH) {
				System.out.println("Invalid input! More than " + NUMBERS_LENGTH + " numbers "
						+ "are provided. Please try again!");
			} else if (!validValues) {
				System.out.println("Invalid input! All numbers must be in the range from "
						+ NUMBERS_MIN + " to " + NUMBERS_MAX + "!");
			}

			// then check that all the 7 digits are different
			else {
				int digitIndex = 0;
				int testIndex = 0;
				boolean isDuplicates = false;
				for (digitIndex = 0; digitIndex <= NUMBERS_LENGTH - 1; digitIndex++) {
					int testDuplicate = numbers[digitIndex];
					for (testIndex = 0; testIndex <= digitsCount - 1; testIndex++) {
						int testDuplicate2 = numbers[testIndex];
						if ((testDuplicate == testDuplicate2) && (digitIndex != testIndex)) {
							isDuplicates = true;
							break;
						}
					}
					if (isDuplicates == true) {
						System.out.println("Invalid input! All numbers must be different!");
						break;
					}
				}
				if ((digitIndex == NUMBERS_LENGTH) && (testIndex == NUMBERS_LENGTH)) {
					isValidEntry = true;
				}
			}
		} while (!isValidEntry);
		Arrays.sort(numbers);
	}

	
	
	
	/**
	 * Overrides the basic printEntries method to print entries according to NumbersEntry format.
	 */
	public void printEntries() {
		System.out.print("Entry ID: ");
		System.out.printf("%-7d", getEntryID());
		System.out.print("Numbers:");
		for (int digit = 0; digit < NUMBERS_LENGTH; digit++) {
			System.out.printf("%3s", numbers[digit]);
		}
		if (!getIsManual()) {
			System.out.print(" [Auto]");
		}
		System.out.println();
	}

	
	
	
	
	/**
	 * Overrides the basic printWinningEntries method to print winning entries according to 
	 * NumbersEntry format i.e. including numbers.
	 */
	public void printWinningEntries(Entry winner) {

		Member winningMember = getDatabase().searchMember(getMemberID());
		
		System.out.print("Member ID: " + getMemberID() + ", Member Name: " + winningMember
				.getMemberName());
		System.out.print(", Prize: ");
		System.out.printf("%-5d", getPrize());
		System.out.println();
		System.out.print("--> Entry ID: " + getEntryID() + ", Numbers:");
		for (int digit = 0; digit < NUMBERS_LENGTH; digit++) {
			System.out.printf("%3d", numbers[digit]);
		}
		if (!getIsManual()) {
			System.out.print(" [Auto]");
		}
	}

	
	
	
	
}
